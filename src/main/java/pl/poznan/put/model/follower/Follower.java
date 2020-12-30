package pl.poznan.put.model.follower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "FOLLOWERS")
@IdClass(Follower.FollowerId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follower implements Serializable {
    @Id
    @JoinColumn(name = "FOLLOWER", referencedColumnName = "EMAIL")
    @ManyToOne
    private User follower;
    @Id
    @JoinColumn(name = "FOLLOWEE", referencedColumnName = "EMAIL")
    @ManyToOne
    private User followee;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowerId implements Serializable {
        private User follower;
        private User followee;
    }

    @Override
    public String toString() {
        return "Follower{" +
                "follower=" + follower.getEmail() +
                ", followee=" + followee.getEmail() +
                '}';
    }
}
