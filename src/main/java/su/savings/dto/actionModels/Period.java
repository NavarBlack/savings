package su.savings.dto.actionModels;

import su.savings.dto.OperationDTO;
import su.savings.dto.PeriodDTO;

import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Period extends PeriodDTO {
    public Map<String, Long> countMoneyStat(){
        Long expSum = 0L;
        Long incSum = 0L;
        Long remExpOnDey = expOnDey * periodDays;
        for (OperationDTO op : operations){
            if (op.getExpType()) {
                expSum +=op.getSum();
            }else {
                incSum += op.getSum();
            }
        }
        Long endSum = startSum - remExpOnDey - expSum + incSum;
        Long remSum = startSum - expSum + incSum;
        Long countOperation = incSum - expSum;
        return Map.of(
                "remSum", remSum,
                "expSum",expSum,
                "incSum", incSum,
                "remExpOnDey", remExpOnDey,
                "endSum", endSum,
                "countOperation", countOperation
        );
    }

    public Long countPeriodDays(){
        return ChronoUnit.DAYS.between(startPeriod, endPeriod);
    }

    public Long currentExpOnDay(Long startSum) {
        return ((startSum + countMoneyStat().get("countOperation")) / (planDays - pastDaysOnPlan))/100*100;
    }
}
