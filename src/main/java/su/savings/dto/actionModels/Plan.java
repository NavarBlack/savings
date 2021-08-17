package su.savings.dto.actionModels;

import su.savings.dto.PlansDTO;

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


}
