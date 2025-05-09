package br.com.leomanzini.loans.service;

import br.com.leomanzini.loans.dto.LoansDto;

public interface LoansServiceInterface {

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createLoan(String mobileNumber);

    /**
     * @param mobileNumber - Input mobile Number
     *  @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);

    /**
     * @param loansDto - LoansDto Object
     */
    void updateLoan(LoansDto loansDto);

    /**
     * @param mobileNumber - Input Mobile Number
     */
    void deleteLoan(String mobileNumber);
}
