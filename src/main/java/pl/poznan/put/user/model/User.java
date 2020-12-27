package pl.poznan.put.user.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "hash")
    private String hash;

    @Column(name = "birth_date")
    private Date birthday;
}
