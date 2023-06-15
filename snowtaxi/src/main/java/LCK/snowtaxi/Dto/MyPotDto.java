package LCK.snowtaxi.Dto;


import LCK.snowtaxi.domain.Potlist;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class MyPotDto implements Serializable {
    private Potlist potlist;
    private List<MyPotMemberDto> members;

}
