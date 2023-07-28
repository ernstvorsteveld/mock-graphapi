package com.sternitc.mockgraphapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentityPayload {

    private String signInType;
    private String issuerAssignedId;

}
