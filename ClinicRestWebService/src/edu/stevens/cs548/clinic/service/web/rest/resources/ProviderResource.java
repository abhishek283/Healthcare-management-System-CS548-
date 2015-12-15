package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ProvierType;

@Path("/provider")
@RequestScoped
public class ProviderResource {
	
	final static Logger logger = Logger.getLogger(ProviderResource.class.getCanonicalName());
	
    @Context
    private UriInfo context;

    @Inject
    private IProviderServiceLocal providerService;
    
    private ProviderDtoFactory providerDtoFactory=new ProviderDtoFactory();
    /**
     * Default constructor. 
     */
    public ProviderResource() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieves representation of an instance of ProviderResource
     * @return an instance of ProvierType
     */
    @GET
    @Produces("application/xml")
    public ProvierType getXml() {
        // TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ProviderResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(ProvierType content) {
    }
    
    @GET
    @Path("site")
    @Produces("text/plain")
    public String getSiteInfo() {
    	return providerService.siteInfo();
    }
    
    @POST
   	@Consumes("application/xml")
       public Response addProvider(ProviderRepresentation providerRep) {
       	try {
       		ProviderDto dto = providerDtoFactory.createProviderDto();
       		dto.setNpi(providerRep.getNpi());
       		dto.setName(providerRep.getName());
       		dto.setSpecialization(providerRep.getSpecialization());
       		long pid = providerService.addProvider(dto);
       		UriBuilder ub = context.getAbsolutePathBuilder().path("{pid}");
       		URI url = ub.build(Long.toString(pid));
       		return Response.created(url).build();
       	} catch (ProviderServiceExn e) {
       		throw new WebApplicationException("Not Found",403);
       	}
       }
           
                 
       @GET
       @Path("{id}")
       @Produces("application/xml")
       public ProviderRepresentation getProviderByDbId(@PathParam("id") String pid)
       {
       	try
       	{ 		
       			long providerId = Long.parseLong(pid);
       			ProviderDto providerDTO = providerService.getProviderByDbId(providerId);
       			ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO, context);
       			return providerRep;
       	}
       	catch(ProviderServiceExn e)
       	{
       		throw new WebApplicationException("Not Found",403);
       	}    	
       }
             
       @GET
       @Path("byNPI")
       @Produces("application/xml")
       public ProviderRepresentation getProviderByNpi(@QueryParam("id") String npi)
       {
	       	try
	       	{ 		
       			ProviderDto providerDTO = providerService.getProviderByNPI(npi);
       			ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO, context);
       			return providerRep;
	       	}
	       	catch(ProviderServiceExn e)
	       	{
	       		throw new WebApplicationException("Not Found",403);
	       	}    	
       }
       
       @GET
       @Path("id/treatments")
       @Produces("application/xml")
       public List<LinkType> getTreatmentofPatient(@HeaderParam("x-patient") String patientId){   		
       	PatientResource patres = new PatientResource();
       	PatientRepresentation patrep=patres.getPatientByPatientId(patientId);
       	return patrep.getLinkTreatments();
       }
      
}