package su.savings.dto;

import su.savings.actionModels.Operation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PeriodDTO {
    protected Long id = null;
    protected LocalDate startPeriod = LocalDate.now();
    protected LocalDate endPeriod =  LocalDate.now().plusMonths(1);
    protected Long startSum = 0L;
    protected Long endSum = 0L;
    protected Long expOnDey = 0L;
    protected Integer periodDays = Math.toIntExact(ChronoUnit.DAYS.between(startPeriod, endPeriod));
    protected ArrayList<Operation> operations = new ArrayList<>();
    protected Long planId;

    public Long getId() {
        return id;
    }

    public PeriodDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getStartPeriod() {
        return startPeriod;
    }

    public PeriodDTO setStartPeriod(LocalDate startPeriod) {
        this.startPeriod = startPeriod;
        return this;
    }

    public LocalDate getEndPeriod() {
        return endPeriod;
    }

    public PeriodDTO setEndPeriod(LocalDate endPeriod) {
        this.endPeriod = endPeriod;
        return this;
    }

    public Long getStartSum() {
        return startSum;
    }

    public PeriodDTO setStartSum(Long startSum) {
        this.startSum = startSum;
        return this;
    }

    public Long getEndSum() {
        return endSum;
    }

    public PeriodDTO setEndSum(Long endSum) {
        this.endSum = endSum;
        return this;
    }

    public Long getExpOnDey() {
        return expOnDey;
    }

    public PeriodDTO setExpOnDey(Long expOnDey) {
        this.expOnDey = expOnDey;
        return this;
    }

    public Integer getPeriodDays() {
        return periodDays;
    }

    public PeriodDTO setPeriodDays(Integer periodDays) {
        this.periodDays = periodDays;
        return this;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public PeriodDTO setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
        return this;
    }

    public Long getPlanId() {
        return planId;
    }

    public PeriodDTO setPlanId(Long planId) {
        this.planId = planId;
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
                ", expOnDey=" + expOnDey +
                ", periodDays=" + periodDays +
                ", operations=" + operations +
                ", planId=" + planId +
                '}';
    }
}
