package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by pic on 29/04/2017.
 */
@Configuration
@ComponentScan(basePackages = "service")
public class ApplicationConfig extends WebMvcConfigurerAdapter
{

    @Bean
    public Properties dbProperties()
    {
        Properties properties = new Properties();

        try
        {
            properties.load(ApplicationConfig.class.getClassLoader().getResourceAsStream("db.properties"));
        }catch (IOException ioE)
        {
            System.err.println("erreur chgment db.properties");
        }
        return properties;
    }
}
