package br.com.leomanzini.accounts.controller;

import br.com.leomanzini.accounts.dto.AccountsDto;
import br.com.leomanzini.accounts.dto.CustomerDto;
import br.com.leomanzini.accounts.dto.ErrorResponseDto;
import br.com.leomanzini.accounts.dto.ResponseDto;
import br.com.leomanzini.accounts.service.AccountsServiceInterface;
import br.com.leomanzini.accounts.utils.AccountsConstants;
import br.com.leomanzini.accounts.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "ManziniBank Accounts",
        description = "API for accounts resources"
)
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/accounts")
public class AccountsController {

    private final AccountsServiceInterface accountsService;

    @Operation(
            summary = "Create account",
            description = "Creates a mock account and mocked customer for account"
    )
    @ApiResponse(
            responseCode = "201",
            description = "CREATED"
    )
    @PostMapping
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        Long accountNumber = accountsService.createAccount(customerDto);
        return ResponseBuilder.created(AccountsConstants.MESSAGE_201, accountNumber);
    }

    @Operation(
            summary = "Fetch account details",
            description = "Fetch a specific mocked account and customer details"
    )
    @GetMapping
    public ResponseEntity<AccountsDto> fetchAccountDetails(@Parameter(
            name = "mobileNumber",
            description = "Mocked mobile number of the mock customer",
            required = true,
            example = "1234567890"
    ) @RequestParam @Pattern(regexp = "(^$|[0-9]{10,15})", message = "Mobile number should be valid") String mobileNumber) {
        return ResponseEntity.ok(accountsService.fetchAccountDetails(mobileNumber));
    }

    @Operation(
            summary = "Update account details",
            description = "Update mocked account and customer details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PutMapping("/{accountNumber}")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Parameter(
            name = "accountNumber",
            description = "Account number",
            required = true,
            example = "1234567890"
    ) @PathVariable Long accountNumber, @Valid @RequestBody AccountsDto accountsDto) {
        accountsService.updateAccount(accountNumber, accountsDto);
        return ResponseBuilder.ok(AccountsConstants.MESSAGE_200);
    }

    @Operation(
            summary = "Delete account",
            description = "Deletes a mocked account and its customer"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "NO CONTENT"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @DeleteMapping("/{mobileNumber}")
    public ResponseEntity<Void> deleteAccountDetails(@Parameter(
            name = "mobileNumber",
            description = "Mobile number of the customer",
            required = true,
            example = "1234567890"
    ) @PathVariable @Pattern(regexp = "(^$|[0-9]{10,15})", message = "Mobile number should be valid") String mobileNumber) {
        accountsService.deleteAccount(mobileNumber);
        return ResponseBuilder.noContent();
    }
}
