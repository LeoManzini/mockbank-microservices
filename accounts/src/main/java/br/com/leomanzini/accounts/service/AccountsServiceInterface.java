package br.com.leomanzini.accounts.service;

import br.com.leomanzini.accounts.dto.AccountsDto;
import br.com.leomanzini.accounts.dto.CustomerDto;

public interface AccountsServiceInterface {

    /**
     * @param customerDto - CustomerDto Object
     * @return - Created account Number
     */
    Long createAccount(CustomerDto customerDto);

    /**
     * @param mobileNumber - Mobile Number
     * @return - AccountsDto Object
     */
    AccountsDto fetchAccountDetails(String mobileNumber);

    /**
     * @param accountNumber - Account Number
     * @param accountsDto   - AccountsDto Object
     */
    void updateAccount(Long accountNumber, AccountsDto accountsDto);

    /**
     * @param mobileNumber - Mobile Number
     */
    void deleteAccount(String mobileNumber);
}
