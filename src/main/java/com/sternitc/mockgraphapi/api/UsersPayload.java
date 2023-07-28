package com.sternitc.mockgraphapi.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsersPayload {

    private String nextLink;
    private List<UserPayload> value;

}
