package service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import dbconnection.DBConnection;
import interfaces.ProductsInterface;
import pojo.Admin;

public class ProductsService implements ProductsInterface {

	public void detailedAboutAllTheProducts() throws SQLException {
		Connection con = DBConnection.getDBConnection();
		Statement stmt = con.createStatement();
		ResultSet resultOfAllProducts = stmt.executeQuery("select * from products;");
		for (int i = 0; resultOfAllProducts.next(); i++) {
			System.out.println("               PRODUCT " + i);
			System.out.println("PRODUCT ID               :" + resultOfAllProducts.getInt(1));
			System.out.println("PRODUCT NAME             :" + resultOfAllProducts.getString(2));
			System.out.println("PRODUCT PRICE            :" + resultOfAllProducts.getInt(3));
			System.out.println("PRODUCT QUANTITY         :" + resultOfAllProducts.getInt(4));
			System.out.println("ADMIN ID                 :" + resultOfAllProducts.getInt(5));
			System.out.println("PRODUCT ADD ON           :" + resultOfAllProducts.getDate(6));
		}
	}

	private void displayProductsName(int choice) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparedStmt;
		int i = 1;
		if (choice == 1)
			preparedStmt = con.prepareStatement("select name from products");
		else
			preparedStmt = con.prepareStatement("select name from products where quantity>0");
		ResultSet result = preparedStmt.executeQuery();

		while (result.next()) {
			System.out.println(i + ". " + result.getString(1));
			i++;
		}
	}

	public void addProductQuantity() throws SQLException {
		Connection con = DBConnection.getDBConnection();
		System.out.println("Choose the corresponding product name you want to add ");
		displayProductsName(1);
		Scanner scanner = new Scanner(System.in);
		String name = scanner.next();
		while (!checkProducts(name)) {
			System.out.println("Please enter the correct name ");
			name = scanner.next();
		}
		System.out.println("Enter how much amount of quantity you want to add ");
		int quantity = scanner.nextInt();
		PreparedStatement preparedStmtForQuantity = con.prepareStatement("select quantity from products where name=?;");
		preparedStmtForQuantity.setString(1, name);
		ResultSet resultOfQuantity = preparedStmtForQuantity.executeQuery();
		resultOfQuantity.next();
		PreparedStatement preparestatement = con.prepareStatement("update products set quantity=? where name=?");
		preparestatement.setInt(1, quantity + resultOfQuantity.getInt(1));
		preparestatement.setString(2, name);
		preparestatement.executeUpdate();
		System.out.println("The products is added...");
		scanner.close();
		preparestatement.close();
	}

	public void addNewProducts(Admin admin) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the number of products you want to add");
		int totalProducts = scanner.nextInt();
		for (int i = 0; i < totalProducts; i++) {
			PreparedStatement preparedStmt = con.prepareStatement(
					"insert into products(name,price,quantity,add_on,admin_id) values(?,?,?,NOW(),?);");
			System.out.println("Enter the product name ");
			String name = scanner.next();
			if (checkProducts(name)) {
				System.out.println("The product is already there. Do you want to add excess stock to that product");
				System.out.println("Type '1' for YES");
				System.out.println("Type '2' for NO");
				if (scanner.nextInt() == 1)
					addProductQuantity();
				else
					break;
			}
			preparedStmt.setString(1, name);
			System.out.println("Enter the product price ");
			preparedStmt.setInt(2, scanner.nextInt());
			System.out.println("Enter the product Quantity ");
			preparedStmt.setInt(3, scanner.nextInt());
			preparedStmt.setInt(4, admin.getId());
			preparedStmt.executeUpdate();
			System.out.println("The product " + name + " is added");
			preparedStmt.close();
			preparedStmt.close();
		}
		con.close();
		scanner.close();
	}

	public void removeProducts() throws SQLException {
		Connection con = DBConnection.getDBConnection();
		Scanner scanner = new Scanner(System.in);
		displayProductsName(1);
		System.out.println("Enter the corresponding product name you want to remove");
		String name = scanner.next();
		while (!checkProducts(name)) {
			System.out.println("Please enter the correct name ");
			name = scanner.next();
		}
		PreparedStatement preparedStmt = con.prepareStatement("select id from products where name=?;");
		preparedStmt.setString(1, name);
		ResultSet result = preparedStmt.executeQuery();
		result.next();
		PreparedStatement preparedStatementChild = con
				.prepareStatement("delete from customer_order where product_id=?");
		preparedStatementChild.setInt(1, result.getInt(1));
		preparedStatementChild.executeUpdate();
		PreparedStatement preparedstatement = con.prepareStatement("delete from products where id=? ");
		preparedstatement.setInt(1, result.getInt(1));
		preparedstatement.executeUpdate();
		System.out.println("The product is removed from our shop");
		// adminService.adminActionChoice();
		scanner.close();
		preparedstatement.close();
	}

	public void resetCost() throws SQLException {
		Connection con = DBConnection.getDBConnection();
		Scanner scanner = new Scanner(System.in);
		displayProductsName(1);
		System.out.println("Enter the product name you want to reset the cost of the product ");
		String name = scanner.next();
		PreparedStatement preparedstatement = con.prepareStatement("select price from products where name=?");
		preparedstatement.setString(1, name);
		ResultSet costResult = preparedstatement.executeQuery();
		costResult.next();
		System.out.println("The old cost of the product id " + costResult.getInt(1));
		PreparedStatement preparedStmtUpdate = con.prepareStatement("update products set price=? where name=?");
		System.out.println("Enter the new cost of the product ");
		preparedStmtUpdate.setInt(1, scanner.nextInt());
		preparedStmtUpdate.setString(2, name);
		preparedStmtUpdate.executeUpdate();
		System.out.println("New cost is setted");
		// adminService.adminActionChoice();

	}

	private boolean checkProducts(String productName) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparestatementId = con.prepareStatement("select id from products where name=?");
		preparestatementId.setString(1, productName);
		ResultSet result = preparestatementId.executeQuery();
		if (result.next()) {
			return true;
		}
		return false;
	}

	public int getProductQuatity(String productName) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparedstatement = con.prepareStatement("select quantity from products where name=?");
		preparedstatement.setString(1, productName);
		ResultSet result = preparedstatement.executeQuery();
		result.next();
		if (result.getInt(1) > 0)
			return result.getInt(1);
		else
			return 0;
	}

	public int getProductId(String name) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparedStmt = con.prepareStatement("select id from products where name=?;");
		preparedStmt.setString(1, name);
		ResultSet result = preparedStmt.executeQuery();
		result.next();
		return result.getInt(1);
	}

	public boolean checkProductName(String productName, Connection con) throws SQLException {
		PreparedStatement preparedstatement = con.prepareStatement("select name from products where name=?");
		preparedstatement.setString(1, productName);
		ResultSet result = preparedstatement.executeQuery();
		return result.next();
	}

	public String correctProductName(String productName, Connection con) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		while (!checkProductName(productName, con)) {
			System.out.println("Please enter the correct product name ");
			productName = scanner.next();
		}
		return productName;

	}
}
