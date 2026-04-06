package org.example.multi_tenant_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@EnableJpaAuditing
public class MultiTenantTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenantTaskApplication.class, args);
    }

}
