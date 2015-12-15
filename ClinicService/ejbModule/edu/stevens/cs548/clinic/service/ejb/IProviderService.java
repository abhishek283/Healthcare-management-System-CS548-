package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public interface IProviderService {
	public class ProviderServiceExn extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ProviderServiceExn(String m){
				super(m);
		}
	}
	
	public class ProviderNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;

		public ProviderNotFoundExn(String m) {
			super(m);
		}
	}

	public class TreatmentNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m) {
			super(m);
		}
	}
	long addProvider(ProviderDto dto) throws ProviderServiceExn;
	public ProviderDto getProviderByDbId(long id) throws ProviderServiceExn;
	public ProviderDto getProviderByNPI(String npi) throws ProviderServiceExn;
	public void deleteProvider(String name,long id) throws ProviderServiceExn;
	public TreatmentDto[] getTreatments(long id, long[] tids)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn;

	public TreatmentDto[] getPatientTreatments(long id, long pid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn;

	public void deleteTreatment(long id, long tid) throws ProviderNotFoundExn,
			TreatmentNotFoundExn, ProviderServiceExn;

	public void addTreatment(long id, long pid, TreatmentDto treatmentDto)
			throws ProviderNotFoundExn, ProviderServiceExn;
	
	long addDrugTreatment(long id, long patientid, String diagnosis, String name, float dosage)
			throws ProviderServiceExn;
	long addRadiologyType(long id, long pid, String diagnosis, List<Date> dates) throws ProviderServiceExn;
	long addSurgeryType(long id, long patientid, String diagnosis, Date date) throws ProviderServiceExn;
	
	String siteInfo();
	
	
}
