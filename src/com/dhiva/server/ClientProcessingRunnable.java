package com.dhiva.server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.dhiva.server.HttpRequest.HttpMethod;
import com.dhiva.test.TestHarness;

public class ClientProcessingRunnable implements Runnable {
	// instance variable
	private String name;
	private String rootDirectory;
	private boolean runnableState = false;
	static boolean append = true;

	// setter and getter to access the private fields
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRunnableState(boolean isStopped) {
		this.runnableState = isStopped;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	@Override
	public void run() {
		try {
			while (true && !runnableState) {
				// each thread getting a client socket in a synchronized manner
				// since the synchronization is done on the method
				Socket currentClient = ClientConnectionsRunnable.getClientSocket();
				// if the is no client request,sleep and continue to check for
				// client connections
				if (currentClient == null) {
					Thread.sleep(50);
					continue;
				} else {
					processClientRequest(currentClient);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void processClientRequest(Socket currentClient) throws InterruptedException, IOException {
		System.out.println("processing on thread ");
		ReadSocketForParsing socketObj = new ReadSocketForParsing(currentClient);
		StringBuffer clientRequest = socketObj.readrequest();
		HttpRequestParser parseObj = new HttpRequestParser(clientRequest);
		HttpRequest requestObj = parseObj.parse();
		TestHarness testObj = new TestHarness();
		HashMap<String, HttpServlet> servlets = testObj.getServlets();
		HashMap<String, String> mappings = testObj.getServeletMapping();
		String servletName = null;
		String resourceURI = requestObj.getRequestURI();
		if (requestObj.getHttpMethod().equals("GET") && mappings.containsValue(resourceURI)) {
			for (String name : mappings.keySet()) {
				if (mappings.get(name).equals(resourceURI)) {
					servletName = name;
				}
			}

			HttpServlet s = servlets.get(servletName);
			HttpResponse responseObj = new HttpResponse();
			try {
				s.service(requestObj, responseObj);
			} catch (ServletException e) {
				e.printStackTrace();
			}

		} else {
			CreateResponse createResponseObj = new CreateResponse(requestObj);
			createResponseObj.setRootDirectory(rootDirectory);
			HttpResponse responseObj = createResponseObj.createResponseBody();
			sendClientFile(currentClient, responseObj);
			currentClient.close();
		}
	}

	private void sendClientFile(Socket currentClient, HttpResponse responseObj) {
		byte[] responseBody = responseObj.getResponseBody();
		byte[] responseHeader = responseObj.getResponseHeader().getBytes();
		OutputStream stream;
		try {
			stream = currentClient.getOutputStream();
			stream.write(responseHeader);
			stream.write(responseBody);
			stream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
