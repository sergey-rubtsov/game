package tic.tac.toe.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tic.tac.toe.server.GameConstants;
import tic.tac.toe.server.Utils;
import tic.tac.toe.server.exception.BadRequestException;
import tic.tac.toe.server.exception.InternalServerError;
import tic.tac.toe.server.exception.ResourceNotFoundException;
import tic.tac.toe.server.message.CellMessage;
import tic.tac.toe.server.message.EndGameMessage;
import tic.tac.toe.server.message.TurnMessage;
import tic.tac.toe.server.model.Cell;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.GameType;
import tic.tac.toe.server.model.User;
import tic.tac.toe.server.repository.GameRepository;
import tic.tac.toe.server.repository.UserReposytory;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceImpl.class);
    @Value("${player.one}")
    private Character one;
    @Value("${player.two}")
    private Character two;
    @Value("${player.three}")
    private Character three;
    @Value(("${playfield.size}"))
    private int size;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserReposytory userReposytory;

    @Transactional
    public Game processUserStatusRequest(String uuid) {
        Optional<User> found = userReposytory.findUserByUuid(uuid);
        return found.map(User::getGame).orElse(processNewUserAndGetGame(uuid));
    }

    public User findUserByUuid(String uuid) {
        return userReposytory.findUserByUuid(uuid).orElseThrow(ResourceNotFoundException::new);
    }

    private Game processNewUserAndGetGame(String uuid) {
        List<Game> games = gameRepository.findGameByGameType(GameType.NEW);
        if (games.isEmpty()) {
            return createNewGameAndUser(uuid);
        } else {
            return addNewUserIntoGameAndStart(uuid, games.get(0));
        }
    }

    private Game addNewUserIntoGameAndStart(String uuid, Game game) {
        List<User> users = game.getUsers();
        if (users.size() < GameConstants.NUMBER_OF_HUMAN_PLAYERS) {
            User previous = users.get(users.size() - 1);
            User newUser = new User();
            newUser.setUuid(uuid);
            if (previous.getMark() == one) {
                newUser.setNumber(1);
                newUser.setMark(two);
            } else {
                newUser.setNumber(0);
                newUser.setMark(one);
            }
            users.add(newUser);
            newUser = userReposytory.save(newUser);
            game.setGameType(GameType.ACTIVE);
            newUser.setGame(game);
            userReposytory.save(newUser);
            if (newUser.getMark() == one) {
                game.setNext(newUser);
            }
            return gameRepository.save(game);
        } else {
            throw new InternalServerError("game logic error");
        }
    }

    private Game createNewGameAndUser(String uuid) {
        Game game = new Game();
        char[] field = new char[size * size];
        for (int i = 0; i < field.length; i++) {
            field[i] = GameConstants.EMPTY_CELL;
        }
        game.setField(field);
        game.setGameType(GameType.NEW);
        game.setUuid(UUID.randomUUID().toString());
        List<User> users = new ArrayList<>();
        User user = buildFirstUser(uuid);
        user = userReposytory.save(user);
        users.add(user);
        game.setNext(user);
        game = gameRepository.save(game);
        game.setUsers(users);
        user.setGame(game);
        userReposytory.save(user);
        return gameRepository.save(game);
    }

    private User buildFirstUser(String uuid) {
        User user = new User();
        user.setUuid(uuid);
        Random r = new Random();
        if (r.nextBoolean()) {
            user.setNumber(0);
            user.setMark(one);
        } else {
            user.setNumber(1);
            user.setMark(two);
        }
        return user;
    }

    public void processUserTurn(@NotNull String uuid, int x, int y) {
        if (x >= size || y >= size || x < 0 || y < 0) {
            throw new BadRequestException("impossible turn");
        }
        Optional<User> found = userReposytory.findUserByUuid(uuid);
        User user = found.orElseThrow(ResourceNotFoundException::new);
        checkTurnAndExecute(user.getGame(), x, y, user.getMark());
        checkWinner(user.getGame(), x, y, user.getMark());
        Optional<Game> game = gameRepository.findGameByUuid(user.getGame().getUuid());
        game.ifPresent(updated -> {
            User next = updated.getNext();
            if (next.getNumber() == 0) {
                serverTurn(updated, next);
            }
        });
    }

    @Transactional
    private void checkTurnAndExecute(@NotNull Game game, int x, int y, Character mark) {
        if (game.getGameType() != GameType.ACTIVE) {
            throw new BadRequestException("game is not active");
        }
        if (!turnIsPossible(game, x, y)) {
            throw new BadRequestException("point is occupied");
        }
        if (game.getNext().getMark() == mark) {
            game.getField()[Utils.translate(size, x, y)] = mark;
            List<User> users = game.getUsers();
            users.sort(Comparator.comparing(User::getNumber));
            User next;
            if (game.getNext().getNumber() + 1 < users.size()) {
                next = users.get(game.getNext().getNumber() + 1);
            } else {
                next = users.get(0);
            }
            game.setNext(next);
            game = gameRepository.save(game);
        } else {
            throw new BadRequestException("not your turn");
        }
        sendToSubscribers(game, x, y, mark, game.getNext().getMark());
    }

    private boolean turnIsPossible(Game game, int x, int y) {
        return game.getField()[Utils.translate(size, x, y)] == GameConstants.EMPTY_CELL;
    }

    private void checkWinner(@NotNull Game game, int x, int y, Character mark) {
        List<Cell> winner = findWinnerCells(game.getField(), x, y, mark);
        if (!winner.isEmpty()) {
            LOGGER.error("game end {}", winner);
            game.setGameType(GameType.END);
            gameRepository.save(game);
            sendWinnerToSubscribers(game, winner, mark);
            return;
        }
        List<Integer> variants = checkEmptyPositions(game);
        if (variants.isEmpty()) {
            game.setGameType(GameType.END);
            sendDeadHeatToSubscribers(game);
            return;
        }
    }

    private List<Cell> findWinnerCells(char[] field, int x, int y, Character mark) {
        return Utils.findRow(Utils.translate(size, field), x, y, mark, GameConstants.WIN_ROW_LENGTH);
    }

    private void serverTurn(Game game, User next) {
        List<Integer> variants = checkEmptyPositions(game);
        if (variants.isEmpty()) {
            game.setGameType(GameType.END);
            sendDeadHeatToSubscribers(game);
            return;
        }
        Random random = new Random();
        int randomCellNumber = variants.get(random.nextInt(variants.size()));
        game.getField()[randomCellNumber] = three;
        gameRepository.save(game);
        Cell possible = Utils.translate(size, randomCellNumber);
        sendToSubscribers(game, possible.getX(), possible.getY(), three, next.getMark());
        checkWinner(game, possible.getX(), possible.getY(), three);
    }

    private List<Integer> checkEmptyPositions(Game game) {
        List<Integer> variants = new ArrayList<>();
        for (int i = 0; i < game.getField().length; i++) {
            if (game.getField()[i] == GameConstants.EMPTY_CELL) {
                variants.add(i);
            }
        }
        return variants;
    }

    private void sendToSubscribers(Game game, int x, int y, Character mark, Character next) {
        TurnMessage turnMessage = new TurnMessage();
        turnMessage.setX(x);
        turnMessage.setY(y);
        turnMessage.setMark(mark);
        turnMessage.setNext(next);
        game.getUsers().forEach(user ->
                this.template.convertAndSend("/topic/" + user.getUuid(), turnMessage));
    }

    private void sendDeadHeatToSubscribers(Game game) {
        //dead heat
        EndGameMessage deadHeatMessage = new EndGameMessage();
        deadHeatMessage.setDeadHeat(true);
        game.getUsers().forEach(user ->
                this.template.convertAndSend("/topic/end" + user.getUuid(), deadHeatMessage));
    }

    private void sendWinnerToSubscribers(Game game, List<Cell> winner, Character mark) {
        EndGameMessage deadHeatMessage = new EndGameMessage();
        deadHeatMessage.setDeadHeat(false);
        winner.forEach(cell -> deadHeatMessage.getRow().add(new CellMessage(cell.getX(), cell.getY())));
        deadHeatMessage.setWinner(mark);
        game.getUsers().forEach(user ->
                this.template.convertAndSend("/topic/end" + user.getUuid(), deadHeatMessage));
    }


}
