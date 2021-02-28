package jscgi.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import jscgi.SCGIMessage;

public class SCGIClient {
	
	private String host;
	private int port;
	
	private Socket socket;
	
	private InputStream socketIn;
	private OutputStream socketOut;
	private byte[] buffer = new byte[2048];
	
	public SCGIClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private void setup() throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		
		socketIn = socket.getInputStream();
		socketOut = socket.getOutputStream();
	}
	
	public SCGIMessage sendAndReceiveAsScgiMessage(SCGIMessage request) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(sendAndReceiveAsByteArray(request));
		return new SCGIMessage(bin);
	}

	public byte[] sendAndReceiveAsByteArray(SCGIMessage request) throws UnknownHostException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sendRequest(request, out);
		return out.toByteArray();
	}
	
	public void sendRequest(SCGIMessage request, OutputStream response) throws UnknownHostException, IOException {
		setup();
		
		try {
			request.serialize(socketOut);
			
			int read;
			while ((read = socketIn.read(buffer)) > 0) {
				response.write(buffer, 0, read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socketIn.close();
				socketOut.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
