package com.nitor.todoh2;

import com.nitor.todoh2.model.Todo;
import com.nitor.todoh2.repository.TodoRepository;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class TodoH2Application implements CommandLineRunner {

    @Autowired
    private TodoRepository todoRepository;

    public static void main(String[] args) {
        SpringApplication.run(TodoH2Application.class, args);
    }

    @Override
    public void run(String... args) {
        Todo todo1 = new Todo();
        todo1.setDone(false);
        todo1.setUser("Jack");
        todo1.setTask("Learn Spring Boot!");
        todo1.setTargetDate(LocalDate.of(2024, 12, 29));
        todoRepository.save(todo1);
        System.out.println("Todos saved: " + todoRepository.findAll());
        Todo todo2 = new Todo();
        todo2.setDone(true);
        todo2.setUser("Jill");
        todo2.setTask("Complete Angular project!");
        todo2.setTargetDate(LocalDate.of(2024, 12, 30));
        todoRepository.save(todo2);
        Todo todo3 = new Todo();
        todo3.setDone(false);
        todo3.setUser("Alice");
        todo3.setTask("Implement REST API with Spring Boot!");
        todo3.setTargetDate(LocalDate.of(2024, 12, 31));
        todoRepository.save(todo3);
        System.out.println("Todos saved: " + todoRepository.findAll());

    }

    @Bean
    public OpenAPI springTodoOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Todo API")
                        .description("Todo h2 application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Todo Wiki Documentation")
                        .url("https://Todo.wiki.github.org/docs"));
    }


}
