package nology.io.employee.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    //must have to config enpoint access

    public void addCorsMappings(CorsRegistry registry){

        //local endpoints and possbiely deployed site
        String[] allowedOrigins = {"http://localhost:5173/", "http://127.0.0.1:5173/"};

        registry.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods("*").allowedHeaders("*");

    }
    
}
