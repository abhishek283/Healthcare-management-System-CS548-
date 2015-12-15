package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Patient
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name="SearchPatientByPatientID",
		query="select p from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name="CountPatientByPatientID",
		query="select count(p) from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p")
})
@Table(name="PATIENT")
public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// TODO JPA annotations
	@Id
	@GeneratedValue
	private long id;
	
	private long patientId;
	
	private String name;
	
	// TODO JPA annotation
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	// TODO JPA annotations (propagate deletion of patient to treatments)
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patient")
	@OrderBy
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments() {
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}
	
	public void addTreatment (Treatment t) {
		// Persist treatment and set forward and backward links
		this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if (t.getPatient() != this) {
			t.setPatient(this);
		}
	}
	
	public long addDrugTreatment(Provider provider, String diagnosis, String drug, float d){
		DrugTreatment treatment = new DrugTreatment();
		treatment.setProvider(provider);
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(d);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addSurgery(Provider provider, String diagnosis, Date date){
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setProvider(provider);
		treatment.setDiagnosis(diagnosis);
		treatment.setSurgeryDate(date);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addRadiology(Provider provider, String diagnosis, List<Date> dates){
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setProvider(provider);
		treatment.setDiagnosis(diagnosis);
		treatment.setTrmt_Dates(dates);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public List<Long> getTrmtIds() {
		List<Long> tids = new ArrayList<Long>();
		for(Treatment t : this.getTreatments()){
			tids.add(t.getId());
		}
		return tids;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatment(tid);
		if(t.getPatient() != this){
			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + " treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	public void visitTreatments(ITreatmentVisitor visitor) {
		for(Treatment t : this.getTreatments()) {
			t.visit(visitor);
		}
	}
	
	
	
	public void getTreatmentIds(List<Long> treatmentIds) {
		for (Treatment t : this.getTreatments()) {
			treatmentIds.add(t.getId());
		}
	}
	
	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
		// Export a treatment without violated Aggregate pattern
		// Check that the exported treatment is a treatment for this patient.
		Treatment t = treatmentDAO.getTreatment(tid);
		if (t.getPatient() != this) {
			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}
	
	public void deleteTreatment(long tid) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatment(tid);
		if(t.getPatient() != this){
			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + " treatment = " + tid);
		}
		treatmentDAO.deleteTreatment(t);
	}

	
	public Patient() {
		super();
		/*
		 * TODO initialize lists
		 */
		treatments=new ArrayList<Treatment>();
	}
   
}
