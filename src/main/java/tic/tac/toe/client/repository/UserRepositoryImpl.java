package tic.tac.toe.client.repository;

import org.springframework.stereotype.Component;
import tic.tac.toe.server.model.User;

@Component
public class UserRepositoryImpl implements UserRepository {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void saveCurrentUser(User user) {
        this.currentUser = user;
    }
}
