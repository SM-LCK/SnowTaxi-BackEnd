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

    // isOver 종료 여부 저장.. 참여자가 sendcash 할 떄 마다 전체 참여자의 Participation isPaid 속성을 검사해 업데이트...............
}
