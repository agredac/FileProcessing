package tech.cagreda.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@ConditionalOnClass(MyMongoAutoConfiguration.class)
@EnableConfigurationProperties(MyMongoConfiguration.class)
@RequiredArgsConstructor
@ComponentScan("tech")
public class MyMongoAutoConfiguration {

  private final MyMongoConfiguration mongoDBProperties;

  @Bean(name = "libraryMongoTemplate")
  public MongoTemplate libraryMongoTemplateConfig() {
    return new MongoTemplate(libraryMongoDatabaseFactoryConfig(mongoDBProperties));
  }

  @Bean
  public MongoDatabaseFactory libraryMongoDatabaseFactoryConfig(
      MyMongoConfiguration mongoDBProperties) {
    return new SimpleMongoClientDatabaseFactory(mongoDBProperties.getUri());
  }
}
