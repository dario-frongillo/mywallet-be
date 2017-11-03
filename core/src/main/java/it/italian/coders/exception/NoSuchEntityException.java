package it.italian.coders.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchEntityException extends RuntimeException{
	
	public NoSuchEntityException() {
        super();
    }

    public NoSuchEntityException(String s)
    {
        super(s);
    }

    public NoSuchEntityException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public NoSuchEntityException(Throwable throwable)
    {
        super(throwable);
    }
}
