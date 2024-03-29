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

import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.utils.FileUtils;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeadCodeCheckTest {
    @Test
    void testWithNoDeadCode(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    0\n" +
                "    Maximize Browser Window\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final DeadCodeCheck check = new DeadCodeCheck();
        final Set<SourceNode> nodes = check.collectInstances(project.getSourceFile(FileUtils.IN_MEMORY).get(), new SmellConfiguration());

        assertTrue(nodes.isEmpty());
    }

    @Test
    void testWithDeadCodeKeyword(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${SERVER}    chrome\n" +
                "    Set Selenium Speed    0\n" +
                "    Maximize Browser Window\n" +
                "Go To Login Page\n" +
                "    Go To    ${LOGIN URL}\n" +
                "    Login Page Should Be Open\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${LOGIN URL}      http://${SERVER}/\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final DeadCodeCheck check = new DeadCodeCheck();
        final Set<SourceNode> nodes = check.collectInstances(project.getSourceFile(FileUtils.IN_MEMORY).get(), new SmellConfiguration());

        assertEquals(1, nodes.size());
    }

    @Test
    void testWithDeadCodeVariable(){
        final String code =
                "*** Settings ***\n" +
                "Library    Selenium2Library\n" +
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    ${SERVER}    chrome\n" +
                "    Set Selenium Speed    0\n" +
                "    Maximize Browser Window\n" +
                "\n" +
                "*** Variables ***\n" +
                "${SERVER}         localhost:7272\n" +
                "${LOGIN URL}      http://${SERVER}/\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();
        final DeadCodeCheck check = new DeadCodeCheck();
        final Set<SourceNode> nodes = check.collectInstances(project.getSourceFile(FileUtils.IN_MEMORY).get(), new SmellConfiguration());

        assertEquals(1, nodes.size());
    }
}
