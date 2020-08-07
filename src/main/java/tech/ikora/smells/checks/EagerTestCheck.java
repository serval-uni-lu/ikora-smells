package tech.ikora.smells.checks;

import edu.stanford.nlp.neural.NeuralUtils;
import org.ejml.simple.SimpleMatrix;
import tech.ikora.analytics.difference.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.EagerTestVisitor;

import java.util.*;

public class EagerTestCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        EagerTestVisitor visitor = new EagerTestVisitor(testCase.getSteps().size());

        int position = 0;
        for(Step step: testCase.getSteps()){
            visitor.setPosition(position++);
            visitor.visit(step, new PathMemory());
        }

        List<SimpleMatrix> frequencyVectors = visitor.getFrequencyVectors();

        if(frequencyVectors.isEmpty() || frequencyVectors.get(0).numCols() == 0){
            return new SmellResult(SmellMetric.Type.EAGER_TEST, Double.NaN, Collections.emptySet());
        }

        int size = frequencyVectors.size();
        double sum = 0.0;

        for(int i = 0; i < size; ++i){
            for(int j = i + 1; j< size; ++j){
                double similarity = NeuralUtils.cosine(frequencyVectors.get(i), frequencyVectors.get(j));

                if(!Double.isNaN(similarity)){
                    sum += similarity;
                }
            }
        }

        double metric = 1 -  (sum / (double)size);

        return new SmellResult(SmellMetric.Type.EAGER_TEST, metric, new HashSet<>(testCase.getSteps()));
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP);
    }
}
