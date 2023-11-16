package com.itmo.configuration;


import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class SparkConfig {
    @Value("${spark.application.name}")
    private String appName;

    @Value("${spark.master}")
    private String masterUri;

    @Bean
    @Primary
    public SparkConf sparkConf() {
        log.info("SparkConf Bean");
        return new SparkConf(true)
                .setAppName("appName")
                .setMaster(masterUri);
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        log.info("JavaSparkContext Bean");
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession() {
        log.info("SparkSession Bean");
        return SparkSession
                .builder()
                .appName(appName)
                .sparkContext(javaSparkContext().sc())
                .config(javaSparkContext().getConf())
                .getOrCreate();
    }
}

