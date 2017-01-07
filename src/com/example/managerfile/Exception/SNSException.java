package com.example.managerfile.Exception;



public class SNSException extends Exception
{

	private static final long serialVersionUID = -783370970702574190L;

	protected int exceptionCode = -1;
	
	//add by cly @ 2011.12.28
	protected String errorString;

	//add end

	public SNSException(int code)
	{
		super();
		exceptionCode = code;
	}

	public SNSException(int code, String str)
	{
		super(str);
		exceptionCode = code;
		
		//add by cly @ 2011.12.28
		errorString = str;
		//add end
	}
	
	//add by cly @ 2011.12.28
	public String getErrorString()
	{
		return errorString;
	}
	//add end

	public SNSException(int code, Throwable throwable)
	{
		super(throwable);
		exceptionCode = code;
	}

	//add by cly @ 2011.12.30
	
	//add end

	public int getExceptionCode()
	{
		return exceptionCode;
	}

	public void setExceptionCode(int code)
	{
		exceptionCode = code;
	}
	
	public String toString(){
		return super.toString()+" ExceptionCode:"+exceptionCode+" ErrorMessage:"+errorString;
	}
}
