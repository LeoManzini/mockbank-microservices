package br.com.leomanzini.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(description = "API path invoked by client")
    private String apiPath;
    @Schema(description = "HTTP status code in response")
    private HttpStatus errorCode;
    @Schema(description = "Given error message")
    private String errorMessage;
    @Schema(description = "When error occurred")
    private LocalDateTime errorTime;
}
