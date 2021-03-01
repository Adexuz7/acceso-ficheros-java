package aed.files.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class XML {

	private static Document documentJDOM;
	private static Element rootElement;
	private static List<Element> teams;

	public static void main(String[] args) {
		createDoc();
		visualize();
		modifyEarnedCups("Tenerife", 7);
		deleteTeam("Gran Canaria");

		addContract("GANONDROF", "Tenerife", new GregorianCalendar(2021, Calendar.FEBRUARY, 25).getTime(),
				new GregorianCalendar(2021, Calendar.DECEMBER, 25).getTime());

		saveDoc();
	}

	@SuppressWarnings("unchecked")
	public static void createDoc() {

		SAXBuilder builder = new SAXBuilder();

		try {
			documentJDOM = builder.build(new FileInputStream("src/main/resources/Equipos.xml"));
			rootElement = documentJDOM.getRootElement();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		teams = rootElement.getChildren();

	}

	public static Element getTeam(String teamName) {

		Element selectedTeam = null;

		for (Element team : teams) {

			String currentTeamName = team.getAttributeValue("nomEquipo");

			if (currentTeamName.equals(teamName)) {
				selectedTeam = team;
			}
		}

		return selectedTeam;
	}

	public static ObservableList<String> getTeams() {
		ObservableList<String> observableTeamList = FXCollections.observableArrayList();

		for (Element team : teams) {
			observableTeamList.add(team.getAttributeValue("nomEquipo"));
		}

		return observableTeamList;
	}
	
	@SuppressWarnings("unchecked")
	public static ObservableList<String> getTeamPlayers(Element team) {
		List<Element> contracts = team.getChild("Contratos").getChildren();
		
		ObservableList<String> observableTeamPlayersList = FXCollections.observableArrayList();
		
		for (Element contract : contracts) {
			observableTeamPlayersList.add(contract.getValue());
		}
		
		return observableTeamPlayersList;	
	}

	public static void modifyEarnedCups(String teamName, int earnedCups) {
		Element team = getTeam(teamName);

		if (team != null)
			team.setAttribute("copasGanadas", Integer.toString(earnedCups));
	}

	public static void deleteTeam(String teamName) {
		Element team = getTeam(teamName);

		if (team != null)
			teams.remove(team);
	}

	public static String visualize() {

		XMLOutputter xmlOutputter = new XMLOutputter();

		for (Element team : teams) {

			String teamName = team.getAttributeValue("nomEquipo");
			String earnedCups = team.getAttributeValue("copasGanadas");
			String codLiga = team.getChild("codLiga").getValue();

			String text = team.getValue();

			System.out.println("Nombre: " + teamName);
			System.out.println("Copas: " + earnedCups);
			System.out.println("Liga: " + codLiga);
			System.out.println("Resto de morralla: " + text);

			// String id = team.getAttributeValue("id");

		}

		return xmlOutputter.outputString(documentJDOM);

	}

	public static void addContract(String playerName, String teamName, Date startDate, Date endDate) {
		Element team = getTeam(teamName);

		Element contract = new Element("Futbolista");
		contract.setAttribute("fechaInicio", dateFormatter(startDate));
		contract.setAttribute("fechaFin", dateFormatter(endDate));
		contract.setText(playerName);

		Element contracts = team.getChild("Contratos");
		contracts.addContent(contract);

	}

	public static void saveDoc() {
		try {
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream file = new FileOutputStream("NuevoFichero.xml");
			out.output(documentJDOM, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String dateFormatter(Date date) {
		String pattern = "yyyy-MM-dd";

		DateFormat df = new SimpleDateFormat(pattern);

		return df.format(date);
	}

}
