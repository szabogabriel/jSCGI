package com.jscgi.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.jscgi.Mode;
import com.jscgi.SCGIMessage;

public class SCGIClient {
	
	private String host;
	private int port;
	
	private Socket socket;
	
	private InputStream socketIn;
	private OutputStream socketOut;
	private byte[] buffer = new byte[2048];
	
	private Mode mode;
	
	public SCGIClient(String host, int port) {
		this(host, port, Mode.STANDARD);
	}
	
	public SCGIClient(String host, int port, Mode mode) {
		this.host = host;
		this.port = port;
		this.mode = mode;
	}
	
	private void setup() throws UnknownHostException, IOException {
		if (socket == null || socket.isClosed()) {
			socket = new Socket(host, port);
			
			socketIn = socket.getInputStream();
			socketOut = socket.getOutputStream();
		}
	}
	
	public SCGIMessage sendAndReceiveAsScgiMessage(SCGIMessage request) throws IOException {
		if (mode == Mode.SCGI_MESSAGE_BASED) {
			setup();
			request.serialize(socketOut);
			return new SCGIMessage(socketIn);
		} else {
			throw new IllegalStateException();
		}
	}

	public byte[] sendAndReceiveAsByteArray(SCGIMessage request) throws UnknownHostException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sendRequest(request, out);
		return out.toByteArray();
	}
	
	public void sendRequest(SCGIMessage request, OutputStream response) throws UnknownHostException, IOException {
		if (mode == Mode.STANDARD) {
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
		} else {
			throw new IllegalStateException();
		}
	}
	
}
