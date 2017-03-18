package com.dhiva.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServlet;

import com.dhiva.server.HttpRequest.HttpMethod;
import com.dhiva.test.TestHarness;

public class CreateResponse {
	private HttpRequest requestObj;
	private String rootDirectory;
	private HttpResponse responseObj;
	private String servletResourceUri;

	public CreateResponse(HttpRequest requestObj, HttpResponse responseObj) {
		this.requestObj = requestObj;
		this.responseObj = responseObj;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public HttpResponse createResponseBody() {
		// Check for HTTP Version
		String httpVersion = requestObj.getHttpVersion();
		String httpMethod = requestObj.getHttpMethod();
		String resourceURI = requestObj.getRequestURI();

		if (httpVersion.equals("HTTP/1.1")) {
			responseObj.setHttpVersion("HTTP/1.1");
		}

		if (httpVersion.equals("HTTP/1.0")) {
			responseObj.setHttpVersion("HTTP/1.0");
		}

		if (!httpVersion.equals("HTTP/1.1") && !httpVersion.equals("HTTP/1.0")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody.getBytes());
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(htmlBody.length());
			return responseObj;
		}

		// Check for bad method name
		if (httpMethod.equals("BAD")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody.getBytes());
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(htmlBody.length());
			return responseObj;
		}

		// Methods not implemented - send 501
		if (!httpMethod.equals("GET") && !httpMethod.equals("HEAD") && !httpMethod.equals("POST")) {
			String statusCode = "501 Not Implemented";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody.getBytes());
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(htmlBody.length());
			return responseObj;
		}

		// Check for illegal access
		if (resourceURI.contains("..")) {
			String statusCode = "403 Access Forbidden";
			String htmlBody = "<html><body>" + statusCode + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody.getBytes());
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(htmlBody.length());
			return responseObj;
		}

		if (!resourceURI.startsWith("/")) {
			String statusCode = "400 Bad Request";
			String htmlBody = "<html><body>" + statusCode + "<br>Syntax error in the request line" + "</body></html>";
			responseObj.setStatusCode(statusCode);
			responseObj.setResponseBody(htmlBody.getBytes());
			responseObj.setContentType("text/html");
			getGMTDateTime();
			responseObj.setContentLength(htmlBody.length());
			return responseObj;
		}

		if (httpMethod.equals("GET") && (servletResourceUri == null)) {
			if (resourceURI.contains("%20")) {
				resourceURI = resourceURI.replaceAll("%20", " ");
			}
			final String FILE_TO_SEND = rootDirectory + resourceURI;

			File myFile = new File(FILE_TO_SEND);

			if (!myFile.exists()) {
				String statusCode = "404 Not Found";
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				responseObj.setStatusCode(statusCode);
				responseObj.setResponseBody(htmlBody.getBytes());
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(htmlBody.length());
				return responseObj;
			} else if (myFile.exists() && myFile.isFile()) {

				byte[] mybytearray = new byte[(int) myFile.length()];
				String statusCode = "200 OK";
				BufferedInputStream bis;
				try {
					bis = new BufferedInputStream(new FileInputStream(myFile));
					bis.read(mybytearray, 0, mybytearray.length);
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				responseObj.setStatusCode(statusCode);
				setFileType();
				getGMTDateTime();
				responseObj.setContentLength(mybytearray.length);
				responseObj.setResponseBody(mybytearray);
				return responseObj;
			} else if (myFile.exists() && myFile.isDirectory()) {
				File[] listOfFiles = myFile.listFiles();
				List<String> results = new ArrayList<String>(listOfFiles.length);
				String statusCode = "200 OK";
				String startTag;
				String endTag;
				String image = "";
				String displayName;
				String link = "";

				for (File file : listOfFiles) {
					if (file.isFile()) {
						image = "<img src=\"http://www.sepeb.com/file/image_20170130_072046_59737.png\"style=\"width:20px;height:20px;\">";
						results.add(file.getName());
					}
					if (file.isDirectory()) {
						image = "<img src=\"folder.jpg\">";
						results.add(file.getName());
					}
				}

				if (myFile.getPath().equals(rootDirectory)) {
					for (String fileName : results) {
						startTag = "<!DOCTYPE html> <html><body>" + image + "<a href=\"";
						endTag = "</a></body> </html><br><br>";
						displayName = "\">" + fileName;
						link += startTag + fileName + displayName + endTag;
					}
				} else {
					for (String fileName : results) {
						startTag = "<!DOCTYPE html> <html> <body>" + image + "<a href=\"";
						endTag = "</a></body> </html><br><br>";
						displayName = "\">" + fileName;
						link += startTag + myFile.getName() + "/" + fileName + displayName + endTag;
					}
				}
				responseObj.setStatusCode(statusCode);
				responseObj.setContentLength(link.length());
				responseObj.setResponseBody(link.getBytes());
				responseObj.setContentType("text/html");
				getGMTDateTime();
				return responseObj;
			}

		}
		if (httpMethod.equals("POST") && (servletResourceUri != null)) {
			final String FILE_TO_SEND = responseObj.getServletFile();
			File myFile = new File(FILE_TO_SEND);
			int statusCode = responseObj.getStatus();
			String status;

			if (statusCode == 200) {
				byte[] mybytearray = new byte[(int) myFile.length()];
				status = "200 OK";
				BufferedInputStream bis;

				try {
					bis = new BufferedInputStream(new FileInputStream(myFile));
					bis.read(mybytearray, 0, mybytearray.length);
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				responseObj.setStatusCode(status);
				responseObj.setHttpVersion(httpVersion);
				getGMTDateTime();
				responseObj.setResponseBody(mybytearray);
				return responseObj;
			} else {
				status = "404 Not Found";
				String htmlBody = "<html><body>" + status + "</body></html>";
				responseObj.setStatusCode(status);
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(htmlBody.length());
				responseObj.setResponseBody(htmlBody.getBytes());
				return responseObj;
			}
		}
		if (httpMethod.equals("GET") && (servletResourceUri != null)) {
			final String FILE_TO_SEND = responseObj.getServletFile();
			File myFile = new File(FILE_TO_SEND);
			int statusCode = responseObj.getStatus();
			String status;

			if (statusCode == 200) {
				byte[] mybytearray = new byte[(int) myFile.length()];
				status = "200 OK";
				BufferedInputStream bis;

				try {
					bis = new BufferedInputStream(new FileInputStream(myFile));
					bis.read(mybytearray, 0, mybytearray.length);
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				responseObj.setStatusCode(status);
				responseObj.setHttpVersion(httpVersion);
				getGMTDateTime();
				responseObj.setResponseBody(mybytearray);
				return responseObj;
			} else {
				status = "404 Not Found";
				String htmlBody = "<html><body>" + status + "</body></html>";
				responseObj.setStatusCode(status);
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(htmlBody.length());
				responseObj.setResponseBody(htmlBody.getBytes());
				return responseObj;
			}
		}

		if (httpMethod.equals("HEAD")) {
			final String FILE_TO_SEND = rootDirectory + resourceURI;
			File myFile = new File(FILE_TO_SEND);
			if (!myFile.exists()) {
				String statusCode = "404 Not Found";
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				responseObj.setStatusCode(statusCode);
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(htmlBody.length());
				responseObj.setResponseBody(htmlBody.getBytes());
				return responseObj;
			} else if (myFile.exists()) {
				String statusCode = "204 No Content";
				String htmlBody = "<html><body>" + statusCode + "</body></html>";
				responseObj.setStatusCode(statusCode);
				responseObj.setResponseBody(htmlBody.getBytes());
				responseObj.setContentType("text/html");
				getGMTDateTime();
				responseObj.setContentLength(htmlBody.length());
				return responseObj;
			}
		}
		return responseObj;
	}

	public void getGMTDateTime() {
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		// Give it to me in GMT time.
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		responseObj.setDate(sdf.format(currentTime));
	}

	private void setFileType() {
		String resourceURI = requestObj.getRequestURI();
		if (resourceURI.endsWith(".jpeg") || resourceURI.endsWith(".jpg")) {
			responseObj.setContentType("image/jpeg");
		} else if (resourceURI.endsWith(".bmp")) {
			responseObj.setContentType("image/x-ms-bmp");
		} else if (resourceURI.endsWith(".png")) {
			responseObj.setContentType("image/png");
		} else if (resourceURI.endsWith(".gif")) {
			responseObj.setContentType("image/gif");
		} else if (resourceURI.endsWith(".html") || resourceURI.endsWith(".htm")) {
			responseObj.setContentType("text/html");
		} else if (resourceURI.endsWith(".txt") || resourceURI.endsWith(".rtx") || resourceURI.endsWith(".text")) {
			responseObj.setContentType("text/plain");
		} else if (resourceURI.endsWith(".xml")) {
			responseObj.setContentType("text/xml");
		} else if (resourceURI.endsWith(".css")) {
			responseObj.setContentType("text/css");
		} else if (resourceURI.endsWith(".js")) {
			responseObj.setContentType("text/javascript");
		} else if (resourceURI.endsWith(".doc") || resourceURI.endsWith(".docx")) {
			responseObj.setContentType("application/msword");
		} else if (resourceURI.endsWith(".pdf")) {
			responseObj.setContentType("application/pdf");
		} else {
			responseObj.setContentType("application/octet-stream");
		}
	}

	public void setResourceUri(String resourceURI) {
		// TODO Auto-generated method stub
		this.servletResourceUri = resourceURI;

	}
}
