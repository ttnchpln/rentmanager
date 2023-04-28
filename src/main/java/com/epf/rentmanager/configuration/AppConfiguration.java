package com.epf.rentmanager.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ // packages dans lesquels chercher les beans
        "com.epf.rentmanager.service",
        "com.epf.rentmanager.dao",
        "com.epf.rentmanager.persistence"
})

public class AppConfiguration {}
