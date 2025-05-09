package br.com.leomanzini.accounts.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of bank account")
public enum AccountType {
    @Schema(description = "Savings account")
    SAVINGS,
    @Schema(description = "Checking account")
    CHECKING;
}
