package service;

import models.Customer;

import java.util.Collection;
import java.util.Optional;

import static provider.data.customers;

public class CustomerService {
    public static void addCustomer(String email, String firstNAme, String lastName) {
        customers.add(new Customer(firstNAme, lastName, email));
    }

    public static Customer getCustomer(String customerEmail) {
        Optional<Customer> customer = customers.stream().filter(c -> c.getEmail().equals(customerEmail)).findFirst();

        return customer.orElse(null);
    }

    public static Collection<Customer> getAllCustomers(){
        return customers;
    }

}
