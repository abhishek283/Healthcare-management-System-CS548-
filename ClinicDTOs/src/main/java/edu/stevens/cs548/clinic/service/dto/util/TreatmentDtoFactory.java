package edu.stevens.cs548.clinic.service.dto.util;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.SurgeryTreatment;
import edu.stevens.cs548.clinic.service.dto.DrugType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public class TreatmentDtoFactory {
	
	ObjectFactory factory;
	
	public TreatmentDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public TreatmentDto createDrugTreatmentDto () {
		// TODO
		return factory.createTreatmentDto();
	}
	
	public PatientDto createTreatmentDto (DrugTreatment t) {
		/*
		 * TODO: Initialize the DTO from the drug treatment.
		 */
		PatientDto d = factory.createPatientDto();
		d.setId(t.getId());
		TreatmentDto tdto=factory.createTreatmentDto();
		DrugType drugt=factory.createDrugType();
		drugt.setID(t.getId());
		drugt.setDosage(t.getDosage());
		drugt.setDrugName(t.getDrug());
		tdto.setDrugTreatment(drugt);
		return d;
	}
	
	public TreatmentDto createRadiologyTreatmentDto () {
		// TODO
		return factory.createTreatmentDto();
	}
	
	public PatientDto createTreatmentDto (RadiologyTreatment t) {
		/*
		 * TODO: Initialize the DTO from the drug treatment.
		 */
		PatientDto d = factory.createPatientDto();
		d.setId(t.getId());
		TreatmentDto tdto=factory.createTreatmentDto();
		RadiologyType rdlg=factory.createRadiologyType();
		rdlg.setID(t.getId());
		rdlg.setTreatmentDates(t.getTrmt_Dates());
		tdto.setRadiologyTreatment(rdlg);
		return d;
	}
	
	public TreatmentDto createSurgeyTreatmentDto () {
		// TODO
		
		return factory.createTreatmentDto();
	}
	
	public PatientDto createTreatmentDto (SurgeryTreatment t) {
		/*
		 * TODO: Initialize the DTO from the drug treatment.
		 */
		PatientDto d = factory.createPatientDto();
		d.setId(t.getId());
		TreatmentDto tdto=factory.createTreatmentDto();
		SurgeryType surgery=factory.createSurgeryType();
		surgery.setID(t.getId());
		surgery.setDateOfSurgery(t.getSurgeryDate());
		tdto.setSurgeryTreatment(surgery);
		return d;
		
	}

}
