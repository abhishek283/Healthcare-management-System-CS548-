package edu.stevens.cs548.clinic.test;

import java.util.Calendar;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.ClinicDomain;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	@EJB
	IPatientServiceLocal patientService;
	@EJB
	IProviderServiceLocal providerService;
	
	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
	
	@Inject @ClinicDomain
	private EntityManager em;
	
	

	
	@PostConstruct
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Abhishek");
		IPatientDAO patientDAO = new PatientDAO(em);
		ITreatmentDAO treatmentDAO = new TreatmentDAO(em);
		IProviderDAO providerDAO=new ProviderDAO(em);
		PatientFactory patientFactory = new PatientFactory();
		TreatmentFactory treatmentFactory = new TreatmentFactory();
		
		PatientDtoFactory patientDtoFactory = new PatientDtoFactory();
		
		ProviderFactory providerFactory = new ProviderFactory();
		ProviderDtoFactory providerDtoFactory = new ProviderDtoFactory();
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(1985, 9, 4);
						
			/*
			 * Clear the database and populate with fresh data.
			 * 
			 * If we ensure that deletion of patients cascades deletes of treatments,
			 * then we only need to delete patients.
			 */
			
			patientDAO.deletePatients();
			providerDAO.deleteProviders();
			logger.info(calendar.getTime().toString());
			logger.info("calling patient factory and creating patient dto");
			Patient john = patientFactory.createPatient(12345679L, "John Doe", calendar.getTime(), 30);
			logger.info("patient dto created");
			//patientDAO.addPatient(john);
			
			PatientDto john_dto=patientDtoFactory.createPatientDto(john);
			long patientId = patientService.addPatient(john_dto);
			//logger.info("patientid"+patientId);
				
			//long p_id=patientService.addPatient(john_dto);
			logger.info("Added "+john.getName()+" with id "+patientId);
			Provider mark=providerFactory.createProvider("148960L", "Mark Hemilton", "Oncology");
			logger.info("name "+mark.getName());
			ProviderDto mark_dto=providerDtoFactory.createProviderDto(mark);
			logger.info("created provider dto using provider factory");
			long providerid=providerService.addProvider(mark_dto);
			logger.info("Provider mark creared with id"+providerid);
			patientService.addDrugTreatment(patientId, providerid, "Cancer", "Recitin", (float) 10.5);
			logger.info("created drug treatment for patient John Doe");
			
			
			
		//	logger.info("Added "+john.getName()+" with id "+john.getId());
			
		/*	Provider mark=providerFactory.createProvider("2345679L", "Mark Hemilton", "Radiologist");
			ProviderDto mark_dto=providerDtoFactory.createPatientDto(mark);
			long providerId=providerService.addProvider(mark_dto);
			logger.info("Added "+john.getName()+" with id "+providerId);
			
			long tid=patientService.addDrugTreatment(patientId, providerId, "Drug treatment diagnosis", "Crosine", 2.5f);
			logger.info("Drug Treatment Added with treatment id"+tid);
			calendar.set(2015, 9, 04);
			
			tid=patientService.addSurgery(patientId, providerId, "appendix",calendar.getTime() );
			logger.info("Surgery Treatment Added with treatment id"+tid);*/

		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(e.toString());
		}
		
			
			// TODO add more testing, including treatments and providers
			
		
		
	}
}

