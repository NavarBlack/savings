package su.savings.customFiews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import su.savings.dto.actionModels.Period;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class NCellPeriodKeyPoint extends ListCell<Period> {

    @FXML private Text start;
    @FXML private Text end;
    @FXML private Text day;
    @FXML private Text startSum;
    @FXML private Text endSum;
    @FXML private Text expOnDey;
    private ChangeForm changeForm;

    public NCellPeriodKeyPoint(ChangeForm changeForm){
        this.changeForm = changeForm;
    }

    public NCellPeriodKeyPoint(){
       loadFXML();
    }

    public interface ChangeForm{
        void onChangeForm(Period plansDTO);
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../views/custom/NCellPeriodKeyPoint.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Period item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            start.setText(item.getStartPeriod().format(formatter));
            end.setText(item.getEndPeriod().format(formatter));
            day.setText(item.getPeriodDays().toString());
            startSum.setText(item.getStartSum().toString());
            endSum.setText(item.getEndSum().toString());
            expOnDey.setText(item.getExpOnDey().toString());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}
