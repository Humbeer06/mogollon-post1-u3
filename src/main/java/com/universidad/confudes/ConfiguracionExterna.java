package com.universidad.confudes;

import com.universidad.confudes.externo.qrcheck.QRCheckClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionExterna {

    @Bean
    public QRCheckClient qrCheckClient() {
        return new QRCheckClient();
    }
}