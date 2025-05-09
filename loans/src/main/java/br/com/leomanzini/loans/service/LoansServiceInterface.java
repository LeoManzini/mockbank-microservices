package br.com.leomanzini.loans.service;

import br.com.leomanzini.loans.dto.LoansDto;

public interface LoansServiceInterface {

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    String createLoan(String mobileNumber);

    /**
     * @param mobileNumber - Input mobile Number
     *  @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);

    /**
     * @param mobileNumber - Input Mobile Number
     * @param loansDto - LoansDto Object
     */
    void updateLoan(String mobileNumber, LoansDto loansDto);

    /**
     * @param mobileNumber - Input Mobile Number
     */
    void deleteLoan(String mobileNumber);
}
