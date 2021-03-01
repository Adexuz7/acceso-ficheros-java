package aed.files.xml;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import aed.files.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class XMLModifyCupsController implements Initializable {

	@FXML
	private HBox root;

	@FXML
	private TextField xmlModifyCupsTextField;

	@FXML
	private Button xmlModifyCupsButton;
	
	private Controller controller;
	private String teamName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		xmlModifyCupsButton.setOnAction(e -> onModifyCupsAction(e));
	}

	private void onModifyCupsAction(ActionEvent e) {
		try {
			XML.modifyEarnedCups(teamName, Integer.parseInt(xmlModifyCupsTextField.getText()));
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		controller.updateData();
	}

	public XMLModifyCupsController(String teamName, Controller controller) throws IOException {
		this.controller = controller;
		this.teamName = teamName;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/XMLModifyCupsView.fxml"));
		loader.setController(this);
		loader.load();
	}

	public HBox getView() {
		return root;
	}

}
