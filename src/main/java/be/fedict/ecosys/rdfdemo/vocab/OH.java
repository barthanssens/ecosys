/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.ecosys.rdfdemo.vocab;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Limited set of Opening Hours vocabulary, for demo purposes.
 * 
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class OH {
    private static final Model M = ModelFactory.createDefaultModel();
    
    public static final String NS = "http://semweb.mmlab.be/ns/oh#";
    
    public static String getURI() {return NS;}
    
    public static final Resource NAMESPACE = M.createResource(NS);
    
    public static final Property hasOpeningHours = M.createProperty(NS + "hasOpeningHours");
    public static final Property OpeningHours = M.createProperty(NS + "OpeningHours");
    public static final Property OpeningHoursType = M.createProperty(NS + "OpeningHoursType");
}
