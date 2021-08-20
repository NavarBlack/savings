package su.savings.oae.dto;

import com.google.common.base.MoreObjects;
import su.savings.oae.Days;
import su.savings.oae.Operation;

import java.time.LocalDate;
import java.util.ArrayList;

public class PeriodDTO {
    protected Long id;
    protected LocalDate startPeriod;
    protected LocalDate endPeriod;
    protected Long startSum;
    protected Long endSum;
    protected Long expOnDey;
    protected Long periodDays;
    protected Long pastDaysOnPlan;
    protected ArrayList<Operation> operations = new ArrayList<>();
    protected ArrayList<Days> days = new ArrayList<>();
    protected Long planId;
    protected Boolean finalSing = false;

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

    public Long getPeriodDays() {
        return periodDays;
    }

    public PeriodDTO setPeriodDays(Long periodDays) {
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

    public Boolean getFinalSing() {
        return finalSing;
    }

    public PeriodDTO setFinalSing(Boolean finalSing) {
        this.finalSing = finalSing;
        return this;
    }

    public Long getPastDaysOnPlan() {
        return pastDaysOnPlan;
    }

    public PeriodDTO setPastDaysOnPlan(Long pastDaysOnPlan) {
        this.pastDaysOnPlan = pastDaysOnPlan;
        return this;
    }

    public ArrayList<Days> getDays() {
        return days;
    }

    public PeriodDTO setDays(ArrayList<Days> days) {
        this.days = days;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("startPeriod", startPeriod)
                .add("endPeriod", endPeriod)
                .add("startSum", startSum)
                .add("endSum", endSum)
                .add("expOnDey", expOnDey)
                .add("periodDays", periodDays)
                .add("pastDaysOnPlan", pastDaysOnPlan)
                .add("operations", operations)
                .add("days", days)
                .add("planId", planId)
                .add("finalSing", finalSing)
                .toString();
    }
}
