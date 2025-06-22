package com.Parfetch.ParFetch.service;

import com.Parfetch.ParFetch.model.Parcel;
import com.Parfetch.ParFetch.model.Sender;

import com.Parfetch.ParFetch.model.Parcel;
import com.Parfetch.ParFetch.model.Sender;

import java.util.List;

public interface ParcelService {
    List<Parcel> getAllParcels();
    List<Parcel> getParcelsBySender(Sender sender);
    void save(Parcel parcel); // ✅ New method
    void deleteParcel(Parcel parcel);
}
