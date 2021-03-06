package com.dhiva.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpResponse implements HttpServletResponse {
	private String responseHeader;
	private byte[] responseBody;
	private String statusCode;
	private int status;
	private String contentLength;
	private int contentLeng;
	private String contentType;
	private String httpVersion;
	private String cookie;
	private String date;
	Socket currentClient;
	PrintWriter out;
	private String input = "C:\\Users\\DHIVA\\Desktop\\servletoutput.txt";
		

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}
	
	public String getServletFile(){
		return this.input;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setDate(String date) {
		this.date = "Date: " + date;
	}

	public String getDate() {
		return date;
	}

	public void setContentType(String contentType) {
		this.contentType = "Content-Type: " + contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = "Content-Length: " + contentLength;
	}

	public String getContentLength() {
		return contentLength;
	}

	public String getResponseHeader() {
		responseHeader = httpVersion + " " + statusCode + "\r\n" + date + "\r\n" + contentType + "\r\n"+ cookie +"\r\n" + contentLength
				+ "\r\n\r\n";
		System.out.println(responseHeader);
		return responseHeader;
	}

	public void setResponseBody(byte[] mybytearray) {
		this.responseBody = mybytearray;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return "ISO-8859-1";
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return  null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		try {
			 out = new PrintWriter(new FileOutputStream(input));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out ;
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub

	}

	//@Override
//	public void setContentLength(int arg0) {
//		// TODO Auto-generated method stub
//		this.contentLeng = arg0;
//	}

	@Override
	public void setContentLengthLong(long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void addCookie(Cookie arg0) {
		// TODO Auto-generated method stub
		this.cookie = "Set-Cookie: " + "username=" + arg0.getValue();
	}

	public void addDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	public void addHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void addIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public boolean containsHeader(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStatus() {
		// TODO Auto-generated method stub
		return this.status;
	}

	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	public void setHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	public void setIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void setStatus(int arg0) {
		// TODO Auto-generated method stub
		this.status  = arg0;
	}

	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub
		//this.status = arg0;
	}

	public void setCurrentClient(Socket currentClient) {
		// TODO Auto-generated method stub
		this.currentClient = currentClient;
		
	}
	
	public Socket getCurrentClient() {
		// TODO Auto-generated method stub
		return currentClient;
	}
}
