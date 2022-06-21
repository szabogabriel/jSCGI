package com.jscgi.server;

import java.io.OutputStream;

import com.jscgi.Mode;
import com.jscgi.SCGIMessage;

public interface SCGIRequestHandler {

	void handle(SCGIMessage request, OutputStream response, Mode mode);
	
}
