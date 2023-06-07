package LCK.snowtaxi.domain;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="participation")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long participationId;

    private long userId;
    private long potlistId;
    private boolean isPaid = false;

}
