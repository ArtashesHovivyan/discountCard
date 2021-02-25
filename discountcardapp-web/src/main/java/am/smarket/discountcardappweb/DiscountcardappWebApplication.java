package am.smarket.discountcardappweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"am.smarket.discountcardappcommon.*", "am.smarket.*"})
@EntityScan(basePackages = "am.smarket.discountcardappcommon.model")
@EnableJpaRepositories(basePackages = "am.smarket.discountcardappcommon.repository")
public class DiscountcardappWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscountcardappWebApplication.class, args);
    }

}
