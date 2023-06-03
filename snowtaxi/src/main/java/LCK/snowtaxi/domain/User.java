package LCK.snowtaxi.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="user")
public class User {
    @Id
    private String userId;

    //private String name;
    private String phone;

}
