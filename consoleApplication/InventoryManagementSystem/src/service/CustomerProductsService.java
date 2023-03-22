package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import application.start.ProcessingService;
import dbconnection.DBConnection;
import exception.PaymentException;
import interfaces.CustomerProductsInterface;
import pojo.Customer;

public class CustomerProductsService implements CustomerProductsInterface {
	PaymentService paymentService = new PaymentService();
	ProductsService productsService = new ProductsService();
	Customer customer = new Customer();

	public void customerProduct(List<String> productName, List<Integer> productsQuantity) {
		try {
			Connection con = DBConnection.getDBConnection();
			ProductsService productsService = new ProductsService();
			for (int i = 0; i < productName.size() && i < productsQuantity.size(); i++) {
				PreparedStatement preparedStmt = con.prepareStatement(
						"insert into customer_order(customer_id,product_id,product_quantity)values(?,?,?); ");
				preparedStmt.setInt(1, customer.getId());
				preparedStmt.setInt(2, productsService.getProductId(productName.get(i)));
				preparedStmt.setInt(3, productsQuantity.get(i));
				preparedStmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public void getProductByCustomer() throws SQLException {
		try {
			int i = 0, index, count = 0;
			int userQuantity = 0;
			String userproductName;
			List<String> name = new LinkedList<String>();
			List<Integer> quantity = new LinkedList<Integer>();

			Connection con = DBConnection.getDBConnection();
			Scanner scanner = new Scanner(System.in);
			PreparedStatement preparedStmt = con.prepareStatement("select name, price from products;");
			do {
				ResultSet result = preparedStmt.executeQuery();
				System.out.println("PRODUCT NAME                PRICE ");
				while (result.next()) {
					System.out.println(result.getString(1) + "\t              \t " + result.getInt(2));
				}
				System.out.println("Enter the product name ");
				userproductName = scanner.next();

//				if (count == 0) {
//					userproductName = productsService.correctProductName(userproductName, con);
//					name.add(userproductName);
//					count++;
//				}

				userproductName = productsService.correctProductName(userproductName, con);
				index = name.indexOf(userproductName);

				if(index==-1)
				{
					name.add(userproductName);
				
				System.out.println("How many items of " + name.get(i) + " you want ");
				}
				else
				{
					System.out.println("How many items of " + name.get(index) + " you want ");
					i--;
				}
				userQuantity = scanner.nextInt();

				if (index == -1)
					quantity.add(userQuantity);
				else {
					quantity.set(index, quantity.get(index) + userQuantity);
					userQuantity = quantity.get(index);
				}

				if (index == -1) {
					if (productsService.getProductQuatity(name.get(i)) < userQuantity) {
						System.out.println("The available " + name.get(i) + " is "
								+ productsService.getProductQuatity(name.get(i)));
						System.out.println("But, the " + name.get(i) + " you want is " + userQuantity);
						System.out.println("Just that is ENOUGH for you?");
						System.out.println("1--> OKAY");
						System.out.println("2--> No, I don't want that product ");
						if (scanner.nextInt() == 1)
							quantity.set(i, productsService.getProductQuatity(name.get(i)));
						else {
							name.remove(i);
							quantity.remove(i);
							i--;
						}
					}
				} else {
					if (productsService.getProductQuatity(name.get(index)) < userQuantity) {
						System.out.println("The available " + name.get(index) + " is "
								+ productsService.getProductQuatity(name.get(index)));
						System.out.println("But, the " + name.get(index) + " you want is " + userQuantity);
						System.out.println("Just that is ENOUGH for you?");
						System.out.println("1--> OKAY");
						System.out.println("2--> No, I don't want that product ");
						if (scanner.nextInt() == 1)
							quantity.set(index, productsService.getProductQuatity(name.get(index)));
						else {
							name.remove(index);
							quantity.remove(index);
							i--;
						}
					}
				}

				System.out.println(" Do you want any other products ?");
				System.out.println("1--> YES");
				System.out.println("2--> NO");
				i++;
			} while (scanner.nextInt() == 1);

			customer = createNewCustomer();
			customerProduct(name, quantity);
			if (paymentService.payProducts(paymentService.calculateProductsAmount(customer), customer)) {
				paymentService.bill(customer);
				for (int j = 0; j < name.size() && j < quantity.size(); j++) {
					PreparedStatement stmt1 = con.prepareStatement("update products set quantity=? where id=?");
					stmt1.setInt(1, productsService.getProductQuatity(name.get(j)) - quantity.get(j));
					stmt1.setInt(2, productsService.getProductId(name.get(j)));
					stmt1.executeUpdate();
				}
			}
			ProcessingService.processing();
		} catch (PaymentException e) {
			getProductByCustomer();
		}

	}

	private Customer createNewCustomer() {
		try {
			Customer newCustomer = new Customer();
			Scanner scanner = new Scanner(System.in);
			Connection con = DBConnection.getDBConnection();
			PreparedStatement preparedStmt = con.prepareStatement(
					"insert into customer (name,phone_number)values(?,?);", Statement.RETURN_GENERATED_KEYS);
			System.out.println("Enter the Customer Name without space ");
			newCustomer.setName(scanner.next());
			preparedStmt.setString(1, newCustomer.getName());
			System.out.println("Enter the phone number of the customer ");
			newCustomer.setPhoneNumber(scanner.next());
			while (newCustomer.getPhoneNumber().length() != 10) {
				System.out.println("Please enter the valid phone number ");
				newCustomer.setPhoneNumber(scanner.next());
			}
			preparedStmt.setString(2, newCustomer.getPhoneNumber());
			preparedStmt.executeUpdate();
			ResultSet rs = preparedStmt.getGeneratedKeys();

			if (rs.next()) {
				newCustomer.setId(rs.getInt(1));
			}
			return newCustomer;
		} catch (SQLException e) {
			System.out.println(e);
		}
		return customer;
	}
}
