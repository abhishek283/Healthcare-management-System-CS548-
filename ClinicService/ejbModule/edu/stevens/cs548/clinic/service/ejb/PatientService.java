package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.service.dto.DrugType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
@Local(IPatientServiceLocal.class)
@Remote(IPatientServiceRemote.class)
public class PatientService implements IPatientServiceLocal,IPatientServiceRemote {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());

	private PatientFactory patientFactory;
			
	private PatientDtoFactory PatientDtoFactory;

	private IPatientDAO patientDAO;
	
	private IProviderDAO providerDAO;
	
	
	/**
	 * Default constructor.
	 */
	public PatientService() {
		// TODO initialize factories
		patientFactory=new PatientFactory();
		//patientDAO=new PatientDAO(em);
	}
	
	@Inject @ClinicDomain
	private EntityManager em;
	
	// TODO use dependency injection and EJB lifecycle methods to initialize DAOs
	@PostConstruct
	private void initialize(){
		patientDAO=new PatientDAO(em);
		providerDAO = new ProviderDAO(em);

	}

	/**
	 * @see IPatientService#addPatient(String, Date, long)
	 */
	@Override
	public long addPatient(PatientDto dto) throws PatientServiceExn {
		// Use factory to create patient entity, and persist with DAO
		long pid=dto.getPatientId();
    	String name=dto.getName();
    	Date dob=dto.getDob();
    	int age=dto.getAge();
    	try
    	{
       Patient patient=this.patientFactory.createPatient(pid, name, dob, age);
       try
		{
    	   	
			long patId=patientDAO.addPatient(patient);
			return patId;
			
		}
		catch(PatientExn e)
		{
			throw new PatientServiceExn(e.toString());
		}
    	}
    	catch(PatientExn e)
    	{
    		throw new PatientServiceExn(e.toString());
    	}   
	}
	
	/**
	 * @see IPatientService#getPatient(long)
	 */
	@Override
	public PatientDto getPatientByDbId(long id) throws PatientServiceExn {
		// TODO use DAO to get patient by database key
		try{
			Patient patient= patientDAO.getPatient(id);
			return new PatientDto(patient);
		}
		catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
			
		}
		
	}

	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		// TODO use DAO to get patient by patient id
		try{
			Patient patient= patientDAO.getPatientByPatientId(pid);
			return new PatientDto(patient);
		}
		catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
			
		}
	}
	
	@Override
	public PatientDto[] getPatientByNameDob(String Name, Date Dob) {
		// TODO Auto-generated method stub
		List<Patient> patients=patientDAO.getPatientByNameDob(Name, Dob);
		PatientDto[] dto=new PatientDto[patients.size()];
		for(int i=0;i<dto.length;i++){
			dto[i]=new PatientDto(patients.get(i));
		}
		return dto;
	}

	public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {
		
		private ObjectFactory factory = new ObjectFactory();
		
		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug,
				float dosage) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugType drugInfo = factory.createDrugType();
			drugInfo.setDosage(dosage);
			drugInfo.setDrugName(drug);
			dto.setDrugTreatment(drugInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportRadiology(long tid, String diagnosis, List<Date> dates) {
			// TODO Auto-generated method stub	
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radiologyInfo = factory.createRadiologyType();
			radiologyInfo.setTreatmentDates(dates);
			dto.setRadiologyTreatment(radiologyInfo);
			
			return dto;
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date) {
			// TODO Auto-generated method stub	
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeryInfo = factory.createSurgeryType();
			surgeryInfo.setDateOfSurgery(date);
			dto.setSurgeryTreatment(surgeryInfo);
			return dto;
		}
		
	}
	

	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return patient.exportTreatment(tid, visitor);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public long createPatient(String name, Date dob, long patientid,int age) throws PatientServiceExn {
		// TODO Auto-generated method stub
		
		try {
			Patient patient=this.patientFactory.createPatient(patientid, name, dob, age);
			patientDAO.addPatient(patient);
			return patient.getId();
		} 
		catch (PatientExn e) {
			// TODO Auto-generated catch block
		    throw new PatientServiceExn(e.toString());
		}
	}

	

	@Override
	public void deletePatient(String name, long id) throws PatientServiceExn {
		// TODO Auto-generated method stub
		try{
		Patient patient=patientDAO.getPatient(id);
		if(!name.equals(patient.getName())){
			throw new PatientServiceExn("Invalid record for delete,name"+name+" ,id"+id);
		}
		else{
			patientDAO.deletePatient(patient);
		}
		}
		catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
	}

	static class TreatmentPDOToDTO implements ITreatmentVisitor{
		
		private TreatmentDto dto;
		public TreatmentDto getDTO(){
			return dto;
		}

		@Override
		public void visitDrugTreatment(long tid, Provider provider, String diagnosis, String drug, float dosage) {
			// TODO Auto-generated method stub
			dto= new TreatmentDto();
			dto.setId(tid);
			dto.setDiagnosis(diagnosis);
			dto.setProvider(provider.getProviderId());
			DrugType druginfo=new DrugType();
			druginfo.setDosage(dosage);
			druginfo.setDrugName(drug);
			dto.setDrugTreatment(druginfo);
			
		}

		@Override
		public void visitSurgery(long tid, Provider provider, String diagnosis, Date date) {
			// TODO Auto-generated method stub
			dto= new TreatmentDto();
			dto.setId(tid);
			dto.setDiagnosis(diagnosis);
			dto.setProvider(provider.getProviderId());
			SurgeryType surgeryInfo=new SurgeryType();
			surgeryInfo.setDateOfSurgery(date);
			dto.setSurgeryTreatment(surgeryInfo);
		}

		@Override
		public void visitRadiology(long tid, Provider provider, String diagnosis, List<Date> date) {
			// TODO Auto-generated method stub
			dto.setId(tid);
			dto.setDiagnosis(diagnosis);
			dto.setProvider(provider.getProviderId());
			RadiologyType radiologyInfo=new RadiologyType();
			radiologyInfo.setTreatmentDates(date);
			dto.setRadiologyTreatment(radiologyInfo);
		}
		
	}
	
	@Override
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// TODO Auto-generated method stub
		try {
			Patient patient=patientDAO.getPatient(id);
			TreatmentDto[] treatments=new TreatmentDto[tids.length];
			TreatmentPDOToDTO visitor= new TreatmentPDOToDTO();
			for(int i=0;i<tids.length;i++){
					
					patient.visitTreatment(tids[i], visitor);
					treatments[i]=visitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			// TODO Auto-generated catch block
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			// TODO Auto-generated catch block
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// TODO Auto-generated method stub
		try{
			Patient patient=patientDAO.getPatient(id);
			patient.deleteTreatment(tid);
		}
		catch(PatientExn e){	
			throw new PatientNotFoundExn(e.toString());
		} 
		catch (TreatmentExn e) {
			// TODO Auto-generated catch block
			throw new TreatmentNotFoundExn(e.toString());
		}
		
	}

	@Override
	public long addDrugTreatment(long id, long prov, String diagnosys, String drug, float dosage)
			throws PatientNotFoundExn,PatientServiceExn {
		// TODO Auto-generated method stub
		try{
			Patient patient=patientDAO.getPatient(id);
			Provider provider=providerDAO.getProvider(prov);
			long tid =patient.addDrugTreatment(provider, diagnosys, drug, dosage);
			return tid;
		}
		catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			// TODO Auto-generated catch block
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public long addRadiology(long id, long provid, String diagnosis, List<Date> dates) throws PatientNotFoundExn,PatientServiceExn {
		// TODO Auto-generated method stub
		try{
			Patient patient=patientDAO.getPatient(id);
			Provider prov = providerDAO.getProvider(provid);
			long tid =patient.addRadiology(prov, diagnosis, dates);
			return tid;
		}
		catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			// TODO Auto-generated catch block
			throw new PatientServiceExn(e.toString());
		}
		
	}

	@Override
	public long addSurgery(long id, long provid, String diagnosis, Date date) throws PatientServiceExn,PatientNotFoundExn {
		// TODO Auto-generated method stub
		try{
			Patient patient=patientDAO.getPatient(id);
			Provider provider = providerDAO.getProvider(provid);
			long tid =patient.addSurgery(provider, diagnosis, date);
			return tid;
		}
		catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			// TODO Auto-generated catch block
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public PatientDto getPatient(long id) throws PatientServiceExn {
		// TODO Auto-generated method stub
		return null;
	}
	
		@Resource(name="SiteInfo")
		private String siteInformation;
		

		@Override
		public String siteInfo() {
			return siteInformation;
		}
		
		
}
