/* See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * Esri Inc. licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esri.gpt.server.openls;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esri.gpt.framework.context.RequestContext;
import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.provider.components.OperationResponse;
import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.openls.provider.components.IOriginalXmlProvider;
import com.esri.gpt.server.openls.provider.components.IProviderFactory;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.components.ProviderFactory;
import com.esri.gpt.server.openls.provider.components.RequestHandler;


/**
 * OpenLS provider servlet.
 */
public class OpenLSServlet extends HttpServlet {
  
/**
* 
*/
private static final long serialVersionUID = 1L;

/** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(OpenLSServlet.class.getName());
  
  /** instance variables ====================================================== */
  private String xlsSubContextPath;
  private String resourceFilePrefix;
      
  /** methods ================================================================= */
  
  /**
   * Initializes the  servlet.
   * <br/>Reads the "xlsSubContextPath" and "resourceFilePrefix".
   * init params from the servlet configuration.
   * @param config the servlet configuration
   * @throws ServletException if an initialization exception occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.xlsSubContextPath = config.getInitParameter("xlsSubContextPath");
    this.resourceFilePrefix = config.getInitParameter("resourceFilePrefix");
  }
  
  public void doGet (HttpServletRequest p_request, HttpServletResponse p_response)
  throws ServletException, java.io.IOException {
	  try {
		execute(p_request, p_response);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
  
  public void doPost (HttpServletRequest p_request, HttpServletResponse p_response)
  throws ServletException, java.io.IOException {
	  try {
		execute(p_request, p_response);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
        
  /**
   * Executes a xls request.
   * @param request the HTTP servlet request
   * @param response the HTTP servlet response
   * @param context the request context
   * @throws Exception if a processing exception occurs
   */
  protected void execute(HttpServletRequest request, 
                            HttpServletResponse response)
    throws Exception {
    
    // process the request
    LOGGER.fine("Executing xls provider request....");
    RequestContext reqContext = null;
    String xlsResponse = "";
    String mimeType = "application/xml";
    OperationResponse opResponse = null;
    try {
      String xlsRequest = readInputCharacters(request);
      LOGGER.finer("xlsRequest:\n"+xlsRequest);
      reqContext = RequestContext.extract(request);
      RequestHandler handler = this.makeRequestHandler(request,response,reqContext);
      handler.getOperationContext().setRequestContext(reqContext);
      if (xlsRequest.length() > 0) {
        opResponse = handler.handleXML(xlsRequest);
      } else {
        opResponse = handler.handleGet(request);
      }
      if (opResponse != null) {
        xlsResponse = Val.chkStr(opResponse.getResponseXml());
        String fmt = Val.chkStr(opResponse.getOutputFormat());
        if (fmt.equalsIgnoreCase("text/xml")) {
          mimeType = "text/xml";
        }
      }
 
    } catch (Exception e) {
      xlsResponse = handleException(e);
    }
    
    // write the response
    LOGGER.finer("xlsResponse:\n"+xlsResponse);
    if (xlsResponse.length() > 0) {
      writeCharacterResponse(response,xlsResponse,"UTF-8",mimeType+"; charset=UTF-8");
    }
  }
  
  /**
   * Gets the logger.
   * @return the logger
   */
  protected Logger getLogger() {
    return LOGGER;
  }
  
  /**
   * Creation an ExceptionReport response when an exception is encountered.
   * @param e the exception
   * @return the exception report string
   * @throws throws Exception if an authorization related exception occurs
   */
  protected String handleException(Exception e) throws Exception {
    if (e instanceof OwsException) {
      OwsException ows = (OwsException)e;
      LOGGER.finer("Invalid xls request: "+e.getMessage());
      return ows.getReport();
    } else {
      OwsException ows = new OwsException(e);
      LOGGER.log(Level.WARNING,e.toString(),e);
      return ows.getReport();
    }
  }    
    
  /**
   * Makes a handler for the xls request.
   * @param request the HTTP servlet request
   * @param response the HTTP servlet response
   * @param context the request context
   * @return the request handler
   */
  protected RequestHandler makeRequestHandler(HttpServletRequest request,
          HttpServletResponse response, RequestContext reqContext) {
    IProviderFactory factory = new ProviderFactory();
    RequestHandler handler = factory.makeRequestHandler(
	        request,reqContext, this.xlsSubContextPath, this.resourceFilePrefix);
    return handler;
  }
  
  /**
   * Reads the full XML associated with a document UUID.
   * @param request the HTTP servlet request
   * @param response the HTTP servlet response
   * @param context the request context
   * @param id the document id
   * @return the document XML
   * @throws Exception if an exception occurs
   */
  protected String readFullXml(HttpServletRequest request,
                               HttpServletResponse response,
                               RequestContext context,
                               String id) 
    throws Exception {
    RequestHandler handler = this.makeRequestHandler(request,response,context);
    OperationContext opContext = handler.getOperationContext();
    opContext.setRequestContext(context);
    IProviderFactory factory = opContext.getProviderFactory();
    IOriginalXmlProvider oxp = factory.makeOriginalXmlProvider(opContext);
    if (oxp == null) {
      String msg = "The getxml parameter is not supported.";
      String locator = "getxml";
      throw new OwsException(OwsException.OWSCODE_InvalidParameterValue,locator,msg);
    } else {
      String xml = oxp.provideOriginalXml(opContext,id);
      return xml;
    }
  }
  
  /**
   * Fully reads the characters from the request input stream.
   * @param request the HTTP servlet request
   * @return the characters read
   * @throws IOException if an exception occurs
   */
  protected String readInputCharacters(HttpServletRequest request)
    throws IOException {
    StringBuffer sb = new StringBuffer();
    InputStream is = null;
    InputStreamReader ir = null;
    BufferedReader br = null;
    try {
      //if (request.getContentLength() > 0) {
        char cbuf[] = new char[2048];
        int n = 0;
        int nLen = cbuf.length;
        String sEncoding = request.getCharacterEncoding();
        if ((sEncoding == null) || (sEncoding.trim().length() == 0)) {
          sEncoding = "UTF-8";
        }
        is = request.getInputStream();
        ir = new InputStreamReader(is,sEncoding);
        br = new BufferedReader(ir);
        while ((n = br.read(cbuf,0,nLen)) > 0) {
          sb.append(cbuf,0,n);
        }
      //}
    } finally {
      try {if (br != null) br.close();} catch (Exception ef) {}
      try {if (ir != null) ir.close();} catch (Exception ef) {}
      try {if (is != null) is.close();} catch (Exception ef) {}
    }
    return sb.toString();
  }
  
  /**
   * Writes characters to the response stream.
   * @param response the servlet response
   * @param content the content to write
   * @param charset the response character encoding 
   * @param contentType the response content type
   * @throws IOException if an IO exception occurs
   */
  protected void writeCharacterResponse(HttpServletResponse response, 
                                        String content,
                                        String charset,
                                        String contentType) 
    throws IOException {
    PrintWriter writer = null;
    try {
      if (content.length() > 0) {
        response.setCharacterEncoding(charset);
        response.setContentType(contentType);
        writer = response.getWriter();
        writer.write(content.trim());
        writer.flush();
      }
    } finally {
      try {
        if (writer != null) {
          writer.flush();
          writer.close();
        }
      } catch (Exception ef) {
        getLogger().log(Level.SEVERE,"Error closing PrintWriter.",ef);
      }
    }
  }
  
  /**
   * Convience method for writeCharacterResponse.
   * <br/>charset="UTF-8"
   * <br/>contentType="text/xml; charset=UTF-8"
   * @param response the servlet response
   * @param content the content to write
   * @throws IOException if an IO exception occurs
   */
  protected void writeXmlResponse(HttpServletResponse response, 
                                  String content) 
    throws IOException {
    writeCharacterResponse(response,content,"UTF-8","text/xml; charset=UTF-8");
  }
}
