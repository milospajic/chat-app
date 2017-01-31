package rs.bg.chat.angular;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class AngularTest {

	@RequestMapping("/")
	public String home() {
		return "forward:/angular_test.html";
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(AngularTest.class).properties("server.port=9999", "security.basic.enabled=false")
				.run(args);
	}

}
