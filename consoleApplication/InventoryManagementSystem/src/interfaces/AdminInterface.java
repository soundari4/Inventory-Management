package interfaces;

import java.sql.SQLException;

import pojo.Admin;

public interface AdminInterface
{
	void administrator() throws SQLException;
	void adminProductsAction()throws SQLException;
	void adminActionChoice()throws SQLException;
}
