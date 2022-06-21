package io.github.szabogabriel.jscgi.server;

import java.io.OutputStream;

import io.github.szabogabriel.jscgi.Mode;
import io.github.szabogabriel.jscgi.SCGIMessage;

/**
 * 
 * An SCGI request handler interface. It is a fairly simple one, with only one
 * method.
 * 
 * @author gszabo
 *
 */
public interface SCGIRequestHandler {

	void handle(SCGIMessage request, OutputStream response, Mode mode);

}
