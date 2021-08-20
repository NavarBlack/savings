package su.savings.customFiews;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import su.savings.oae.Period;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class NCellPeriodKeyPoint extends BaseCell<Period> {

    @FXML private Text start;
    @FXML private Text end;
    @FXML private Text day;
    @FXML private Text startSum;
    @FXML private Text endSum;
    @FXML private Text expOnDey;
    @FXML private FontAwesomeIconView finalSing;



    public NCellPeriodKeyPoint(){
       loadFXML("NCellPeriodKeyPoint");
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
            finalSing.setVisible(item.getFinalSing());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}
