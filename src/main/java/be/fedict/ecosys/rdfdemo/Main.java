/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fedict.ecosys.rdfdemo;

import be.fedict.ecosys.rdfdemo.vocab.ADMS;
import be.fedict.ecosys.rdfdemo.vocab.DCAT;

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
import org.apache.jena.vocabulary.VCARD;



/**
 *
 * @author Bart Hanssens <bart.hanssens@fedict.be>
 */
public class Main {
    private final static String OUT_META_NT = "demo-meta.nt";
    private final static String OUT_META_TTL = "demo-meta.ttl";
    private final static String OUT_META_JSON = "demo-meta.json";
        
    // Some contstants
    private final static String BELGIUM = "http://data.belgium.be";
    private final static String FEDICT = "http://data.fedict.be";
    private final static String EU_PO = "http://publications.europa.eu/resource/authority";

    /**
     * Save RDF model / triples to equivalent files in different file formats
     * 
     * @param m RDF form
     * @throws IOException 
     */
    private static void save(Model m) throws IOException {
        // Shorter notation for formats supporting it
        m.setNsPrefix("adms", ADMS.getURI());
        m.setNsPrefix("dcat", DCAT.getURI());
        m.setNsPrefix("dcterms", DCTerms.getURI());
        //m.setNsPrefix("oh", OH.getURI());
        m.setNsPrefix("vcard", VCARD.getURI());
        
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
        Model m = ModelFactory.createDefaultModel();
        
        // Eurostat identifier for geographical area "Belgium"
        Resource belgium = m.createResource("http://nuts.geovocab.org/id/BE");
        
        // EU Publication Office identifiers for NL / FR
        Resource dutch = m.createResource(EU_PO + "/language/NLD");
        Resource french = m.createResource(EU_PO + "/language/FRA");
        
        // EU Publication Office identifiers for theme / category
        Resource agri = m.createResource(EU_PO + "/data-theme/AGRI");
        
        // Open Corporates identifier for "Fedict"
        Resource fedict = m.createResource("https://opencorporates.com/id/companies/be/0367302178");
        
        
        Resource form = m.createResource(BELGIUM + "/form/ECOPOC01");
        Resource form_nl = m.createResource(BELGIUM + "/form/ECOPOC01/nl");
        Resource form_fr = m.createResource(BELGIUM + "/form/ECOPOC01/fr");
        
        form.addProperty(DCTerms.title, "Intelligent formulier", "nl")
            .addProperty(DCTerms.title, "Formulaire intelligent", "fr")
            .addProperty(DCTerms.description, "Langere beschrijving", "nl")
            .addProperty(DCTerms.description, "Description longue", "fr")
            .addProperty(DCTerms.created, "2012-10-14", XSDDatatype.XSDdate)
            .addProperty(DCTerms.modified, "2015-11-19", XSDDatatype.XSDdate)
            .addProperty(DCTerms.type, DCTypes.InteractiveResource)
            .addProperty(DCTerms.publisher, fedict)
            .addProperty(DCTerms.spatial, belgium)
            .addProperty(DCTerms.language, dutch)
            .addProperty(DCTerms.language, french)
            .addProperty(DCTerms.hasVersion, form_nl)
            .addProperty(DCTerms.hasVersion, form_fr);
        
        form_nl.addProperty(DCTerms.isVersionOf, form)
            .addProperty(FOAF.page, "http://www.belgium.be/form/contact/nl/")
            .addProperty(DCTerms.language, dutch);
        
        form_fr.addProperty(DCTerms.isVersionOf, form)
            .addProperty(FOAF.page, "http://www.belgium.be/form/contact/fr/")
            .addProperty(DCTerms.language, french);
        
        // Define the helpdesk contact
        Resource tech = m.createResource(FEDICT + "/org/helpdesk");
        Resource mailtech = m.createResource("mailto:servicedesk@fedict.be");
        Resource teltech = m.createResource("tel:+3222129600");
        tech.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "Nederlandse helpdesk", "nl")
            .addProperty(VCARD.FN, "Helpdesk néerlandophone", "fr")
            .addProperty(VCARD.ROLE, "Technical")
            .addProperty(VCARD.TEL, teltech)
            .addProperty(VCARD.EMAIL, mailtech)
            .addProperty(DCTerms.subject, agri)
            .addProperty(DCTerms.language, dutch);
        form.addProperty(DCAT.contactPoint, tech);
        
        // Business contact
        Resource bizz = m.createResource(FEDICT + "/org/pga");
        Resource mailbiz = m.createResource("mailto:pga@fedict.be");
        Resource telbiz = m.createResource("tel:+3222129601");
        bizz.addProperty(RDF.type, VCARD.ORG)
            .addProperty(VCARD.FN, "PGA")
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
