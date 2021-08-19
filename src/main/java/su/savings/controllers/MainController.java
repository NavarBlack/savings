package su.savings.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import su.savings.controllers.tabs.TabDaysController;
import su.savings.controllers.tabs.TabPeriodsController;
import su.savings.controllers.tabs.TabPlansController;
import su.savings.oae.Period;
import su.savings.oae.Plan;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabPlan;
    @FXML
    private Tab tabPeriod;
    @FXML
    private Tab tabDay;

    @FXML
    private SplitPane tabPlans;
    @FXML
    private TabPlansController tabPlansController;

    @FXML
    private AnchorPane tabPeriods;
    @FXML
    private TabPeriodsController tabPeriodsController;

    @FXML
    private AnchorPane tabDays;
    @FXML
    private TabDaysController tabDaysController;

    @FXML private MenuItem savePlan;
    @FXML private MenuItem lockPeriod;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPlansController.init(this);
        tabPeriodsController.init(this);
        tabDaysController.init(this);
        savePlan.setDisable(true);
        tabPeriod.setDisable(true);
        ArrayList<Plan> plans = new ArrayList<>(tabPlansController.getFxAllPlans().getItems());
        if(!plans.isEmpty()){
            tabPeriodsController.setComboBox(plans);
            tabPeriod.setDisable(false);
        }
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.getText().equals("Периоды")){
                savePlan.setDisable(false);
                lockPeriod.setDisable(false);
            }else {
                savePlan.setDisable(true);
                lockPeriod.setDisable(true);
            }
        });
    }

    @FXML
    public void onSavePlan (){
        tabPeriodsController.getFxCurrentPlan().getValue().update();
    }
    @FXML
    public void setLockPeriod(){
        ArrayList<Period> periods = tabPeriodsController.getFxCurrentPlan().getValue().getPeriods();
        if(Objects.nonNull(periods)){
            for(Period period : periods){
                period.setFinalSing(!period.getFinalSing());
            }
            tabPeriodsController.getFxListViewPeriods().getItems().setAll(periods);
        }

    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public MainController setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
        return this;
    }

    public Tab getTabPlan() {
        return tabPlan;
    }

    public MainController setTabPlans(Tab tabPlan) {
        this.tabPlan = tabPlan;
        return this;
    }

    public Tab getTabPeriods() {
        return tabPeriod;
    }

    public MainController setTabPeriods(Tab tabPeriod) {
        this.tabPeriod = tabPeriod;
        return this;
    }

    public Tab getTabDays() {
        return tabDay;
    }

    public MainController setTabDays(Tab tabDay) {
        this.tabDay = tabDay;
        return this;
    }

    public TabPlansController getTabPlansController() {
        return tabPlansController;
    }

    public MainController setTabPlansController(TabPlansController tabPlansController) {
        this.tabPlansController = tabPlansController;
        return this;
    }

    public TabPeriodsController getTabPeriodsController() {
        return tabPeriodsController;
    }

    public MainController setTabPeriodsController(TabPeriodsController tabPeriodsController) {
        this.tabPeriodsController = tabPeriodsController;
        return this;
    }

    public TabDaysController getTabDaysController() {
        return tabDaysController;
    }

    public MainController setTabDaysController(TabDaysController tabDaysController) {
        this.tabDaysController = tabDaysController;
        return this;
    }

    public MenuItem getSavePlan() {
        return savePlan;
    }

    public MainController setSavePlan(MenuItem savePlan) {
        this.savePlan = savePlan;
        return this;
    }

    public MenuItem getLockPeriod() {
        return lockPeriod;
    }

    public MainController setLockPeriod(MenuItem lockPeriod) {
        this.lockPeriod = lockPeriod;
        return this;
    }
}
