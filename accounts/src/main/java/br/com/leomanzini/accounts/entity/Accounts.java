package br.com.leomanzini.accounts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Accounts extends BaseEntity {

    @Id
    private Long accountNumber;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private String branchAddress;
}
