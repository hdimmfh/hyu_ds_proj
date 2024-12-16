package com.example.ds_proj.configuration;

import com.example.ds_proj.repository.DBRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Properties;


@Configuration
public class DBSTConfig {
    @Bean
    public Map<String, Object[][]> scenario(Properties dbProperty) {
        DBRepository dbRepository = new DBRepository();
        dbRepository.dbProperties = dbProperty;
        Map<String, Object[][]> data = dbRepository.getDataSource("Scenario_ap_st", 80000, 4, 0);
        return data;
    }

    @Bean
    public Map<String, Object[][]> encodedVar(Properties dbProperty) {
        DBRepository dbRepository = new DBRepository();
        dbRepository.dbProperties = dbProperty;
        Map<String, Object[][]> data = dbRepository.getDataSource("EncodedVar_st", 80000, 3000, 0);
        return data;
    }

}
