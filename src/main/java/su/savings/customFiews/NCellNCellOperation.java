package su.savings.customFiews;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import su.savings.oae.Operation;

import java.io.IOException;

public class NCellNCellOperation extends ListCell<Operation> {
    @FXML
    private Label name;
    @FXML
    private Label sum;
    @FXML
    private Label type;
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem deleteItem = new MenuItem();
    private ChangeForm changeForm;

    public NCellNCellOperation(ChangeForm changeForm) {
        this.changeForm = changeForm;
        loadFXML();
        addContextMenu();
        changeFormtOnClick();
    }
    public NCellNCellOperation(){
        loadFXML();
    }

    public interface ChangeForm{
        void onChangeForm(Operation plansDTO);
    }

    private void addContextMenu() {
        try {
            this.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    this.setContextMenu(null);
                } else {
                    deleteItem.textProperty().bind(Bindings.format("Удалить \"%s\"", this.itemProperty().getValue().getName()));
                    deleteItem.setOnAction(event -> this.listViewProperty().getValue().getItems().remove(this.getItem()));
                    contextMenu.getItems().setAll(deleteItem);
                    this.setContextMenu(contextMenu);
                }
            });
        } catch (Exception e) {
            System.out.println("addContextMenu");
        }
    }

    private void changeFormtOnClick(){
        this.setOnMouseClicked(event -> {
          changeForm.onChangeForm(this.itemProperty().getValue());
        });
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../views/custom/NCellOperation.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
