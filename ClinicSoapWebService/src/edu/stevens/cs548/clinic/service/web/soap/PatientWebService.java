package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;

// Use JSR-181 annotations to specify Web service.
//Specify: endpoint interface (FQN), target namespace, service name, port name

@WebService(
		endpointInterface = "edu.stevens.cs548.clinic.service.web.soap.IPatientWebService", 
		targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap/patient", 
		serviceName = "PatientWebService", portName = "PatientWebPort")

public class PatientWebService {
	//implements IPatientWebService 

	@EJB(beanName="PatientServiceBean")
	IPatientServiceRemote service;

	public long addPatient(PatientDto dto) throws PatientServiceExn {
		return service.addPatient(dto);
	}

	
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		return service.getPatientByPatId(pid);
	}

	
	public TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn,
			PatientServiceExn {
		return service.getTreatment(id, tid);
	}

	
	public String siteInfo() {
		return service.siteInfo();
	}

	
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		
		return service.getPatientByDbId(id);
	}

	
	public PatientDto[] getPatientByNameDob(String Name, Date Dob) {
		
		return service.getPatientByNameDob(Name, Dob);
	}

	
	public void deletePatient(String name, long id) throws PatientServiceExn {
		
		service.deletePatient(name, id);
	}

	
	public long createPatient(String name, Date dob, long patintentid, int age) throws PatientServiceExn {
			return service.createPatient(name, dob, patintentid, age);
	}

	
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
			return service.getTreatments(id, tids);
	}

	
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
			service.deleteTreatment(id, tid);
	}

	
	public long addSurgery(long id, long provid, String diagnosis, Date date)
			throws PatientNotFoundExn, PatientServiceExn {
			return service.addSurgery(id, provid, diagnosis, date);
	}

	
	public long addRadiology(long id, long provid, String diagnosis, List<Date> dates)
			throws PatientNotFoundExn, PatientServiceExn {
		return  service.addRadiology(id, provid, diagnosis, dates);
		
	}

	
	public long addDrugTreatment(long id, long prov, String diagnosys, String drug, float dosage)
			throws PatientNotFoundExn, PatientServiceExn {
		return  service.addDrugTreatment(id, prov, diagnosys, drug, dosage);
	}

}
