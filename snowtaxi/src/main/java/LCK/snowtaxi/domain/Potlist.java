package LCK.snowtaxi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="potlist")
public class Potlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long potlistId;

    private String departure;
    private String ridingTime;

    private int headCount;
    private long hostUserId;

    private LocalDate createdAt;

    private boolean isPaidRequest;
}
