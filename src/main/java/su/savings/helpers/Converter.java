package su.savings.helpers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Converter {
    public static <T> ArrayList<T> arrayToList(Array array) throws SQLException {
        ArrayList<T> stringList = new ArrayList<>();
        for (Object obj : (Object[]) array.getArray()) {
            try {
                T arr = (T) obj;
                stringList.add(arr);
            } catch (ClassCastException e) {
                System.out.println("error");
                e.printStackTrace();

            }
        }
        return stringList;
    }

    public static ArrayList<LocalDate> stringToArrayList(String string) {
        ArrayList<LocalDate> localDateArrayList = new ArrayList<>();
        String[] date = string.split(",");
        for (String s : date){
            localDateArrayList.add(LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return localDateArrayList;
    }

    public static ArrayList<LocalDate> listViewToArrayList(ListView<LocalDate> listView) {
        ArrayList<LocalDate> localDateArrayList = new ArrayList<>();
        for (LocalDate localDate : listView.getItems().stream().toList()) {
            try {
                localDateArrayList.add(localDate);
            } catch (ClassCastException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
        return localDateArrayList;
    }

//    public static <T> ListView<T> listToListView(List<T> list){
//        ObservableList<T> observableArray = FXCollections.observableArrayList();
//        observableArray.addAll(list);
//        return observableArray;
//    }
}
