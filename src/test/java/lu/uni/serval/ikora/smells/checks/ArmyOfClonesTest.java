package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.analytics.clones.KeywordCloneDetection;
import lu.uni.serval.ikora.core.builder.BuildResult;
import lu.uni.serval.ikora.core.builder.Builder;
import lu.uni.serval.ikora.core.model.Project;
import lu.uni.serval.ikora.core.model.TestCase;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArmyOfClonesTest {
    @Test
    void testWithNoClones(){
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
                        "Open Again Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "\n" +
                        "*** Variables ***\n" +
                        "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();
        configuration.setClones(KeywordCloneDetection.findClones(project));

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ArmyOfClonesCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getNormalizedValue(), 0.0001);
        assertEquals(0., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithCloneType1(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "    Open Again Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "Open Again Browser To Login Page\n" +
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
        configuration.setClones(KeywordCloneDetection.findClones(project));

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ArmyOfClonesCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
        assertEquals(2., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithCloneType2(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "    Open Again Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    firefox\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "Open Again Browser To Login Page\n" +
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
        configuration.setClones(KeywordCloneDetection.findClones(project));

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ArmyOfClonesCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
        assertEquals(2., metric.getRawValue(), 0.0001);
    }

    @Test
    void testWithCloneType3(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "    Open Again Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    firefox\n" +
                        "    Set Selenium Speed    ${DELAY}\n" +
                        "    Log    ${DELAY}\n" +
                        "    Maximize Browser Window\n" +
                        "    Title Should Be    Login Page\n" +
                        "Open Again Browser To Login Page\n" +
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
        configuration.setClones(KeywordCloneDetection.findClones(project));

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final SmellCheck check = new ArmyOfClonesCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(1., metric.getNormalizedValue(), 0.0001);
        assertEquals(2., metric.getRawValue(), 0.0001);
    }
}