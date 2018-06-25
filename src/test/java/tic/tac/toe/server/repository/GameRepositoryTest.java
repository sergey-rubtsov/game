package tic.tac.toe.server.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.GameType;
import tic.tac.toe.server.model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * integration test for database
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserReposytory userReposytory;

    @Test
    public void findGameByGameType() {
        User user = new User();
        user.setMark('X');
        user.setUuid("UUID");
        user.setNumber(0);
        userReposytory.save(user);
        Game game = new Game();
        game.setGameType(GameType.ACTIVE);
        char[] field = "test data".toCharArray();
        game.setField(field);
        List<User> users = new ArrayList<>();
        users.add(user);
        game.setUsers(users);
        game.setNext(user);
        game.setUuid("GAME_UUID0");
        gameRepository.save(game);
        List<Game> games = gameRepository.findGameByGameType(GameType.ACTIVE);
        assertFalse(games.isEmpty());
        games = gameRepository.findGameByGameType(GameType.NEW);
        assertTrue(games.isEmpty());
    }

    @Test
    public void findGameByUuid() {
        User user = new User();
        user.setMark('X');
        user.setUuid("UUID3");
        user.setNumber(0);
        userReposytory.save(user);
        Game game = new Game();
        game.setGameType(GameType.ACTIVE);
        char[] field = "test data".toCharArray();
        game.setField(field);
        List<User> users = new ArrayList<>();
        users.add(user);
        game.setUsers(users);
        game.setNext(user);
        game.setUuid("GAME_UUID1");
        game = gameRepository.save(game);
        Optional<Game> found = gameRepository.findGameByUuid(game.getUuid());
        assertTrue(found.isPresent());
    }
}