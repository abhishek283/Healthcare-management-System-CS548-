package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
@WebService(
		name="IProviderWebPort",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/provider")
@SOAPBinding(
		style=SOAPBinding.Style.DOCUMENT,
		use=SOAPBinding.Use.LITERAL,
		parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
	/*
	 * Endpoint interface for the provider Web service.
	 */

public interface IProviderWebService {
	@WebMethod
	long addProvider(ProviderDto dto) throws ProviderServiceExn;
	
	@WebMethod
	public ProviderDto getProviderByDbId(long id) throws ProviderServiceExn;
	
	@WebMethod
	public ProviderDto getProviderByNPI(String npi) throws ProviderServiceExn;
	
	@WebMethod
	public void deleteProvider(String name,long id) throws ProviderServiceExn;
	
	@WebMethod
	public void addTreatment(long id, long pid, TreatmentDto treatmentDto)
			throws ProviderNotFoundExn, ProviderServiceExn;
	
	@WebMethod
	public long addDrugTreatment(long id, long patientid, String diagnosis, String name, float dosage)
			throws ProviderServiceExn;
	
	@WebMethod
	public long addRadiologyType(long id, long pid, String diagnosis, List<Date> dates) throws ProviderServiceExn;
	
	@WebMethod
	public long addSurgeryType(long id, long patientid, String diagnosis, Date date) throws ProviderServiceExn;

	@WebMethod
	public String siteInfo();
}
