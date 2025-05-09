package br.com.leomanzini.accounts.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Type of bank account")
public enum AccountType {
    @Schema(description = "Savings account")
    SAVINGS("Savings"),
    @Schema(description = "Checking account")
    CHECKING("Checking");

    private final String type;

    AccountType(String type) {
        this.type = type;
    }
}
