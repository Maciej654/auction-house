package pl.poznan.put.model.auction.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.poznan.put.model.auction.Auction;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "BOOKS")
@DiscriminatorValue("BOOK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Book extends Auction implements Serializable {
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "COVER")
    private Cover  cover;
    @Column(name = "GENRE")
    private String genre;

    public enum Cover {
        SOFT,
        HARD
    }

    @Override
    public String toString() {
        return "Book{" +
                "Auction='" + super.toString() + '\'' +
                "author='" + author + '\'' +
                ", cover=" + cover +
                ", genre='" + genre + '\'' +
                '}';
    }
}
