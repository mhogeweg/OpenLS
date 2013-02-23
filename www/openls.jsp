<%@page contentType="application/xml"%><%@ page import="java.io.*,java.util.*,java.util.logging.Logger,java.net.*, javax.xml.parsers.DocumentBuilder, javax.xml.parsers.DocumentBuilderFactory, org.w3c.dom.Document, org.w3c.dom.Element" %>
<%
		java.io.InputStream is;
        Logger LOGGER = Logger.getLogger("openls.jsp");
		String requestString = request.getParameter("body");
		String urlString=request.getParameter("url");
		String sgString=request.getParameter("serviceGroup");
		LOGGER.info("The url is " +urlString);
		LOGGER.info("The request is " +requestString);
		LOGGER.info("The serviceGroup is " +sgString);
	    try {
			java.net.URL l_url=new java.net.URL(urlString);
			java.net.HttpURLConnection soapConnection=(java.net.HttpURLConnection)l_url.openConnection();
       		soapConnection.setRequestMethod("POST");
	      	soapConnection.setDoInput(true);
       		soapConnection.setDoOutput(true);
       		soapConnection.setUseCaches(false);
       		soapConnection.setRequestProperty("Content-Type", "application/xml");
			soapConnection.setRequestProperty("ServiceGroup", sgString);
       		java.io.PrintWriter out1 = new java.io.PrintWriter(soapConnection.getOutputStream());
       		out1.write(requestString);
       		out1.flush();
       		out1.close();

	     	is=soapConnection.getInputStream();
	     	int c;
	     //	Reader r = new InputStreamReader(is, "UTF-8");
	     	StringBuffer sb = new StringBuffer();
	     	out.clear();
	     	out.clearBuffer();
	     	while((c=is.read()) != -1){
	     		out.write(c);
	     	}
	     	//out.write(sb.toString().trim());
			out.flush();
	     }
		 catch(Exception ge){
        		out.println("exception: "+ge.getMessage());
	     }
%>
