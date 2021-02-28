jSCGI
=====

This project is an implementation of the SCGI protocol.

Description
-----------

It uses Java ServerSocket to handle incoming requests and the java CachedThreadPool executor to handle them. The main entry point is the `JSCGIServer` class. It requires an integer as the listen port and a `CGIRequestHandler` interface, which should be able to handle all expected SCGI requests.

The `SCGIRequestHandler` is a functional interface. It accepts an `SCGIMessage` instance and an OutputStream, into which the created response should be written.

The `SCGIMessage` is used to pass requests around. It is responsible for parsing the SCGI message from an InputStream, but it is also capable of serializing the object to an OutputStream. It holds the implementation for the SCGI protocol.

There is also an `SCGIClient` available which should be used to send requests to the `SCGIServer`. It offers several variants, how to handle the response data. It accepts either an OutputStream or can return a byte array or an `SCGIMessage` instance. Returning the `SCGIMessage` instance isn't in line with the SCGI protocol, which should return the HTTP response directly. However, using the `SCGIMessage` for bidirectional communication it offers a better application logic separation thus be available for custom protocol implementations as well.

Example
-------

A basic example is available in the `scgi.MainFlowTest`.
