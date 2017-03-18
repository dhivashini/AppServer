package com.dhiva.server;

//public class HttpRequestParser {

//	private StringBuffer clientRequest;
//
//	public HttpRequestParser(StringBuffer clientRequest) {
//		this.clientRequest = clientRequest;
//	}
//
//	public HttpRequest parse() {
//		String request = clientRequest.toString();
//		String[] splited = request.split("\\s+");
//		HttpRequest requestObj = new HttpRequest();
//		if(splited.length!=3){
//			System.out.println(splited.length);
//			System.out.println(request);
//		}
//		requestObj.setHttpMethod(splited[0]);
//		requestObj.setRequestURI(splited[1]);
//		requestObj.setHttpVersion(splited[2]);
//		return requestObj;
//	}
//}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.Hashtable;

/**
 * Class for HTTP request parsing as defined by RFC 2612:
 * 
 * Request = Request-Line ; Section 5.1 (( general-header ; Section 4.5 |
 * request-header ; Section 5.3 | entity-header ) CRLF) ; Section 7.1 CRLF [
 * message-body ] ; Section 4.3
 * 
 * @author izelaya
 *
 */
public class HttpRequestParser {
	private StringBuffer clientRequest;
	private Socket currentClient;
	private String _requestLine;
	private Hashtable<String, String> _requestHeaders;
	private StringBuffer _messagetBody;

	public HttpRequestParser(Socket currentClient) {
		this.currentClient = currentClient;
		_requestHeaders = new Hashtable<String, String>();
		_messagetBody = new StringBuffer();
	}

	/**
	 * Parse and HTTP request.
	 * 
	 * @param request
	 *            String holding http request.
	 * @throws IOException
	 *             If an I/O error occurs reading the input stream.
	 * @throws HttpFormatException
	 *             If HTTP Request is malformed
	 */
	public HttpRequest parse() throws IOException, HttpFormatException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(currentClient.getInputStream()));

		HttpRequest requestObj = setRequestLine(reader.readLine()); // Request-Line
																	// ; Section
																	// 5.1

		String header = reader.readLine();
		while (header.length() > 0) {
			appendHeaderParameter(header);
			header = reader.readLine();
		}
		System.out.println(_requestLine);
		System.out.println(_requestHeaders);

		// String bodyLine = reader.readLine();
		// while (bodyLine != null) {
		// appendMessageBody(bodyLine);
		// bodyLine = reader.readLine();
		// }

		return requestObj;
	}

	/**
	 * 
	 * 5.1 Request-Line The Request-Line begins with a method token, followed by
	 * the Request-URI and the protocol version, and ending with CRLF. The
	 * elements are separated by SP characters. No CR or LF is allowed except in
	 * the final CRLF sequence.
	 * 
	 * @return String with Request-Line
	 */
	public String getRequestLine() {
		return _requestLine;
	}

	private HttpRequest setRequestLine(String requestLine) throws HttpFormatException {
		if (requestLine == null || requestLine.length() == 0) {
			throw new HttpFormatException("Invalid Request-Line: " + requestLine);
		}
		_requestLine = requestLine;
		HttpRequest requestObj = new HttpRequest();
		String[] splited = _requestLine.split("\\s+");
		// if(splited.length!=3){
		// System.out.println(splited.length);
		// System.out.println(request);
		// }
		requestObj.setHttpMethod(splited[0]);
		requestObj.setRequestURI(splited[1]);
		requestObj.setHttpVersion(splited[2]);
		if (splited[0].equals("POST")) {
			int index = splited[1].indexOf("?");
			requestObj.setRequestURI(splited[1].substring(0, index));
			String query = splited[1].substring(index+1);
			String[] params = query.split("&");
			for (String current : params) {
				String[] param = current.split("=");
				requestObj.setParameter(param[0], param[1]);
			}
		}
		return requestObj;
	}

	private void appendHeaderParameter(String header) throws HttpFormatException {
		int idx = header.indexOf(":");
		if (idx == -1) {
			throw new HttpFormatException("Invalid Header Parameter: " + header);
		}
		_requestHeaders.put(header.substring(0, idx), header.substring(idx + 1, header.length()));

	}

	/**
	 * The message-body (if any) of an HTTP message is used to carry the
	 * entity-body associated with the request or response. The message-body
	 * differs from the entity-body only when a transfer-coding has been
	 * applied, as indicated by the Transfer-Encoding header field (section
	 * 14.41).
	 * 
	 * @return String with message-body
	 */
	public String getMessageBody() {
		return _messagetBody.toString();
	}

	private void appendMessageBody(String bodyLine) {
		_messagetBody.append(bodyLine).append("\r\n");
	}

	/**
	 * For list of available headers refer to sections: 4.5, 5.3, 7.1 of RFC
	 * 2616
	 * 
	 * @param headerName
	 *            Name of header
	 * @return String with the value of the header or null if not found.
	 */
	public String getHeaderParam(String headerName) {
		return _requestHeaders.get(headerName);
	}
}
