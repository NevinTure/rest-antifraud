package rest.antifraud.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public Role(String name) {
        this.name = name;
    }
}
