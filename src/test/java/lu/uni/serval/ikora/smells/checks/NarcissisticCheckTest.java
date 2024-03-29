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

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NarcissisticCheckTest {
    @Test
    void testWithNoPronoun(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test case without personal pronoun\n" +
                "    Given browser is opened to login page\n" +
                "    When user \"demo\" logs in with password \"mode\"\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Log    Browser is opened to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Log    ${username}\n" +
                "    Log    ${password}\n" +
                "\n" +
                "Welcome page should be open\n" +
                "    Log    Welcome page should be open\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test case without personal pronoun").iterator().next();
        final NarcissisticCheck check = new NarcissisticCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithAllPronoun(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test case with all personal pronoun\n" +
                "    Given I am on login page\n" +
                "    When I put my username and password\n" +
                "    Then I should be on the welcome page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "I am on login page\n" +
                "    Log    I am on login page\n" +
                "\n" +
                "I put my username and password\n" +
                "    Log    I put my username and password\n" +
                "\n" +
                "I should be on the welcome page\n" +
                "    Log    I should be on the welcome page";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test case with all personal pronoun").iterator().next();
        final NarcissisticCheck check = new NarcissisticCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
        assertEquals(3., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithSomePronoun(){
        final String code =
                "*** Test Cases ***\n" +
                "\n" +
                "Test case with some personal pronoun\n" +
                "    Given I am on login page\n" +
                "    When I put my usernam and password\n" +
                "    Then welcome page should be open\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Browser is opened to login page\n" +
                "    Log    Browser is opened to login page\n" +
                "\n" +
                "User \"${username}\" logs in with password \"${password}\"\n" +
                "    Log    ${username}\n" +
                "    Log    ${password}\n" +
                "\n" +
                "Welcome page should be open\n" +
                "    Log    Welcome page should be open\n" +
                "\n" +
                "I am on login page\n" +
                "    Log    I am on login page\n" +
                "\n" +
                "I put my usernam and password\n" +
                "    Log    I put my username and password\n" +
                "\n" +
                "I should be on the welcome page\n" +
                "    Log    I should be on the welcome page";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Test case with some personal pronoun").iterator().next();
        final NarcissisticCheck check = new NarcissisticCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.6666, metric.getNormalizedValue(), 0.0001);
        assertEquals(2., metric.getRawValue(), 0.0001);
    }
}
