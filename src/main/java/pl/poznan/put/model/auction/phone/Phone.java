package pl.poznan.put.model.auction.phone;

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
@Table(name = "PHONES")
@DiscriminatorValue("PHONE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Phone extends Auction implements Serializable {
    @Column(name = "PRODUCER")
    private String producer;

    @Column(name = "SCREEN_SIZE")
    private String screenSize;

    @Column(name = "BATTERY")
    private String battery;

    @Column(name = "PROCESSOR")
    private String processor;

    @Column(name = "RAM")
    private String ram;

    @Column(name = "OPERATING_SYSTEM")
    private String operatingSystem;

    @Override
    public String toString() {
        return "Phone{" +
                "Auction='" + super.toString() + '\'' +
                "producer='" + producer + '\'' +
                ", screenSize='" + screenSize + '\'' +
                ", battery='" + battery + '\'' +
                ", processor='" + processor + '\'' +
                ", ram='" + ram + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                '}';
    }
}
