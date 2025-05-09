package br.com.leomanzini.loans.controller;

import br.com.leomanzini.loans.dto.LoansDto;
import br.com.leomanzini.loans.dto.ResponseDto;
import br.com.leomanzini.loans.service.LoansServiceInterface;
import br.com.leomanzini.loans.utils.LoansConstants;
import br.com.leomanzini.loans.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
}
