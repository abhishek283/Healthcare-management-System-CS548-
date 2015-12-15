package edu.stevens.cs548.clinic.domain;

import static javax.persistence.CascadeType.REMOVE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name="SearchProviderByNPI",
		query="select p from Provider p where p.NPI = :npi"),
	
	@NamedQuery(
		name="CountProviderByNPI",
		query="select count(p) from Provider p where p.NPI = :npi"),
	@NamedQuery(
		name = "RemoveAllProviders", 
		query = "delete from Provider p")
})
public class Provider implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Provider() {
		super();
		treatments=new ArrayList<Treatment>();
	}
	
	@Id
	@GeneratedValue
	private Long Providerid;

	private String NPI;
	private String name;
    private String Specialization;
    
	public ITreatmentDAO getTreatmentDAO() {
		return treatmentDAO;
	}
	public void setTreatmentDAO(ITreatmentDAO treatmentDAO) {
		this.treatmentDAO = treatmentDAO;
	}

	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public Long getProviderId() {
		return Providerid;
	}
	public void setProviderid(Long providerid) {
		Providerid = providerid;
	}

	  
    @OneToMany(cascade = REMOVE,mappedBy = "provider")
	@OrderBy
	private List<Treatment> treatments; 
    
    public List<Treatment> getTreatments() {
		return treatments;
	}
	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	public String getNPI() {
		return NPI;
	}
	public void setNPI(String nPI) {
		NPI = nPI;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpecialization() {
		return Specialization;
	}
	public void setSpecialization(String specialization) {
		Specialization = specialization;
	}
	
	public void addTreatment(Treatment treatment){
		this.treatmentDAO.addTreatment(treatment);
		this.getTreatments().add(treatment);
		if(treatment.getProvider() != this)
			treatment.setProvider(this);
	}
	
	public List<Long> getTrmtIds() {
		List<Long> tids = new ArrayList<Long>();
		for(Treatment t : this.getTreatments()){
			tids.add(t.getId());
		}
		return tids;
	}
	
	public List<Long> PatientTIds(Patient patient) {
		List<Long> tids = new ArrayList<Long>();
		for(Treatment t : this.getTreatments()){
			Patient p = t.getPatient();
			if(p.getId() == patient.getId()) {
				tids.add(t.getId());
			}
		}
		return tids;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatment(tid);
		if(t.getProvider() != this){
			throw new TreatmentExn("Inappropriate treatment access: provider = " + Providerid + " treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	public void visitTreatments(ITreatmentVisitor visitor) {
		for(Treatment t : this.getTreatments()) {
			t.visit(visitor);
		}
	}
	
	public void deleteTreatment(long tid) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatment(tid);
		if(t.getProvider() != this){
			throw new TreatmentExn("Inappropriate treatment access: provider = " + Providerid + " treatment = " + tid);
		}
		treatmentDAO.deleteTreatment(t);
	}

}
