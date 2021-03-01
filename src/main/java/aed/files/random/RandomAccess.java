package aed.files.random;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RandomAccess {

	private static final int TEAM_BYTES = 126;
	private static final int EARNED_CUPS_BYTES = 117;
	private static RandomAccessFile file = null;

	public static void loadData() {
		try {
			file = new RandomAccessFile("src/main/resources/datos.dat", "rw");
			// insertExampleData();
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private static void insertExampleData() {
		insertTeamData(1, "Fútbol Club Barcelona", "LFP", "Barcelona, Spain", 79, true);
		insertTeamData(2, "Real Madrid Fútbol Club", "LFP", "Madrid, Spain", 102, true);
		insertTeamData(3, "Club Atlético de Madrid", "LFP", "Madrid, Spain", 32, true);
		insertTeamData(4, "Club Deportivo Tenerife", "LFP", "Canary Islands, Spain", 0, false);
		insertTeamData(5, "Unión Deportiva Las Palmas", "LFP", "Canary Islands, Spain", 0, false);
	}

	public static void insertTeamData(int codigoEquipo, String nombreEquipo, String codigoLiga, String localidad,
			int numeroCopasGanadas, boolean internacional) {

		try {
			file.writeInt(codigoEquipo);
			file.writeChar(',');

			file.writeBytes(getFilledString(nombreEquipo, 40));
			file.writeChar(',');

			file.writeBytes(getFilledString(codigoLiga, 5));
			file.writeChar(',');

			file.writeBytes(getFilledString(localidad, 60));
			file.writeChar(',');

			file.writeInt(numeroCopasGanadas);
			file.writeChar(',');

			file.writeBoolean(internacional);
			file.writeChar(',');
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getFilledString(String ogStr, int length) {
		char[] newString = new char[length];
		Arrays.fill(newString, '\0');

		if (ogStr.length() <= length) {
			for (int i = 0; i < ogStr.length(); i++) {
				newString[i] = ogStr.charAt(i);
			}
		} else {
			for (int i = 0; i < length; i++) {
				newString[i] = ogStr.charAt(i);
			}
		}

		return new String(newString);
	}

	public static String getData() {
		String data = "";

		try {
			file.seek(0); // nos situamos al principio
			while (true) {
				byte[] nombreEquipoBytes = new byte[40];
				byte[] codigoLigaBytes = new byte[5];
				byte[] localidadBytes = new byte[60];

				data += file.readInt();
				data += file.readChar();

				data += " ";

				file.read(nombreEquipoBytes);
				data += new String(nombreEquipoBytes);
				data += file.readChar();

				data += " ";

				file.read(codigoLigaBytes);
				data += new String(codigoLigaBytes);
				data += file.readChar();

				data += " ";

				file.read(localidadBytes);
				data += new String(localidadBytes);
				data += file.readChar();

				data += " ";

				data += file.readInt();
				data += file.readChar();

				data += " ";

				data += file.readBoolean();
				data += file.readChar();

				data += System.lineSeparator();
			}
		} catch (EOFException e) {
			// System.out.println("Fin de fichero");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return data;
	}

	public static void modifyCups(int codTeam, int newEarnedCups) {
		try {
			int codTeamOffset = (codTeam - 1) * TEAM_BYTES;
			int earnedCupsOffset = codTeamOffset + EARNED_CUPS_BYTES;

			file.seek(earnedCupsOffset);
			file.writeInt(newEarnedCups);
			
			showTeam(codTeam);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void showTeam(int codTeam) {
		if (codTeam > 0) {
			try {

				file.seek((codTeam - 1) * TEAM_BYTES);

				byte[] nombreEquipoBytes = new byte[40];
				byte[] codigoLigaBytes = new byte[5];
				byte[] localidadBytes = new byte[60];

				System.out.println(file.readInt());
				System.out.println(file.readChar());

				file.read(nombreEquipoBytes);
				System.out.println(new String(nombreEquipoBytes));
				System.out.println(file.readChar());

				file.read(codigoLigaBytes);
				System.out.println(new String(codigoLigaBytes));
				System.out.println(file.readChar());

				file.read(localidadBytes);
				System.out.println(new String(localidadBytes));
				System.out.println(file.readChar());

				System.out.println(file.readInt());
				System.out.println(file.readChar());

				System.out.println(file.readBoolean());
				System.out.println(file.readChar());

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

}
