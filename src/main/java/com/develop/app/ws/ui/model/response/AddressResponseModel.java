package com.develop.app.ws.ui.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class AddressResponseModel extends RepresentationModel<AddressResponseModel> {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}