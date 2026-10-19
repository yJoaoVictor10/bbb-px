package br.com.fiap.votingaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VotingAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingAggregatorApplication.class, args);
    }

}
