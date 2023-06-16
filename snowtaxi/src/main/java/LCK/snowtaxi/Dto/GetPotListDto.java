package LCK.snowtaxi.Dto;

import LCK.snowtaxi.domain.Potlist;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class GetPotListDto {
    private List<PotListDto> potList;
    private Boolean isParticipating;
}
