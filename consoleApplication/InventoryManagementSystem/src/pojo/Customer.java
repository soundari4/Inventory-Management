package pojo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import dbconnection.DBConnection;

public class Customer
{ 	
	private int id;
	private String name;
	private String phoneNumber;
	public static int customerId; //rename to id 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public static int getCustomerId() 
	{
		return customerId;
	}
	public static void setCustomerId(int customerId)
	{
		Customer.customerId = customerId;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", phoneNumber=" + phoneNumber + "]";
	}
	
	//remove once you added auto_increment 
	public static int getCustomerLastId() throws SQLException
	{
		Connection con=DBConnection.getDBConnection();
		Statement statement=con.createStatement();
		ResultSet resultSet=statement.executeQuery("select * from customer order by id desc limit 1; ");
		resultSet.next();
		customerId=resultSet.getInt(1)+1;
		return resultSet.getInt(1);
	}
//	public static int getCustomerId() {
//		return customerId;
//	}
//	public static void setCustomerId(int customerId) {
//		Customer.customerId = customerId;
//	}
}
