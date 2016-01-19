/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vocab;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Limited set of DCAT vocabulary, for demo purposes.
 * 
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class DCAT {
    private static final Model M= ModelFactory.createDefaultModel();
    public static final String NS = "http://www.w3.org/ns/dcat#";
    
    public static String getURI() {return NS;}
    
    public static final Resource NAMESPACE = M.createResource(NS);
    
    public static final Property contactPoint = M.createProperty(NS + "contactPoint");
}
