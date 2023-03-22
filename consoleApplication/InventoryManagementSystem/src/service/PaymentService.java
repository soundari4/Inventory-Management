package service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dbconnection.DBConnection;
import exception.PaymentException;
import interfaces.PaymentInterface;
import pojo.Customer;

public class PaymentService implements PaymentInterface {
	Customer customer = new Customer();

	public int calculateProductsAmount(Customer customer) throws SQLException {
		int amount = 0;
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparedStmt = con.prepareStatement(
				"select co.product_quantity,p.price from customer_order co left join products p on co.product_id=p.id where customer_id=?");
		preparedStmt.setInt(1, customer.getId());
		ResultSet result = preparedStmt.executeQuery();
		for (int i = 0; result.next(); i++)
			amount = amount + result.getInt(1) * result.getInt(2);
		return amount;

	}

	public void bill(Customer customer) {
		try {
			Connection con = DBConnection.getDBConnection();
			PreparedStatement preparedStmt = con.prepareStatement(
					"select c.name ,p.name,cp.product_quantity,p.price,pay.amount,pay.status"
					+ " from customer_order cp left join customer c on cp.customer_id=c.id left join products p on cp.product_id=p.id left join payment pay on c.id=pay.customer_id  where cp.customer_id=? order by cp.product_quantity desc;");
			preparedStmt.setInt(1, customer.getId());
			ResultSet result = preparedStmt.executeQuery();
			int i = 0;
			while (result.next()) {
				if (i == 0) {
					System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<< HELLO >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					System.out.println("******************************************************************");
					System.out.println("   CUSTOMER NAME :                 " + result.getString(1) + "      \t\t ");
					System.out.println("******************************************************************");
					System.out.println(" PRODUCT NAME " + "\t" + " Quantity      Amount     Product Amount");
					System.out.println("******************************************************************");
				}
				System.out.println(result.getString(2) + "\t\t*  " + result.getInt(3) + "\t*\t  " + result.getInt(4)
						+ " \t*\t" + result.getInt(3) * result.getInt(4));
				i++;
			}
			result = preparedStmt.executeQuery();
			result.next();
			System.out.println("******************************************************************");
			System.out.println("                   TOTAL AMOUNT             :      	   " + result.getInt(5));
			System.out.println("******************************************************************");
			System.out.println("    PAYMENT STATUS     :    " + result.getString(6));
			System.out.println("******************************************************************");

		} catch (SQLException e) {
			System.out.println(e);
		}

	}

	public boolean payProducts(int amount, Customer customer) throws PaymentException, SQLException {
		Connection con = DBConnection.getDBConnection();
		int userAmount, i;
		Scanner scanner = new Scanner(System.in);
		System.out.println("The amount you need to pay " + amount);
		System.out.println("Please pay the amount");
		userAmount = scanner.nextInt();
		for (i = 0; i < 3; i++) {
			if (userAmount == amount) {
				System.out.println("Successfully paid....");
				System.out.println("<<<<<THANK YOU>>>>>");
				PreparedStatement preparedStmt = con
						.prepareStatement("insert into payment  (customer_id,status,amount) values(?,'SUCCESS',?); ");
				preparedStmt.setInt(1, customer.getId());
				preparedStmt.setInt(2, amount);
				preparedStmt.executeUpdate();
				return true;
			} else {
				if (i < 2) {
					System.out.println("Please pay the correct amount " + amount);
					userAmount = scanner.nextInt();
				}
			}
		}
		if (i == 3) {
			PreparedStatement preparedStmt = con
					.prepareStatement("insert into payment (customer_id,status,amount) values(?,'FAILED',?); ");
			preparedStmt.setInt(1, customer.getId());
			preparedStmt.setInt(2, amount);
			preparedStmt.executeUpdate();
			throw new PaymentException(
					"Payment Failed. So you can't get the products which you bought. You need to choose the products again");
		}

		return false;
	}

}
