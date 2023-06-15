package LCK.snowtaxi.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MyPotMemberDto implements Serializable {
    private String nickname;
    private String phone;
    private boolean isPaid;
}
