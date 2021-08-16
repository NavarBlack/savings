package su.savings.helpers;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import su.savings.controllers.tabs.TabPlansController;
import su.savings.dto.OperationDTO;
import su.savings.dto.PeriodDTO;
import su.savings.dto.PlansDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Utils {
    public static Integer getIntCheckNull(Long l) {
        if (l != null) return l.intValue();
        else return 0;
    }

    public static Long getLongToString(String s) {
        if (s.length() > 0) return Long.parseLong(s);
        else return 0L;
    }

    public static Integer getIntToString(String s) {
        if (s.length() > 0) return Integer.parseInt(s);
        else return 0;
    }

    public static LocalDate stringToLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static PlansDTO filPlan(PlansDTO plans, TabPlansController pc) {
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

    public static ArrayList<PeriodDTO> fitPeriod(PlansDTO plans) {
        ArrayList<PeriodDTO> periodDTOArrayList = new ArrayList<>();
        LocalDate step = plans.getStartPlane();
        Long startSumFirstPeriod = plans.getStartSum();
        for (LocalDate ld : plans.getKeyPoints()) {
            PeriodDTO period = new PeriodDTO();
            period.setStartPeriod(step);
            period.setEndPeriod(ld);
            period.setStartSum(startSumFirstPeriod);
            period.setExpOnDey(plans.getExpPlanOnDays());
            period.setEndSum(countEndSumPeriod(period));
            periodDTOArrayList.add(period);
            step = ld;
            startSumFirstPeriod = period.getEndSum();
        }
        PeriodDTO period = new PeriodDTO();
        period.setStartPeriod(step);
        period.setEndPeriod(plans.getEndPlane());
        period.setStartSum(startSumFirstPeriod);
        period.setExpOnDey(plans.getExpPlanOnDays());
        period.setEndSum(countEndSumPeriod(period));
        periodDTOArrayList.add(period);
        return periodDTOArrayList;
    }

    public static PlansDTO updatePlanOnForm(PlansDTO plans) {
        plans.setPeriods(upDatePeriod(plans.getPeriods(), plans));
        return plans;
    }

    public static ArrayList<PeriodDTO> upDatePeriod(ArrayList<PeriodDTO> periods, PlansDTO plans) {
        AtomicReference<Long> startSum = new AtomicReference<>(periods.get(0).getStartSum());
        return periods.stream().peek(p -> {
                    p.setStartSum(startSum.get());
                    p.setEndSum(countEndSumPeriod(p));
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

    private static Long countOperation(Long startSum, ArrayList<OperationDTO> operation) {
        Long controlSum = startSum;
        if (operation.isEmpty()) return startSum;
        for (OperationDTO op : operation) {
            long resSum = op.getExpType() ? op.getSum() * -1 : op.getSum();
            controlSum = controlSum + resSum;
        }
        return controlSum;
    }

    private static Long countEndSumPeriod(PeriodDTO period){
        Long expOnDeyInPeriod = period.getExpOnDey() * period.getPeriodDays();
        Long resCountOperation = countOperation(period.getStartSum(), period.getOperations());
        return resCountOperation - expOnDeyInPeriod;
    }

    public static <T> Object ifNull(Object object, T els) {
        if (Objects.isNull(object)) return els;
        return object;
    }

}
