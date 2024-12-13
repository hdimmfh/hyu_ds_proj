package com.example.ds_proj.configuration;

import com.example.ds_proj.repository.DBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import java.util.*;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class DBPathConfig {

    @Bean
    @Primary
    public Properties dbProperty() {
        Properties dbProperties = new Properties();
        dbProperties.put("SOLUTION_ODL", "DB/Solution_odl");
        dbProperties.put("VAR_ODL", "DB/var_odl");
        dbProperties.put("ENCODEDVAR_ST", "DB/EncodedVar_st.csv");
        dbProperties.put("SCENARIO_AP_ST", "DB/Scenario_ap_st.csv");
        return dbProperties;
    }
}

