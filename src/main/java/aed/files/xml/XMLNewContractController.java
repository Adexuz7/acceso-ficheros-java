package aed.files.xml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class XMLNewContractController implements Initializable {

    @FXML
    private GridPane root;

    @FXML
    private TextField xmlNCTextField;

    @FXML
    private DatePicker xmlNCstartingDatePicker;

    @FXML
    private DatePicker xmlNCstartingDatePickerxmlNCendingDatePicker;

    @FXML
    private Button xmlNCsaveButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public XMLNewContractController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/XMLNewContractView.fxml"));
		loader.setController(this);
		loader.load();
	}

}
