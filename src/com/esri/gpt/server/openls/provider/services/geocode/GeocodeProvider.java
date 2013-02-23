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
package com.esri.gpt.server.openls.provider.services.geocode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.provider.components.ISupportedValues;
import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.csw.provider.components.ParseHelper;
import com.esri.gpt.server.csw.provider.components.ServiceProperties;
import com.esri.gpt.server.csw.provider.components.ValidationHelper;
import com.esri.gpt.server.openls.provider.components.IOperationProvider;
import com.esri.gpt.server.openls.provider.components.IProviderFactory;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.components.XlsConstants;
import com.esri.gpt.server.openls.provider.util.Address;
import com.esri.gpt.server.openls.provider.util.GeocodedAddress;

/**
 * Provides the Openls Geocode operation.
 */
public class GeocodeProvider implements IOperationProvider {

	/**
	 * class variables =========================================================
	 */

	/** The Logger. */
	private static Logger LOGGER = Logger.getLogger(GeocodeProvider.class
			.getName());

	/**
	 * constructors ============================================================
	 */

	/** Default constructor */
	public GeocodeProvider() {
		super();
	}

	/**
	 * methods =================================================================
	 */

	/**
	 * Handles a URL based request (HTTP GET).
	 * 
	 * @param context
	 *            the operation context
	 * @param request
	 *            the HTTP request
	 * @throws Exception
	 *             if a processing exception occurs
	 */
	public void handleGet(OperationContext context, HttpServletRequest request)
			throws Exception {

		// initialize
		LOGGER.finer("Handling Geocode request URL...");
		throw new OwsException("HTTP Get is not supported for this operation.");
	}

	/**
	 * Handles an XML based request (normally HTTP POST).
	 * 
	 * @param context
	 *            the operation context
	 * @param root
	 *            the root node
	 * @param xpath
	 *            an XPath to enable queries (properly configured with name
	 *            spaces)
	 * @throws Exception
	 *             if a processing exception occurs
	 */
	public void handleXML(OperationContext context, Node root, XPath xpath)
			throws Exception {

		// initialize
		LOGGER.finer("Handling Geocode request XML...");
		GeocodeParams tOptions = context.getRequestOptions()
				.getGeocodeOptions();
		ServiceProperties svcProps = context.getServiceProperties();
		ParseHelper pHelper = new ParseHelper();
		ValidationHelper vHelper = new ValidationHelper();
		String locator;
		String[] parsed;
		ISupportedValues supported;

		// service and version are parsed by the parent RequestHandler

		// output format
		locator = "@outputFormat";
		parsed = pHelper.getParameterValues(root, xpath, locator);
		supported = svcProps
				.getSupportedValues(XlsConstants.Parameter_OutputFormat);
		context.getOperationResponse().setOutputFormat(
				vHelper.validateValue(supported, locator, parsed, false));

		// request ID
		locator = "@requestId";
		parsed = pHelper.getParameterValues(root, xpath, locator);
		tOptions.setRequestId(vHelper.validateValue(locator, parsed, false));

		GeocodeParams reqParams = new GeocodeParams();
	    locator = "xls:GeocodeRequest";
	    Node ndReq = (Node)xpath.evaluate(locator,root,XPathConstants.NODE);
	    if (ndReq != null) {
	    	parseRequest(reqParams, ndReq, xpath);	    
	    } 
	       	        
        try {
        	context.getRequestOptions()
			.getGeocodeOptions().setgAddrList(executeRequest(context,reqParams));
        	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		generateResponse(context);
    }
	
	/**
	 * Reads Address Information from request
	 * @param reqParams
	 * @param ndReq
	 * @param xpath
	 * @throws XPathExpressionException
	 */
	public void parseRequest(GeocodeParams reqParams, Node ndReq, XPath xpath) throws XPathExpressionException{
		NodeList ndAddresses = (NodeList)xpath.evaluate("xls:Address",ndReq,XPathConstants.NODESET);
        if(ndAddresses != null){
		    for(int i=0; i<ndAddresses.getLength(); i++){
		    	Node address = ndAddresses.item(i);
		    	if(address != null){
		    		Address addr = new Address();
		    		Node ndStrAddr = (Node)xpath.evaluate("xls:StreetAddress",address,XPathConstants.NODE);
		    		if(ndStrAddr != null){
		    			Node ndStr = (Node)xpath.evaluate("xls:Street",ndStrAddr,XPathConstants.NODE);
		    			if(ndStr != null){
		    				addr.setStreet(ndStr.getTextContent());
		    			}
		    		}
		    		Node ndPostalCode = (Node)xpath.evaluate("xls:PostalCode",address,XPathConstants.NODE);
		    		if(ndPostalCode != null){
		    			addr.setPostalCode(ndPostalCode.getTextContent());
		    		}
		    		NodeList ndPlaces = (NodeList)xpath.evaluate("xls:Place",address,XPathConstants.NODESET);
		    		if(ndPlaces != null){
		    			for(int j=0; j<ndPlaces.getLength(); j++){
		    				Node ndPlace = ndPlaces.item(j);			    				
		    				String type = Val.chkStr((String) xpath.evaluate("@type",ndPlace,XPathConstants.STRING));
		    				addr.setPlaceType(type);
		    				if(type.equalsIgnoreCase("Municipality")){
		    					addr.setMunicipality(ndPlace.getTextContent());
		    				}else if(type.equalsIgnoreCase("CountrySubdivision")){
		    					addr.setCountrySubdivision(ndPlace.getTextContent());
		    				}else if(type.equalsIgnoreCase("BuildingNumber")){
		    					addr.setBuildingNumber(ndPlace.getTextContent());
		    				}else if(type.equalsIgnoreCase("Intersection")){
		    					addr.setIntersection(ndPlace.getTextContent());
		    				}else if(type.equalsIgnoreCase("StreetVec")){
		    					addr.setStreetVec(ndPlace.getTextContent());
		    				}
		    			}
		    		}	
		    		reqParams.getAddresses().add(addr);
		    	}			    	
		    }
	    }
	}
	    
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
	     * Executes geocode request
	     * @param context
	     * @param params
	     * @return
	     * @throws Throwable
	     */
	public ArrayList<GeocodedAddress> executeRequest(OperationContext context, 
			GeocodeParams params) throws Throwable {
		
		String srvCfg = context.getRequestContext().getApplicationConfiguration()
	  	  .getCatalogConfiguration().getParameters().getValue("openls.geocode");
		
		
	//	String m_srsName = params.getSrsName();
		if (params.getAddresses().size() <= 0) {
			Logger.getLogger("In processRequest: No address.");
		}
		InputStream is = null;
		for (int i = 0; i < params.getAddresses().size(); i++) {
			Address reqAddr = new Address();
			reqAddr = (Address) params.getAddresses().get(i);
	
			// MAKE THE GEOCODE REQUEST
			/*
			 * http://tasks.arcgisonline.com/ArcGIS/rest/services/Locators/TA_Address_NA
			 * /GeocodeServer
			 * .../findAddressCandidates?Address=380+New+York+Street&City
			 * =Redlands&State=CA& Zip=92373&Zip4=&Country=USA&outFields=&f=pjson
			 */
			String sGeocodeRequest = makeGeocodeRequest(reqAddr);
	
			// SUBMIT THE REQUEST
			String sUrl = srvCfg + "/findAddressCandidates?" + sGeocodeRequest;
			LOGGER.info("REQUEST=\n" + sUrl);
	
			URL url = new URL(sUrl);
			URLConnection conn = url.openConnection();
	
			// Get the response
			String line = "";
			String sResponse = "";
	
			is = conn.getInputStream();
	
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader rd = new BufferedReader(isr);
			while ((line = rd.readLine()) != null) {
				sResponse += line;
			}
			rd.close();
			url = null;
		
			return parseGeocodeResponse(sResponse); 
		}
		
		return null;
	}

	/*
	 * .../findAddressCandidates?Address=380+New+York+Street&City=Redlands&State=
	 * CA & Zip=92373&Zip4=&Country=USA&outFields=&f=pjson
	 */
	private String makeGeocodeRequest(Address reqAddr) {
		try {
			String sGeocodeRequest = "";

			sGeocodeRequest = "Address="
					+ java.net.URLEncoder.encode(reqAddr.getStreet(), "UTF-8");
			sGeocodeRequest += "&City="
					+ java.net.URLEncoder.encode(reqAddr.getMunicipality(),
							"UTF-8");
			sGeocodeRequest += "&State="
					+ java.net.URLEncoder.encode(
							reqAddr.getCountrySubdivision(), "UTF-8");
			sGeocodeRequest += "&Zip="
					+ java.net.URLEncoder.encode(reqAddr.getPostalCode(),
							"UTF-8");
			sGeocodeRequest += "&Country="
					+ java.net.URLEncoder.encode(reqAddr.getCountry(), "UTF-8");
			sGeocodeRequest += "&outFields=&f=json";

			return sGeocodeRequest;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Parses Geocode response
	 * @param sResponse
	 * @return
	 * @throws Throwable
	 */
	private ArrayList<GeocodedAddress> parseGeocodeResponse(String sResponse)
			throws Throwable {

		try {

			JSONObject jResponse = new JSONObject(sResponse);
			ArrayList<GeocodedAddress> gAddrList = new ArrayList<GeocodedAddress>();
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

			DocumentBuilderFactory xfactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder db = xfactory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xResponse));
			Document doc = db.parse(inStream);

			doc.getDocumentElement().normalize();
			LOGGER.info("Root element "
					+ doc.getDocumentElement().getNodeName());
			NodeList nodeLst = doc.getElementsByTagName("candidates");
			LOGGER.info("Information of all candidates:");
			String country = "US";
			GeocodedAddress gAddr = null;
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				gAddr = new GeocodedAddress();
				Element fstElmnt = (Element) fstNode;

				street = "";
				intStreet = "";
				city = "";
				state = "";
				zip = "";
				scoreStr = "";

				// LOCATION
				NodeList locationList = fstElmnt
						.getElementsByTagName("location");
				Element fstNmElmnt = (Element) locationList.item(0);
				NodeList nodeY = fstNmElmnt.getElementsByTagName("y");
				y = nodeY.item(0).getTextContent();
				LOGGER.info("y = " + y);
				NodeList nodeX = fstNmElmnt.getElementsByTagName("x");
				x = nodeX.item(0).getTextContent();
				LOGGER.info("x = " + x);
				
				gAddr.setCountry(country);
				gAddr.setX(x);
				gAddr.setY(y);

				// ADDRESS
				NodeList addressList = fstElmnt.getElementsByTagName("address");

				addressName = addressList.item(0).getTextContent();
				LOGGER.info("addressName = " + addressName);
				String[] addressParts = addressName.split(",");
				int count = 0;
				for(String addr:addressParts){
					count++;
					if(count == 1){
						street = addr.trim();
					}else if(count == 2){
						city = addr.trim();
					}else if(count == 3){
						state = addr.trim();
					}else if(count == 4){
						zip = addr.trim();
					}
				}
				

				// SCORE
				NodeList scoreList = fstElmnt.getElementsByTagName("score");
				scoreStr = scoreList.item(0).getTextContent();
				LOGGER.info("score = " + scoreStr);

				// NOW ADD THIS RESULT TO THE OUTPUT pos
				respAddr = new Address();
				respAddr.setStreet(street);
				respAddr.setMunicipality(city);
				respAddr.setPostalCode(zip);
				respAddr.setCountrySubdivision(state);
				respAddr.setIntersection(intStreet);

				gAddr.setAddress(respAddr);
				gAddr.setScore(new Double(scoreStr));
				gAddrList.add(gAddr);
			}
			return gAddrList;
		} catch (Exception p_e) {
			Logger.getLogger("Caught Exception" + p_e.getLocalizedMessage());
		}
		return null;
	}

}
