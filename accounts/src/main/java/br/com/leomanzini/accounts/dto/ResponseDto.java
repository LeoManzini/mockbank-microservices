package br.com.leomanzini.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(
        name = "Response",
        description = "Schema to ghold successful response information"
)
@AllArgsConstructor
public class ResponseDto {

    @Schema(
            description = "HTTP status code in response",
            example = "200"
    )
    private String statusCode;

    @Schema(
            description = "Status message in response",
            example = "Request processed successfully"
    )
    private String statusMessage;
}
