package jscgi.demo;

import java.io.IOException;

import jscgi.server.SCGIServer;

public class EchoServer {
	
	public static void main(String [] args) {
		try {
			new SCGIServer(65001, (req, res, mode) -> {
					if (req.isBodyAvailable()) {
						String name = new String(req.getBody());
						try {
							res.write(("hello, " + name).getBytes());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
