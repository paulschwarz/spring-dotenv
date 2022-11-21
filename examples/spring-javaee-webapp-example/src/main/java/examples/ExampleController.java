package examples;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

  @Value("${EXAMPLE_NAME:World}")
  String name;

  @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  public String home() {
    return String.format("Hello, <b>%s</b>", name);
  }
}
