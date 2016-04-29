package com.jscgi;

import java.io.IOException;

public class Main {
	
	public static void main(String [] args) {
		try {
			new Thread(new JSCGIServer(9876, new HandlerFactory())).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
