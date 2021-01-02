package pl.poznan.put.model.auction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "DEFAULTS")
@DiscriminatorValue("DEFAULT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Default extends Auction implements Serializable {
    @Column(name = "CATEGORY")
    private String category;

    @Override
    public String toString() {
        return "Default{" +
                "Auction='" + super.toString() + '\'' +
                "category='" + category + '\'' +
                '}';
    }
    @Override
    public String getCategory(){
        return this.category;
    }
}
