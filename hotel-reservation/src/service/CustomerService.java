package service;

import models.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class CustomerService {
    private static CustomerService customerService = new CustomerService();

    private List<Customer> customers = new ArrayList<>();

    public static CustomerService getInstance(){
        return customerService;
    }

    public void addCustomer(String email, String firstNAme, String lastName) {
        customers.add(new Customer(firstNAme, lastName, email));
    }

    public Customer getCustomer(String customerEmail) {
        Optional<Customer> customer = customers.stream().filter(c -> c.getEmail().equals(customerEmail)).findFirst();

        return customer.orElse(null);
    }

    public Collection<Customer> getAllCustomers(){
        return customers;
    }

}
