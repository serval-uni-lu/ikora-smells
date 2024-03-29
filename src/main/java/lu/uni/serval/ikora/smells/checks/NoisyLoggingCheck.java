package lu.uni.serval.ikora.smells.checks;

/*-
 * #%L
 * Ikora Smells
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;
import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class NoisyLoggingCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        Set<SourceNode> nodes = Collections.emptySet();
        double rawValue = Double.NaN;
        double normalizedValue = Double.NaN;

        if(testCase.getSetup().isPresent() || testCase.getTearDown().isPresent()){
            int statements = testCase.getSetup().map(KeywordStatistics::getStatementCount).orElse(1) - 1
                    + testCase.getTearDown().map(KeywordStatistics::getStatementCount).orElse(1) - 1;

            nodes = getFixtureLoggingNodes(testCase);
            rawValue = nodes.size();
            normalizedValue = rawValue / statements;
        }

        return new SmellResult(SmellMetric.Type.NOISY_LOGGING, rawValue, normalizedValue, nodes);
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return file.getTestCases().stream()
                .flatMap(t -> getFixtureLoggingNodes(t).stream())
                .filter(n -> n.getSourceFile() == file)
                .collect(Collectors.toSet());
    }

    private Set<SourceNode> getFixtureLoggingNodes(TestCase testCase){
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.LOG);

        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));
        testCase.getTearDown().ifPresent(s -> visitor.visit(s, new PathMemory()));

        return visitor.getNodes();
    }
}
