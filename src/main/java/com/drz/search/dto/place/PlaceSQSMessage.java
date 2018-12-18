package com.drz.search.dto.place;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceSQSMessage {

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("Subject")
    private String subject;

    @JsonProperty("Message")
    private String message;
}
