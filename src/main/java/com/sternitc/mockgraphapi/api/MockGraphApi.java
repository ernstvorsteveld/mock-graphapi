package com.sternitc.mockgraphapi.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class MockGraphApi {

    @Value("${mockgraphapi.rate_limit_at}")
    private String rateLimitAt;

    // beta/v1.0/users?$select=<attribute names>&$top=&$skiptoken
    @GetMapping("/beta/v1.0/users")
    public Mono<ResponseEntity<UsersPayload>> getAll(
            @RequestParam("$select") String attrs,
            @RequestParam("$stop") String stop,
            @RequestParam("$skipToken") String skipToken) {
        if (rateLimitAt.equalsIgnoreCase(stop)) {
            throw new TooManyRequests(stop, skipToken);
        }
        return Mono.just(ResponseEntity.status(HttpStatus.OK).headers(getHeaders(stop, skipToken)).body(getUsersPayload()));
    }

    private UsersPayload getUsersPayload() {
        UsersPayload usersPayload = new UsersPayload();

        usersPayload.setNextLink("");
        usersPayload.setValue(getUserPaylod());

        return usersPayload;
    }

    private List<UserPayload> getUserPaylod() {
        List<UserPayload> users = new ArrayList<>();
        return users;
    }

    private HttpHeaders getHeaders(String stop, String skipToken) {
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.set("X-STOP", stop);
        responseHeaders.set("X-SKIP_TOKEN", skipToken);
        responseHeaders.set("SKIP-TOKEN", UUID.randomUUID().toString());

        return responseHeaders;
    }

    @ExceptionHandler(TooManyRequests.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Mono<ResponseEntity<String>> tooManyRequests(TooManyRequests exception
    ) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(getExceptionHeaders(exception))
                .body(exception.getMessage()));
    }

    private HttpHeaders getExceptionHeaders(TooManyRequests exception) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("SKIP-TOKEN", exception.getSkipToken());
        responseHeaders.set("X-STOP", exception.getStop());
        responseHeaders.set(HttpHeaders.RETRY_AFTER, "4");
        return responseHeaders;
    }

    @Getter
    public static class TooManyRequests extends RuntimeException {
        private final String stop;
        private final String skipToken;

        public TooManyRequests(String stop, String skipToken) {
            this.stop = stop;
            this.skipToken = skipToken;
        }
    }
}
