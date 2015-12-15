package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: SurgeryTreatment
 *
 */
@Entity
@DiscriminatorValue("SU")

public class SurgeryTreatment extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public SurgeryTreatment() {
		super();
		this.setTreatmentType("S");
	}
	
	@Temporal(TemporalType.DATE)
	private Date surgeryDate;

	public Date getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}
   
	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportSurgery(this.getId(), 
								     this.getDiagnosis(),
								     this.surgeryDate);
	}

	@Override
	public void visit(ITreatmentVisitor visitor) {
		visitor.visitSurgery(this.getId(), this.getProvider(), this.getDiagnosis(), this.surgeryDate);
		
	}
   
}
