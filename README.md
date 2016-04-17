<<<<<<< HEAD
# jSCGI
Implementation of the SCGI protocol
=======
JSCGI
=====

This project is an implementation of the SCGI protocol.

Description
-----------

It uses Java ServerSocket to handle incoming requests
and the java CachedThreadPool executor to handle them.
The main entry point is the JSCGIServer class. It requires
an integer as the listen port and a JSCGIRequestHandlerFactory,
which should produce a JSCGIRequestHandler implementation.

The JSCGIRequestHandler interface defines a single method to
handle requests represented by the JSCGIRequest interface.

The JSCGIRequest is implemented by the JSCGISocketHandler class
in this project. The readData methods should be used to read
additional data beside the SCGI headers (typically body of a POST
request). It can be read into memory or into a file. There
is no additional logic performed when reading the body of the
request.

When returning data, the sendData method should be used. It
can be invoked several times and the String and byte array
attribute versions are interchangeable. When the transfer is
finished, the finish method must be called to flush and close
the connection.

Example
-------

The simplest example consists of a JSCGIRequestHandlerFactory
class that always returns a new JSCGIRequestHandler instance.

An example of this implementation can be found in the Main
class.

> new JSCGIRequestHandlerFactory(){
> 	@Override
> 	public JSCGIRequestHandler createHandler() {
> 		return new JSCGIRequestHandler(){
> 			@Override
> 			public void handle(JSCGIRequest request) {
> 				request.sendData("Status: 200 OK\nContent-type: text/plain\nContent-length: 13\n\nHello, world!");
> 				request.finish();
> 			}
> 		};
> 	}
> })
>>>>>>> Initial commit
