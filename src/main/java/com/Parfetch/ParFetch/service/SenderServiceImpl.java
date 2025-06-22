package com.Parfetch.ParFetch.service;

import com.Parfetch.ParFetch.model.Sender;
import com.Parfetch.ParFetch.repository.SenderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SenderServiceImpl implements SenderService {
    private final SenderRepository senderRepository;

    public SenderServiceImpl(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }

    @Override
    public Sender registerSender(Sender sender) {
        return senderRepository.save(sender);
    }

    @Override
    public Sender findByPhone(String phone) {
        return senderRepository.findByPhone(phone);
    }

    @Override
    public Optional<Sender> findById(Long id) {
        return senderRepository.findById(id);
    }
}
