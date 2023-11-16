package tinkoffinvestementbot.dto.bot;


import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateTokenResponse(@JsonProperty("isSuccess") boolean isSuccess) {
}
