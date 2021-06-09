package ru.otus.migration.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.migration.convertor.ConvertBookToRelationalBook;
import ru.otus.migration.model.nosql.Book;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
public class JobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job migrationLibraryJob(Step transformData) {
        return jobBuilderFactory.get("migrationLibraryJob")
                .incrementer(new RunIdIncrementer())
                .flow(transformData)
                .end()
                .build();
    }


    @Bean
    public MongoItemReader<Book> reader(MongoTemplate mongoTemplate) {
        return new MongoItemReaderBuilder<Book>()
                .name("mongoItemReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(Book.class).sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JpaItemWriter<ru.otus.migration.model.relational.Book> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<ru.otus.migration.model.relational.Book> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public ItemProcessor<Book, ru.otus.migration.model.relational.Book> processor(ConvertBookToRelationalBook convertBookToRelationalBook) {
        return convertBookToRelationalBook::convert;
    }


    @Bean
    public Step transformData(ItemReader<Book> reader, ItemWriter<ru.otus.migration.model.relational.Book> writer,
                              ItemProcessor<Book, ru.otus.migration.model.relational.Book> itemProcessor) {
        return stepBuilderFactory.get("step1")
                .<Book, ru.otus.migration.model.relational.Book>chunk(1)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}
