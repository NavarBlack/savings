package su.savings.oae.dto;

import com.google.common.base.MoreObjects;
import su.savings.oae.Operation;

import java.time.LocalDate;
import java.util.ArrayList;

public class DaysDto {
    protected Long id;
    protected Long periodId;
    protected Long remainsStart = 0L;
    protected Long remainsFact = 0L;
    protected Long accumulation = 0L;
    protected Long expDay = 0L;
    protected ArrayList<Operation> operations = new ArrayList<>();
    protected Long remainsEnd = 0L;
    protected Long remainsEndDeficit = 0L;
    protected Long remainsEndProfit = 0L;
    protected LocalDate date = LocalDate.now();

    public Long getId() {
        return id;
    }

    public DaysDto setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public DaysDto setPeriodId(Long periodId) {
        this.periodId = periodId;
        return this;
    }

    public Long getRemainsStart() {
        return remainsStart;
    }

    public DaysDto setRemainsStart(Long remainsStart) {
        this.remainsStart = remainsStart;
        return this;
    }

    public Long getRemainsFact() {
        return remainsFact;
    }

    public DaysDto setRemainsFact(Long remainsFact) {
        this.remainsFact = remainsFact;
        return this;
    }

    public Long getAccumulation() {
        return accumulation;
    }

    public DaysDto setAccumulation(Long accumulation) {
        this.accumulation = accumulation;
        return this;
    }

    public Long getExpDay() {
        return expDay;
    }

    public DaysDto setExpDay(Long expDay) {
        this.expDay = expDay;
        return this;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public DaysDto setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
        return this;
    }

    public Long getRemainsEnd() {
        return remainsEnd;
    }

    public DaysDto setRemainsEnd(Long remainsEnd) {
        this.remainsEnd = remainsEnd;
        return this;
    }

    public Long getRemainsEndDeficit() {
        return remainsEndDeficit;
    }

    public DaysDto setRemainsEndDeficit(Long remainsEndDeficit) {
        this.remainsEndDeficit = remainsEndDeficit;
        return this;
    }

    public Long getRemainsEndProfit() {
        return remainsEndProfit;
    }

    public DaysDto setRemainsEndProfit(Long remainsEndProfit) {
        this.remainsEndProfit = remainsEndProfit;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public DaysDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("periodId", periodId)
                .add("remainsStart", remainsStart)
                .add("remainsFact", remainsFact)
                .add("accumulation", accumulation)
                .add("expDay", expDay)
                .add("operations", operations)
                .add("remainsEnd", remainsEnd)
                .add("remainsEndDeficit", remainsEndDeficit)
                .add("remainsEndProfit", remainsEndProfit)
                .add("date", date)
                .toString();
    }
}
