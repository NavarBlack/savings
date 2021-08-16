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
            Map<String,Long> calcPer = per.statistic();
            expSum += calcPer.get("expSum");
            incSum += calcPer.get("incSum");
            totalExpOnDey += calcPer.get("remExpOnDey");
        }
        remSum = remSum - expSum + incSum ;
        Long totalRem = remSum - totalExpOnDey;

        return Map.of("remSum", remSum, "expSum",expSum,"incSum", incSum, "totalRem", totalRem);
    }

    public Long countPlanDays(){
        Long res = ChronoUnit.DAYS.between(startPlane, endPlane);
        return res;
    }

    public Long preliminaryExpOnDey (){
        return (startSum / planDays)/100*100;
    }

}
