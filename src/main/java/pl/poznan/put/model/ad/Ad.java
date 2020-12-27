package pl.poznan.put.model.ad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ADS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @Id
    @Column(name = "AUCTION")
    @OneToOne
    private Auction auction;

    @Column(name = "SLOGAN")
    private String slogan;
}
