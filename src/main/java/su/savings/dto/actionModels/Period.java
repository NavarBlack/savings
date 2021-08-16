package su.savings.dto.actionModels;

import su.savings.dto.OperationDTO;
import su.savings.dto.PeriodDTO;

import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Period extends PeriodDTO {
    public Map<String, Long> statistic(){
        Long remSum = startSum;
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
        remSum = remSum - expSum + incSum;
        return Map.of("remSum", remSum, "expSum",expSum,"incSum", incSum, "remExpOnDey", remExpOnDey);
    }


    public Long countEndSumPeriod(){
        Long expOnDeyInPeriod = expOnDey * periodDays;
        Long resCountOperation = countOperation();
        return startSum - expOnDeyInPeriod + resCountOperation;
    }

    public Long countOperation() {
        Long res = 0L;
        if (operations.isEmpty()) return 0L;
        for (Operation op : operations) {
            res += op.getExpType() ? op.getSum() * -1 : op.getSum();
        }
        return res;
    }

    public Long countPeriodDays(){
        return ChronoUnit.DAYS.between(startPeriod, endPeriod);
    }
}
