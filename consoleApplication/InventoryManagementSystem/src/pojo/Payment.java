package pojo;

import java.sql.SQLException;

public class Payment
{
	private int id;
	private int customer_id;
	private String status;
	private int amount;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public int getCustomer_id() 
	{
		return customer_id;
	}
	public void setCustomer_id(int customer_id) throws SQLException
	{
			this.customer_id = Customer.getCustomerLastId()+1;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public int getAmount()
	{
		return amount;
	}
	public void setAmount(int amount)
	{
		this.amount = amount;
	}
	
}
