package ru.ksu.niimm.cll.uima.morph.opencorpora.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import ru.ksu.niimm.cll.uima.morph.opencorpora.model.Wordform;

public class MorphDictionaryImplTest {

	MorphDictionary dict;
	GramModel gm;

	@Before
	public void setUp() throws Exception {
		FileInputStream fis = FileUtils.openInputStream(new File(Thread
				.currentThread().getContextClassLoader()
				.getResource("opencorpora/dict.opcorpora.test.xml").getPath()));
		try {
			dict = XmlDictionaryParser.parse(fis);
			gm = dict.getGramModel();
		} finally {
			IOUtils.closeQuietly(fis);
		}
		dict.setWfPredictor(new DummyWordformPredictor(dict));
	}

	@Test
	public void testGetEntries() {
		assertEquals(1, dict.getEntries("гаджимуратович").size());
		assertEquals(3, dict.getEntries("село").size());
		assertEquals(3, dict.getEntries("а").size());
		assertEquals(4, dict.getEntries("мыркающий").size());
		int prtfGramIdx = gm.getGrammemNumId("PRTF");
		int verbGramIdx = gm.getGrammemNumId("VERB");
		assertTrue(Wordform.getAllGramBits(dict.getEntries("мыркающий").get(0),
				dict).get(prtfGramIdx));
		assertFalse(Wordform.getAllGramBits(
				dict.getEntries("мыркающий").get(0), dict).get(verbGramIdx));
		int mascGramIdx = gm.getGrammemNumId("masc");
		int singGramIdx = gm.getGrammemNumId("sing");
		int femnGramIdx = gm.getGrammemNumId("femn");
		int plurGramIdx = gm.getGrammemNumId("plur");
		assertTrue(dict.getEntries("мыркающий").get(0).getGrammems()
				.get(mascGramIdx));
		assertTrue(dict.getEntries("мыркающий").get(0).getGrammems()
				.get(singGramIdx));
		assertFalse(dict.getEntries("мыркающий").get(0).getGrammems()
				.get(femnGramIdx));
		assertFalse(dict.getEntries("мыркающий").get(0).getGrammems()
				.get(plurGramIdx));
	}

}
