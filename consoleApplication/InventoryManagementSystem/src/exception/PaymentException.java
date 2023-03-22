package exception;


public class PaymentException extends RuntimeException
{
	
	/**Payment is not successful....
	 * 
	 */

	public PaymentException(String message)
	{
		System.out.println(message);
		
	}
}
