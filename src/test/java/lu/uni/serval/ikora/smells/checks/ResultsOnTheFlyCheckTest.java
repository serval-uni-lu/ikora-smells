package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class ResultsOnTheFlyCheckTest {
    @Test
    void testWithNoResultsOnTheFly(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    Login Page\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyFromVariableAssignment(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    ${LOGIN PAGE}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n" +
                "${LOGIN PAGE}    Login Page";


        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyFromKeywordAssignment(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    ${LOGIN PAGE}=    Get Element Attribute    title_page\n" +
                "    Title Should Be    ${LOGIN PAGE}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getValue(), 0.0001);
    }

    @Test
    void testWithResultsOnTheFlyKeywordAssignment(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To   Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To\n" +
                "    [Arguments]    ${Login Page}\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    ${DELAY}\n" +
                "    Maximize Browser Window\n" +
                "    Title Should Be    ${Login Page}\n" +
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final ResultsOnTheFlyCheck check = new ResultsOnTheFlyCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getValue(), 0.0001);
    }
}