package aed.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.jdom.Element;

import aed.files.xml.XML;
import aed.files.xml.XMLModifyCupsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller implements Initializable {

	@FXML
	private TabPane root;

	@FXML
	private Label checkFileLabel;

	@FXML
	private TextField currentPathTextField;

	@FXML
	private TextField fileTextField;

	@FXML
	private Button checkFileButton;

	@FXML
	private Button createButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button moveButton;

	@FXML
	private Button showFilesButton;

	@FXML
	private ListView<String> filesListView;

	@FXML
	private Button showContentButton;

	@FXML
	private Button modifyButton;

	@FXML
	private TextArea fileContentTextArea;

	@FXML
	private CheckBox isFileCheckBox;

	@FXML
	private CheckBox isFolderCheckBox;

	@FXML
	private ListView<String> xmlTeamsListView;

	@FXML
	private Button xmlVisualizeTeamDataButton;

	@FXML
	private ListView<String> xmlTeamPlayersList;

	@FXML
	private Button xmlDeleteTeamButton;

	@FXML
	private Button xmlNewContractButton;

	@FXML
	private Button xmlModifyCupsButton;

	@FXML
	private Button xmlSaveButton;

	@FXML
	private Label xmlLeagueLabel;

	@FXML
	private Label xmlEarnedCupsLabel;

	private File[] pathnames;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Access file functions
		checkFileButton.setOnAction(e -> onCheckFileAction(e));
		createButton.setOnAction(e -> onCreateFileAction(e));
		moveButton.setOnAction(e -> onMoveButtonAction(e));
		deleteButton.setOnAction(e -> onDeleteButtonAction(e));
		showFilesButton.setOnAction(e -> onShowFilesAction(e));
		modifyButton.setOnAction(e -> onModifyFileAction(e));
		showContentButton.setOnAction(e -> onShowFileContentAction(e));

		// Set content of xml tab
		XML.createDoc();
		xmlTeamsListView.setItems(XML.getTeams());

		// XML functions
		xmlVisualizeTeamDataButton.setOnAction(e -> onVisualizeTeamData(e));
		xmlDeleteTeamButton.setOnAction(e -> onDeleteTeamAction(e));
		xmlModifyCupsButton.setOnAction(e -> onModifyCupsAction(e));
		xmlSaveButton.setOnAction(e -> onSaveAction(e));

	}

	private void onCheckFileAction(ActionEvent e) {
		File f = new File(currentPathTextField.getText());

		if (f.exists()) {
			checkFileLabel.setText("Sí existe");

			if (f.isDirectory()) {
				isFolderCheckBox.setSelected(true);
				isFileCheckBox.setSelected(false);
			} else {
				isFolderCheckBox.setSelected(false);
				isFileCheckBox.setSelected(true);
			}

		} else {
			checkFileLabel.setText("No existe");
			isFolderCheckBox.setSelected(false);
			isFileCheckBox.setSelected(false);
		}
	}

	private void onCreateFileAction(ActionEvent e) {
		File file = new File(currentPathTextField.getText());

		try {
			if (file.createNewFile()) {
				System.out.println("Fichero creado");
			} else {
				System.out.println("El fichero ya existe");
			}
		} catch (IOException e1) {
			System.out.println("Houston, tenemos un problema");
			e1.printStackTrace();
		}
	}

	private void onMoveButtonAction(ActionEvent e) {
		File file = new File(currentPathTextField.getText());

		if (file.renameTo(new File(file.getParent() + File.separator + fileTextField.getText()))) {
			System.out.println("fichero movido");
		} else {
			System.out.println("el fichero no se ha movido");
		}
	}

	private void onDeleteButtonAction(ActionEvent e) {
		File file = new File(currentPathTextField.getText());

		if (file.delete()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Look, an Information Dialog");
			alert.setContentText("El fichero ha sido eliminado");

			alert.showAndWait();

			System.out.println("Fichero borrado");
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Look, an Error Dialog");
			alert.setContentText("No se ha podido eliminar el fichero");

			alert.showAndWait();

			System.out.println("Fichero NO borrado");
		}

	}

	private void onShowFilesAction(ActionEvent e) {
		File file = new File(currentPathTextField.getText());
		// File[] pathnames;
		ArrayList<String> filesArrayList = new ArrayList<String>();

		if (file.isDirectory()) {
			pathnames = file.listFiles();

		} else {
			File parent = new File(file.getParent());
			pathnames = parent.listFiles();
		}

		for (File pathname : pathnames) {
			filesArrayList.add(pathname.getName());
		}

		ObservableList<String> list = FXCollections.observableArrayList(filesArrayList);

		filesListView.setItems(list);

		if (filesListView.getItems().contains(file.getName())) {
			filesListView.getSelectionModel().select(file.getName());
		} else {
			filesListView.getSelectionModel().clearSelection();
		}

		filesListView.requestFocus();
	}

	private void onShowFileContentAction(ActionEvent e) {
		fileContentTextArea.clear();
		try {
			File file = null;
			for (File pathname : pathnames) {
				if (pathname.getName().equals(filesListView.getSelectionModel().getSelectedItem())) {
					file = new File(pathname.getPath());
				}
			}

			Scanner reader = new Scanner(file);
			int i = 0;
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				System.out.println(line);
				if (i == 0) {
					fileContentTextArea.setText(line);
				} else {
					fileContentTextArea.setText(fileContentTextArea.getText() + System.lineSeparator() + line);
				}
				i++;
			}
			reader.close();

		} catch (FileNotFoundException e1) {
			System.out.println("Ha ocurrido un error.");
			e1.printStackTrace();
		}
	}

	private void onModifyFileAction(ActionEvent e) {
		try {
			FileWriter fileWriter = new FileWriter(currentPathTextField.getText());
			fileWriter.write(fileContentTextArea.getText());
			fileWriter.close();
			System.out.println("Se ha modificado el fichero.");
		} catch (IOException e1) {
			System.out.println("Ha ocurrido un error.");
			e1.printStackTrace();
		}
	}

	private void onDeleteTeamAction(ActionEvent e) {
		XML.deleteTeam(xmlTeamsListView.getSelectionModel().getSelectedItem());
		xmlTeamsListView.setItems(XML.getTeams());
	}

	private void onVisualizeTeamData(ActionEvent e) {
		updateData();
	}

	public void updateData() {
		Element team = XML.getTeam(xmlTeamsListView.getSelectionModel().getSelectedItem());

		xmlLeagueLabel.setText(team.getChild("codLiga").getValue());
		xmlEarnedCupsLabel.setText(team.getAttributeValue("copasGanadas"));
		xmlTeamPlayersList.setItems(XML.getTeamPlayers(team));
	}

	private void onModifyCupsAction(ActionEvent e) {
		String team = xmlTeamsListView.getSelectionModel().getSelectedItem();

		try {
			XMLModifyCupsController modifyCupsController = new XMLModifyCupsController(team, this);
			Scene scene = new Scene(modifyCupsController.getView());
			Stage stage = new Stage();

			stage.setTitle("Modificar copas");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void onSaveAction(ActionEvent e) {
		XML.saveDoc();
	}

	public Controller() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
		loader.setController(this);
		loader.load();
	}

	public TabPane getView() {
		return root;
	}

}
