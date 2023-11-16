package tinkoffinvestementbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
    @Id
    @Column
    private Long id;
    @Column
    private String token;
}
