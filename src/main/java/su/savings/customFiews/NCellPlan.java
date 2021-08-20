package su.savings.customFiews;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import su.savings.oae.Plan;
import su.savings.oae.classinterface.actors.ChangeForm;
import su.savings.oae.classinterface.actors.DeleteItemFromContext;


public class NCellPlan extends BaseCell<Plan> {
    @FXML
    private Label planeName;
    private ChangeForm<Plan> changeForm;

    public NCellPlan(ChangeForm<Plan> changeForm, DeleteItemFromContext<Plan> deleteItemFromContext) {
        this.changeForm = changeForm;
        loadFXML("NCell");
        addContextMenu(deleteItemFromContext, "Удалить план?");
        changeFormOnClick(changeForm);
    }
    public NCellPlan(){
        loadFXML("NCell");
    }

    @Override
    protected void updateItem(Plan item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            planeName.setText(item.getPlaneName());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
