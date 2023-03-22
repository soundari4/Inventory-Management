package interfaces;

import java.sql.Connection;
import java.sql.SQLException;

import pojo.Admin;

public interface ProductsInterface 
{
	void addProductQuantity()throws SQLException;
	void addNewProducts(Admin admin)throws SQLException;
	void removeProducts()throws SQLException;
	void resetCost()throws SQLException;
	void detailedAboutAllTheProducts() throws SQLException;
	int getProductId(String name) throws SQLException ;
	boolean checkProductName(String productName, Connection con) throws SQLException;
}
