package LCK.snowtaxi.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MyInfoDto {
    private String nickname;
    private String phone;
    private long amount;
}