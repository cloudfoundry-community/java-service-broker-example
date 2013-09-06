import cf.component.util.PidFile;
import cf.nats.CfNats;
import cf.nats.DefaultCfNats;
import cf.nats.RouterRegisterHandler;
import cf.spring.YamlPropertyContextInitializer;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import nats.client.Nats;
import nats.client.spring.NatsBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

/**
 * @author Mike Heath <elcapo@gmail.com>
 */
@Configuration
@EnableAutoConfiguration
public class Main {

	@Bean
	PidFile pidFile(Environment environment) throws IOException {
		return new PidFile(environment.getProperty("pidfile"));
	}

	@Bean
	Nats nats(ApplicationEventPublisher publisher, Environment environment) {
		return new NatsBuilder(publisher).addHost(environment.getProperty("nats.url", "nats://localhost")).connect();
	}

	@Bean
	CfNats cfNats(Nats nats) {
		return new DefaultCfNats(nats);
	}

	@Bean
	RouterRegisterHandler routerRegisterHandler(CfNats cfNats, Environment environment) {
		return new RouterRegisterHandler(
				cfNats,
				environment.getProperty("host.local", "127.0.0.1"),
				Integer.valueOf(environment.getProperty("host.port", "8080")),
				environment.getProperty("host.public", "service-broker")
				);
	}

	public static void main(String[] args) {
		final SpringApplication springApplication = new SpringApplication(Main.class);
		springApplication.addInitializers(new YamlPropertyContextInitializer("config", "config", "file:config/service-broker.yml"));
		final ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) springApplication.run(args);

		final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		final Level level = Level.toLevel(applicationContext.getEnvironment().getProperty("logging.level"), Level.INFO);
		loggerContext.getLogger("ROOT").setLevel(level);
	}

}
