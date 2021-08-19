package su.savings.controllers.tabs;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import su.nevar.controls.NumberField;
import su.savings.oae.Plan;
import su.savings.controllers.MainController;
import su.savings.customFiews.NCell;
import su.savings.db.Repository;
import su.savings.helpers.Utils;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;



public class TabPlansController implements Initializable, NCell.ChangeForm {

    private MainController mainController;

    public void init(MainController mainController) {

        this.mainController = mainController;
    }

    private Plan plans = new Plan();

    @FXML
    private TextField fxPlaneName;
    @FXML
    private DatePicker fxStartPlane;
    @FXML
    private DatePicker fxEndPlane;
    @FXML
    private NumberField fxStartSum;
    @FXML
    private Text fxPlanDays;
    @FXML
    private ListView<LocalDate> fxKeyPoints;
    @FXML
    private DatePicker fxKeyPointsDate;
    @FXML
    private ListView<Plan> fxAllPlans;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Plan> planList = Repository.getAllPlans();
        if (!planList.isEmpty()) {
            plans = planList.get(0);
            fxAllPlans.getItems().setAll(planList);
        }
        fxStartSum.focusedProperty().addListener((observable, oldValue, newValue) ->
                {
                    if (newValue) {
                        if ("0".equals(fxStartSum.getText())) {
                            fxStartSum.setText("");
                        }
                    }
                }
        );
        fxAllPlans.setCellFactory(param -> new NCell(this));
        setDataForm(plans);
        addListener();
    }


    private void setDataForm(Plan plans) {
        fxStartSum.setText(plans.getStartSum().toString());
        fxPlaneName.setText(plans.getPlaneName());
        fxStartPlane.setValue(plans.getStartPlane());
        fxEndPlane.setValue(plans.getEndPlane());
        fxPlanDays.setText("Дней: " + plans.getPlanDays().toString());
        fxKeyPoints.getItems().setAll(FXCollections.observableArrayList(plans.getKeyPoints()));
    }

    private void addListener() {
        fxEndPlane.valueProperty().addListener((obs, ov, nv) -> {
            fxPlanDays.setText("Дней: " + ChronoUnit.DAYS.between(fxStartPlane.getValue(), fxEndPlane.getValue()));
        });
        fxEndPlane.getEditor().textProperty().addListener((obs, ov, nv) -> {
            if (nv.matches("^[0-2]\\d\\.[0|1][0-9]\\.\\d{4}$")) {
                fxEndPlane.setValue(Utils.stringToLocalDate(nv));
                fxPlanDays.setText("Дней: " + ChronoUnit.DAYS.between(fxStartPlane.getValue(), Utils.stringToLocalDate(nv)));
            }
        });

    }

    public void addKeyPoints() {
        if (
                (fxKeyPointsDate.getValue() != null && !fxKeyPoints.getItems().contains(fxKeyPointsDate.getValue())) &&
                        (fxKeyPointsDate.getValue().isAfter(fxStartPlane.getValue()) && fxKeyPointsDate.getValue().isBefore(fxEndPlane.getValue()))
        ) {
            fxKeyPoints.getItems().add(fxKeyPointsDate.getValue());
            fxKeyPoints.getItems().setAll(fxKeyPoints.getItems().sorted());
        }
        fxKeyPointsDate.setValue(null);
    }

    public void remoteKeyPoints() {
        ObservableList<LocalDate> listOfItems = fxKeyPoints.getSelectionModel().getSelectedItems();
        if (!listOfItems.isEmpty()) {
            fxKeyPoints.getItems().remove(listOfItems.get(0));
        }
    }

    public void savePlan() {
        if (validForm()) {
            if (checkContainsPlan()) {
                Plan newPlan = new Plan().masSet(this);
                newPlan.setId(Repository.savePlan(newPlan));
                fxAllPlans.getItems().add(newPlan);
                mainController.getTabPeriodsController().getFxCurrentPlan().setItems(fxAllPlans.getItems());
                mainController.getTabPeriods().setDisable(false);
            } else {
                Plan oldPlan = fxAllPlans
                        .getItems()
                        .stream()
                        .filter(Plan -> Plan.getPlaneName().equals(fxPlaneName.getText()))
                        .findFirst().orElse(null);
                assert oldPlan != null;
                Plan newPlan = oldPlan.masSet(this);
                fxAllPlans.getItems().set(fxAllPlans.getItems().indexOf(oldPlan), newPlan);
                updatePlan(newPlan);
                mainController.getTabPeriods().setDisable(false);
            }
        }
    }

    public void updatePlan(Plan plans) {
    mainController.getTabPeriodsController().getFxListViewPeriods().getSelectionModel().selectFirst();
//        repository.upDatePlan(plans);
    }

    private Boolean validForm() {
        return !(
                fxPlaneName.getText().equals("") ||
                        fxStartPlane.getValue() == null ||
                        fxEndPlane.getValue() == null ||
                        fxStartSum.getText().equals("0") ||
                        fxPlanDays.getText().equals("0") ||
                        fxKeyPoints.getItems().isEmpty()
        );
    }

    private Boolean checkContainsPlan() {
        return fxAllPlans.getItems().stream().noneMatch(plans -> {
            assert plans != null;
            return plans.getPlaneName().equals(fxPlaneName.getText());
        });
    }

    @Override
    public void onChangeForm(Plan plans) {
        setDataForm(plans);
    }

    @Override
    public void onDeletePlan(Plan plan){
        if(!Objects.isNull(plan)) Repository.deletePlan(plan);
    }

    public MainController getMainController() {
        return mainController;
    }

    public Plan getPlans() {
        return plans;
    }

    public TextField getFxPlaneName() {
        return fxPlaneName;
    }

    public DatePicker getFxStartPlane() {
        return fxStartPlane;
    }

    public DatePicker getFxEndPlane() {
        return fxEndPlane;
    }

    public NumberField getFxStartSum() {
        return fxStartSum;
    }

    public Text getFxPlanDays() {
        return fxPlanDays;
    }

    public ListView<LocalDate> getFxKeyPoints() {
        return fxKeyPoints;
    }

    public DatePicker getFxKeyPointsDate() {
        return fxKeyPointsDate;
    }

    public ListView<Plan> getFxAllPlans() {
        return fxAllPlans;
    }
}
