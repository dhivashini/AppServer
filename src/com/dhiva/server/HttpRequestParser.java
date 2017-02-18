package com.dhiva.server;

public class HttpRequestParser {
	private StringBuffer clientRequest;

	public HttpRequestParser(StringBuffer clientRequest) {
		this.clientRequest = clientRequest;
	}

	public HttpRequest parse() {
		String request = clientRequest.toString();
		String[] splited = request.split("\\s+");
		HttpRequest requestObj = new HttpRequest();
		if(splited.length!=3){
			System.out.println(splited.length);
			System.out.println(request);
		}
		requestObj.setHttpMethod(splited[0]);
		requestObj.setRequestURI(splited[1]);
		requestObj.setHttpVersion(splited[2]);
		return requestObj;
	}
}
