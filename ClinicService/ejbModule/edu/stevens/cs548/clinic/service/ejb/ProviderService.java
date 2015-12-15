package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Specialization;
import edu.stevens.cs548.clinic.service.dto.DrugType;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.PatientService.TreatmentPDOToDTO;

@Stateless(name = "ProviderServiceBean")
@Local(IProviderServiceLocal.class)
@Remote(IProviderServiceRemote.class)
@LocalBean
public class ProviderService implements IProviderServiceLocal,IProviderServiceRemote {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderService.class.getCanonicalName());

	private ProviderFactory providerFactory;
	
	private ProviderDtoFactory ProviderDtoFactory;

	private IProviderDAO providerDAO;
	
	private IPatientDAO patientDAO;
	
	 @Inject @ClinicDomain
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public ProviderService() {
		// TODO initialize factories
		providerFactory=new ProviderFactory();
		//patientDAO=new PatientDAO(em);
	}
	
	// TODO use dependency injection and EJB lifecycle methods to initialize DAOs
	@PostConstruct
	private void initialize(){
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);

	}

	@Override
	public ProviderDto getProviderByDbId(long id) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider prov= providerDAO.getProvider(id);
			return new ProviderDto(prov);
		}
		catch(ProviderExn e){
			throw new ProviderServiceExn(e.toString());
			
		}
	}

	@Override
	public ProviderDto getProviderByNPI(String npi) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider prov= providerDAO.getProviderByNPI(npi);
			return new ProviderDto(prov);
		}
		catch(ProviderExn e){
			throw new ProviderServiceExn(e.toString());
			
		}
	}

	@Override
	public void deleteProvider(String name, long id) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider prov=providerDAO.getProvider(id);
			if(!name.equals(prov.getName())){
				throw new ProviderServiceExn("Invalid record for delete,name"+name+" ,id"+id);
			}
			else{
				providerDAO.deleteProvider(prov);
			}
			}
			catch (ProviderExn e) {
				// TODO Auto-generated catch block
				throw new ProviderServiceExn(e.toString());
			}

	}

	@Override
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try {
			Provider provider = this.providerFactory.createProvider(dto.getNpi(), dto.getName(), dto.getSpecialization());
			providerDAO.addProvider(provider);
			return provider.getProviderId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	
	@Override
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProvider(id);
			TreatmentDto[] treatments = new TreatmentDto[tids.length];
			TreatmentPDOToDTO visitor = new TreatmentPDOToDTO();
			for (int i = 0; i < tids.length; i++) {
				provider.visitTreatment(tids[i], visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public void deleteTreatment(long id, long tid) throws ProviderNotFoundExn,
			TreatmentNotFoundExn, ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProvider(id);
			provider.deleteTreatment(tid);
		} catch (ProviderExn pe) {
			throw new ProviderNotFoundExn(pe.toString());
		} catch (TreatmentExn te) {
			throw new TreatmentNotFoundExn(te.toString());
		}

	}


	@Override
	public TreatmentDto[] getPatientTreatments(long id, long pid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProvider(id);
			Patient patient = patientDAO.getPatientByPatientId(pid);
			List<Long> tids = provider.PatientTIds(patient);
			TreatmentPDOToDTO visitor = new TreatmentPDOToDTO();
			TreatmentDto[] treatments = new TreatmentDto[tids.size()];
			for (int i = 0; i < tids.size(); i++) {
				provider.visitTreatment(tids.get(i), visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	
	@Override
	public long addDrugTreatment(long id, long patientid, String diagnosis, String name, float dosage)
	throws ProviderServiceExn
	{
		try
		{
			Provider provider=providerDAO.getProvider(id);
			Patient pat=patientDAO.getPatient(patientid);
			long tid=pat.addDrugTreatment(provider, diagnosis, name,dosage);
			return tid;

		}
		catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			// TODO Auto-generated catch block
			throw new ProviderServiceExn(e.toString());
		}
	}
	@Override
	public long addSurgeryType(long id, long patientid, String diagnosis, Date date) throws ProviderServiceExn {
		try
		{
			Provider provider=providerDAO.getProvider(id);
			Patient pat=patientDAO.getPatient(patientid);
			long tid=pat.addSurgery(provider, diagnosis, date);
			return tid;
		}
		catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			// TODO Auto-generated catch block
			throw new ProviderServiceExn(e.toString());
		}
	}
	@Override
	public long addRadiologyType(long id, long pid, String diagnosis, List<Date> dates)
			throws ProviderServiceExn {
		try
		{
			Provider provider=providerDAO.getProvider(id);
			Patient pat=patientDAO.getPatient(pid);
			long tid=pat.addRadiology(provider, diagnosis, dates);
			return tid;

		}
		catch(ProviderExn e)
		{
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			// TODO Auto-generated catch block
			throw new ProviderServiceExn(e.toString());
		}
	}
	@Resource(name="ProviderSiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTreatment(long id, long pid, TreatmentDto treatmentDto)
			throws ProviderNotFoundExn, ProviderServiceExn {
		try
    	{
    		Provider provider = providerDAO.getProvider(id);
			if(provider.getSpecialization().equals(Specialization.DrugTreatment))
			{
				DrugType dtto=treatmentDto.getDrugTreatment();
				String name=dtto.getDrugName();
				float dosage=dtto.getDosage();
				String diag=treatmentDto.getDiagnosis();				
				if(dtto!=null)
				{
					this.addDrugTreatment(id, pid, diag,name,dosage);
				}
			}
			else if(provider.getSpecialization().equals(Specialization.Radiology))
			{
				RadiologyType dtto=treatmentDto.getRadiologyTreatment();
				List<Date> dates=dtto.getTreatmentDates();
				String diag=treatmentDto.getDiagnosis();				
				if(dtto!=null)
				{
					this.addRadiologyType(id, pid, diag, dates);
				}
			}
			else 
			{
				SurgeryType dtto=treatmentDto.getSurgeryTreatment();
				Date dates=dtto.getDateOfSurgery();
				String diag=treatmentDto.getDiagnosis();				
				if(dtto!=null)
				{
					this.addSurgeryType(id, pid, diag, dates);
				}
			}
    	}
    	catch(ProviderExn e)
    	{
    		throw new ProviderServiceExn(e.toString());
    	}
	}
	
	
	
	

}
