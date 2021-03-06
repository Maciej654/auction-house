package pl.poznan.put.model.auction.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.poznan.put.model.auction.Auction;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "CARS")
@DiscriminatorValue("CAR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Car extends Auction implements Serializable {
    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "MILEAGE")
    private int mileage;


    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSMISSION")
    private Transmission transmission;

    @Column(name = "ENGINE")
    private String engine;

    @Enumerated(EnumType.STRING)
    @Column(name = "FUEL")
    private Fuel fuel;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONDITION")
    private Condition condition;

    public enum Transmission {
        MANUAL,
        AUTOMAT
    }

    public enum Fuel {
        GAS,
        DIESEL,
        HYDROGEN,
        ELECTRIC,
        HYBRID
    }

    public enum Condition {
        NEW,
        USED,
        CRASHED
    }

    @Override
    public String toString() {
        return "Car{" +
               "Auction='" + super.toString() + '\'' +
               "make='" + make + '\'' +
               ", model='" + model + '\'' +
               ", mileage=" + mileage +
               ", transmission=" + transmission +
               ", engine='" + engine + '\'' +
               ", fuel=" + fuel +
               ", condition=" + condition +
               '}';
    }
}
