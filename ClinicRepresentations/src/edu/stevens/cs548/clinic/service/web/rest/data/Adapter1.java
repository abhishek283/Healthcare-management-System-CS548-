//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.01 at 11:53:55 PM EST 
//


package edu.stevens.cs548.clinic.service.web.rest.data;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.stevens.cs548.clinic.service.web.rest.DateAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (DateAdapter.parseDate(value));
    }

    public String marshal(Date value) {
        return (DateAdapter.printDate(value));
    }

}
