package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.LackOfDocumentationVisitor;

import java.util.Set;

public class LackOfDocumentationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        LackOfDocumentationVisitor visitor = new LackOfDocumentationVisitor();
        visitor.visit(testCase, new PathMemory());

        final int documented = visitor.getDocumentedKeyword();
        final int total = visitor.getTotalKeywords();
        final double metric = (double)documented / (double)total;

        return new SmellResult(SmellMetric.Type.LACK_OF_DOCUMENTATION, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return change.getActions().stream().anyMatch(a -> a.getType() == Action.Type.ADD_DOCUMENTATION);
    }
}
