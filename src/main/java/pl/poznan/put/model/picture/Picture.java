package pl.poznan.put.model.picture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.auction.Auction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Blob;

@Entity
@Table(name = "PICTURES")
@IdClass(Picture.PictureId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture implements Serializable {
    @Id
    @JoinColumn(name = "AUCTION", referencedColumnName = "AUCTION_ID")
    @ManyToOne
    private Auction auction;

    @Id
    @Column(name = "NAME")
    private String name;

    @Lob
    @Column(name = "IMAGE")
    private Blob image;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PictureId implements Serializable {
        private Auction auction;
        private String  name;
    }

    @Override
    public String toString() {
        return "Picture{" +
               "auction=" + auction.getId() +
               ", name='" + name + '\'' +
               '}';
    }
}
