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
package com.esri.gpt.server.openls.provider.services.reversegeocode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.openls.provider.components.IOperationProvider;
import com.esri.gpt.server.openls.provider.components.IProviderFactory;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.util.Address;
import com.esri.gpt.server.openls.provider.util.GeocodedAddress;

/**
 * Provides the OpenLS ReverseGeocode operation.
 */
public class ReverseGeocodeProvider implements IOperationProvider {
  
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(ReverseGeocodeProvider.class.getName());
  
  /** constructors ============================================================ */
  
  /** Default constructor */
  public ReverseGeocodeProvider() {
    super();
  }
          
  /** methods ================================================================= */
  
  /**
   * Generates the response.
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
    
	    // initialize
	    LOGGER.finer("Handling request URL...");
	    throw new OwsException("HTTP Get is not supported for this operation."); 
    
    }
  
  /**
   * Handles an XML based request (normally HTTP POST).
   * @param context the operation context
   * @param root the root node
   * @param xpath an XPath to enable queries (properly configured with name spaces)
 * @throws Exception 
   */
  public void handleXML(OperationContext context, Node root, XPath xpath)
    throws Exception {
    
    // initialize
    LOGGER.finer("Handling ReverseGeocodeRequest request XML...");
    
    // find ReverseGeocodeRequest node
    String locator = "xls:ReverseGeocodeRequest";
    Node ndReq = (Node)xpath.evaluate(locator,root,XPathConstants.NODE);
    if (ndReq != null) {
    	GeocodedAddress addr = null;
    	parseRequest(context, ndReq, xpath);
        try {
        	addr = executeRequest(context);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		context.getRequestOptions().getReverseGeocodeOptions().setgAddr(addr);
      }
    
    
    // generate the response
    generateResponse(context);
  }
  
  /**
   * Parses reverse geocode request.
   * @param context
   * @param ndReq
   * @param xpath
   * @throws XPathExpressionException
   */
  private void parseRequest(OperationContext context, Node ndReq, XPath xpath) throws XPathExpressionException{
	  ReverseGeocodeParams reqParams = context.getRequestOptions().getReverseGeocodeOptions();
	  Node ndPosition = (Node)xpath.evaluate("xls:Position",ndReq,XPathConstants.NODE);
      if(ndPosition != null){
	    	Node ndPoint = (Node)xpath.evaluate("gml:Point",ndPosition,XPathConstants.NODE);
	    	if(ndPoint != null){
	    		Node ndPos = (Node)xpath.evaluate("gml:pos",ndPoint,XPathConstants.NODE);
	    		if(ndPos != null){
	    			String[] vals = ndPos.getTextContent().split(" ");
	    			reqParams.setLat(vals[0]);
	    			reqParams.setLng(vals[1]);
	    		}
	    	}
      } 
      Node ndPreference = (Node)xpath.evaluate("xls:ReverseGeocodePreference",ndReq,XPathConstants.NODE);
      if(ndPreference != null){
      	reqParams.setPreference(ndPreference.getTextContent());
      }
  }
  
  /**
   * Processes and publishes the XML document child nodes associated with 
   * the parent action.
   * @param context the operation context
   * @param publisher the authenticated publisher
   * @param typeName the CSW collection type
   * @param handle a client supplied name (can be echoed within the response)
   * @param xmlNodes a list of child nodes containing XML's to publish
 * @return 
 * @throws Throwable 
   */
  private GeocodedAddress executeRequest(OperationContext context) 
    throws Throwable {
	  long t1 = System.currentTimeMillis();
		
	  ReverseGeocodeParams params = context.getRequestOptions().getReverseGeocodeOptions();
	  
	  String srvCfg = context.getRequestContext().getApplicationConfiguration()
	  	  .getCatalogConfiguration().getParameters().getValue("openls.reverseGeocode");
	  
		String sUrl = srvCfg
				+ "/reverseGeocode?"
				+ makeReverseGeocodeRequest(params.getLat(), params.getLng(), "json", 0);

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

		GeocodedAddress addr = parseReverseGeocodeResponse(sResponse);

		
		long t2 = System.currentTimeMillis();
		LOGGER.info("PERFORMANCE: " + (t2 - t1) + " ms spent performing service");
  
		return addr;
  }
  
	/*
	 * .../reverseGeocode?location=-71.458961673001,41.83176073543&distance=0&f=pjson
	 */
	private String makeReverseGeocodeRequest(String x, String y, String format,
			double distance) {
		StringBuffer sReverseGeocodeRequest = new StringBuffer();
		sReverseGeocodeRequest.append("location=").append(x).append(",").append(y);
		sReverseGeocodeRequest.append("&distance=").append(distance);
		sReverseGeocodeRequest.append("&f=").append(format);
		return sReverseGeocodeRequest.toString();
	}

	/**
	 * Parses reverse Geocode response.
	 * @param sResponse
	 * @return
	 * @throws Throwable
	 */
	private GeocodedAddress parseReverseGeocodeResponse(String sResponse) throws Throwable {
		GeocodedAddress addr = new GeocodedAddress();
		try {
			
			JSONObject jResponse = new JSONObject(sResponse);

			String xResponse = "<?xml version='1.0'?><response>"
					+ org.json.XML.toString(jResponse) + "</response>";
			LOGGER.info("XML from JSON = " + xResponse);

			String addressName = "";

			Address respAddr = null;
			String street = "";
			String intStreet = "";
			String city = "";
			String state = "";
			String zip = "";
			String scoreStr = "";
			String x = "";
			String y = "";
			String country = "US";
			
			DocumentBuilderFactory xfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = xfactory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xResponse));
			Document doc = db.parse(inStream);

			doc.getDocumentElement().normalize();
			LOGGER.info("Root element "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getChildNodes();
			LOGGER.info("Information of all candidates:");

			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				Element fstElmnt = (Element) fstNode;

				street = "";
				intStreet = "";
				city = "";
				state = "";
				zip = "";
				scoreStr = "";

				// LOCATION
				NodeList locationList = fstElmnt.getElementsByTagName("location");
				Element fstNmElmnt = (Element) locationList.item(0);
				NodeList nodeY = fstNmElmnt.getElementsByTagName("y");
				y = nodeY.item(0).getTextContent();
				LOGGER.info("y = " + y);
				NodeList nodeX = fstNmElmnt.getElementsByTagName("x");
				x = nodeX.item(0).getTextContent();
				LOGGER.info("x = " + x);

				// ADDRESS
				NodeList addressList = fstElmnt.getElementsByTagName("address");
				Node addressNode = addressList.item(0); 
				addressName = addressList.item(0).getTextContent();
				LOGGER.info("addressName = " + addressName);
				NodeList children = addressNode.getChildNodes();
				for(int i=0; i < children.getLength(); i++){
					Node child = children.item(i);
					if(child.getNodeName().equalsIgnoreCase("address")){
						street = child.getTextContent(); 
					}else if (child.getNodeName().equalsIgnoreCase("city")){
						city = child.getTextContent();
					}else if(child.getNodeName().equalsIgnoreCase("zip")){
						zip = child.getTextContent();
					} else if(child.getNodeName().equalsIgnoreCase("state")) { 			
						state = child.getTextContent();
					} else if (child.getNodeName().equalsIgnoreCase("country")) { 
						country = child.getTextContent();
					}
			 }

				// SCORE
				NodeList scoreList = fstElmnt.getElementsByTagName("score");
				if(scoreList != null && scoreList.getLength() > 0){
					scoreStr = scoreList.item(0).getTextContent();
					new Double(scoreStr);
					LOGGER.info("score = " + scoreStr);
				}

				// NOW ADD THIS RESULT TO THE OUTPUT pos
				respAddr = new Address();
				respAddr.setStreet(street);
				respAddr.setMunicipality(city);
				respAddr.setPostalCode(zip);
				respAddr.setCountrySubdivision(state);
				respAddr.setIntersection(intStreet);
				
				
				addr.setX(x);
				addr.setY(y);
				addr.setAddress(respAddr);
				addr.setCountry(country);
				
			}
		} catch (Exception p_e) {
			LOGGER.severe("Caught Exception" + p_e.getMessage());
		}
		return addr;
	}
      
    
}
