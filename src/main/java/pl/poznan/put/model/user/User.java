package pl.poznan.put.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.follower.Follower;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthday;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    @OneToMany(mappedBy = "FOLLOWEE", cascade = CascadeType.ALL)
    private Collection<Follower> followersRaw;

    @OneToMany(mappedBy = "FOLLOWER", cascade = CascadeType.ALL)
    private Collection<Follower> followeesRaw;

    public Collection<User> getFollowers() {
        return followersRaw.stream()
                           .map(Follower::getFollower)
                           .collect(Collectors.toList());
    }

    public Collection<User> getFollowees() {
        return followeesRaw.stream()
                           .map(Follower::getFollowee)
                           .collect(Collectors.toList());
    }
}
