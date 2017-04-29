package app;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by pic on 29/04/2017.
 */
public class Application {


    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("config");

        context.close();
    }
}
