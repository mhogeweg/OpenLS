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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;

/**
 * Generates a Openls PortrayMap response.
 */
public class GetPortrayMapCapabilitiesResponse implements IResponseGenerator {
  
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(GetPortrayMapCapabilitiesResponse.class.getName());
  
  /** constructors ============================================================ */
  
  /** Default constructor */
  public GetPortrayMapCapabilitiesResponse() {}
          
  /** methods ================================================================= */
  
  /**
   * Generates the response.
   * @param context the operation context
   * @throws Exception if a processing exception occurs
   */
  public void generateResponse(OperationContext context) throws Exception {
    
    // initialize
    LOGGER.finer("Generating xls:GetPortrayMapCapabilitiesResponse ...");
    GetPortrayMapCapabilitiesParams params = context.getRequestOptions().getGetPortrayMapCapabilitiesOptions();
    
    HashMap<String,String> layerList = params.getLayerMap();		
	ArrayList<String> formatList = params.getFormatList();
	ArrayList<String> srsList = params.getSrsList();
	ArrayList<String> styleList = params.getStyleList();
    
    String xlsNamespace = "http://www.opengis.net/xls";
    String gmlNamespace = "http://www.opengis.net/gml";
    String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
    String xsdNamespace = "http://schemas.opengis.net/ols/1.1.0/LocationUtilityService.xsd";
    String version = "1.1";
    String requestId = params.getRequestId();
    
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
    if(params != null){
	    sb.append("\r\n<xls:GetPortrayMapCapabilitiesResponse>");

	    if(srsList != null){
		    sb.append("\r\n<xls:AvailableCRS>");
		    for(String srs:srsList){
		    	 sb.append("\r\n<xls:CRS>");
		    	 sb.append(srs);
		    	 sb.append("\r\n</xls:CRS>");
		    }
			sb.append("\r\n</xls:AvailableCRS>");
	    }
	    if(layerList != null){
		    sb.append("\r\n<xls:AvailableLayers>");
		    String[] layers = layerList.keySet().toArray(new String[0]);
		    for(String layer: layers){
		    	 sb.append("\r\n<xls:Layer>");
		    	 sb.append(layer);
		    	 sb.append("\r\n</xls:Layer>");
		    }
		    sb.append("\r\n</xls:AvailableLayers>");
	    }
	    if(formatList != null){
		    sb.append("\r\n<xls:AvailableFormats>");
		    for(String format:formatList){
		    	 sb.append("\r\n<xls:Format>");
		    	 sb.append(format);
		    	 sb.append("\r\n</xls:Format>");
		    }
		    sb.append("\r\n</xls:AvailableFormats>");
	    }
	    if(styleList != null){
		    sb.append("\r\n<xls:AvailableStyles>");
		    for(String style:styleList){
		    	 sb.append("\r\n<xls:Style>");
		    	 sb.append(style);
		    	 sb.append("\r\n</xls:Style>");
		    }
		    sb.append("\r\n</xls:AvailableStyles>");
	    }
	    sb.append("\r\n</xls:GetPortrayMapCapabilitiesResponse>");
    }
    sb.append("\r\n</xls:Response>");
    sb.append("\r\n</xls:XLS>");
    
    context.getOperationResponse().setOutputFormat("text/xml");   
    context.getOperationResponse().setResponseXml(sb.toString().trim());  
  }
  
}
