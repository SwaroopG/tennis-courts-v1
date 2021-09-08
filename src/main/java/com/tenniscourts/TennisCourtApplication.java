package com.tenniscourts;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
public class TennisCourtApplication {

    public static void main(String[] args) {
        SpringApplication.run(TennisCourtApplication.class, args);
    }

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .tags(new Tag("Tennis Courts", "Tennis Court Management"),
                new Tag("Schedules", "Tennis Court Scheduling Management"),
                new Tag("Reservations", "Reservations Management"),
                new Tag("Guests", "Guests Management"))
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.tenniscourts"))
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Tennis Court Services")
            .description("Services to manage tennis court reservations.")
            .contact(new Contact("Swaroop Gaddameedhi", "https://www.linkedin.com/in/swaroop-gaddameedhi", "sg@gmail.com - Not my email"))
            .version("1.0")
            .build();
    }
}
