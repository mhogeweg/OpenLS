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
package com.esri.gpt.server.openls.provider.services.map;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.openls.provider.components.IOperationProvider;
import com.esri.gpt.server.openls.provider.components.IProviderFactory;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;

/**
 * Provides the Openls GetPortrayMapCapabilities operation.
 */
public class GetPortrayMapCapabilitiesProvider implements IOperationProvider {
  
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(GetPortrayMapCapabilitiesProvider.class.getName());
    
  /** constructors ============================================================ */
  
  /** Default constructor */
  public GetPortrayMapCapabilitiesProvider() {
    super();
  }
          
  /** methods ================================================================= */
  
  /**
   * Executes a parsed operation request.
   * @param context the operation context
   * @throws Exception if a processing exception occurs
   */
  private void generateResponse(OperationContext context) throws Exception {
    IProviderFactory factory = context.getProviderFactory();
    IResponseGenerator generator = factory.makeResponseGenerator(context);
    if (generator == null) {
      String msg = "IProviderFactory.makeResponseGenerator: instantiation failed.";
      LOGGER.log(Level.SEVERE,msg);
      throw new OwsException(msg);
    } else {
      generator.generateResponse(context);
    } 
  }
    
  /**
   * Handles a URL based request (HTTP GET).
   * @param context the operation context
   * @param request the HTTP request
   * @throws Exception if a processing exception occurs
   */
  public void handleGet(OperationContext context, HttpServletRequest request) 
    throws Exception {
  }

  /**
   * Handles an XML based request (normally HTTP POST).
   * @param context the operation context
   * @param root the root node
   * @param xpath an XPath to enable queries (properly configured with name spaces)
   * @throws Exception if a processing exception occurs
   */
  public void handleXML(OperationContext context, Node root, XPath xpath)
    throws Exception {
    
    // initialize 
    LOGGER.finer("Handling xls:GetPortrayMapCapabilitiesRequest request XML...");
    String locator="xls:GetPortrayMapCapabilitiesRequest";
    Node ndReq = (Node)xpath.evaluate(locator,root,XPathConstants.NODE);
    if (ndReq != null) {    	
    	 // params = processRequest(context, root,ndReq, xpath);
	  try {	    	
			executeRequest(context);			
		} catch (Throwable e) {
			e.printStackTrace();
		}	    
		generateResponse(context);
    }  
  }
  
    /**
     * Executes get capabilities request.
     * @param context
     * @throws Throwable
     */
	public void executeRequest(OperationContext context) throws Throwable {
		long t1 = System.currentTimeMillis();
		
		GetPortrayMapCapabilitiesParams params = context.getRequestOptions().getGetPortrayMapCapabilitiesOptions();

		String srvCfg = context.getRequestContext().getApplicationConfiguration()
	  	  .getCatalogConfiguration().getParameters().getValue("openls.portrayMap");
		
		String sUrl = srvCfg+ "?f=json&pretty=true";

		URL url = new URL(sUrl);
		URLConnection conn = url.openConnection();
		String line = "";
		String sResponse = "";

		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader rd = new BufferedReader(isr);
		while ((line = rd.readLine()) != null) {
			sResponse += line;
		}
		rd.close();		
		
		parseResponse(params,sResponse);
		
		long t2 = System.currentTimeMillis();
		LOGGER.info("PERFORMANCE: " + (t2 - t1) + " ms spent performing service");
	}
	
	/**
	 * Parse rest response.
	 * @param params
	 * @param sResponse
	 * @throws JSONException
	 */
	private void parseResponse(GetPortrayMapCapabilitiesParams params,String sResponse) throws JSONException{

		HashMap<String,String> layerList = null;		
		ArrayList<String> formatList = null;
		ArrayList<String> srsList = null;
		ArrayList<String> styleList = null;
		
		JSONObject jResponse = new JSONObject(sResponse);

		String xResponse = "<?xml version='1.0'?><response>"
				+ org.json.XML.toString(jResponse) + "</response>";
		LOGGER.info("XML from JSON = " + xResponse);
		
		JSONArray layers = jResponse.getJSONArray("layers");
		if(layers != null){
			layerList = new HashMap<String,String>();
			for(int i=0; i <layers.length(); i++){
				JSONObject layer = layers.getJSONObject(i);
				layerList.put(layer.getString("name"),layer.getString("id"));
			}
		}
		
		JSONObject spatialReference = jResponse.getJSONObject("spatialReference");	
		if(spatialReference != null){
			srsList = new ArrayList<String> ();
			srsList.add(spatialReference.optString("wkid"));
		}
		
		String supportedImageFormatTypes = jResponse.getString("supportedImageFormatTypes");	
		if(supportedImageFormatTypes != null){
			formatList = new ArrayList<String> ();
			String[] formats = supportedImageFormatTypes.split(",");
			for(String format:formats){
				formatList.add(format);
			}
		}
		
		params.setLayerMap(layerList);
		params.setFormatList(formatList);
		params.setSrsList(srsList);
		params.setStyleList(styleList);
						
	}

}
