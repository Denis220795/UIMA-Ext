/**
 * 
 */
package ru.ksu.niimm.cll.uima.morph.opencorpora.resource;

import ru.ksu.niimm.cll.uima.morph.opencorpora.model.Wordform;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class MorphDictionaryListenerBase implements MorphDictionaryListener {

	@Override
	public void onGramModelSet(MorphDictionary dict) {
	}

	@Override
	public void onWordformAdded(MorphDictionary dict, String wfString, Wordform wf) {
	}

}
