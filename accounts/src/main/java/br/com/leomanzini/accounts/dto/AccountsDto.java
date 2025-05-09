package br.com.leomanzini.accounts.dto;

import br.com.leomanzini.accounts.entity.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Schema to hold account and customer information"
)
public class AccountsDto {

    @Schema(
            name = "accountNumber",
            description = "Account number",
            example = "1234567890"
    )
    @NotNull(message = "Account number is mandatory")
    @Digits(integer = 10, fraction = 0, message = "Account number should be valid")
    private Long accountNumber;

    @Schema(
            name = "accountType",
            description = "Account type",
            example = "SAVINGS",
            allowableValues = {"SAVINGS", "CURRENT", "SALARY"}
    )
    @NotNull(message = "Account type is mandatory")
    private AccountType accountType;

    @Schema(
            name = "branchAddress",
            description = "Branch address",
            example = "123 Main St, City, State, ZIP"
    )
    @NotBlank(message = "Branch code is mandatory")
    private String branchAddress;

    @Schema(
            name = "customer",
            description = "Customer information"
    )
    @NotNull
    private CustomerDto customer;
}
