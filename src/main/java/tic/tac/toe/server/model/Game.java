package tic.tac.toe.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(unique = true)
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<User> users;

    @OneToOne(optional = false)
    private User next;

    @Column(nullable = false)
    private char[] field;

    @Column(nullable = false)
    private GameType gameType;

}
