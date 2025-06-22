package com.Parfetch.ParFetch.repository;

import com.Parfetch.ParFetch.model.Sender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SenderRepository extends JpaRepository<Sender, Long> {
    @Query("SELECT s FROM Sender s WHERE TRIM(s.phone) = TRIM(:phone)")
    Sender findByPhone(@Param("phone") String phone);
}
