package br.com.leomanzini.loans.controller;

import br.com.leomanzini.loans.dto.ErrorResponseDto;
import br.com.leomanzini.loans.dto.LoansDto;
import br.com.leomanzini.loans.dto.ResponseDto;
import br.com.leomanzini.loans.service.LoansServiceInterface;
import br.com.leomanzini.loans.utils.LoansConstants;
import br.com.leomanzini.loans.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "ManziniBank Loans",
        description = "API for loans resources"
)
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/loans")
public class LoansController {

    private final LoansServiceInterface loansService;

    @Operation(
            summary = "Create loan",
            description = "Creates a mocked loan based on a mocked customer mobile number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "CREATED"
    )
    @PostMapping
    public ResponseEntity<ResponseDto> createLoan(@Parameter(
            name = "mobileNumber",
            description = "Mocked mobile number to create a mocked loan",
            required = true,
            example = "1234567890") @Valid @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        String loanNumber = loansService.createLoan(mobileNumber);
        return ResponseBuilder.created(LoansConstants.MESSAGE_201, loanNumber);
    }

    @Operation(
            summary = "Fetch loan details",
            description = "Fetch a specific mocked loan detail"
    )
    @GetMapping
    public ResponseEntity<LoansDto> fetchLoanDetails(@Parameter(
            name = "mobileNumber",
            description = "Mobile number to search for mock loan",
            required = true,
            example = "1234567890") @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        return ResponseEntity.ok(loansService.fetchLoan(mobileNumber));
    }

    @Operation(
            summary = "Update loan details",
            description = "Update mocked loan details"
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
    @PutMapping("/{loanNumber}")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Parameter(
            name = "loanNumber",
            description = "Loan number to search for mock loan",
            required = true,
            example = "548732457654"
    ) @PathVariable @Pattern(regexp="(^$|[0-9]{12})",message = "LoanNumber must be 12 digits") String loanNumber, @Valid @RequestBody LoansDto loansDto) {
        loansService.updateLoan(loanNumber, loansDto);
        return ResponseBuilder.ok(LoansConstants.MESSAGE_200);
    }

    @Operation(
            summary = "Delete loan",
            description = "Deletes a mocked loan"
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
    @DeleteMapping
    public ResponseEntity<Void> deleteLoanDetails(@Parameter(
            name = "mobileNumber",
            description = "Mobile number to delete a mock card",
            required = true,
            example = "1234567890"
    ) @RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        loansService.deleteLoan(mobileNumber);
        return ResponseBuilder.noContent();
    }
}
