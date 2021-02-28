package jscgi.server;

import java.io.OutputStream;

import jscgi.SCGIMessage;

public interface SCGIRequestHandler {

	void handle(SCGIMessage request, OutputStream response);
	
}
