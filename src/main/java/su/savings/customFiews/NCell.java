package su.savings.customFiews;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import su.savings.oae.Plan;

import java.io.IOException;

public class NCell extends ListCell<Plan> {
    @FXML
    private Label planeName;
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem deleteItem = new MenuItem();
    private ChangeForm changeForm;

    public NCell(ChangeForm changeForm) {
        this.changeForm = changeForm;
        loadFXML();
        addContextMenu();
        changeFormtOnClick();
    }
    public NCell(){
        loadFXML();
    }

    public interface ChangeForm{
        void onChangeForm(Plan plan);
        void onDeletePlan(Plan plan);
    }

    private void addContextMenu() {
        try {
            this.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    this.setContextMenu(null);
                } else {
                    deleteItem.textProperty().bind(Bindings.format("Удалить \"%s\"", this.itemProperty().getValue().getPlaneName()));
                    deleteItem.setOnAction(event -> {
                        changeForm.onDeletePlan(this.getItem());
                        this.listViewProperty().getValue().getItems().remove(this.getItem());
                    });
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../views/custom/NCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
