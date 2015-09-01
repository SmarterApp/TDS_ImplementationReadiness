package behavior.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base class for integration tests
 */
@WebAppConfiguration
@ContextHierarchy({
    @ContextConfiguration(locations = {"classpath:cucumber.xml"}),
    @ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/webapp-context.xml"})
})
public class BaseIntegration {
    @Autowired
    protected WebApplicationContext wac;
}
