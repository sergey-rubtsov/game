package tic.tac.toe.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tic.tac.toe.server.exception.BadRequestException;
import tic.tac.toe.server.exception.InternalServerError;
import tic.tac.toe.server.exception.ResourceNotFoundException;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.GameType;
import tic.tac.toe.server.model.Turn;
import tic.tac.toe.server.model.User;
import tic.tac.toe.server.repository.GameRepository;
import tic.tac.toe.server.repository.UserReposytory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class GameService {

    @Value("${player.one}")
    private Character one;

    @Value("${player.two}")
    private Character two;

    @Value(("${playfield.size}"))
    private int size;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserReposytory userReposytory;

    public Game processUserStatusRequest(String uuid) {
        Optional<User> found = userReposytory.findUserByUuid(uuid);
        return found.map(User::getGame).orElse(processNewUserAndGetGame(uuid));
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
        if (users.size() < 2) {
            User previous = users.get(users.size() - 1);
            User newUser = new User();
            newUser.setUuid(uuid);
            newUser.setNumber(previous.getNumber() + 1);
            if (previous.getSymbol() == one) {
                newUser.setSymbol(two);
            } else {
                newUser.setSymbol(one);
            }
            users.add(newUser);
            newUser = userReposytory.save(newUser);
            game.setGameType(GameType.ACTIVE);
            newUser.setGame(game);
            userReposytory.save(newUser);
            Random random = new Random();
            if (random.nextBoolean()) {
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
            field[i] = ' ';
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
        user.setNumber(0);
        user.setUuid(uuid);
        Random r = new Random();
        if (r.nextBoolean()) {
            user.setSymbol(one);
        } else {
            user.setSymbol(two);
        }
        return user;
    }

    public void greeting() {
        this.template.convertAndSend("/topic/greetings", "Hello");
    }

    public Turn processUserTurn(String gameUuid, int x, int y, Character mark) {
        if (x >= size || y >= size) {
            throw new BadRequestException("impossible turn");
        }
        Optional<Game> game = gameRepository.findGameByUuid(gameUuid);
        return game.map(found -> checkTurnAndExecute(found, x, y, mark)).orElseThrow(ResourceNotFoundException::new);
    }

    private Turn checkTurnAndExecute(@NotNull Game game, int x, int y, Character mark) {
        if (game.getGameType() != GameType.ACTIVE) {
            throw new BadRequestException("game is not active");
        }
        if (game.getNext().getSymbol() == mark) {
            game.getField()[y * size + x] = mark;
            List<User> users = game.getUsers();
            users.sort(Comparator.comparing(User::getNumber));
            User next;
            if (game.getNext().getNumber() + 1 < users.size()) {
                next = users.get(game.getNext().getNumber() + 1);
            } else {
                next = users.get(0);
                serverTurn(game);
            }
            game.setNext(next);
            game = gameRepository.save(game);
            return new Turn(game.getNext().getUuid(), x, y, mark);
        } else {
            throw new BadRequestException("not your turn");
        }
    }

    private void serverTurn(Game game) {
        //char[][] field = new char[size][size];
    }
}
