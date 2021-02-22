package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellResult;
import org.junit.jupiter.api.Test;
import lu.uni.serval.ikora.builder.BuildResult;
import lu.uni.serval.ikora.builder.Builder;
import lu.uni.serval.ikora.model.Project;
import lu.uni.serval.ikora.model.TestCase;

import static org.junit.jupiter.api.Assertions.*;

class LackOfEncapsulationCheckTest {
    @Test
    void testWithOneDirectLibraryCall(){
        final String code =
            "*** Test Cases ***\n" +
            "Valid Login\n" +
            "    Input username    user\n" +
            "    Input password    password\n" +
            "    Click Button    login_button\n" +
            "\n" +
            "*** Keywords ***\n" +
            "Input Username\n" +
            "    [Arguments]    ${username}\n" +
            "    Input Text    username_field    ${username}\n" +
            "\n" +
            "Input Password\n" +
            "    [Arguments]    ${password}\n" +
            "    Input Text    password_field    ${password}";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final LackOfEncapsulationCheck check = new LackOfEncapsulationCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(metric.getValue(), 0.33333, 0.0001);
    }

    @Test
    void testWithNoDirectLibraryCall(){
        final String code =
                "*** Test Cases ***\n" +
                        "Valid Login\n" +
                        "    Input username    user\n" +
                        "    Input password    password\n" +
                        "\n" +
                        "*** Keywords ***\n" +
                        "Input Username\n" +
                        "    [Arguments]    ${username}\n" +
                        "    Input Text    username_field    ${username}\n" +
                        "\n" +
                        "Input Password\n" +
                        "    [Arguments]    ${password}\n" +
                        "    Input Text    password_field    ${password}";

        final BuildResult build = Builder.build(code, true);
        final Project project = build.getProjects().iterator().next();

        final SmellConfiguration configuration = new SmellConfiguration();

        final TestCase testCase = project.findTestCase("<IN_MEMORY>", "Valid Login").iterator().next();
        final LackOfEncapsulationCheck check = new LackOfEncapsulationCheck();
        final SmellResult metric = check.computeMetric(testCase, configuration);

        assertEquals(0., metric.getValue(), 0.0001);
    }
}