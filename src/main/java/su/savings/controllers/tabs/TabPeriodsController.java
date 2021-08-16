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
import su.savings.controllers.MainController;
import su.savings.customFiews.NCell;
import su.savings.customFiews.NCellNCellOperation;
import su.savings.customFiews.NCellPeriodKeyPoint;
import su.savings.dto.OperationDTO;
import su.savings.dto.PeriodDTO;
import su.savings.dto.PlansDTO;

import static su.savings.helpers.Utils.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;


public class TabPeriodsController implements Initializable, NCellNCellOperation.ChangeForm {
    private MainController mainController;
    private Boolean transitionDetected = false;

    public void init(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private ComboBox<PlansDTO> fxCurrentPlan;
    @FXML
    private ListView<PeriodDTO> fxListViewPeriods;
    @FXML
    private ListView<OperationDTO> fxListViewOperations;
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

    private void onSelectOperation(ObservableValue<? extends OperationDTO> observable, OperationDTO ov, OperationDTO nv) {
        remoteOperation.setDisable(Objects.isNull(nv));
    }

    private void onSelectPeriod(ObservableValue<? extends PeriodDTO> observable, PeriodDTO op, PeriodDTO np) {
        remoteOperation.setDisable(Objects.isNull(np));
        addOperation.setDisable(Objects.isNull(np));
        if (np != null) {
            fxListViewOperations.setItems(FXCollections.observableArrayList(np.getOperations()));
            fxRemainsStart.setText(np.getStartSum().toString());
            getStatisticPeriod(np);
        }
    }


    private void onChangeSelectCurrentPlan(ObservableValue<? extends PlansDTO> observableValue, PlansDTO op, PlansDTO np) {
        if (np != null) {
            fxListViewPeriods.getItems().setAll(np.getPeriods());
            fxListViewOperations.getItems().clear();
            getStatisticPlan();
        }
    }

    private void getStatisticPlan(){
        PlansDTO plans = fxCurrentPlan.getValue();
        fxRemainsStartPlan.setText(plans.getStartSum().toString());
        fxRemainsEndPlan.setText(plans.getStatistic().get("remSum").toString());
        fxTotalExpPlan.setText(plans.getStatistic().get("expSum").toString());
        fxTotalIncPlan.setText(plans.getStatistic().get("incSum").toString());
    }

    private void getStatisticPeriod(PeriodDTO period){
        fxRemainsEnd.setText(period.getStatistic().get("remSum").toString());
        fxTotalExp.setText(period.getStatistic().get("expSum").toString());
        fxTotalInc.setText(period.getStatistic().get("incSum").toString());
    }

    public void setComboBox(ObservableList<PlansDTO> observableList) {
        fxCurrentPlan.setItems(observableList);
        if (!transitionDetected) {
            fxCurrentPlan.getSelectionModel().selectFirst();
            transitionDetected = true;
        }
    }

    @FXML
    private void addOperation() {
        PeriodDTO period = fxListViewPeriods.getSelectionModel().getSelectedItem();
        if (!Objects.isNull(period)) {
            if (!fxOperationsSum.getText().equals("")) {
                OperationDTO operation = new OperationDTO();
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
        PeriodDTO period = fxListViewPeriods.getSelectionModel().getSelectedItem();
        OperationDTO operation = fxListViewOperations.getSelectionModel().getSelectedItem();
        fxListViewOperations.getItems().remove(operation);
        period.setOperations(new ArrayList<>(fxListViewOperations.getItems()));
        upDatePlanPeriodOperation(period);
    }


    private void upDatePlanPeriodOperation(PeriodDTO newPeriod) {
        ListView<PlansDTO> plansDTOListView = mainController.getTabPlansController().getFxAllPlans();
        PlansDTO oldPlan = plansDTOListView.getItems().get(plansDTOListView.getItems().indexOf(fxCurrentPlan.getValue()));
        PeriodDTO oldPeriod = oldPlan.getPeriods().stream().filter(op -> op.getStartPeriod() == newPeriod.getStartPeriod()).findFirst().orElse(null);
        oldPlan.getPeriods().set(oldPlan.getPeriods().indexOf(oldPeriod), newPeriod);
        PlansDTO newPlan = updatePlanOnForm(oldPlan);
        replacementObj(plansDTOListView, oldPlan, newPlan);
        replacementObj(fxCurrentPlan, oldPlan, newPlan);
        fxListViewPeriods.getItems().setAll(fxCurrentPlan.getValue().getPeriods());
        fxListViewPeriods.getSelectionModel().select(newPeriod);
    }


    @Override
    public void onChangeForm(OperationDTO plansDTO) {

    }

    public ComboBox<PlansDTO> getFxCurrentPlan() {
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
            default -> System.out.println(keyEvent.getCode().name());
        }
    }
}
