package br.com.leomanzini.accounts.mapper;

import br.com.leomanzini.accounts.dto.CustomerDto;
import br.com.leomanzini.accounts.entity.Customer;

public final class CustomerMapper {

    // Receives a CustomerDto and a Customer and maps the fields from the DTO to the entity
    // Not even the entity Customer is created here, it is passed as a parameter, for update some fields if needed
    public static CustomerDto mapToCustomerDto(Customer customer, CustomerDto customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }

    public static Customer mapToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }
}
