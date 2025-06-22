package com.Parfetch.ParFetch.service;

import com.Parfetch.ParFetch.model.Sender;
import java.util.Optional;

public interface SenderService {
    Sender registerSender(Sender sender);
    Sender findByPhone(String phone);
    Optional<Sender> findById(Long id);
}
