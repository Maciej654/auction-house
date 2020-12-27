package pl.poznan.put.model.ad.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PERSONAL_ADS")
@IdClass(PersonalAd.PersonalAdId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalAd implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION")
    @ManyToOne
    private Auction auction;
    @Id
    @JoinColumn(name = "RECIPIENT")
    @OneToOne
    private User    recipient;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalAdId implements Serializable {
        private Auction auction;
        private User    recipient;
    }
}
