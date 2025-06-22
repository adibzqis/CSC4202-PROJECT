package com.Parfetch.ParFetch.service;

import com.Parfetch.ParFetch.model.Parcel;
import com.Parfetch.ParFetch.model.Sender;
import com.Parfetch.ParFetch.repository.ParcelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;

    public ParcelServiceImpl(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    @Override
    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    @Override
    public List<Parcel> getParcelsBySender(Sender sender) {
        return parcelRepository.findBySender(sender);
    }

    @Override
    public void save(Parcel parcel) {
        parcelRepository.save(parcel);
    }

    @Override
    public void deleteParcel(Parcel parcel) {
    parcelRepository.delete(parcel);
    }

}
