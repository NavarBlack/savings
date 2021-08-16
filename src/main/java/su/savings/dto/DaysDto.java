package su.savings.dto;

import java.time.LocalDate;
import java.util.ArrayList;

public class DaysDto {
    private Long id;
    private LocalDate dayDate;
    private ArrayList<Long> exp;
    private ArrayList<Long> inc;
    private Long planetRemains;
    private Long currentRemains;
    private Long balance;
    private Long surplus;
    private Long deficit;
}
