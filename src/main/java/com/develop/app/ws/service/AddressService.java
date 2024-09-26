package com.develop.app.ws.service;

import com.develop.app.ws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddresses(String userId);

    AddressDto getAddress(String addressId);
}