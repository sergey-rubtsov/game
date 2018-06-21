package tic.tac.toe.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tic.tac.toe.server.model.Game;
import tic.tac.toe.server.model.GameType;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findGameByGameType(GameType type);

    Optional<Game> findGameByUuid(String uuid);

}
