package su.savings.helpers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import su.savings.oae.Period;
import su.savings.oae.Plan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Utils {
    public static Long getLongToString(String s) {
        if (s.length() > 0) return Long.parseLong(s);
        else return 0L;
    }

    public static LocalDate stringToLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static Plan updatePlanOnForm(Plan plans, Period oldPeriod, Period newPeriod) {
        int indexOlpPeriod = plans.getPeriods().indexOf(oldPeriod);
        Long expOnDayPlan = plans.getStatistic().get("expOnDay");
        plans.getPeriods().set(indexOlpPeriod, newPeriod);
        plans.setPeriods(upDatePeriods(plans.getPeriods(), indexOlpPeriod, expOnDayPlan));
        return plans;
    }

    public static ArrayList<Period> upDatePeriods(ArrayList<Period> periods, Integer indexPeriod, Long expOnDayPlan) {
        if (indexPeriod == 0) {
            return upDatePeriod(periods, expOnDayPlan, null, false);
        } else if (indexPeriod > 0 && checkFinalSing(periods)) {
            return upDatePeriod(periods, expOnDayPlan, null, false);
        } else {
            return upDatePeriod(periods, expOnDayPlan, indexPeriod, true);
        }

    }

    private static Boolean checkFinalSing(ArrayList<Period> periods) {
        return periods.stream().noneMatch(period -> {
            assert period != null;
            return period.getFinalSing();
        });
    }

    private static ArrayList<Period> upDatePeriod(ArrayList<Period> periods, Long expOnDayPlan, Integer indexPeriod, Boolean flag) {
        Long startSum;
        if (flag) {
            Period prevPeriod = periods.get(indexPeriod - 1);
            startSum = prevPeriod.getEndSum();
        } else {
            startSum = periods.get(0).getStartSum();
        }
        boolean setNewRemDayExpDay = true;
        Long currentExpOnDay = expOnDayPlan;
        for (Period period : periods) {
            int indexPeriodCurrent = periods.indexOf(period);
            if (flag && setNewRemDayExpDay && indexPeriodCurrent >= indexPeriod) {
                setNewRemDayExpDay = false;
                currentExpOnDay = period.currentExpOnDay(startSum);
                System.out.println("currentExpOnDay" + " " + currentExpOnDay);
            }
            if (flag && indexPeriodCurrent >= indexPeriod) {
                period.setStartSum(startSum).setExpOnDey(currentExpOnDay).setEndSum(period.countMoneyStat().get("endSum"));
                startSum = period.getEndSum();
            } else if (!flag) {
                period.setStartSum(startSum).setExpOnDey(currentExpOnDay).setEndSum(period.countMoneyStat().get("endSum"));
                startSum = period.getEndSum();
            }
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
