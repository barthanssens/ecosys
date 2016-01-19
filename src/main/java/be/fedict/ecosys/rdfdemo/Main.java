/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.ecosys.rdfdemo;

import be.fedict.ecosys.rdfdemo.vocab.ADMS;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;

import be.fedict.ecosys.rdfdemo.vocab.DCAT;
import java.io.OutputStream;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class Main {
    private final static String OUT_META_NT = "demo-meta.nt";
    private final static String OUT_META_TTL = "demo-meta.ttl";
    
    private final static String BASE = "http://www.belgium.be";
    private final static String FEDICT = "http://data.fedict.be";
    
    /**
     * Generate various SKOS / codelist / taxonomy files.
     */
    private static void generateSkosFiles() {
        //generateSkos();
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

        // Eurostat "Belgium"
        Resource belgium = m.createResource("hhttp://nuts.geovocab.org/id/BE");
        form.addProperty(DCTerms.spatial, belgium);
        
        // Helpdesk
        Resource tech = m.createResource(FEDICT + "/org/helpdesk");
        Resource mailtech = m.createResource("mailto:servicedesk@fedict.be");
        Resource teltech = m.createResource("tel:+3222129600");
        tech.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "Helpdesk")
            .addProperty(VCARD.TEL, teltech)
            .addProperty(VCARD.EMAIL, mailtech);
        form.addProperty(DCAT.contactPoint, tech);
        
        // Business contact
        Resource bizz = m.createResource(FEDICT + "/org/pga");
        Resource mailbiz = m.createResource("mailto:info@fedict.be");
        bizz.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "PGA")
            .addProperty(VCARD.EMAIL, mailbiz);
        form.addProperty(DCAT.contactPoint, bizz);
        
        // N-Triples format
        try(OutputStream out = Files.newOutputStream(Paths.get(OUT_META_NT))) { 
            RDFDataMgr.write(out, m, Lang.NTRIPLES);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        generateSkosFiles();
        generateMetadataFile();
    }
}
