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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PERSONAL_ADS")
@IdClass(PersonalAd.PersonalAdId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(
        name = PersonalAd.QUERY_FIND_BY_RECIPIENT,
        query = "select ad from PersonalAd ad where recipient = :" + PersonalAd.PARAM_RECIPIENT
)
public class PersonalAd implements Serializable {
    public static final String QUERY_FIND_BY_RECIPIENT = "PersonalAd.QUERY_FIND_BY_RECIPIENT";

    public static final String PARAM_RECIPIENT = "recipient";

    @Id
    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @ManyToOne
    private Auction auction;

    @Id
    @JoinColumn(name = "RECIPIENT", referencedColumnName = "EMAIL")
    @ManyToOne
    private User recipient;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalAdId implements Serializable {
        private Auction auction;
        private User    recipient;
    }

    @Override
    public String toString() {
        return "PersonalAd{" +
               "auction=" + auction.getId() +
               ", recipient=" + recipient.getEmail() +
               '}';
    }
}
