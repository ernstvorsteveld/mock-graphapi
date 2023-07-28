package com.sternitc.mockgraphapi.api;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class UserPayload {

    private String id;
    private String displayName;
    private String firstName;
    private String lastName;
    private ZonedDateTime createdDate;
    private List<IdentityPayload> identities;
    private SignInPayload signInActivity;
    private String companyName;

}
