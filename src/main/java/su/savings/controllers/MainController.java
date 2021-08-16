package su.savings.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import su.savings.controllers.tabs.TabDaysController;
import su.savings.controllers.tabs.TabPeriodsController;
import su.savings.controllers.tabs.TabPlansController;
import su.savings.dto.PlansDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private TabPane tabPane;
    @FXML
    private TabPlansController tabPlansController;
    @FXML
    private TabPeriodsController tabPeriodsController;
    @FXML
    private TabDaysController tabDaysController;
    @FXML private MenuItem savePlan;

    public TabPlansController getTabPlansController() {
        return tabPlansController;
    }

    public TabPeriodsController getTabPeriodsController() {
        return tabPeriodsController;
    }

    public TabDaysController getTabDaysController() {
        return tabDaysController;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPlansController.init(this);
        tabPeriodsController.init(this);
        tabDaysController.init(this);
        savePlan.setDisable(true);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.getText().equals("Периоды")){
                tabPeriodsController.setComboBox(tabPlansController.getFxAllPlans().getItems());
                savePlan.setDisable(false);
            }else {
                savePlan.setDisable(true);
            }
        });
    }

    @FXML
    public void onSavePlan (){
        tabPlansController.updatePlan(tabPeriodsController.getFxCurrentPlan().getValue());
    }

}
