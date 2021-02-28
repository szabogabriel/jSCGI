package com.jscgi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerFactory implements JSCGIRequestHandlerFactory {
	
	private final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
	
	@Override
	public void register(Runnable handler) {
		EXECUTOR_SERVICE.execute(handler);
	}

	@Override
	public JSCGIRequestHandler createHandler() {
		return new Handler();
	}

}
