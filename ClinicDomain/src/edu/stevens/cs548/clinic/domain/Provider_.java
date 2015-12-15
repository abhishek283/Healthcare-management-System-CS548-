package edu.stevens.cs548.clinic.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-10-11T13:15:55.593-0400")
@StaticMetamodel(Provider.class)
public class Provider_ {
	public static volatile SingularAttribute<Provider, Long> Providerid;
	public static volatile SingularAttribute<Provider, String> NPI;
	public static volatile SingularAttribute<Provider, String> name;
	public static volatile SingularAttribute<Provider, String> Specialization;
	public static volatile ListAttribute<Provider, Treatment> treatments;
}
