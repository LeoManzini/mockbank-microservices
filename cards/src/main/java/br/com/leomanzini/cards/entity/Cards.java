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
    @Column(unique = true, nullable = false)
    private String mobileNumber;
    @Column(unique = true, updatable = false)
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
}
