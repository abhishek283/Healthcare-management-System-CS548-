package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ProvierType;
@XmlRootElement
public class ProviderRepresentation extends ProvierType {
	
	private ProviderDtoFactory providerDtoFactory;

	public ProviderRepresentation () {
		super();
		this.providerDtoFactory = new ProviderDtoFactory();
	}
	
	public ProviderRepresentation (ProviderDto dto, UriInfo uriInfo) {
		this();
		this.providerId = getProviderLink(dto.getProviderId(), uriInfo);
		this.npi =  dto.getNpi();
		this.name = dto.getName();
		this.specialization = dto.getSpecialization();
		/*
		 * Call getTreatments to initialize empty list.
		 */
		List<LinkType> treatments = this.getTreatments();
		for (long t : dto.getTreatments()) {
			treatments.add(TreatmentRepresentation.getTreatmentLink(t, uriInfo));
		}
	}
	
	public ProviderDto getProviderDto() {
		ProviderDto p = providerDtoFactory.createProviderDto();
		p.setProviderId(Representation.getId(this.providerId));
		p.setNpi(this.npi);
		p.setName(this.name);
		p.setSpecialization(this.specialization);
		return p;
	}
	
	public List<LinkType> getLinkTreatments(){
		return this.getTreatments();
	}

	public static LinkType getProviderLink(long id, UriInfo uriInfo) {
		// TODO Auto-generated method stub
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("provider").path("{id}");
		String providerURI = ub.build(Long.toString(id)).toString();

		LinkType link = new LinkType();
		link.setUrl(providerURI);
		link.setRelation(Representation.RELATION_PATIENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
}
