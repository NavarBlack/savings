package su.savings.controllers.tabs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import su.savings.controllers.MainController;
import su.savings.oae.Period;

import java.net.URL;
import java.util.ResourceBundle;


public class TabDaysController implements Initializable {

    private MainController mainController;

    public void init(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void onTest(ActionEvent actionEvent) {
        Period p = mainController.getTabPeriodsController().getFxListViewPeriods().getItems().get(0);
        p.getDays().forEach(System.out::println);
    }
}
