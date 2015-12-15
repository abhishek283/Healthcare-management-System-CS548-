package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceRemote;

@WebService(
		endpointInterface = "edu.stevens.cs548.clinic.service.web.soap.IProviderWebService",
		targetNamespace = "http://cs548.stevens.edu/clinic/service/web/soap/provider", 
		serviceName = "ProviderWebService", portName = "ProviderWebPort")
public class ProviderWebService {
	//implements IProviderWebService 
	
	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote service;

	
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return service.addProvider(dto);
	}

	
	public ProviderDto getProviderByDbId(long id) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return service.getProviderByDbId(id);
	}

	
	public ProviderDto getProviderByNPI(String npi) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return service.getProviderByNPI(npi);
	}

	
	public void deleteProvider(String name, long id) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		service.deleteProvider(name, id);
	}

	
	public String siteInfo() {
		// TODO Auto-generated method stub
		return service.siteInfo();
	}


	public void addTreatment(long id, long pid, TreatmentDto treatmentDto)
			throws ProviderNotFoundExn, ProviderServiceExn {
		service.addTreatment(id, pid, treatmentDto);		
	}


	public long addDrugTreatment(long id, long patientid, String diagnosis, String name, float dosage)
			throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return service.addDrugTreatment(id, patientid, diagnosis, name, dosage);
	}


	public long addRadiologyType(long id, long pid, String diagnosis, List<Date> dates) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return service.addRadiologyType(id, pid, diagnosis, dates);
	}


	public long addSurgeryType(long id, long patientid, String diagnosis, Date date) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		return service.addSurgeryType(id, patientid, diagnosis, date);
	}

}
