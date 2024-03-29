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

class HardcodedValuesCheckTest {
    @Test
    void testWithOneHardcodedValue(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    User \\\"demo\\\" logs in with password \\\"mode\\\"\n" +
                "\n" +
                "*** Keywords ***\n" +
                "\n" +
                "User \\\"${username}\\\" logs in with password \\\"${password}\\\"\n" +
                "    Input username    ${username}\n" +
                "    Input password    ${password}\n" +
                "    Submit credentials\n" +
                "\n" +
                "Input Username\n" +
                "    [Arguments]    ${username}\n" +
                "    Input Text    ${USERNAME_FIELD}    ${username}\n" +
                "\n" +
                "Input Password\n" +
                "    [Arguments]    ${password}\n" +
                "    Input Text    password_field    ${password}\n" +
                "\n" +
                "Submit Credentials\n" +
                "    Click Button    ${BOTTON_FIELD}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${USERNAME_FIELD}      username_field\n" +
                "${BOTTON_FIELD}        login_button";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final HardcodedValuesCheck check = new HardcodedValuesCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0.14285, metric.getNormalizedValue(), 0.0001);
    }
}
