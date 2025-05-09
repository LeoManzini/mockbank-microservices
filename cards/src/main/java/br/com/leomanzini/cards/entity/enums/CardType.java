package br.com.leomanzini.cards.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Type of card")
public enum CardType {
    @Schema(description = "Credit card")
    CREDIT("Credit"),
    @Schema(description = "Debit card")
    DEBIT("Debit"),
    @Schema(description = "Prepaid card")
    PREPAID("Prepaid");

    private final String type;

    CardType(String type) {
        this.type = type;
    }
}
