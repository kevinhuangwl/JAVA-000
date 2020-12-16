package io.kimmking.rpcfx;

public class RpcfxException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1861230240330077717L;

	public RpcfxException() {
		super();
	}

	public RpcfxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RpcfxException(String message, Throwable cause) {
		super(message, cause);
	}

	public RpcfxException(String message) {
		super(message);
	}

	public RpcfxException(Throwable cause) {
		super(cause);
	}

}
