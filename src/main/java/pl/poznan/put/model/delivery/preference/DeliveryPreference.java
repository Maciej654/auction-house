package pl.poznan.put.model.delivery.preference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.poznan.put.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "DELIVERY_PREFERENCES")
@IdClass(DeliveryPreference.DeliveryPreferenceId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPreference implements Serializable {
    @Id
    @JoinColumn(name = "USER", referencedColumnName = "EMAIL")
    @ManyToOne
    private User user;

    @Id
    @Column(name = "ADDRESS")
    private String address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryPreferenceId implements Serializable {
        private User   user;
        private String address;
    }
}

