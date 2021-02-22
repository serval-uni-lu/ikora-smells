package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class HardCodedEnvironmentConfigurationCheckTest {
    @Test
    void testWithHardcodedEnvironmentConfiguration(){
        final String code =
                "*** Test Cases ***\n" +
                "Valid Login\n" +
                "    Open Browser To Login Page\n" +
                "\n" +
                "*** Keywords ***\n" +
                "Open Browser To Login Page\n" +
                "    Open Browser    http://localhost/    chrome\n" +
                "    Set Selenium Speed    0" +
                "    Maximize Browser Window\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final HardCodedEnvironmentConfigurationCheck check = new HardCodedEnvironmentConfigurationCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(1., metric.getValue(), 0.0001);
    }

    @Test
    void testWithoutHardcodedEnvironmentConfiguration(){
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
                "\n" +
                "*** Variables ***\n" +
                "${DELAY}      0\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final HardCodedEnvironmentConfigurationCheck check = new HardCodedEnvironmentConfigurationCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }

    @Test
    void testWithoutEnvironmentConfiguration(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Open Browser To Login Page\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Open Browser To Login Page\n" +
                        "    Open Browser    http://localhost/    chrome\n" +
                        "    Maximize Browser Window\n";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();

        final HardCodedEnvironmentConfigurationCheck check = new HardCodedEnvironmentConfigurationCheck();
        final SmellResult metric = check.computeMetric(testCase, null);

        assertEquals(Double.NaN, metric.getValue(), 0.0001);
    }

}