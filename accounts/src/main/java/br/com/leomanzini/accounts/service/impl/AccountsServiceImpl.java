package br.com.leomanzini.accounts.service.impl;

import br.com.leomanzini.accounts.dto.AccountsDto;
import br.com.leomanzini.accounts.dto.CustomerDto;
import br.com.leomanzini.accounts.entity.Accounts;
import br.com.leomanzini.accounts.entity.Customer;
import br.com.leomanzini.accounts.entity.enums.AccountType;
import br.com.leomanzini.accounts.exception.CustomerAlreadyExistsException;
import br.com.leomanzini.accounts.exception.ResourceNotFoundException;
import br.com.leomanzini.accounts.mapper.AccountsMapper;
import br.com.leomanzini.accounts.mapper.CustomerMapper;
import br.com.leomanzini.accounts.repository.AccountsRepository;
import br.com.leomanzini.accounts.repository.CustomerRepository;
import br.com.leomanzini.accounts.service.AccountsServiceInterface;
import br.com.leomanzini.accounts.utils.AccountsConstants;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsServiceInterface {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public Long createAccount(CustomerDto customerDto) {
        if (customerRepository.findByMobileNumber(customerDto.getMobileNumber()).isPresent()) {
            throw new CustomerAlreadyExistsException("Mobile number already exists in database: " + customerDto.getMobileNumber());
        }

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        customer = customerRepository.save(customer);
        Accounts account = accountsRepository.save(instantiateAccount(customer));
        return account.getAccountNumber();
    }

    @Override
    public AccountsDto fetchAccountDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setCustomer(CustomerMapper.mapToCustomerDto(customer, new CustomerDto()));
        return AccountsMapper.mapToAccountsDto(account, accountsDto);
    }

    @Override
    @Transactional
    public void updateAccount(Long accountNumber, AccountsDto accountsDto) {
        if (accountsDto == null) {
            throw new IllegalArgumentException("AccountsDto cannot be null");
        }

        if (!accountNumber.equals(accountsDto.getAccountNumber())) {
            throw new IllegalArgumentException("Account number in the request body does not match the path variable");
        }

        Accounts accounts = accountsRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString()));
        AccountsMapper.mapToAccounts(accountsDto, accounts);
        accounts = accountsRepository.save(accounts);

        Long customerId = accounts.getCustomerId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));
        CustomerMapper.mapToCustomer(accountsDto.getCustomer(), customer);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
    }

    private static Accounts instantiateAccount(Customer customer) {
        Accounts account = new Accounts();
        account.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 100000000L + new Random().nextInt(900000000);

        account.setAccountNumber(randomAccNumber);
        account.setAccountType(AccountType.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);
        return account;
    }
}
