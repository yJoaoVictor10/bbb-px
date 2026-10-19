package br.com.fiap.votingaggregator.repository;

import br.com.fiap.votingaggregator.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, String> {
}
