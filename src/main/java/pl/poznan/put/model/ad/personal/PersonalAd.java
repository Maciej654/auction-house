package pl.poznan.put.model.ad.personal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.ad.Ad;
import pl.poznan.put.model.auction.Auction;
import pl.poznan.put.model.user.User;
import pl.poznan.put.util.persistence.entity.manager.provider.EntityManagerProvider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PERSONAL_ADS")
@IdClass(PersonalAd.PersonalAdId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalAd {
    private static final EntityManager em = EntityManagerProvider.getEntityManager();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalAdId implements Serializable {
        private Auction auction;
        private User    recipient;
    }

    @Id
    @Column(name = "AUCTION")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "RECIPIENT")
    @ManyToOne
    private User recipient;
}
