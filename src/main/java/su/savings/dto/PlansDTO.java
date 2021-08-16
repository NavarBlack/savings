package su.savings.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

public class PlansDTO {
    private Long id = null;
    private String planeName = "План от "+LocalDate.now();
    private LocalDate startPlane = LocalDate.now();
    private LocalDate endPlane =  LocalDate.now().plusMonths(1);
    private Long startSum = 0L;
    private Long expPlanOnDays = 0L;
    private Integer planDays = Math.toIntExact(ChronoUnit.DAYS.between(startPlane, endPlane));
    private ArrayList<LocalDate> keyPoints = new ArrayList<>();
    private ArrayList<PeriodDTO> periods = new ArrayList<>();

    public Map<String, Long> getStatistic(){
        Long remSum = startSum;
        Long expSum = 0L;
        Long incSum = 0L;
        for (PeriodDTO per : periods){
            Map<String,Long> calcPer = per.getStatistic();
            expSum += calcPer.get("expSum");
            incSum += calcPer.get("incSum");
        }
        remSum = remSum - expSum + incSum;
        return Map.of("remSum", remSum, "expSum",expSum,"incSum", incSum);
    }

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

    public Integer getPlanDays() {
        return planDays;
    }

    public PlansDTO setPlanDays(Integer planDays) {
        this.planDays = planDays;
        return this;
    }

    public ArrayList<PeriodDTO> getPeriods() {
        return periods;
    }

    public PlansDTO setPeriods(ArrayList<PeriodDTO> periods) {
        this.periods = periods;
        return this;
    }

    public Long getExpPlanOnDays() {
        return expPlanOnDays;
    }

    public PlansDTO setExpPlanOnDays(Long expPlanOnDays) {
        this.expPlanOnDays = expPlanOnDays;
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
