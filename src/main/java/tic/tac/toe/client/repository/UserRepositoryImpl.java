package tic.tac.toe.client.repository;

import org.springframework.stereotype.Repository;
import tic.tac.toe.model.User;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void saveCurrentUser(User user) {
        this.currentUser = user;
    }
}
