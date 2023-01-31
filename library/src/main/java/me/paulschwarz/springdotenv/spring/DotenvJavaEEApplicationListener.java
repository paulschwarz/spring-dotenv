package me.paulschwarz.springdotenv.spring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class DotenvJavaEEApplicationListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    Object obj = sce.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

    if (obj instanceof ConfigurableApplicationContext) {
      ConfigurableApplicationContext context = (ConfigurableApplicationContext) obj;
      DotenvPropertySource.addToEnvironment(context.getEnvironment());
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ServletContextListener.super.contextDestroyed(sce);
  }
}
