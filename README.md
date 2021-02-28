jSCGI
=====

This project is an implementation of the SCGI protocol.

Description
-----------

It uses Java ServerSocket to handle incoming requests and the java CachedThreadPool executor to handle them. The main entry point is the `JSCGIServer` class. It requires an integer as the listen port and a `SCGIRequestHandler` interface, which should be able to handle all expected SCGI requests.

The `SCGIRequestHandler` is a functional interface. It accepts an `SCGIMessage` instance and an OutputStream, into which the created response should be written.

The `SCGIMessage` is used to pass requests around. It is responsible for parsing the SCGI message from an InputStream, but it is also capable of serializing the object to an OutputStream. It holds the implementation for the SCGI protocol.

There is also an `SCGIClient` available which should be used to send requests to the `SCGIServer`. It offers several variants, how to handle the response data. It accepts either an OutputStream or can return a byte array or an `SCGIMessage` instance. Returning the `SCGIMessage` instance isn't in line with the SCGI protocol, which should return the HTTP response directly. However, using the `SCGIMessage` for bidirectional communication it offers a better application logic separation thus be available for custom protocol implementations as well.

Running the server
------------------

The server can be created by passing the listen port and an `SCGIRequestHandler` implementation, where the request is an `SCGIMessage` instance and the response is an `OutputStream`.

```
new SCGIServer(port, (req, res) -> {
  if (req.isBodyAvailable()) {
    String name = new String(req.getBody());
    try {
      res.write(("Hello, " + name).getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
});
 ```
 
 Running the client
 ------------------
 
 The client is as simple as creating an `SCGIClient` instance and sending the `SCGIMessage` parameter to it.
 
 ```
 SCGIClient client = new SCGIClient("localhost", port);
 String response = new String(client.sendAndReceiveAsByteArray(new SCGIMessage(new HashMap<>(), "World!".getBytes()))); // Should return "Hello, World!"
 ```

