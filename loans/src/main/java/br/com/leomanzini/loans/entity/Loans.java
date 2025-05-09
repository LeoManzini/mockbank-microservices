package br.com.leomanzini.loans.entity;

import br.com.leomanzini.loans.entity.enums.LoanType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Loans extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;
    @Column(unique = true, nullable = false)
    private String mobileNumber;
    @Column(unique = true, nullable = false, updatable = false)
    private String loanNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;
}
