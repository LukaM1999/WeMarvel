package com.wemarvel.wemarvel.config;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {
    @Bean
    public Pusher getPusher() {
        Pusher pusher = new Pusher("1463277", "b0aaeba2e43d3ebdf234", "1c3d4c6e4d082b96968b");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);
        return pusher;
    }
}
