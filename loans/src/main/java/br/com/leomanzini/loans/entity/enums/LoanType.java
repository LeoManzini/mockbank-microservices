package br.com.leomanzini.loans.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Type of loan")
public enum LoanType {
    @Schema(description = "Personal loan")
    PERSONAL("Personal"),
    @Schema(description = "Home loan")
    HOME("Home"),
    @Schema(description = "Car loan")
    AUTO("Auto"),
    @Schema(description = "Education loan")
    EDUCATION("Education"),
    @Schema(description = "Business loan")
    BUSINESS("Business");

    private final String type;

    LoanType(String type) {
        this.type = type;
    }
}
