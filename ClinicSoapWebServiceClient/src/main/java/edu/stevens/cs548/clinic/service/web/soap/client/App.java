package edu.stevens.cs548.clinic.service.web.soap.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import edu.stevens.cs548.clinic.service.dto.DrugType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public class App {

	private class ServiceError extends Exception {
		private static final long serialVersionUID = 5755181463947657850L;
	}

	Logger logger = Logger.getLogger(App.class.getCanonicalName());
	
	ObjectFactory factory = new ObjectFactory();

	/*
	 * Input file. Defaults to standard input.
	 */
	private String InputFileName = "<stdin>";

	private BufferedReader InputFile = new BufferedReader(new InputStreamReader(System.in));

	/*
	 * Output file. Defaults to standard output.
	 */
	private String OutputFileName;

	private PrintWriter OutputFile = new PrintWriter(new OutputStreamWriter(System.out));

	/*
	 * Client proxy for the Web service. Generated from the service class, which is generated from the WSDL.
	 */
	private IPatientWebPort patient = null;

	/*
	 * Line number in input file, for error reporting.
	 */
	private int lineNo = 0;

	private String readLine() {
		/*
		 * Read a command from the input.
		 */
		try {
			lineNo++;
			return InputFile.readLine();
		} catch (IOException e) {
			error("Error reading input: " + e);
			return null;
		}
	}

	private void error(String msg) {
		if (lineNo > 0) {
			System.err.print(lineNo + ": ");
		}
		System.err.println("** Error **");
		System.err.println(msg);
		System.exit(-1);
	}

	private void warning(String msg) {
		if (lineNo > 0) {
			System.err.print(lineNo + ": ");
		}
		System.err.println("** Warning **");
		System.err.println(msg);
	}

	private void print(String s) {
		OutputFile.print(s);
	}

	private void println(String s) {
		OutputFile.println(s);
	}

	private void newline() {
		OutputFile.println();
	}

	private void displayLong(long n) {
		OutputFile.print(n);
	}
	
	private void displayTreatment(TreatmentDto t) {
		displayTreatment("", t, "");
	}

	private void displayTreatment(String prefix, TreatmentDto t, String suffix) {
		if (t == null) {
			print(prefix + "null" + suffix);
			return;
		}
		print(prefix);
		logger.info("Displaying a treatment.");
		print("Treatment(){");
		print("diagnosis=");
		print(t.getDiagnosis());
		print(",treatment=");
		if (t.getDrugTreatment() != null) {
			DrugType dt = t.getDrugTreatment();
			print("DrugTreatment{");
			print("name=");
			print(dt.getDrugName());
			print(",dosage=");
			print(Float.toString(dt.getDosage()));
			print("}");
		} else if (t.getRadiologyTreatment() != null) {
			List<Date> rads = t.getRadiologyTreatment().getTreatmentDates();
			DateFormat datefmt = DateFormat.getInstance();
			print("Radiology{dates=[");
			for (Date rad : rads) {
				print(datefmt.format(rad));
				print(",");
			}
			print("]}");
		} else if (t.getSurgeryTreatment() != null) {
			SurgeryType st = t.getSurgeryTreatment();
			print("Surgery{");
			print("date=");
			print(DateFormat.getInstance().format(st.getDateOfSurgery()));
			print("}");
		}
		print("}");
	}

	private void displayPatient(String prefix, PatientDto p, String suffix) {
		if (p == null) {
			print(prefix + "null" + suffix);
			return;
		}
		print(prefix);
		print("Patient(");
		print(Long.toString(p.getId()));
		print("){");
		print("name=");
		print(p.getName());
		print(",birthDate=");
		print(DateFormat.getInstance().format(p.getDob().toGregorianCalendar().getTime()));
		print(",patientId=");
		print(Long.toString(p.getPatientId()));
		print("}");

	}

	private void displayPatient(PatientDto p) {
		displayPatient("", p, "\n");
	}

	private long addPatient(String[] args) throws ServiceError {
		if (args.length != 3) {
			error("Usage: patient addPatient name dob patient-id age");
		}

		try {
			PatientDto dto = factory.createPatientDto();
			dto.setName(args[0]);
			Calendar dob = DatatypeConverter.parseDate(args[1]);
			GregorianCalendar gcal = new GregorianCalendar();
			gcal.setTime(dob.getTime());
			dto.setDob(DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal));
			dto.setPatientId(Long.parseLong(args[2]));
			dto.setAge(Integer.parseInt(args[3]));

			logger.info("Adding a patient (name=" + dto.getName() + ").");
			return patient.addPatient(dto);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	private PatientDto getPatient(String[] args) throws ServiceError {
		if (args.length != 1) {
			error("Usage: patient getPatient patient-key");
		}
		long patientKey = Long.parseLong(args[0]);
		try {
			return patient.getPatient(patientKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PatientDto getPatientByPatId(String[] args) throws ServiceError {
		if (args.length != 1) {
			error("Usage: patient getPatientByPatId patient-id");
		}
		long patientId = Long.parseLong(args[0]);
		logger.info("Getting a patient by patient id (" + patientId + ").");
		try {
			return patient.getPatientByPatId(patientId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unused")
	private void addDrugTreatment(String[] args) throws ServiceError {
		if (args.length != 5) {
			error("Usage: provider addDrugTreatment patient-key provider-key diagnosis drug dosage");
		}
		TreatmentDto dto = factory.createTreatmentDto();
		dto.setPatient(Long.parseLong(args[0]));
		dto.setProvider(Long.parseLong(args[1]));
		dto.setDiagnosis(args[2]);
		
		DrugType dt = factory.createDrugType();
		dt.setDrugName(args[3]);
		dt.setDosage(Float.parseFloat(args[4]));
		dto.setDrugTreatment(dt);
		
//		try {
			/*
			 * TODO: My code does not yet have providers.
			 */
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private TreatmentDto getTreatment(String[] args) throws ServiceError {
		if (args.length < 1) {
			error("Usage: patient getTreatment patient-key tid");
		}
		long id = Long.parseLong(args[0]);
		long tid = Long.parseLong(args[1]);
		try {
			return patient.patientGetTreatment(id, tid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String siteInfo(String[] args) {
		if (args.length > 0) {
			error("Usage: patient siteInfo");
			return null;
		}
		try {
			return patient.siteInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void invokePatientService(String cmd, String[] args) {
		try {
			if ("addPatient".equals(cmd)) {
				displayLong(addPatient(args));
				newline();
			} else if ("getPatientBy".equals(cmd)) {
				displayPatient(getPatient(args));
				newline();
			} else if ("getPatientByPatId".equals(cmd)) {
				displayPatient(getPatientByPatId(args));
				newline();
			} else if ("getTreatment".equals(cmd)) {
				displayTreatment(getTreatment(args));
				newline();
			} else if ("siteInfo".equals(cmd)) {
				println(siteInfo(args));
			} else {
				error("Unrecognized patient service command: " + cmd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void invokeProviderService(String cmd, String[] args) {
		error("Unimplemented provider client.");
	}

	private String currentWorkingDir() {
		return new File(".").getAbsolutePath();
	}

	private App processArgs(String[] args) {
		/*
		 * Process the command line arguments:
		 * 
		 * --input filename -i filename File containing a list of commands. Default is standard input.
		 * 
		 * --output filename -o filename File to write out results of commands. Default is standard output.
		 * 
		 * --url endpoint-url -u endpoint-url Endpoint URL for the service. No default.
		 */
		for (int iarg = 0; iarg < args.length; iarg++) {
			if ("--input".equals(args[iarg]) || "-i".equals(args[iarg])) {
				if (iarg + 1 < args.length) {
					InputFileName = args[++iarg];
					try {
						InputFile = new BufferedReader(new FileReader(InputFileName));
					} catch (FileNotFoundException e) {
						error("Input file not found: " + InputFileName + "\nDirectory: " + currentWorkingDir());
					}
				} else {
					error("Missing value for --input or -i option.");
				}
			} else if ("--output".equals(args[iarg]) || "-o".equals(args[iarg])) {
				if (iarg + 1 < args.length) {
					OutputFileName = args[++iarg];
					try {
						OutputFile = new PrintWriter(new FileWriter(OutputFileName));
					} catch (IOException e) {
						error("Problem opening output file: " + OutputFileName + "\nDirectory: " + currentWorkingDir());
					}
				} else {
					error("Missing value for --output or -o option");
				}
			}
		}

		return this;
	}

	private void processLine(String[] args) {
		if (args.length < 2) {
			error("Usage: (patient|provider) command arg1 ... argn");
		} else if ("patient".equals(args[0])) {
			invokePatientService(args[1], Arrays.copyOfRange(args, 2, args.length));
		} else if ("provider".equals(args[0])) {
			invokeProviderService(args[1], Arrays.copyOfRange(args, 2, args.length));
		} else {
			error("Service name must be \"patient\" or \"provider\".");
		}
	}

	private void processLines() {
		String line;
		while ((line = this.readLine()) != null) {
			String[] args = line.split("\\s");
			processLine(args);
		}
		try {
			InputFile.close();
		} catch (IOException e) {
			warning("Failed to close input: " + e);
		}
		OutputFile.close();
	}

	public App() {
		PatientWebService service = new PatientWebService();
		this.patient = service.getPatientWebPort();
	}

	public static void main(String[] args) {
		new App().processArgs(args).processLines();
	}

}
