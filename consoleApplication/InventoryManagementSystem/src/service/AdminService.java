package service;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.start.ProcessingService;
import dbconnection.DBConnection;
import exception.UserAlreadyThereException;
import interfaces.AdminInterface;
import pojo.Admin;
import scanner.connection.ScannerConnection;

public class AdminService implements AdminInterface {


	ProductsService productsService = new ProductsService();
	Admin admin = new Admin();

	public void administrator() throws SQLException {
		try {
		System.out.println("1--> LOGIN");
		System.out.println("2--> CREATE NEW ADMIN");
		System.out.println("3--> HOME PAGE");
		System.out.println("4--> EXIT");
		Scanner scanner = new Scanner(System.in);
		switch (scanner.nextInt()) {
		case 1:
			checkDetails();
			break;
		case 2:
			addNewAdmin();
			break;
		case 3:
			ProcessingService.processing();
			break;
		case 4:
			System.out.println("THANK YOU..!!");
			break;
		default:
			System.out.println("Please choose the correct option");
			administrator();
			break;
		}
		}
		catch(InputMismatchException e)
		{
			administrator();
		}
	}

	private void addNewAdmin() throws SQLException{
		try {
			Scanner scanner = new Scanner(System.in);
			Connection con = DBConnection.getDBConnection();
			System.out.println("Create the username(NAME MUST NOT CONTAIN ANY SPACE AND MUST BE SMALL CASE)");
			admin.setUserName(scanner.next());
			if (!checkUsernameExists(admin.getUserName(),con)) {
				PreparedStatement preparedStmt = con.prepareStatement(
						"INSERT INTO ADMINISTRATOR (user_name,password,age,gender,phone_number,name) values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				preparedStmt.setString(1, admin.getUserName());
				System.out.println("Enter your name ");
				admin.setName(scanner.next());
				preparedStmt.setString(6, admin.getName());
				System.out.println("Create the password");
				admin.setPassWord(scanner.next());
				preparedStmt.setString(2, admin.getPassWord());
				System.out.println("Enter the age ");
				admin.setAge(scanner.nextInt());
				while (admin.getAge() < 0) {
					System.out.println("Please enter the valid age it must contain 10 digits");
					admin.setAge(scanner.nextInt());
				}
				preparedStmt.setInt(3, admin.getAge());
				System.out.println("Enter the Gender (M/F)");
				admin.setGender(scanner.next());
				preparedStmt.setString(4, admin.getGender());
				System.out.println("Enter the PhoneNumber");
				admin.setPhoneNumber(scanner.next());
				while (admin.getPhoneNumber().length() != 10) {
					System.out.println("Please enter the valid phone number it must contain 10 digits");
					admin.setPhoneNumber(scanner.next());
				}
				preparedStmt.setString(5, admin.getPhoneNumber());
				preparedStmt.executeUpdate();
				ResultSet rs = preparedStmt.getGeneratedKeys();
				if (rs.next()) {
					admin.setId(rs.getInt(1));
					System.out.println("Inserted ID -" + admin.getId()); // display inserted record
				}
				preparedStmt.close();
				System.out.println("Try to login again with our newly created username ");
				checkDetails();
			}
		}
		catch(UserAlreadyThereException e)
		{
			addNewAdmin();
		}
	}

	private boolean checkUsernameExists(String userName,Connection con) throws SQLException {
		PreparedStatement prepareStmt = con.prepareStatement("select user_name from administrator where user_name=?");
		prepareStmt.setString(1, userName);
		ResultSet resultSet = prepareStmt.executeQuery();
		if (resultSet.next())
			throw new UserAlreadyThereException("The user already there ");
		else
			return false;
	}

	private void checkDetails() throws SQLException {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the user name");
			String userName = scanner.next();
			System.out.println("Enter the password");
			String passWord = scanner.next();
			if (checkUsernamePassword(userName, passWord)) {
				adminPerformance();
			} else {
				System.out.println("User and password wrong");
				System.out.println("Please try once again");
				checkDetails();
			}
			scanner.close();
	}

	private void adminPerformance() throws SQLException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("1--> Products Updation");
		System.out.println("2--> Admin Details Updation");
		System.out.println("3--> Home Page");
		switch (scanner.nextInt()) {
		case 1:
			adminProductsAction();
			break;
		case 2:
			adminDetailsUpdation();
			break;
		case 3:
			ProcessingService.processing();
			break;
		default:
			System.out.println("Please choose the correct option");
			adminPerformance();
		}
	}

	private void adminDetailsUpdation() throws SQLException {
		System.out.println("Please choose the choice");
		System.out.println("1--> Password changes");
		System.out.println("2--> Age change");
		System.out.println("3--> PhoneNumber");
		System.out.println("4--> Back");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		switch (choice) {
		case 1:
			System.out.println("Enter the new password ");
			
				adminDetailChange(scanner.next(), choice);
			
			break;
		case 2:
			System.out.println("Enter the current age ");
				adminDetailChange(scanner.nextInt());
			break;
		case 3:
			System.out.println("Enter the new phone number ");
				adminDetailChange(scanner.next(), choice);
			break;
		case 4:
				adminPerformance();
			break;
		default:
			System.out.println("Please choose the correct option");
			adminDetailsUpdation();
		}
	}

	private void adminDetailChange(String detail, int choice) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparedStmt;
		if (choice == 1) {
			preparedStmt = con.prepareStatement("update administrator set password=? where id=?");
			preparedStmt.setString(1, detail);
		} else {
			preparedStmt = con.prepareStatement("update administrator set phone_number=? where id=?");
			preparedStmt.setString(1, detail);
		}
		preparedStmt.setInt(2, admin.getId());
		preparedStmt.executeUpdate();
		preparedStmt.close();
	}

	private void adminDetailChange(int detail) throws SQLException {
		Connection con = DBConnection.getDBConnection();
		PreparedStatement preparedStmt = con.prepareStatement("update administrator set age=? where id=?");
		preparedStmt.setInt(1, detail);
		preparedStmt.setInt(2, admin.getId());
		preparedStmt.executeUpdate();
		preparedStmt.close();
	}

	private boolean checkUsernamePassword(String userName, String passWord) throws SQLException{
			Connection con = DBConnection.getDBConnection();
			PreparedStatement preparedStmt = con.prepareStatement(
					"SELECT user_name,password from administrator a where a.user_name = ? && a.password=? ");
			preparedStmt.setString(1, userName);
			preparedStmt.setString(2, passWord);
			ResultSet result = preparedStmt.executeQuery();
			if (result.next())
				return true;
			preparedStmt.close();
			result.close();
		return false;
	}

	public void adminActionChoice() throws SQLException {
		try
		{
		System.out.println("Any other changes you want to perform?");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type 1--> for YES");
		System.out.println("Type 2--> for NO");
		switch (scanner.nextInt()) {
		case 1:
			adminProductsAction();
			break;
		case 2:
			System.out.println("....THANK YOU....");
			break;
		default:
			System.out.println("Please choose the correct option");
			adminActionChoice();
		}
		}
		catch(InputMismatchException e)
		{
			adminActionChoice();
		}
	}

	public void adminProductsAction() throws SQLException {
		try
		{
		System.out.println("Enter the choice");
		System.out.println("Choose 1--> for Add the stocks to the product which is already available ");
		System.out.println("Choose 2--> For Add New Product");
		System.out.println("Choose 3--> For Remove the Product from the shop");
		System.out.println("Choose 4--> For Reset the cost of the Product");
		System.out.println("Choose 5--> For see all the products ");
		System.out.println("Choose 6--> For back");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		switch (choice) {
		case 1:
			productsService.addProductQuantity();
			break;
		case 2:
			productsService.addNewProducts(admin);
			break;
		case 3:
			productsService.removeProducts();
			break;
		case 4:
			productsService.resetCost();
			break;
		case 5:
			productsService.detailedAboutAllTheProducts();
		case 6:
				adminPerformance();
			break;
		default:
			System.out.println("Please choose the correct option");
			adminProductsAction();
		}
		adminActionChoice();
		}
		catch(InputMismatchException e)
		{
			adminProductsAction();
		}
	}
}
