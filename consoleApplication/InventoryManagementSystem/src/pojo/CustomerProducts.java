package pojo;

public class CustomerProducts 
{
	private int id;
	private int customerId;
	private int productId;
	public int getId() 
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getCustomerId()
	{
		return customerId;
	}
	public void setCustomerId(int customerId) 
	{
		this.customerId = customerId;
	}
	public int getProductId()
	{
		return productId;
	}
	public void setProductId(int productId) 
	{
		this.productId = productId;
	}
}
