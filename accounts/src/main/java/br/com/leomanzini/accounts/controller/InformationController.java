package br.com.leomanzini.accounts.controller;

import br.com.leomanzini.accounts.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "ManziniBank Information",
        description = "Provides information about the application"
)
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/information")
public class InformationController {

    @Value("${build.version}")
    private String buildVersion;
    private final Environment environment;

    @Operation(
            summary = "Get build info",
            description = "Returns the build version of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/build")
    @ResponseStatus(HttpStatus.OK)
    public String getBuildInfo() {
        return buildVersion;
    }

    @Operation(
            summary = "Get java info",
            description = "Returns the java version of the application"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL SERVER ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/java")
    @ResponseStatus(HttpStatus.OK)
    public String getJavaVersion() {
        return environment.getProperty("JAVA_HOME");
    }
}
