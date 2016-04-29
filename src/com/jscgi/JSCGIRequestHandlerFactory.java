package com.jscgi;

public interface JSCGIRequestHandlerFactory {
	
	JSCGIRequestHandler createHandler();
	
	void register(Runnable runnable);

}
