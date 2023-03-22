package service;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.start.ProcessingService;
import interfaces.CustomerInterface;
import pojo.Customer;
import scanner.connection.ScannerConnection;

public class CustomerService implements CustomerInterface {
	Customer customer = new Customer();
	CustomerProductsService customerProductsService = new CustomerProductsService();

	public void customerChoice() throws SQLException {
		try {
			Scanner scanner = ScannerConnection.getScannerConnection();
			System.out.println("Please choose you choice ");
			System.out.println("1--> Buy Products");
			System.out.println("2--> Home Page");
			System.out.println("3--> EXIT");
			switch (scanner.nextInt()) {
			case 1:
				customerProductsService.getProductByCustomer();
				break;
			case 2:
				ProcessingService.processing();
				break;
			case 3:
				System.out.println("Thank you");
				break;
			default:
				System.out.println("Please choose the correct option");
				customerChoice();
				break;
			}
		} catch (InputMismatchException e) {
			customerChoice();
		}
	}
}
