package interfaces;

import java.sql.SQLException;
import java.util.List;

public interface CustomerProductsInterface
{
	void customerProduct(List<String> productId,List<Integer>productsQuantity);
	void getProductByCustomer() throws SQLException ;
}
