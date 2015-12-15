package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor {
	public void visitDrugTreatment(long tid, Provider provider, String diagnosis, String drug, float dosage);
	
	public void visitSurgery(long tid, Provider provider, String diagnosis, Date date);
	
	public void visitRadiology(long tid, Provider provider, String diagnosis, List<Date> date);
}
