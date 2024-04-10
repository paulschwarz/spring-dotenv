package me.paulschwarz.springdotenv.spring;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * Add the {@link DotenvPropertySource} to Spring's application environment.
 */
public class DotenvJakartaApplicationListener implements ServletContextListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    Object obj = sce.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

    if (obj instanceof ConfigurableApplicationContext context) {
      DotenvPropertySource.addToEnvironment(context.getEnvironment());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContextListener.super.contextDestroyed(sce);
  }
}
