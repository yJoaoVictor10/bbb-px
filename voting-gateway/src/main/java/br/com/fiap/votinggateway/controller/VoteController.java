package br.com.fiap.votinggateway.controller;

import br.com.fiap.votinggateway.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

    private final RabbitTemplate rabbitTemplate;

    public VoteController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private record VoteRequest(String participantId) {}

    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String vote(@RequestBody VoteRequest request){
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                request.participantId()
        );
        return "Vote accepted successfully";
    }

}
