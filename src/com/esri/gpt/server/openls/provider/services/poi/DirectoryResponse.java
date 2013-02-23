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
package com.esri.gpt.server.openls.provider.services.poi;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.util.Point;

/**
 * Generates a ReverseGeocode response.
 */
public class DirectoryResponse implements IResponseGenerator {
    
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(DirectoryResponse.class.getName());
  
  /** constructors ============================================================ */
  
  /** Default constructor */
  public DirectoryResponse() {
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
    LOGGER.finer("Generating directory response...");
    DirectoryParams dOptions = context.getRequestOptions().getDirectoryOptions();
     
    String xlsNamespace = "http://www.opengis.net/xls";
    String gmlNamespace = "http://www.opengis.net/gml";
    String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";
    String xsdNamespace = "http://schemas.opengis.net/ols/1.1.0/LocationUtilityService.xsd";
    String version = "1.1";
    String requestId = dOptions.getRequestID();
    ArrayList<POIContext> poiContexts = dOptions.getPoiContexts();
    
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
    sb.append("\r\n<xls:DirectoryResponse>");
    if(poiContexts != null){
	    for(POIContext poiCtx:poiContexts){
	    	POI poi = poiCtx.getPoint();
	    	Point pt = poi.getLocation();
	    	POIInfoList poiInfoList = poi.getPoiAttributeList();
		    sb.append("\r\n<xls:POIContext>");
		    sb.append("\r\n<xls:POI POIName=\"").append(poi.getPoiName()).append("\" ID=\"").append(poi.getId()).append("\">");
		    sb.append("\r\n<xls:POIAttributeList>");
		    if(poiInfoList!= null){	    	
			    sb.append("\r\n<xls:POIInfoList>");
			    for(POIInfo poiInfo: poiInfoList){
			    	sb.append("\r\n<xls:POIInfo name=\"").append(poiInfo.getName()).append("\" value=\"").append(poiInfo.getValue()).append("\"/>");
			    }
				sb.append("\r\n</xls:POIInfoList>");
		    }
		    sb.append("\r\n</xls:POIAttributeList>");
			sb.append("\r\n<gml:Point>");
		    sb.append("\r\n<gml:pos>").append(pt.getX()).append(" ").append(pt.getY()).append("</gml:pos>");
		    sb.append("\r\n</gml:Point>");
			sb.append("\r\n</xls:POI>");
		    sb.append("\r\n<xls:Distance value=\"").append(poiCtx.getDistance()).append("\"/>");
			sb.append("\r\n</xls:POIContext>");
	    }
    }
    
    sb.append("\r\n</xls:DirectoryResponse>");    
    sb.append("\r\n</xls:Response>");
    sb.append("\r\n</xls:XLS>");
    
    context.getOperationResponse().setOutputFormat("text/xml");   
    context.getOperationResponse().setResponseXml(sb.toString().trim()); 
  }
    
}
