package com.geometrypuzzle.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    public class GetProperitiesBean {
        public GetProperitiesBean(@Value("${server.port}") String port) {
            log.info("Started on http://localhost:{}", port);
        }
    }
}
