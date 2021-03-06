package ru.kfu.itis.issst.denis;

import org.apache.uima.UIMAException;

import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.*;
import org.apache.uima.collection.metadata.CpeDescriptorException;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.xml.sax.SAXException;
import ru.kfu.itis.cll.uima.consumer.XmiWriter;
import ru.kfu.itis.cll.uima.cpe.XmiCollectionReader;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

public class Application {

    private String InputDir, OutputDir;

        Application(String input, String output) throws InvalidXMLException, ResourceInitializationException, IOException, AnalysisEngineProcessException, CpeDescriptorException, SAXException {
            InputDir=input;
            OutputDir=output;
            System.out.println("Директории назначены");
            initialize();
        }

        private void initialize() throws IOException, InvalidXMLException, ResourceInitializationException, AnalysisEngineProcessException, CpeDescriptorException, SAXException {

            CollectionReaderDescription readerDescription =
                    CollectionReaderFactory.createReaderDescription(
                            XmiCollectionReader.class,
                            XmiCollectionReader.PARAM_INPUTDIR, InputDir);

            CollectionReader collectionReader = CollectionReaderFactory.createReader(readerDescription); // по дескриптору readerDescription

            AnalysisEngineDescription writerDesc =
                    AnalysisEngineFactory.createEngineDescription(
                            XmiWriter.class,
                            XmiWriter.PARAM_OUTPUTDIR, OutputDir);

            AnalysisEngine writer = AnalysisEngineFactory.createEngine(writerDesc); // по дескриптору writerDesc

            AnalysisEngine pipelineEngine = createEngine("ru.kfu.itis.issst.denis.pipeline");

            try {
                SimplePipeline.runPipeline(collectionReader, pipelineEngine, writer);
            } catch (UIMAException e) {
                e.printStackTrace();
            }

        }
    }