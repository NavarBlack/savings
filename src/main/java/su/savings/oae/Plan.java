package su.savings.oae;

import su.savings.controllers.tabs.TabPlansController;
import su.savings.db.Repository;
import su.savings.oae.dto.PlansDTO;
import su.savings.helpers.Converter;
import su.savings.helpers.Utils;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Plan extends PlansDTO {

    public Map<String, Long> getStatistic(){
        Long remSum = startSum;
        Long expSum = 0L;
        Long incSum = 0L;
        Long totalExpOnDey = 0L;
        for (Period per : periods){
            Map<String,Long> calcPer = per.countMoneyStat();
            expSum += calcPer.get("expSum");
            incSum += calcPer.get("incSum");
            totalExpOnDey += calcPer.get("remExpOnDey");
        }
        remSum = remSum - expSum + incSum ;
        Long totalRem = remSum - totalExpOnDey;
        Long expOnDay = (remSum/planDays)/100*100;

        return Map.of("remSum", remSum, "expSum",expSum,"incSum", incSum, "totalRem", totalRem, "expOnDay", expOnDay);
    }

    public Long countPlanDays(){
        return ChronoUnit.DAYS.between(startPlane, endPlane);
    }

    public Long preliminaryExpOnDey (){
        return (startSum / planDays)/100*100;
    }

    public Plan masSet(TabPlansController pc){
        setPlaneName(pc.getFxPlaneName().getText());
        setStartPlane(pc.getFxStartPlane().getValue());
        setEndPlane(pc.getFxEndPlane().getValue());
        setStartSum(Utils.getLongToString(pc.getFxStartSum().getText()));
        setPlanDays(countPlanDays());
        setKeyPoints(Converter.listViewToArrayList(pc.getFxKeyPoints()));
        createPeriods();
        return this;
    }

    public static Plan mapper(ResultSet rs){
        try {
            Plan plan = new Plan();
            plan.setId(rs.getLong("id"))
                    .setPlaneName(rs.getString("PLAN_NAME"))
                    .setStartPlane(Instant.ofEpochMilli(rs.getDate("START_PLAN").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setEndPlane(Instant.ofEpochMilli(rs.getDate("END_PLAN").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setStartSum(rs.getLong("START_SUM"))
                    .setPlanDays(rs.getLong("PLAN_DAYS"))
                    .setKeyPoints(Converter.stringToArrayList(rs.getString("KEY_POINTS")))
                    .setPeriods(Period.getBdOperation(rs.getLong("id")));
            return plan;

        } catch (Exception e) {
            throw new RuntimeException("allPlansMapper", e);
        }
    }

    public void createPeriods(){
        LocalDate step = startPlane;
        Long startSumFirstPeriod = startSum;
        Long planDays = this.planDays;
        Long preliminaryExpOnDay = preliminaryExpOnDey();
        Long pastDay = 0L;
        for (LocalDate ld : keyPoints) {
            Period newPeriod = new Period().masSet(step, ld, startSumFirstPeriod, planDays, preliminaryExpOnDay, pastDay);
            pastDay += newPeriod.getPeriodDays();
            periods.add(newPeriod);
            step = ld;
            startSumFirstPeriod = newPeriod.getEndSum();
        }
        periods.add(new Period().masSet(step, endPlane, startSumFirstPeriod, planDays, preliminaryExpOnDay, pastDay));
    }
}
