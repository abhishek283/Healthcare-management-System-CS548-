package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.Date;

public class TreatmentFactory implements ITreatmentFactory {

	@Override
	public Treatment createDrugTreatment(String diagnosis, String drug, float dosage) {
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setTreatmentType(TreatmentType.DRUG_TREATMENT.getTag());
		return null;
	}
	
	@Override
	public Treatment createRadiology(String diagnosis, List<Date> dates) {
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setTrmt_Dates(dates);
		treatment.setTreatmentType(TreatmentType.RADIOLOGY.getTag());
		return null;
	}
	
	@Override
	public Treatment createSurgery(String diagnosis, Date date) {
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setSurgeryDate(date);
		treatment.setTreatmentType(TreatmentType.SURGERY.getTag());
		return null;
	}

}
