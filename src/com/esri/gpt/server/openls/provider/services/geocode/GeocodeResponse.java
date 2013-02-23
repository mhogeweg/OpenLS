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
import java.util.ArrayList;
import java.util.logging.Logger;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.util.Address;
import com.esri.gpt.server.openls.provider.util.GeocodedAddress;

/**
 * Generates a CSW Transaction response.
 */
public class GeocodeResponse implements IResponseGenerator {
    
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(GeocodeResponse.class.getName());
  
  /** constructors ============================================================ */
  
  /** Default constructor */
  public GeocodeResponse() {
    super();
  }
          
  /** methods ================================================================= */
  
  /**
   * Generates the response.
   * @param context the operation context
   * @throws Exception if a processing exception occurs
   */
  public void generateResponse(OperationContext context) throws Exception {
    
    // initialize
    LOGGER.finer("Generating csw:Transaction response...");
    GeocodeParams gOptions = context.getRequestOptions().getGeocodeOptions();
    ArrayList<GeocodedAddress> gAddrList = gOptions.getgAddrList();
    String xlsNamespace = "http://www.opengis.net/xls";
    String gmlNamespace = "http://www.opengis.net/gml";
    String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
    String xsdNamespace = "http://schemas.opengis.net/ols/1.1.0/LocationUtilityService.xsd";
    String version = "1.1";
    String requestId = gOptions.getRequestId();
    
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    sb.append("\r\n<xls:XLS");
    sb.append(" xmlns:xls=\"").append(xlsNamespace).append("\"");
    sb.append(" xmlns:gml=\"").append(gmlNamespace).append("\"");
    sb.append(" xmlns:xsi=\"").append(xsiNamespace).append("\"");
    sb.append(" xsi:noNamespaceSchemaLocation=\"").append(xsdNamespace).append("\"");
    
    if ((version != null) && (version.length() > 0)) {
      sb.append(" version=\"").append(Val.escapeXml(version)).append("\"");
    }
    sb.append(">");
    sb.append("<xls:ResponseHeader/>");
    sb.append("\r\n<xls:Response");
    if ((requestId != null) && (requestId.length() > 0)) {
      sb.append(" requestId=\"").append(Val.escapeXml(requestId)).append("\"");
    }
    sb.append(">");
    sb.append("\r\n<xls:GeocodeResponse>");
    sb.append("\r\n<xls:GeocodeResponseList numberOfGeocodedAddresses=\"").append(gAddrList.size()).append("\">");
    
    for(GeocodedAddress gAddr:gAddrList){
    	Address addr = gAddr.getAddress();
	    sb.append("\r\n<xls:GeocodedAddress>");
	    
	    sb.append("\r\n<gml:Point>");
	    	sb.append("\r\n<gml:pos>").append(gAddr.getStrX()).append(" ").append(gAddr.getStrY()).append("</gml:pos>");
	    sb.append("\r\n</gml:Point>");
	
	    sb.append("\r\n<xls:Address countryCode=\"US\">");			
	    sb.append("\r\n<xls:Place type=\"Street\">").append(addr.getStreet()).append("</xls:Place>");
	    sb.append("\r\n<xls:Place type=\"Municipality\">").append(addr.getMunicipality()).append("</xls:Place>");
	    sb.append("\r\n<xls:Place type=\"CountrySubdivision\">").append(addr.getCountrySubdivision()).append("</xls:Place>");
	    sb.append("\r\n<xls:Place type=\"CountrySecondarySubdivision\">").append(addr.getPostalCode()).append("</xls:Place>");
	    sb.append("\r\n</xls:Address>");
	    
	    sb.append("\r\n<xls:GeocodeMatchCode accuracy=\"").append(gAddr.getScore()).append("\"/>");

	    sb.append("\r\n</xls:GeocodedAddress>");
    }
    
    sb.append("\r\n</xls:GeocodeResponseList>");
    sb.append("\r\n</xls:GeocodeResponse>");    
    sb.append("\r\n</xls:Response>");
    sb.append("\r\n</xls:XLS>");
    
    context.getOperationResponse().setOutputFormat("text/xml");   
    context.getOperationResponse().setResponseXml(sb.toString().trim()); 
  }
    
}
