package application.start;

import java.sql.SQLException;
import java.util.*;

import scanner.connection.ScannerConnection;
import service.AdminService;
import service.CustomerService;

public class ProcessingService {


	public static void main(String[] args) {
		processing();

	}

	public static void processing() {
		CustomerService customerService = new CustomerService();
		AdminService adminService = new AdminService();
		int a=1;
		try {

		Scanner scanner =new Scanner(System.in);
		System.out.println("Select the person, who are you?");
		System.out.println("1--> OWNER");
		System.out.println("2--> CUSTOMER");
		System.out.println("3--> Exit");
		a=scanner.nextInt();

		switch (a) {
		case 1:
			try {
				adminService.administrator();
			} catch (SQLException e) {
				System.out.println(e);
			}
			break;
		case 2:
			try {
				customerService.customerChoice();
			} catch (SQLException e) {
				System.out.println(e);
			}
			break;
		case 3:
			System.out.print(".....THANK YOU.....");
			break;
		default:
			System.out.println("Please enter the correct option ");
			ProcessingService.processing();
			break;
		}
		scanner.close();
		}
		catch(InputMismatchException e)
		{
			System.out.println("Please enter the correct option");
			ProcessingService.processing();
		}
	}
}
