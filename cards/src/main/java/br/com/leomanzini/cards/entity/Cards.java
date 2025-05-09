package br.com.leomanzini.cards.entity;

import br.com.leomanzini.cards.entity.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cards extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    private String mobileNumber;
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
}
