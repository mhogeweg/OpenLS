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
import java.util.logging.Logger;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;


/**
 * Generates a ReverseGeocode response.
 */
public class ReverseGeocodeResponse implements IResponseGenerator {
    
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(ReverseGeocodeResponse.class.getName());
  
  /** constructors ============================================================ */
  
  /** Default constructor */
  public ReverseGeocodeResponse() {
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
    LOGGER.finer("Generating reverse geocoding response...");
    ReverseGeocodeParams rOptions = context.getRequestOptions().getReverseGeocodeOptions();
     
    String xlsNamespace = "http://www.opengis.net/xls";
    String gmlNamespace = "http://www.opengis.net/gml";
    String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
    String xsdNamespace = "http://schemas.opengis.net/ols/1.1.0/LocationUtilityService.xsd";
    String version = "1.1";
    String requestId = rOptions.getRequestId();
    
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
    sb.append("\r\n<xls:ResponseHeader/>");
    sb.append("\r\n<xls:Response");
    if ((requestId != null) && (requestId.length() > 0)) {
      sb.append(" requestId=\"").append(Val.escapeXml(requestId)).append("\"");
    }
    sb.append(">");
    sb.append("\r\n<xls:ReverseGeocodeResponse>");
    sb.append("\r\n<xls:ReverseGeocodedLocation>");
    sb.append("\r\n<gml:Point>");
    sb.append("\r\n<gml:pos>").append(rOptions.getgAddr().getStrX()).append(" ").append(rOptions.getgAddr().getStrY()).append("</gml:pos>");
    sb.append("\r\n</gml:Point>");

    sb.append("\r\n<xls:Address countryCode=\"US\">");			
    sb.append("\r\n<xls:Place type=\"Street\">").append(rOptions.getgAddr().getStreet()).append("</xls:Place>");
    sb.append("\r\n<xls:Place type=\"Municipality\">").append(rOptions.getgAddr().getMunicipality()).append("</xls:Place>");
    sb.append("\r\n<xls:Place type=\"CountrySubdivision\">").append(rOptions.getgAddr().getCountrySubdivision()).append("</xls:Place>");
    sb.append("\r\n<xls:Place type=\"CountrySecondarySubdivision\">").append(rOptions.getgAddr().getPostal()).append("</xls:Place>");
    sb.append("\r\n</xls:Address>");

    sb.append("\r\n</xls:ReverseGeocodedLocation>");
    sb.append("\r\n</xls:ReverseGeocodeResponse>");
    
    sb.append("\r\n</xls:Response>");
    sb.append("\r\n</xls:XLS>");
    
    context.getOperationResponse().setOutputFormat("text/xml");   
    context.getOperationResponse().setResponseXml(sb.toString().trim()); 
  }
    
}
