package su.savings.controllers.tabs;


import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import su.nevar.controls.NumberField;
import su.savings.actionModels.Operation;
import su.savings.actionModels.Period;
import su.savings.actionModels.Plan;
import su.savings.controllers.MainController;
import su.savings.customFiews.NCell;
import su.savings.customFiews.NCellNCellOperation;
import su.savings.customFiews.NCellPeriodKeyPoint;

import static su.savings.helpers.Utils.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;


public class TabPeriodsController implements Initializable, NCellNCellOperation.ChangeForm {
    private MainController mainController;
    private Boolean transitionDetected = false;

    public void init(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private ComboBox<Plan> fxCurrentPlan;
    @FXML
    private ListView<Period> fxListViewPeriods;
    @FXML
    private ListView<Operation> fxListViewOperations;
    @FXML
    private TextField fxOperationsName;
    @FXML
    private NumberField fxOperationsSum;
    @FXML
    private CheckBox fxExpType;
    @FXML
    private Text fxRemainsStart;
    @FXML
    private Text fxRemainsEnd;
    @FXML
    private Text fxTotalExp;
    @FXML
    private Text fxTotalInc;
    @FXML
    private Button addOperation;
    @FXML
    private Button remoteOperation;

    @FXML
    private Text fxRemainsStartPlan;
    @FXML
    private Text fxRemainsEndPlan;
    @FXML
    private Text fxTotalExpPlan;
    @FXML
    private Text fxTotalIncPlan;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fxCurrentPlan.setCellFactory(param -> new NCell());
        fxCurrentPlan.valueProperty().addListener(this::onChangeSelectCurrentPlan);
        fxListViewPeriods.setCellFactory(param -> new NCellPeriodKeyPoint());
        fxListViewPeriods.getSelectionModel().selectedItemProperty().addListener(this::onSelectPeriod);
        fxListViewOperations.setCellFactory(param -> new NCellNCellOperation(this));
        fxListViewOperations.getSelectionModel().selectedItemProperty().addListener(this::onSelectOperation);
        addOperation.setDisable(true);
        remoteOperation.setDisable(true);

    }

    private void onSelectOperation(ObservableValue<? extends Operation> observable, Operation ov, Operation nv) {
        remoteOperation.setDisable(Objects.isNull(nv));
    }

    private void onSelectPeriod(ObservableValue<? extends Period> observable, Period op, Period np) {
        remoteOperation.setDisable(Objects.isNull(np));
        addOperation.setDisable(Objects.isNull(np));
        if (np != null) {
            fxListViewOperations.setItems(FXCollections.observableArrayList(np.getOperations()));
            fxRemainsStart.setText(np.getStartSum().toString());
            getStatisticPeriod(np);
        }
    }


    private void onChangeSelectCurrentPlan(ObservableValue<? extends Plan> observableValue, Plan op, Plan np) {
        if (np != null) {
            fxListViewPeriods.getItems().setAll(np.getPeriods());
            fxListViewOperations.getItems().clear();
            getStatisticPlan();
        }
    }

    private void getStatisticPlan(){
        Plan plans = fxCurrentPlan.getValue();
        fxRemainsStartPlan.setText(plans.getStartSum().toString());
        fxRemainsEndPlan.setText(plans.getStatistic().get("totalRem").toString());
        fxTotalExpPlan.setText(plans.getStatistic().get("expSum").toString());
        fxTotalIncPlan.setText(plans.getStatistic().get("incSum").toString());
    }

    private void getStatisticPeriod(Period period){
        fxRemainsEnd.setText(period.countEndSumPeriod().toString());
        fxTotalExp.setText(period.getStatistic().get("expSum").toString());
        fxTotalInc.setText(period.getStatistic().get("incSum").toString());
    }

    public void setComboBox(ObservableList<Plan> observableList) {
        fxCurrentPlan.setItems(observableList);
        if (!transitionDetected) {
            fxCurrentPlan.getSelectionModel().selectFirst();
            transitionDetected = true;
        }
    }

    @FXML
    private void addOperation() {
        Period period = fxListViewPeriods.getSelectionModel().getSelectedItem();
        if (!Objects.isNull(period)) {
            if (!fxOperationsSum.getText().equals("")) {
                Operation operation = new Operation();
                if (!fxOperationsName.getText().equals("")) operation.setName(fxOperationsName.getText());
                operation.setPeriodId(period.getId());
                operation.setSum(getLongToString(fxOperationsSum.getText()));
                operation.setExpType(fxExpType.isSelected());
                fxListViewOperations.getItems().add(operation);
            }
            period.setOperations(new ArrayList<>(fxListViewOperations.getItems()));
            upDatePlanPeriodOperation(period);
            getStatisticPlan();
            fxOperationsSum.setText("");
        }
    }

    @FXML
    private void remoteOperation() {
        Period period = fxListViewPeriods.getSelectionModel().getSelectedItem();
        Operation operation = fxListViewOperations.getSelectionModel().getSelectedItem();
        fxListViewOperations.getItems().remove(operation);
        period.setOperations(new ArrayList<>(fxListViewOperations.getItems()));
        upDatePlanPeriodOperation(period);
    }


    private void upDatePlanPeriodOperation(Period newPeriod) {
        ListView<Plan> PlanListView = mainController.getTabPlansController().getFxAllPlans();
        Plan oldPlan = PlanListView.getItems().get(PlanListView.getItems().indexOf(fxCurrentPlan.getValue()));
        Period oldPeriod = oldPlan.getPeriods().stream().filter(op -> op.getStartPeriod() == newPeriod.getStartPeriod()).findFirst().orElse(null);
        oldPlan.getPeriods().set(oldPlan.getPeriods().indexOf(oldPeriod), newPeriod);
        Plan newPlan = updatePlanOnForm(oldPlan);
        replacementObj(PlanListView, oldPlan, newPlan);
        replacementObj(fxCurrentPlan, oldPlan, newPlan);
        fxListViewPeriods.getItems().setAll(fxCurrentPlan.getValue().getPeriods());
        fxListViewPeriods.getSelectionModel().select(newPeriod);
    }


    @Override
    public void onChangeForm(Operation Plan) {

    }

    public ComboBox<Plan> getFxCurrentPlan() {
        return fxCurrentPlan;
    }

    public void operationSaveOnEnter(KeyEvent keyEvent) {
        switch (keyEvent.getCode().name()) {
            case "ENTER" -> addOperation();
            case "SUBTRACT" -> fxExpType.setSelected(true);
            case "ADD" -> fxExpType.setSelected(false);
            case "UP" -> fxListViewPeriods.getSelectionModel().selectPrevious();
            case "DOWN" -> {
                fxListViewPeriods.getSelectionModel().selectNext();
            }
        }
    }

    public void onTest(){
        Period p = fxListViewPeriods.getSelectionModel().getSelectedItem();
        System.out.println("" + p.toString());
        System.out.println("p.countOperation() = " + p.countOperation());
        System.out.println("p.countEndSumPeriod() = " + p.countEndSumPeriod());
    }
}
