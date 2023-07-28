package com.sternitc.mockgraphapi.api;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SignInPayload {

    private Instant lastSignInDateTime;
}
