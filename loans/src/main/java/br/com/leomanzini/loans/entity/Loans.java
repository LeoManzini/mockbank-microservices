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
    private String mobileNumber;
    private String loanNumber;
    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;
}
