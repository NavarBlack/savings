package su.savings.oae;

import su.savings.db.Repository;
import su.savings.oae.dto.OperationDTO;
import su.savings.oae.dto.PeriodDTO;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

public class Period extends PeriodDTO {

    public Map<String, Long> countMoneyStat() {
        Long expSum = 0L;
        Long incSum = 0L;
        Long remExpOnDey = expOnDey * periodDays;
        for (OperationDTO op : operations) {
            if (op.getExpType()) {
                expSum += op.getSum();
            } else {
                incSum += op.getSum();
            }
        }
        Long endSum = startSum - remExpOnDey - expSum + incSum;
        Long remSum = startSum - expSum + incSum;
        Long countOperation = incSum - expSum;
        return Map.of(
                "remSum", remSum,
                "expSum", expSum,
                "incSum", incSum,
                "remExpOnDey", remExpOnDey,
                "endSum", endSum,
                "countOperation", countOperation
        );
    }

    public Long countPeriodDays() {
        return ChronoUnit.DAYS.between(startPeriod, endPeriod);
    }

    public Long currentExpOnDay(Long startSum) {
        return ((startSum + countMoneyStat().get("countOperation")) / (planDays - pastDaysOnPlan)) / 100 * 100;
    }

    public Period masSet(LocalDate step, LocalDate end, Long statSum, Long planDey, Long preliminaryExpOnDay, Long pastDay) {
        setStartPeriod(step);
        setEndPeriod(end);
        setStartSum(statSum);
        setExpOnDey(preliminaryExpOnDay);
        setPeriodDays(countPeriodDays());
        setEndSum(countMoneyStat().get("endSum"));
        setPastDaysOnPlan(pastDay);
        return this;
    }

    public static Period mapper(ResultSet rs) {
        try {
            Period newPeriod = new Period();
            newPeriod.setId(rs.getLong("id"))
                    .setStartPeriod(Instant.ofEpochMilli(rs.getDate("START_PERIOD").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setEndPeriod(Instant.ofEpochMilli(rs.getDate("END_PERIOD").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setPeriodDays(rs.getLong("PERIOD_DAYS"))
                    .setStartSum(rs.getLong("START_SUM"))
                    .setEndSum(rs.getLong("END_SUM"))
                    .setExpOnDey(rs.getLong("EXP_ON_DEY"))
                    .setPlanId(rs.getLong("PLAN_ID"))
                    .setPastDaysOnPlan(rs.getLong("PAST_DAYS"))
                    .setOperations(Operation.getBdOperation(rs.getLong("id")));
            return newPeriod;

        } catch (Exception e) {
            throw new RuntimeException("periodMapper", e);
        }
    }


    public static ArrayList<Period> getBdOperation (Long planId){

        return new ArrayList<>((Repository.getPeriodOnPlan(planId)));
    }
}
