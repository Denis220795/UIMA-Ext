package ru.kfu.itis.issst.denis;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Created by Денис on 27.10.2015.
 */
public class Main {
    public static void main(String[] args) throws AnalysisEngineProcessException, InvalidXMLException, CpeDescriptorException, IOException, SAXException, ResourceInitializationException {
        Application app = new Application();
    }
}
