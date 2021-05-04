package com.epam.audio_streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@EnableJms
public class AudioStreamingApplication {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(AudioStreamingApplication.class, args);
    }

}
