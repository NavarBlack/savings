package su.savings.customFiews;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import su.savings.oae.classinterface.actors.ChangeForm;
import su.savings.oae.classinterface.actors.DeleteItemFromContext;


import java.io.IOException;

public abstract class BaseCell <T> extends ListCell<T> {
    private final ContextMenu contextMenu = new ContextMenu();
    private final MenuItem deleteItem = new MenuItem();
    protected void loadFXML(String fileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/custom/" + fileName + ".fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void addContextMenu(DeleteItemFromContext<T> deleteItemFromContext, String text) {
        try {
            this.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    this.setContextMenu(null);
                } else {
                    deleteItem.textProperty().set(text);
                    deleteItem.setOnAction(event -> {
                        deleteItemFromContext.delete(this.getItem());
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

    protected void changeFormOnClick(ChangeForm<T> changeForm){
        this.setOnMouseClicked(event -> {
            changeForm.change(this.itemProperty().getValue());
        });
    }
}
