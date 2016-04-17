package com.jscgi;

import java.io.File;
import java.util.Set;

public interface JSCGIRequest {
	
	Set<String> getKeys();
	
	String getParameter(String key);
	
	byte[] readData(int length);
	
	void readData(int length, File targetFile);
	
	void sendData(String data);
	
	void sendData(byte [] data);
	
	void finish();

}
