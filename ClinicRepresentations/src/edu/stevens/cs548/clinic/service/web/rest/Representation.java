package edu.stevens.cs548.clinic.service.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;

public abstract class Representation {
public static final String Relations="http://cs548.stevens.edu/clinic/rest/";
	
	public static final String RELATION_TREATMENT=Relations+"treatment";
	
	public static final String RELATION_PATIENT=Relations+"patient";
	
	public static final String RELATION_PROVIDER=Relations+"provider";
	
	public static final String NAMESPACE="http://cs548.stevens.edu/clinic/service/web/rest/data";
	
	public static final String DAP_NAMESPACE=NAMESPACE+"/dap";
	
	public static final String MEDIA_TYPE= "application/xml";
	
	//public abstract List<Link> getlinks();
	public static Long getId(LinkType link) {
		try {
			URI uri = new URI(link.getUrl());
			String[] segments = uri.getPath().split("/");
			if (segments == null || segments.length <= 0) {
				return null;
			}
			return Long.parseLong(segments[segments.length - 1]);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
}
