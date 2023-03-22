package scanner.connection;

import java.util.Scanner;

public class ScannerConnection {
	static Scanner scanner;

	static {
		scanner=new Scanner(System.in);
	}

	public static Scanner getScannerConnection() {
		return scanner;
	}
}
