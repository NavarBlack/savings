package su.savings.customFiews;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import su.savings.oae.Operation;
import su.savings.oae.classinterface.actors.DeleteItemFromContext;


public class NCellOperation extends BaseCell<Operation> {
    @FXML
    private Label name;
    @FXML
    private Label sum;
    @FXML
    private Label type;

    public NCellOperation(DeleteItemFromContext<Operation> deleteItemFromContext) {
        loadFXML("NCellOperation");
        addContextMenu(deleteItemFromContext, "Удалить операцию?");
    }


    @Override
    protected void updateItem(Operation item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            long resSum = item.getExpType() ? item.getSum() * -1 : item.getSum();
            name.setText(item.getName());
            sum.setText(Long.toString(resSum));
            type.setText(item.getExpType() ? "Расход" : "Доход");
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
