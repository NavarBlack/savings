package su.savings.helpers;

import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import su.savings.dto.actionModels.Period;
import su.savings.dto.actionModels.Plan;
import su.savings.controllers.tabs.TabPlansController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Utils {
    public static Long getLongToString(String s) {
        if (s.length() > 0) return Long.parseLong(s);
        else return 0L;
    }

    public static LocalDate stringToLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static Plan filPlan(Plan plans, TabPlansController pc) {
        plans.setPlaneName(pc.getFxPlaneName().getText());
        plans.setStartPlane(pc.getFxStartPlane().getValue());
        plans.setEndPlane(pc.getFxEndPlane().getValue());
        plans.setStartSum(Utils.getLongToString(pc.getFxStartSum().getText()));
        plans.setPlanDays(plans.countPlanDays());
        plans.setKeyPoints(Converter.listViewToArrayList(pc.getFxKeyPoints()));
        plans.setPeriods(fitPeriod(plans));
        return plans;
    }

    public static ArrayList<Period> fitPeriod(Plan plans) {
        ArrayList<Period> periodDTOArrayList = new ArrayList<>();
        LocalDate step = plans.getStartPlane();
        Long startSumFirstPeriod = plans.getStartSum();
        Long planDays = plans.getPlanDays();
        Long preliminaryExpOnDay = plans.preliminaryExpOnDey();
        for (LocalDate ld : plans.getKeyPoints()) {
            Period newPeriod = fitPeriodOnModel(step, ld, startSumFirstPeriod, planDays, preliminaryExpOnDay);
            periodDTOArrayList.add(newPeriod);
            step = ld;
            startSumFirstPeriod = newPeriod.getEndSum();
        }
        periodDTOArrayList.add(fitPeriodOnModel(step, plans.getEndPlane(), startSumFirstPeriod, planDays, preliminaryExpOnDay));
        return periodDTOArrayList;
    }

    private static Period fitPeriodOnModel(LocalDate step, LocalDate end, Long statSum, Long planDey, Long preliminaryExpOnDay) {
        Period period = new Period();
        period.setStartPeriod(step)
                .setEndPeriod(end)
                .setStartSum(statSum)
                .setExpOnDey(preliminaryExpOnDay)
                .setPeriodDays(period.countPeriodDays())
                .setPlanDays(planDey)
                .setEndSum(period.countEndSumPeriod());
        return period;
    }

    public static Plan updatePlanOnForm(Plan plans, Period oldPeriod, Period newPeriod) {
        int indexOlpPeriod = plans.getPeriods().indexOf(oldPeriod);
        Long expOnDayPlan = plans.getStatistic().get("expOnDay");
        Long planDay = plans.getPlanDays();
        plans.getPeriods().set(indexOlpPeriod, newPeriod);
        plans.setPeriods(upDatePeriod(plans.getPeriods(), indexOlpPeriod, planDay, expOnDayPlan));
        return plans;
    }

    public static ArrayList<Period> upDatePeriod(ArrayList<Period> periods, Integer indexPeriod, Long planDay, Long expOnDayPlan) {
        if (indexPeriod == 0) {
            return test(periods, planDay, expOnDayPlan, null, false);
        } else if (indexPeriod > 0 && test2(periods)) {
            return test(periods, planDay, expOnDayPlan, null, false);
        } else {
            return test(periods, planDay, expOnDayPlan, indexPeriod, true);
        }

    }

    private static Boolean test2(ArrayList<Period> periods) {
        return periods.stream().noneMatch(period -> {
            assert period != null;
            return period.getFinalSing();
        });
    }

    private static ArrayList<Period> test(ArrayList<Period> periods, Long planDay, Long expOnDayPlan, Integer indexPeriod, Boolean flag) {
        Long startSum;
        if (flag) {
            Period prevPeriod = periods.get(indexPeriod - 1);
            startSum = prevPeriod.getEndSum();
        } else {
            startSum = periods.get(0).getStartSum();
        }
        boolean setNewRemDayExpDay = true;
        Long remDay = planDay;
        Long currentExpOnDay = expOnDayPlan;
        Long pastDay = 0L;
        for (Period period : periods) {
            int indexPeriodCurrent = periods.indexOf(period);
            if (flag && setNewRemDayExpDay && indexPeriodCurrent >= indexPeriod) {
                remDay -= pastDay;
                setNewRemDayExpDay = false;
                currentExpOnDay = ((startSum + period.countOperation()) / remDay) / 100 * 100;
                System.out.println(currentExpOnDay);
            }
            if (flag && indexPeriodCurrent >= indexPeriod) {
                period.setStartSum(startSum).setExpOnDey(currentExpOnDay).setEndSum(period.countEndSumPeriod());
                startSum = period.getEndSum();
            } else if (!flag) {
                period.setStartSum(startSum).setExpOnDey(currentExpOnDay).setEndSum(period.countEndSumPeriod());
                startSum = period.getEndSum();
            }
            pastDay += period.getPeriodDays();
        }
        return periods;
    }


    public static <T> void replacementObj(ListView<T> listView, T oldPlan, T newPlan) {
        int index = listView.getItems().indexOf(oldPlan);
        listView.getItems().set(index, newPlan);
        listView.getSelectionModel().select(newPlan);

    }

    public static <T> void replacementObj(ComboBox<T> comboBox, T oldPlan, T newPlan) {
        int index = comboBox.getItems().indexOf(oldPlan);
        comboBox.getItems().set(index, newPlan);
        comboBox.getSelectionModel().select(newPlan);
    }

//    public static <T> Object ifNull(Object object, T els) {
//        if (Objects.isNull(object)) return els;
//        return object;
//    }

}
