package tic.tac.toe.client.repository;

import tic.tac.toe.model.User;

public interface UserRepository {

    public User getCurrentUser();

    public void saveCurrentUser(User user);

}
