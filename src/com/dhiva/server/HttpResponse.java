package com.dhiva.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpResponse implements HttpServletResponse {
	private String responseHeader;
	private byte[] responseBody;
	private String statusCode;
	private String contentLength;
	private String contentType;
	private String httpVersion;
	private String date;
	Socket currentClient;

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
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

	public void setContentLength(String contentLength) {
		this.contentLength = "Content-Length: " + contentLength;
	}

	public String getContentLength() {
		return contentLength;
	}

	public String getResponseHeader() {
		responseHeader = httpVersion + " " + statusCode + "\r\n" + date + "\r\n" + contentType + "\r\n" + contentLength
				+ "\r\n\r\n";
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
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return (ServletOutputStream) currentClient.getOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void setContentLength(int arg0) {
		// TODO Auto-generated method stub

	}

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

	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsHeader(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void sendError(int arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendError(int arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendRedirect(String arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHeader(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int arg0, String arg1) {
		// TODO Auto-generated method stub

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
