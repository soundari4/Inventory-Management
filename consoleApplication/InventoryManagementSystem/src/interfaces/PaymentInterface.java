package interfaces;

import java.sql.SQLException;
import exception.PaymentException;
import pojo.Customer;

public interface PaymentInterface {
	int calculateProductsAmount(Customer customer) throws SQLException;

	boolean payProducts(int amount, Customer customer) throws PaymentException, SQLException;

	void bill(Customer customer);
}
