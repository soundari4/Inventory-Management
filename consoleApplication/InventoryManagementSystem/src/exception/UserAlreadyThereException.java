package exception;

import service.AdminService;

public class UserAlreadyThereException extends RuntimeException
{
	public UserAlreadyThereException(String message)
	{
		System.out.println(message);
	}
}
