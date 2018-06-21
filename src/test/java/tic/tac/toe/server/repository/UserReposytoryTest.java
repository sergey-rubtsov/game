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
public class UserReposytoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserReposytory userReposytory;


    @Test
    public void findUserByUuid() {
        User user = new User();
        user.setSymbol('X');
        user.setNumber(0);
        user.setUuid("UUID2");
        user = userReposytory.save(user);
        Game game = new Game();
        game.setGameType(GameType.ACTIVE);
        char[] field = "test data".toCharArray();
        game.setField(field);
        List<User> users = new ArrayList<>();
        users.add(user);
        game.setUsers(users);
        game = gameRepository.save(game);
        user.setGame(game);
        userReposytory.save(user);
        Optional<User> found = userReposytory.findUserByUuid("UUID2");
        assertTrue(found.isPresent());
        assertEquals(GameType.ACTIVE, found.get().getGame().getGameType());
    }
}