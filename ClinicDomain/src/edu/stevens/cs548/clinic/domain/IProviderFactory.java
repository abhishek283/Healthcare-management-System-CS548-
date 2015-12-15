package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;

public interface IProviderFactory {

	public Provider createProvider(String npi, String name, String specialization) throws ProviderExn;

}
