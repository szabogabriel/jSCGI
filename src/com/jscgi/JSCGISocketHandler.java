package com.jscgi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSCGISocketHandler implements JSCGIRequest, Runnable {
	
	private final Socket SOCKET;
	private final InputStream IN;
	private final OutputStream OUT;
	private final JSCGIRequestHandler HANDLER;
	private final Map<String, String> PARAMS = new HashMap<String, String>();
	
	private byte [] header;
	
	public JSCGISocketHandler(Socket socket, JSCGIRequestHandler handler) throws IOException {
		this.SOCKET = socket;
		this.IN = socket.getInputStream();
		this.OUT = socket.getOutputStream();
		this.HANDLER = handler;
		this.SOCKET.setKeepAlive(true);
		this.SOCKET.setTcpNoDelay(true);
	}

	@Override
	public void run() {
		while (SOCKET.isConnected() && !SOCKET.isClosed()) {
			try {
				handleInput();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleInput() throws IOException {
		int len = readLength();
		header = new byte [len];
		int read = IN.read(header);
		int check = IN.read();
		if (read == len && check == ',') {
			parseHeaders();
			HANDLER.handle(this);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private void parseHeaders() {
		String key, value;
		for (int i = 0; i < header.length; i++) {
			key = read(i);
			i += key.length() + 1;
			value = read(i);
			i += value.length();
			PARAMS.put(key, value);
		}
	}
	
	private String read(int i) {
		byte [] buffer = new byte [128];
		int poz = i, counter = 0;
		while (header[poz] != 0) {
			if (counter + 1 == buffer.length) {
				buffer = Arrays.copyOf(buffer, buffer.length + 128);
			}
			buffer[counter++] = header[poz++];
		}
		return new String(buffer, 0, counter);
	}
	
	private int readLength() throws IOException {
		int swp, ret = 0;
		while ((swp = IN.read()) != ':') {
			ret = (ret * 10) + (swp - '0');
		}
		return ret;
	}
	
	@Override
	public Set<String> getKeys() {
		return PARAMS.keySet();
	}
	
	@Override
	public String getParameter(String key) {
		return PARAMS.get(key);
	}
	
	@Override
	public byte[] readData(int length) {
		byte [] data = new byte [length];
		try {
			int read = IN.read(data);
			if (read != length) {
				throw new IllegalArgumentException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@Override
	public void readData(int length, File target) {
		byte [] buffer = new byte [1024];
		int read, count = 0;
		try (FileOutputStream fOut = new FileOutputStream(target)) {
			while (IN.available() > 0) {
				read = IN.read(buffer);
				count += read;
				fOut.write(buffer, 0, read);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (count != length) {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public void sendData(String data) {
		sendData(data.getBytes());
	}
	
	@Override
	public void sendData(byte [] data) {
		try {
			OUT.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void finish() {
		try {
			OUT.flush();
			OUT.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
