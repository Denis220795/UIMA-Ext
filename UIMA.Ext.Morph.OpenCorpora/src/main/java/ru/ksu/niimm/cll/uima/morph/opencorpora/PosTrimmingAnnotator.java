/**
 * 
 */
package ru.ksu.niimm.cll.uima.morph.opencorpora;

import java.util.Collection;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.opencorpora.cas.Word;
import org.opencorpora.cas.Wordform;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.descriptor.ExternalResource;
import org.uimafit.util.FSCollectionFactory;
import org.uimafit.util.JCasUtil;

import ru.ksu.niimm.cll.uima.morph.opencorpora.resource.MorphDictionaryHolder;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class PosTrimmingAnnotator extends JCasAnnotator_ImplBase {

	public static final String RESOURCE_MORPH_DICTIONARY = "MorphDictionary";
	public static final String PARAM_TARGET_POS_CATEGORIES = "targetPosCategories";

	@ExternalResource(key = RESOURCE_MORPH_DICTIONARY)
	private MorphDictionaryHolder dictHolder;
	@ConfigurationParameter(name = PARAM_TARGET_POS_CATEGORIES, mandatory = true)
	private String[] targetPosCategories;
	// derived
	private PosTrimmer trimmer;

	@Override
	public void initialize(UimaContext ctx) throws ResourceInitializationException {
		super.initialize(ctx);
		trimmer = new PosTrimmer(dictHolder.getDictionary(), targetPosCategories);
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (Word word : JCasUtil.select(jcas, Word.class)) {
			FSArray wordformsFSArray = word.getWordforms();
			if (wordformsFSArray == null) {
				continue;
			}
			Collection<Wordform> wordforms = FSCollectionFactory.create(wordformsFSArray,
					Wordform.class);
			for (Wordform wf : wordforms) {
				trimmer.trim(jcas, wf);
			}
		}
	}
}