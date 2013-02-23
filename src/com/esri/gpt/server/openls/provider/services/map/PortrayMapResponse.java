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
import java.util.logging.Logger;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.util.BBoxContext;
import com.esri.gpt.server.openls.provider.util.ImageOutput;
import com.esri.gpt.server.openls.provider.util.Output;

/**
 * Generates a Openls PortrayMap response.
 */
public class PortrayMapResponse implements IResponseGenerator {
  
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(PortrayMapResponse.class.getName());
  
  /** constructors ============================================================ */
  
  /** Default constructor */
  public PortrayMapResponse() {}
          
  /** methods ================================================================= */
  
  /**
   * Generates the response.
   * @param context the operation context
   * @throws Exception if a processing exception occurs
   */
  public void generateResponse(OperationContext context) throws Exception {
    
    // initialize
    LOGGER.finer("Generatiing xls:PortrayMapResponse ...");
    PortrayMapParams params = context.getRequestOptions().getPortrayMapOptions();
   // ServiceProperties svcProps = context.getServiceProperties();   
        
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
	    sb.append("\r\n<xls:PortrayMapResponse>");
	    
	    ArrayList<ImageOutput> imgOutputList = params.getImgOutputList();
	    if(imgOutputList != null){
	    	for(ImageOutput imgOutput:imgOutputList){
	    		
			    BBoxContext bbox = imgOutput.getBboxContext();
			    Output output = imgOutput.getOutput();
			    
			    sb.append("\r\n<xls:Map>");
			    sb.append("\r\n<xls:Content format=\"").append(output.getFormat()).append("\" width=\"").append(output.getWidth())
			    .append("\" height=\"").append(output.getHeight()).append("\">");			
			    sb.append("\r\n<xls:URL>").append(Val.escapeXml(imgOutput.getUrl())).append("</xls:URL>");
			    sb.append("\r\n</xls:Content>");
			
			    sb.append("\r\n<gml:BBoxContext srsName=\"").append(params.getSrsName()).append("\">");
			    sb.append("\r\n<gml:pos>").append(bbox.getMinX()).append(" ").append(bbox.getMinY()).append("</gml:pos>");
			    sb.append("\r\n<gml:pos>").append(bbox.getMaxX()).append(" ").append(bbox.getMaxY()).append("</gml:pos>");
			    sb.append("\r\n</gml:BBoxContext>");
			    
			    sb.append("\r\n</xls:Map>");
	    	}
	    }
	    
	    sb.append("\r\n</xls:PortrayMapResponse>");
    }
    sb.append("\r\n</xls:Response>");
    sb.append("\r\n</xls:XLS>");
    
    context.getOperationResponse().setOutputFormat("text/xml");   
    context.getOperationResponse().setResponseXml(sb.toString().trim());  
  }
  
}
