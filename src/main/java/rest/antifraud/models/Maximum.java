package rest.antifraud.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "maximums")
public class Maximum {

    @Id
    @Column(name = "maximum_name")
    private String maximumName;

    @Column(name = "maximum_value")
    private long maximumValue;
}
