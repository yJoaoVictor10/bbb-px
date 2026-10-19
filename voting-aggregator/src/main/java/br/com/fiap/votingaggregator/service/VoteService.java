package br.com.fiap.votingaggregator.service;

import br.com.fiap.votingaggregator.config.RabbitConfig;
import br.com.fiap.votingaggregator.model.Vote;
import br.com.fiap.votingaggregator.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private ConcurrentHashMap<String,Integer > totalVotes = new ConcurrentHashMap<>();

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void processVote(String participantId) {
        // { "cabecao": 123, "daine": "456" }
        log.info("Recebendo mensagem com o voto no participante: {}", participantId);
        totalVotes.merge(participantId, 1, Integer::sum);
    }

    public Map<String, Integer> getTotalVotes() {
        return voteRepository.findAll().stream()
                .collect(
                        ConcurrentHashMap::new,
                        (map, vote) -> map.put(vote.getParticipantId(), vote.getTotalVotes()),
                        ConcurrentHashMap::putAll
                );
    }

    @Scheduled(fixedDelay = 5_000)
    public void flush() {
        if (totalVotes.isEmpty()) return;
        totalVotes.forEach(this::addVoteToParticipant);
        totalVotes.clear();
    }

    @Transactional
    public void addVoteToParticipant(String participantId, Integer votes) {
        var vote = voteRepository.findById(participantId).orElseGet(
                () -> new Vote(participantId, 0)
        );

        vote.setTotalVotes(vote.getTotalVotes() + votes);
        voteRepository.save(vote);
    }

}
