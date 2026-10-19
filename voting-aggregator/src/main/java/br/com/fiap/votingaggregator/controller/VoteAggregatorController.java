package br.com.fiap.votingaggregator.controller;

import br.com.fiap.votingaggregator.service.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/placar")
public class VoteAggregatorController {

    private final VoteService voteService;

    public VoteAggregatorController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public Map<String, Integer> placar() {
        return voteService.getTotalVotes();
    }

}
