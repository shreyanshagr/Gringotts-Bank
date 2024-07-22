package com.gringotts.sequence.generator.service.implementation;

import com.gringotts.sequence.generator.model.entity.Sequence;
import com.gringotts.sequence.generator.reporitory.SequenceRepository;
import com.gringotts.sequence.generator.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceServiceImpl implements SequenceService {

    private final SequenceRepository sequenceRepository;

    @Override
    public Sequence create() {

        log.info("creating a account number");
        return sequenceRepository.findById(1L)
                .map(sequence -> {
                    sequence.setAccountNumber(sequence.getAccountNumber() + 1);
                    return sequenceRepository.save(sequence);
                }).orElseGet(() -> sequenceRepository.save(Sequence.builder().accountNumber(1L).build()));
    }
}
