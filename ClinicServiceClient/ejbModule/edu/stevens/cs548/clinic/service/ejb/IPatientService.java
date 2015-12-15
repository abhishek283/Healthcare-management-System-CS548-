package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * Session Bean implementation class PatientService
 */
public interface IPatientService {
	
	public class PatientServiceExn extends Exception{
		private static final long serialVersionUID = 1L;
		public PatientServiceExn(String m){
				super(m);
		}
	}
	public class PatientNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public PatientNotFoundExn (String m) {
			super(m);
		}
	}
	public class TreatmentNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn (String m) {
			super(m);
		}
	}
	
	
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn;
	public PatientDto getPatientByPatId(long id) throws PatientServiceExn;
	public PatientDto[] getPatientByNameDob(String Name,Date Dob);
	public void deletePatient(String name,long id) throws PatientServiceExn;

	String siteInfo();
	long addPatient(PatientDto dto) throws PatientServiceExn;
	long createPatient(String name, Date dob, long patintentid, int age) throws PatientServiceExn;
	
	public TreatmentDto[] getTreatments(long id,long[] tids) throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;
	public void deleteTreatment(long id,long tid) throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;
	public TreatmentDto getTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
	PatientDto getPatient(long id) throws PatientServiceExn;
	public long addSurgery(long id, long provid, String diagnosis, Date date) throws PatientNotFoundExn, PatientServiceExn;
	public long addRadiology(long id, long provid, String diagnosis, List<Date> dates) throws PatientNotFoundExn, PatientServiceExn;
	public long addDrugTreatment(long id, long prov, String diagnosys, String drug, float dosage) throws PatientNotFoundExn,  PatientServiceExn;

}
