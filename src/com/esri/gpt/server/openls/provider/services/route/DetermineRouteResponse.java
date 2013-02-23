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
package com.esri.gpt.server.openls.provider.services.route;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.util.BBoxContext;
import com.esri.gpt.server.openls.provider.util.ImageOutput;
import com.esri.gpt.server.openls.provider.util.Output;

/**
 * Generates a CSW DescribeRecordResponse.
 */
public class DetermineRouteResponse implements IResponseGenerator {
  
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(DetermineRouteResponse.class.getName());
    
  /** constructors ============================================================ */
  
  /** Default constructor */
  public DetermineRouteResponse() {}
          
  /** methods ================================================================= */
  
  /**
   * Generates the response.
   * @param context the operation context
   * @throws Exception if a processing exception occurs
   */
  public void generateResponse(OperationContext context) throws Exception {
    
    /// initialize
    LOGGER.finer("Generating xls:DetermineRouteResponse...");
    DetermineRouteParams params = context.getRequestOptions().getDescribeRecordOptions(); 
    // ServiceProperties svcProps = context.getServiceProperties();       
    RouteMap routeMap = params.getRouteMap();
    RouteSummary routeSummary = params.getRouteSummary();
    RouteInstructionsList routeInstList = params.getRouteInstList();
    ImageOutput imgOutput = routeMap.getImageOutput();
    Output output = imgOutput.getOutput();
    ArrayList<RouteInstruction> routeInstructionList = null;
    
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
    sb.append("\r\n<xls:DetermineRouteResponse>");
    if(routeSummary != null){
	    sb.append("\r\n<xls:RouteSummary>");
	    sb.append("\r\n<xls:TotalTime>").append(routeSummary.getTotalDriveTime()).append("</xls:TotalTime>");
	    sb.append("\r\n<xls:TotalDistance value=\"").append(routeSummary.getTotalDistance()).append("\" uom=\"\" />");
	    BBoxContext bbox = routeSummary.getBbox();
	    if(bbox != null){
		    sb.append("\r\n<xls:BoundingBox>");
		    sb.append("\r\n<gml:pos>").append(bbox.getMinX()).append(" ").append(bbox.getMinY()).append("</gml:pos>");
		    sb.append("\r\n<gml:pos>").append(bbox.getMaxX()).append(" ").append(bbox.getMaxY()).append("</gml:pos>");
		    sb.append("\r\n</xls:BoundingBox>");
	    }
	    sb.append("\r\n</xls:RouteSummary>");
    }
    
    if(routeInstList != null){    	
	    sb.append("\r\n<xls:RouteInstructionsList xls:lang=");
	    if(routeInstList.getLanguage() != null){
	    	sb.append("\"").append(routeInstList.getLanguage()).append("\">");
	    }else{
	    	sb.append("\"En\">");
	    }
	    routeInstructionList = routeInstList.getRouteInstructionList();
		if(routeInstructionList != null){
			for(RouteInstruction rteInst:routeInstructionList){
			    sb.append("\r\n<xls:RouteInstruction duration=\"").append(rteInst.getDuration()).append("\" description=\"").append(rteInst.getDescription()).append("\">");
				sb.append("\r\n<xls:Instruction>").append(rteInst.getInstruction()).append("</xls:Instruction>");
				sb.append("\r\n<xls:distance value=\"").append(rteInst.getDistance()).append("\" uom=\"\"/>");
				sb.append("\r\n</xls:RouteInstruction>");
			}
		}
		sb.append("\r\n</xls:RouteInstructionsList>");
    }
	
	if(routeMap != null && output != null){
	    sb.append("\r\n<xls:RouteMap>");
	    sb.append("\r\n<xls:Content format=\"").append(output.getFormat()).append("\" width=\"").append(output.getWidth())
	    .append("\" height=\"").append(output.getHeight()).append("\">");	
	    if(imgOutput != null){
	    	sb.append("\r\n<xls:URL>").append((imgOutput.getUrl())).append("</xls:URL>");
	    }
	    sb.append("\r\n</xls:Content>");
	    sb.append("\r\n</xls:RouteMap>");
	}
    
    sb.append("\r\n</xls:DetermineRouteResponse>");
    
    sb.append("\r\n</xls:Response>");
    sb.append("\r\n</xls:XLS>");
    
    context.getOperationResponse().setOutputFormat("text/xml");   
    context.getOperationResponse().setResponseXml(sb.toString().trim());  
  }
  
}
