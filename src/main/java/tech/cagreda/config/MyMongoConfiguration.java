package tech.cagreda.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.mongodb-tertiary")
@EnableMongoRepositories(
    basePackages =
            "tech.adapter.out.db.mongo.repository.onebank",
    mongoTemplateRef = "libraryMongoTemplate")
public class MyMongoConfiguration {

  private String uri;
}
