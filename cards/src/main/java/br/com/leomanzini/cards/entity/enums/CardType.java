package br.com.leomanzini.cards.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Type of card")
public enum CardType {
    @Schema(description = "Credit card")
    CREDIT,
    @Schema(description = "Debit card")
    DEBIT,
    @Schema(description = "Prepaid card")
    PREPAID;
}
