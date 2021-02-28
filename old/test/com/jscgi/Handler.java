package com.jscgi;

public class Handler implements JSCGIRequestHandler {
	
//	private static int counter = 0;
//	private int id = counter++;
	
	@Override
	public void handle(JSCGIRequest request) {
//		System.out.println("ID: " + id);
		request.sendData("Status: 200 OK\nContent-type: text/plain\nContent-length: 13\n\nHello, world!");
		request.finish();
	}

}
