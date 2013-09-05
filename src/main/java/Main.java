import cf.spring.YamlPropertyContextInitializer;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mike Heath <elcapo@gmail.com>
 */
@Configuration
@EnableAutoConfiguration
public class Main {

	public static void main(String[] args) {
		final SpringApplication springApplication = new SpringApplication(Main.class);
		springApplication.addInitializers(new YamlPropertyContextInitializer("config", "config", "file:config/service-broker.yml"));
		final ApplicationContext applicationContext = springApplication.run(args);

		final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		final Level level = Level.toLevel(applicationContext.getEnvironment().getProperty("logging.level"), Level.INFO);
		loggerContext.getLogger("ROOT").setLevel(level);
	}

}
