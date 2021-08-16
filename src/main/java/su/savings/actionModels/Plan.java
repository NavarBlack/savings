package su.savings.actionModels;

import su.savings.dto.PlansDTO;
import java.util.Map;

public class Plan extends PlansDTO {
    public Map<String, Long> getStatistic(){
        Long remSum = startSum;
        Long expSum = 0L;
        Long incSum = 0L;
        Long totalExpOnDey = 0L;
        for (Period per : periods){
            Map<String,Long> calcPer = per.getStatistic();
            expSum += calcPer.get("expSum");
            incSum += calcPer.get("incSum");
            totalExpOnDey += calcPer.get("remExpOnDey");
        }
        remSum = remSum - expSum + incSum ;
        Long expOnDey = (remSum / planDays)/100*100;
        Long totalRem = remSum - totalExpOnDey;

        return Map.of("remSum", remSum, "expSum",expSum,"incSum", incSum, "expOnDey", expOnDey, "totalRem", totalRem);
    }

}
