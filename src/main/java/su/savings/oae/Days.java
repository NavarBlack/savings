package su.savings.oae;

import su.savings.db.Repository;
import su.savings.oae.dto.DaysDto;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

public class Days extends DaysDto {

    public static Days mapper(ResultSet rs) {
        try {
            Days day = new Days();
            day.setId(rs.getLong("ID"))
                    .setPeriodId(rs.getLong("PERIOD_ID"))
                    .setDate(Instant.ofEpochMilli(rs.getDate("DAY_DATE").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setRemainsStart(rs.getLong("REMAINS_START"))
                    .setRemainsFact(rs.getLong("REMAINS_FACT"))
                    .setAccumulation(rs.getLong("ACCUMULATION"))
                    .setExpDay(rs.getLong("EXP_DAY"))
                    .setOperations(Operation.getBdOperation(rs.getLong("ID"), false))
                    .setRemainsEnd(rs.getLong("REMAINS_END"))
                    .setRemainsEndDeficit(rs.getLong("REMAINS_END_DEFICIT"))
                    .setRemainsEndProfit(rs.getLong("REMAINS_END_PROFIT"));
            return day;
        } catch (Exception e) {
            throw new RuntimeException("allPlansMapper", e);
        }
    }

    public static ArrayList<Days> getBdDays(Long id){
        return new ArrayList<>(Repository.getDays(id)) ;
    }
}
