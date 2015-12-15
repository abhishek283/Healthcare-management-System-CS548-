package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity
@DiscriminatorValue("RA")
public class RadiologyTreatment extends Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public RadiologyTreatment() {
		super();
		this.setTreatmentType("RA");
	}
	
	@CollectionTable
	private List<Date> trmt_Dates;

	public List<Date> getTrmt_Dates() {
		return trmt_Dates;
	}

	public void setTrmt_Dates(List<Date> trmt_Dates) {
		this.trmt_Dates = trmt_Dates;
	} 
	
	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportRadiology(this.getId(),
				 						   this.getDiagnosis(),
								   		   this.trmt_Dates);
	}
	
	@Override
	public void visit(ITreatmentVisitor visitor) {
		visitor.visitRadiology(this.getId(), this.getProvider(), this.getDiagnosis(), this.trmt_Dates);
		
	}
}
