package tic.tac.toe.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tic.tac.toe.server.model.User;

import java.util.Optional;

@Repository
public interface UserReposytory extends JpaRepository<User, Long> {

    Optional<User> findUserByUuid(String uuid);

}
