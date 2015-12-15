package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {
	
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	public PatientDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	private Logger logger = Logger.getLogger(PatientDAO.class.getCanonicalName());

	@Override
	public long addPatient(Patient patient) throws PatientExn  {
		
		long pid = patient.getPatientId();
		TypedQuery<Patient> query=em.createNamedQuery("SearchPatientByPatientID", Patient.class);
		query.setParameter("pid", pid);
		List<Patient> pat=query.getResultList();
	//	logger.info("patient size"+pat.size());
		if (pat.size() < 1) {
			em.persist(patient);						
			patient.setTreatmentDAO(this.treatmentDAO);
			//em.flush();
			return patient.getId();
		} else {
			Patient patient2=pat.get(0);
			logger.info("Insertion: Patient with patient id (" + pid + ") and name"+patient2.getName()+ " already exists.");
			throw new PatientExn("Insertion: Patient with patient id (" + pid + ") and name"+patient2.getName()+ " already exists.");
		}

	}

	@Override
	public Patient getPatient(long id) throws PatientExn {
		// TODO retrieve patient using primary key
		Patient p=em.find(Patient.class, id);
		if(p==null){
			throw new PatientExn("Patient not found,primary key "+id);
		}
		else{
			return p;
		}
	}

	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		// TODO retrieve patient using query on patient id (secondary key)
		TypedQuery<Patient> query=em.createNamedQuery("SearchPatientByPatientID",Patient.class);
		query.setParameter("pid", pid);
		List<Patient> patients=query.getResultList();
		if(patients.size()>1){
			throw new PatientExn("Duplicate records found for Patient id :"+pid);
		}
		else if(patients.size()<1){
			throw new PatientExn("NO record found for Patient Id :"+pid);
		}
		else{
			return patients.get(0);
		}
	}
	
	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query = em.createNamedQuery("SearchPatientByNameDOB", Patient.class);
		query.setParameter("name", name);
		query.setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		for(Patient patient : patients){
			patient.setTreatmentDAO(this.treatmentDAO);
		}
		return patients;
	}
	
	@Override
	public void deletePatients() {
		em.createQuery("delete from Treatment t").executeUpdate();
		Query update = em.createNamedQuery("RemoveAllPatients");
		update.executeUpdate();
	}

	@Override
	public void deletePatient(Patient patient) throws PatientExn {
		// TODO Auto-generated method stub
		em.remove(patient);
		
	}

}
