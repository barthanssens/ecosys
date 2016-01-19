/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.ecosys.rdfdemo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.XSD;
import vocab.DCAT;

/**
 *
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class Main {
    private final static String OUT_META = "demo-meta.nt";
    
    private final static String BASE = "http://www.belgium.be";
    
    private final static String FEDICT = "http://data.fedict.be";
    
    /**
     * Generate various SKOS / codelist / taxonomy files.
     */
    private static void generateSkosFiles() {
    
    }
    
    /**
     * Generate demo RDF metadata file.
     */
    private static void generateMetadataFile() {
        Model m = ModelFactory.createDefaultModel();
            
        Resource form = m.createResource(BASE + "/form/ECOPOC01");
        form.addProperty(DCTerms.title, "Intelligent formulier", "nl")
            .addProperty(DCTerms.title, "Formulaire intelligent", "fr")
            .addProperty(DCTerms.description, "Beschrijving", "nl")
            .addProperty(DCTerms.description, "Description", "fr")
            .addProperty(DCTerms.created, "2012-10-14", XSDDatatype.XSDdate)
            .addProperty(DCTerms.modified, "2015-11-19", XSDDatatype.XSDdate);
        
        Resource tech = m.createResource(FEDICT + "/org/helpdesk");
        Resource mailtech = m.createResource("mailto:servicedesk@fedict.be");
        Resource teltech = m.createResource("tel:+3222129600");
        tech.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "Helpdesk")
            .addProperty(VCARD.TEL, teltech)
            .addProperty(VCARD.EMAIL, mailtech);
        
        Resource bizz = m.createResource(FEDICT + "/org/pga");
        Resource mailbiz = m.createResource("mailto:servicedesk@fedict.be");
        bizz.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "PGA")
            .addProperty(VCARD.EMAIL, mailbiz);
 
        form.addProperty(DCAT.contactPoint, tech);
        form.addProperty(DCAT.contactPoint, bizz);
        
        
        try(BufferedWriter w = Files.newBufferedWriter(Paths.get(OUT_META))) { 
            m.write(w, "N-TRIPLES");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        generateSkosFiles();
        generateMetadataFile();
    }
}
