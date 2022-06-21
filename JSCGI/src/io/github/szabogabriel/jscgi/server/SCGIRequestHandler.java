package io.github.szabogabriel.jscgi.server;

import java.io.OutputStream;

import io.github.szabogabriel.jscgi.Mode;
import io.github.szabogabriel.jscgi.SCGIMessage;

public interface SCGIRequestHandler {

	void handle(SCGIMessage request, OutputStream response, Mode mode);
	
}
