package com.simple.portal.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(environmentStringPBEConfig());
        return encryptor;
    }

    @Bean("environmentStringPBEConfig")
    public EnvironmentStringPBEConfig environmentStringPBEConfig() {
        EnvironmentStringPBEConfig environmentStringPBEConfig = new EnvironmentStringPBEConfig();
        environmentStringPBEConfig.setPasswordEnvName("APP_ENCRYPTION_PASSWORD");
        environmentStringPBEConfig.setAlgorithm("PBEWithMD5AndDES");
        environmentStringPBEConfig.setPoolSize(1);
        environmentStringPBEConfig.setKeyObtentionIterations("1000");
        environmentStringPBEConfig.setProviderName("SunJCE");
        environmentStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        environmentStringPBEConfig.setStringOutputType("base64");
        return environmentStringPBEConfig;
    }
}
