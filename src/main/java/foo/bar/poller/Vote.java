package foo.bar.poller;

import java.util.Optional;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Vote extends PanacheEntity {

    public String name;

    public int tally;

    public Vote() {
    }

    public Vote(String name) {
        this.name = name;
    }

    public static Optional<Vote> findByName(String name) {
       return Vote.find("name", name).firstResultOptional();
    }
}
