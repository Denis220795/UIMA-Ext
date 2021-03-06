package ru.kfu.itis.issst.uima.ml;

import com.google.common.collect.Sets;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ml.CleartkProcessingException;
import org.cleartk.ml.Feature;
import org.cleartk.ml.Instance;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.kfu.cll.uima.tokenizer.fstype.Token;

import java.util.Objects;
import java.util.Set;

import static com.google.common.collect.ImmutableList.of;
import static org.mockito.Mockito.*;

/**
 * @author Rinat Gareev
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractTieredSequenceDataWriterTest extends TieredSequenceHandlerTestBase {

    @Mock
    private org.cleartk.ml.SequenceDataWriter<String> dataWriter1;
    @Mock
    private org.cleartk.ml.SequenceDataWriter<String> dataWriter2;
    @Mock
    private org.cleartk.ml.SequenceDataWriter<String> dataWriter3;

    @Test
    public void testCorrectWorkflow() throws CleartkProcessingException {
        AbstractTieredSequenceDataWriter dw = new TestTieredSequenceDataWriter();
        // stub
        JCas jCasMock = mock(JCas.class);
        // invoke
        dw.onCASChange(jCasMock);
        dw.write(jCasMock, mock(Annotation.class), of(mock(Token.class), mock(Token.class)),
                of(new String[]{"out-first-token", null, null}, new String[]{null, "out-second-token", null}));
        // verify
        verify(dataWriter1).write(
                argThat(PTestUtils.list(
                        // first instance
                        instance("out-first-token", new Feature("common-feature-0"), new Feature("tier0-0")),
                        // second instance
                        instance((String) null, new Feature("common-feature-1"), new Feature("tier0-1")))));
        verify(dataWriter2).write(
                argThat(PTestUtils.list(
                        // first instance
                        instance((String) null, new Feature("common-feature-0"), new Feature("tier1-0")),
                        // second instance
                        instance("out-second-token", new Feature("common-feature-1"), new Feature("tier1-1")))));
        verify(dataWriter3).write(
                argThat(PTestUtils.list(
                        // first instance
                        instance((String) null, new Feature("common-feature-0"), new Feature("tier2-0")),
                        // second instance
                        instance((String) null, new Feature("common-feature-1"), new Feature("tier2-1")))));
        dw.onCASChange(null);
    }

    private class TestTieredSequenceDataWriter extends AbstractTieredSequenceDataWriter {
        private TestTieredSequenceDataWriter() {
            this.dataWriters = of(dataWriter1, dataWriter2, dataWriter3);
            this.featureExtractor = new TestTieredFeatureExtractor();
        }
    }

    static <OUT> InstanceMatcher<OUT> instance(OUT label, Feature... features) {
        InstanceMatcher<OUT> result = new InstanceMatcher<OUT>();
        result.label = label;
        result.features = Sets.newHashSet(features);
        return result;
    }

    static class InstanceMatcher<OUT> extends TypeSafeMatcher<Instance<OUT>> {

        private OUT label;
        private Set<Feature> features;

        @Override
        protected boolean matchesSafely(Instance<OUT> item) {
            Set<Feature> itemFeatures = Sets.newHashSet(item.getFeatures());
            return Objects.equals(item.getOutcome(), label)
                    && itemFeatures.equals(features);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Instance[label=").appendValue(label)
                    .appendText(", features=").appendValue(features);
        }
    }
}
