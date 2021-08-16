package su.savings.dto;

import su.savings.dto.actionModels.Period;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
public class PlansDTO {
    protected Long id = null;
    protected String planeName = "План от "+LocalDate.now();
    protected LocalDate startPlane = LocalDate.now();
    protected LocalDate endPlane =  LocalDate.now().plusMonths(1);
    protected Long startSum = 0L;
    protected Long planDays = ChronoUnit.DAYS.between(startPlane, endPlane);
    protected ArrayList<LocalDate> keyPoints = new ArrayList<>();
    protected ArrayList<Period> periods = new ArrayList<>();

    public ArrayList<LocalDate> getKeyPoints() {
        return keyPoints;
    }

    public PlansDTO setKeyPoints(ArrayList<LocalDate> keyPoints) {
        this.keyPoints = keyPoints;
        return this;
    }

    public Long getId() {
        return id;
    }

    public PlansDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getPlaneName() {
        return planeName;
    }

    public PlansDTO setPlaneName(String planeName) {
        this.planeName = planeName;
        return this;
    }

    public LocalDate getStartPlane() {
        return startPlane;
    }

    public PlansDTO setStartPlane(LocalDate startPlane) {
        this.startPlane = startPlane;
        return this;
    }

    public LocalDate getEndPlane() {
        return endPlane;
    }

    public PlansDTO setEndPlane(LocalDate endPlane) {
        this.endPlane = endPlane;
        return this;
    }

    public Long getStartSum() {
        return startSum;
    }

    public PlansDTO setStartSum(Long startSum) {
        this.startSum = startSum;
        return this;
    }

    public Long getPlanDays() {
        return planDays;
    }

    public PlansDTO setPlanDays(Long planDays) {
        this.planDays = planDays;
        return this;
    }

    public ArrayList<Period> getPeriods() {
        return periods;
    }

    public PlansDTO setPeriods(ArrayList<Period> periods) {
        this.periods = periods;
        return this;
    }

    @Override
    public String toString() {
        return planeName;
    }


    public String toStringMy() {
        return "PlansDTO{" +
                "id=" + id +
                ", planeName='" + planeName + '\'' +
                ", startPlane=" + startPlane +
                ", endPlane=" + endPlane +
                ", startSum=" + startSum +
                ", planDays=" + planDays +
                ", keyPoints=" + keyPoints +
                ", periods=" + periods +
                '}';
    }
}
