package edu.stevens.cs548.clinic.service.web.rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.DrugType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;

@XmlRootElement
public class TreatmentRepresentation extends TreatmentType {

	private TreatmentDtoFactory treatmentDtoFactory;
	
	public TreatmentRepresentation() {
		super();
		treatmentDtoFactory = new TreatmentDtoFactory();
		
	}
	
	
	private ObjectFactory repFactory = new ObjectFactory();
	
	public static LinkType getTreatmentLink(long tid, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		UriBuilder ubTreatment = ub.clone().path("{tid}");
		String treatmentURI = ubTreatment.build(Long.toString(tid)).toString();
	
		LinkType link = new LinkType();
		link.setUrl(treatmentURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	
	public TreatmentRepresentation(TreatmentDto dto, UriInfo uriInfo) {
		// TODO Auto-generated constructor stub
		this();
		this.id = getTreatmentLink(dto.getId(), uriInfo);
		this.patient =  PatientRepresentation.getPatientLink(dto.getPatient(), uriInfo);
		/*
		 * TODO: Need to fill in provider information.
		 */
		this.provider = ProviderRepresentation.getProviderLink(dto.getProvider(), uriInfo);
		
		this.diagnosis = dto.getDiagnosis();
		
		if (dto.getDrugTreatment() != null) {
					
			DrugTreatmentRepresentation dtr=new DrugTreatmentRepresentation(dto,uriInfo);
			this.drugTreatment=dtr;
			
		} else if (dto.getSurgeryTreatment() != null) {
			/*
			 * TODO finish this
			 */
			SurgeryRepresentation sr=new SurgeryRepresentation(dto,uriInfo);
			this.surgeryTreatment=sr;
		} else if (dto.getRadiologyTreatment() != null) {
			/*
			 * TODO finish this
			 */
			RadiologyRepresentation radio=new RadiologyRepresentation();
			this.radiologyTreatment=radio;
		}
	}

	public LinkType getPatient(){
		return this.getPatient();
	}
	
	public LinkType getProvider(){
		return this.getProvider();
	}
	
	public TreatmentDto getTreatment() {
		TreatmentDto m = null;
		if (this.getDrugTreatment() != null) {
			m = treatmentDtoFactory.createDrugTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			DrugType druginfo=new DrugType();
			druginfo.setDosage(this.drugTreatment.getDosage());
			druginfo.setDrugName(this.drugTreatment.getDrugName());
			m.setDrugTreatment(druginfo);
			
		} else if (this.getSurgeryTreatment() != null) {
			m = treatmentDtoFactory.createDrugTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			SurgeryType surgeryinfo=new SurgeryType();
			surgeryinfo.setDateOfSurgery(this.surgeryTreatment.getDateOfSurgery());
			surgeryinfo.setSurgeonName(this.surgeryTreatment.getSurgeonName());
			m.setSurgeryTreatment(surgeryinfo);
		} else if (this.getRadiologyTreatment() != null) {
			m = treatmentDtoFactory.createDrugTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			RadiologyType rinfo=new RadiologyType();
			rinfo.setRadiologistName(this.radiologyTreatment.getRadiologistName());
			m.setRadiologyTreatment(rinfo);
		}
		return m;
	}
}
