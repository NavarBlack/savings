package su.savings.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

public class PeriodDTO {
    private Long id = null;
    private LocalDate startPeriod = LocalDate.now();
    private LocalDate endPeriod =  LocalDate.now().plusMonths(1);
    private Long startSum = 0L;
    private Long endSum = 0L;
    private Long expOnDey = 0L;
    private Integer periodDays = Math.toIntExact(ChronoUnit.DAYS.between(startPeriod, endPeriod));
    private ArrayList<OperationDTO> operations = new ArrayList<>();
    private Long planId;

    public Map<String, Long> getStatistic(){
        Long remSum = startSum;
        Long expSum = 0L;
        Long incSum = 0L;
        for (OperationDTO op : operations){
            if (op.getExpType()) {
                expSum +=op.getSum();
            }else {
                incSum += op.getSum();
            }
        }
        remSum = remSum - expSum + incSum;
        return Map.of("remSum", remSum, "expSum",expSum,"incSum", incSum);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(LocalDate startPeriod) {
        this.startPeriod = startPeriod;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(LocalDate endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Long getStartSum() {
        return startSum;
    }

    public void setStartSum(Long startSum) {
        this.startSum = startSum;
    }

    public Integer getPeriodDays() {
        periodDays = Math.toIntExact(ChronoUnit.DAYS.between(startPeriod, endPeriod));
        return periodDays;
    }

    public void setPeriodDays(Integer periodDays) {
        this.periodDays = periodDays;
    }

    public ArrayList<OperationDTO> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<OperationDTO> operations) {
        this.operations = operations;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getEndSum() {
        return endSum;
    }

    public void setEndSum(Long endSum) {
        this.endSum = endSum;
    }

    public Long getExpOnDey() {
        return expOnDey;
    }

    public PeriodDTO setExpOnDey(Long expOnDey) {
        this.expOnDey = expOnDey;
        return this;
    }

    @Override
    public String toString() {
        return "PeriodDTO{" +
                "id=" + id +
                ", startPeriod=" + startPeriod +
                ", endPeriod=" + endPeriod +
                ", startSum=" + startSum +
                ", endSum=" + endSum +
                ", periodDays=" + periodDays +
                ", operations=" + operations +
                ", planId=" + planId +
                '}';
    }
}
