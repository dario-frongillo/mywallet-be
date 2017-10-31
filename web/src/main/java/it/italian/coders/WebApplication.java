package it.italian.coders;

import com.mongodb.Mongo;
import it.italian.coders.model.authentication.Authorities;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.service.authentication.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = {"it.italian.coders"})
@ComponentScan(basePackages = {"it.italian.coders"})
@EnableMongoRepositories(basePackages = "it.italian.coders")
@EnableAsync
@EnableMongoAuditing
public class WebApplication {

    public static void main(String []args){
        SpringApplication.run(WebApplication.class);
    }

    private @Autowired
    Mongo mongo;

    private @Autowired
    MongoDbFactory mongoDbFactory;

    @Bean
    public CommandLineRunner loadData(UserManager userManager) {
        return (args) -> {

            mongo.dropDatabase(mongoDbFactory.getDb().getName());
            User user=userManager.findByUsername("admin");

            if(user == null){
                List<String> authorities=new ArrayList<String>();
                authorities.add(Authorities.ROLE_ACCESS.name());

                user=User.newBuilder()
                        .authorities(authorities)
                        .username("admin")
                        .enabled(true)
                        //admin
                        .password("$2a$04$qVmO9ADeOqGjd2xGkAwUR.kyrHyFewdROwdUIADx6LG5tiM1aD6X.")
                        .email("admin@gmail.com")
                        .build();

                user=userManager.save(user);
            }
        };
    }
}
