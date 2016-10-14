package hack.core.config;

import java.util.HashMap;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan({ "hack" })
public class Bootstrap {

    public static void main(String[] args) {
    	HashMap<String, Object> props = new HashMap<>();
    	props.put("server.port", 80);

    	new SpringApplicationBuilder()
    	    .sources(Bootstrap.class)                
    	    .properties(props)
    	    .run(args);
    }

}
