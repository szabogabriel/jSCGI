package com.jscgi;

import java.io.IOException;

public class Main {
	
	public static void main(String [] args) {
		try {
			new Thread(new JSCGIServer(9009, new JSCGIRequestHandlerFactory(){
				@Override
				public JSCGIRequestHandler createHandler() {
					return new JSCGIRequestHandler(){
						@Override
						public void handle(JSCGIRequest request) {
							request.sendData("Status: 200 OK\nContent-type: text/plain\nContent-length: 13\n\nHello, world!");
							request.finish();
						}
					};
				}
			})).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
