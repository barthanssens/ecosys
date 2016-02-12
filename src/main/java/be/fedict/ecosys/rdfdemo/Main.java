/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.ecosys.rdfdemo;

import be.fedict.ecosys.rdfdemo.vocab.ADMS;
import be.fedict.ecosys.rdfdemo.vocab.DCAT;
import be.fedict.ecosys.rdfdemo.vocab.REGORG;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.DCTypes;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.VCARD;



/**
 *
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class Main {
    // Output formats / RDF serializations
    private final static String OUT_META_NT = "demo-meta.nt";
    private final static String OUT_META_TTL = "demo-meta.ttl";
    private final static String OUT_META_JSON = "demo-meta.json";
        
    // Some contstants,(parts of) identifiers for various things
    private final static String ID_BELGIUM = "http://data.belgium.be";
    private final static String ID_FEDICT = "http://data.fedict.be";
    private final static String ID_PRIMEM = "http://data.kanselarij.belgium.be";
    private final static String ID_PO = "http://publications.europa.eu/resource/authority";
    private final static String ID_CORP = "https://opencorporates.com/id/companies";

    // Location of the portal
    private final static String PORTAL = "http://www.belgium.be";
    
    
    /**
     * Save RDF model / triples to equivalent files in different file formats.
     * 
     * @param m RDF form
     * @throws IOException 
     */
    private static void save(Model m) throws IOException {
        // Shorter notations for formats supporting it
        m.setNsPrefix("adms", ADMS.getURI());
        m.setNsPrefix("dcat", DCAT.getURI());
        m.setNsPrefix("dcterms", DCTerms.getURI());
        //m.setNsPrefix("oh", OH.getURI());
        m.setNsPrefix("skos", SKOS.getURI());
        m.setNsPrefix("rov", REGORG.getURI());
        m.setNsPrefix("vcard", VCARD.getURI());
        
        // Note: All output formats represent exactly the same RDF info
        
        // N-Triples format, extremely simple but very verbose
        OutputStream nt = Files.newOutputStream(Paths.get(OUT_META_NT));
        RDFDataMgr.write(nt, m, Lang.NTRIPLES);
        nt.close();
        
        // Same RDF, but in turtle format, a human friendly format
        OutputStream ttl = Files.newOutputStream(Paths.get(OUT_META_TTL));
        RDFDataMgr.write(ttl, m, Lang.TURTLE);
        ttl.close();
        
        // Once more, this time as JSON-LD
        OutputStream json = Files.newOutputStream(Paths.get(OUT_META_JSON));
        RDFDataMgr.write(json, m, Lang.JSONLD);
        json.close();
    }
    
    
    /**
     * Generate demo RDF metadata file.
     */
    private static void generateMetadataFile() {
        // Note: there is no "order" in the output
        // i.e. the info added to the model, can end up anywhere in the output,
        // regardless the order of the code below
        
        Model m = ModelFactory.createDefaultModel();
        
        // Eurostat identifier for "Belgium"
        Resource belgium = m.createResource("http://nuts.geovocab.org/id/BE");
        
        // EU Publication Office identifiers for NL / FR
        Resource dutch = m.createResource(ID_PO + "/language/NLD");
        Resource french = m.createResource(ID_PO + "/language/FRA");
        
        // EU Publication Office identifiers for theme / category
        Resource envi = m.createResource(ID_PO + "/data-theme/ENVI");
        
        // Open Corporates identifier for "Fedict" and "Chancellerie"
        // Improvement: BCE should be available als linked open data 
        Resource fedict = m.createResource(ID_CORP + "/be/0367302178");
        Resource primem = m.createResource(ID_CORP + "/be/0308357951");
        
        Resource form = m.createResource(ID_BELGIUM + "/form/ECOPOC01");
        // Language variants
        Resource form_nl = m.createResource(ID_BELGIUM + "/form/ECOPOC01/nl");
        Resource form_fr = m.createResource(ID_BELGIUM + "/form/ECOPOC01/fr");
        
        form.addProperty(DCTerms.title, "Intelligent formulier", "nl")
            .addProperty(DCTerms.title, "Formulaire intelligent", "fr")
            .addProperty(DCTerms.description, "Via deze online toepassing kunnen organisatoren een duurzaamheidsscore berekenen voor een gepland evenement", "nl")
            .addProperty(DCTerms.description, "Cette application permets aux organisateurs d'evenement de calculer un index de durabilité pour un événement planifié", "fr")
            .addProperty(DCTerms.created, "2012-10-14", XSDDatatype.XSDdate)
            .addProperty(DCTerms.modified, "2015-11-19", XSDDatatype.XSDdate)
            .addProperty(DCTerms.type, DCTypes.InteractiveResource)
            .addProperty(DCTerms.publisher, primem)
            .addProperty(DCTerms.subject, envi)
            .addProperty(DCTerms.spatial, belgium)
            .addProperty(DCTerms.language, dutch)
            .addProperty(DCTerms.language, french)
            .addProperty(DCTerms.hasVersion, form_nl)
            .addProperty(DCTerms.hasVersion, form_fr);
        
        form_nl.addProperty(DCTerms.isVersionOf, form)
            .addProperty(FOAF.page, PORTAL + "/form/milieu/nl/")
            .addProperty(DCTerms.language, dutch);
        
        form_fr.addProperty(DCTerms.isVersionOf, form)
            .addProperty(FOAF.page, PORTAL + "/form/environnement/fr/")
            .addProperty(DCTerms.language, french);
        
        // Add some info on the publisher
        primem.addProperty(RDF.type, REGORG.regOrg)
            .addProperty(REGORG.legalName, "Service Public Fédéral Chancellerie du Premier Ministre", "fr")
            .addProperty(SKOS.altLabel, "SPF Chancellerie", "fr")    
            .addProperty(REGORG.legalName, "Federale Overheidsdienst Kanselarij van de Eerste Minister", "nl")
            .addProperty(SKOS.altLabel, "FOD Kanselarij", "nl");    
        
        // Define the helpdesk contact
        // Note: a contact of an organization is not the organization itself
        Resource tech = m.createResource(ID_FEDICT + "/contact/helpdesk");
        Resource mailtech = m.createResource("mailto:servicedesk@fedict.be");
        Resource teltech = m.createResource("tel:+3222129600");
        tech.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "Nederlandse helpdesk", "nl")
            .addProperty(VCARD.FN, "Helpdesk néerlandophone", "fr")
            .addProperty(VCARD.ROLE, "Technical")
            .addProperty(VCARD.TEL, teltech)
            .addProperty(VCARD.EMAIL, mailtech)
            .addProperty(DCTerms.language, dutch);
        form.addProperty(DCAT.contactPoint, tech);
        
        // Business contact
        Resource bizz = m.createResource(ID_PRIMEM + "/contact/ifdd");
        Resource mailbiz = m.createResource("mailto:contact@ifdd.fed.be");
        Resource telbiz = m.createResource("tel:+3225010462");
        bizz.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "IFDD")
            .addProperty(VCARD.ROLE, "Business")
            .addProperty(VCARD.TEL, telbiz)
            .addProperty(VCARD.EMAIL, mailbiz)
            .addProperty(DCTerms.language, dutch)
            .addProperty(DCTerms.language, french);
        form.addProperty(DCAT.contactPoint, bizz);
        
        try {
            save(m);
        } catch (IOException ex) {
            System.err.println("Error writing RDF to files: " + ex.getMessage());
        }
        
                
    }
    
    public static void main(String[] args) {
        generateMetadataFile();
    }
}
