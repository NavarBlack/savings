package su.savings.helpers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static Long getLongToString(String s) {
        if (s.length() > 0) return Long.parseLong(s);
        else return 0L;
    }

    public static LocalDate stringToLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
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
