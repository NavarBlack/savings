package su.savings.helpers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import su.savings.actionModels.Period;
import su.savings.actionModels.Plan;
import su.savings.controllers.tabs.TabPlansController;
import su.savings.dto.PlansDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Utils {
//    public static Integer getIntCheckNull(Long l) {
//        if (l != null) return l.intValue();
//        else return 0;
//    }

    public static Long getLongToString(String s) {
        if (s.length() > 0) return Long.parseLong(s);
        else return 0L;
    }

//    public static Integer getIntToString(String s) {
//        if (s.length() > 0) return Integer.parseInt(s);
//        else return 0;
//    }

    public static LocalDate stringToLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static Plan filPlan(Plan plans, TabPlansController pc) {
        plans.setPlaneName(pc.getFxPlaneName().getText());
        plans.setStartPlane(pc.getFxStartPlane().getValue());
        plans.setEndPlane(pc.getFxEndPlane().getValue());
        plans.setStartSum(Utils.getLongToString(pc.getFxStartSum().getText()));
        plans.setExpPlanOnDays(pc.countExpOnDey());
        plans.setPlanDays((int) ChronoUnit.DAYS.between(pc.getFxStartPlane().getValue(), pc.getFxEndPlane().getValue()));
        plans.setKeyPoints(Converter.listViewToArrayList(pc.getFxKeyPoints()));
        plans.setPeriods(fitPeriod(plans));
        return plans;
    }

    public static ArrayList<Period> fitPeriod(Plan plans) {
        ArrayList<Period> periodDTOArrayList = new ArrayList<>();
        LocalDate step = plans.getStartPlane();
        Long startSumFirstPeriod = plans.getStartSum();
        for (LocalDate ld : plans.getKeyPoints()) {
            Period newPeriod = fitPeriodOnModel(step, ld, startSumFirstPeriod, plans.getExpPlanOnDays());
            periodDTOArrayList.add(newPeriod);
            step = ld;
            startSumFirstPeriod = newPeriod.getEndSum();
        }
        periodDTOArrayList.add(fitPeriodOnModel(step, plans.getEndPlane(), startSumFirstPeriod, plans.getExpPlanOnDays()));
        return periodDTOArrayList;
    }

    private static Period fitPeriodOnModel(LocalDate step, LocalDate end, Long statSum, Long expOnDey){
        Period period = new Period();
        period.setStartPeriod(step);
        period.setEndPeriod(end);
        period.setStartSum(statSum);
        period.setExpOnDey(expOnDey);
        period.setPeriodDays(period.countPeriodDays());
        period.setEndSum(period.countEndSumPeriod());
        return period;
    }

    public static Plan updatePlanOnForm(Plan plans) {
        plans.setExpPlanOnDays(plans.getStatistic().get("expOnDey"));
        plans.setPeriods(upDatePeriod(plans.getPeriods(), plans.getExpPlanOnDays()));
        return plans;
    }

    public static ArrayList<Period> upDatePeriod(ArrayList<Period> periods, Long expOnDey) {
        AtomicReference<Long> startSum = new AtomicReference<>(periods.get(0).getStartSum());
        return periods.stream().peek(p -> {
                    p.setStartSum(startSum.get());
                    p.setExpOnDey(expOnDey);
                    p.setEndSum(p.countEndSumPeriod());
                    startSum.set(p.getEndSum());
                }
        ).collect(Collectors.toCollection(ArrayList::new));
    }


    public static <T> void replacementObj (ListView<T> listView, T oldPlan, T newPlan) {
        int index = listView.getItems().indexOf(oldPlan);
        listView.getItems().set(index,newPlan);
        listView.getSelectionModel().select(newPlan);

    }

    public static <T> void replacementObj (ComboBox<T> comboBox, T oldPlan, T newPlan) {
        int index = comboBox.getItems().indexOf(oldPlan);
        comboBox.getItems().set(index,newPlan);
        comboBox.getSelectionModel().select(newPlan);
    }

//    public static <T> Object ifNull(Object object, T els) {
//        if (Objects.isNull(object)) return els;
//        return object;
//    }

}
