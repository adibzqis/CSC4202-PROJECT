package com.Parfetch.ParFetch.repository;

import com.Parfetch.ParFetch.model.Parcel;
import com.Parfetch.ParFetch.model.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, Integer> {
    List<Parcel> findBySender(Sender sender); // Method to find parcels by sender
}
