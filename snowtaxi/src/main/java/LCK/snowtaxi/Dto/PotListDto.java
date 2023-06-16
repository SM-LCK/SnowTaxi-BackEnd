package LCK.snowtaxi.Dto;

import LCK.snowtaxi.domain.Potlist;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PotListDto {
    private Potlist pot;
    private Boolean isMyPot;
}
