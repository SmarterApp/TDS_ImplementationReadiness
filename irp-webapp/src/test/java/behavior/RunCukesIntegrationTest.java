package behavior;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Entry point for running Cucumber
 */
@RunWith(Cucumber.class)
@CucumberOptions(glue = {"behavior.steps"})
public class RunCukesIntegrationTest {
}
