package databaseentry;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Rest {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	String hello() {
		System.out.println("Hello World Welcome !");
		return "Hello World !";
	}

	@RequestMapping("/testme")
	String helloAgain() {
		return "You can't see me";
	}

}