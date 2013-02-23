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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.openls.provider.components.IOperationProvider;
import com.esri.gpt.server.openls.provider.components.IProviderFactory;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.util.BBoxContext;
import com.esri.gpt.server.openls.provider.util.BaseMap;
import com.esri.gpt.server.openls.provider.util.ImageOutput;
import com.esri.gpt.server.openls.provider.util.Layer;
import com.esri.gpt.server.openls.provider.util.Output;

/**
 * Provides the Openls PortrayMap operation.
 */
public class PortrayMapProvider implements IOperationProvider {
  
  /** class variables ========================================================= */
  
  /** The Logger. */
  private static Logger LOGGER = Logger.getLogger(PortrayMapProvider.class.getName());
    
  /** constructors ============================================================ */
  
  /** Default constructor */
  public PortrayMapProvider() {
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
    LOGGER.finer("Handling xls:PortrayMapRequest request XML...");
    String locator="xls:PortrayMapRequest";
    PortrayMapParams params = null;
    Node ndReq = (Node)xpath.evaluate(locator,root,XPathConstants.NODE);
    if (ndReq != null) {    	
    	params = parseRequest(context, root,ndReq, xpath);
	    try {
	    	context.getRequestOptions().setPortrayMapOptions(params);
			executeRequest(context);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
	generateResponse(context);
  }
  
  /**
   * Parses portray map request
   * @param context
   * @param root
   * @param ndReq
   * @param xpath
   * @return
   * @throws XPathExpressionException
   */
  private PortrayMapParams parseRequest(OperationContext context, Node root,Node ndReq, XPath xpath) throws XPathExpressionException{
	PortrayMapParams params = new PortrayMapParams();   
  	Node ndXls = root.getParentNode();
  	if(ndXls!= null){
	    	Node ndReqHdr = (Node)xpath.evaluate("xls:RequestHeader",ndXls,XPathConstants.NODE);
	    	if(ndReqHdr != null){
	    		String srsName = (String)xpath.evaluate("@srsName",ndReqHdr,XPathConstants.STRING);
	    		if(srsName != null){
	    			params.setSrsName(srsName);
	    		}
	    	}
  	}
  	
  	NodeList ndOutputList = (NodeList)xpath.evaluate("xls:Output",ndReq,XPathConstants.NODESET);
  	if(ndOutputList != null){
  		ArrayList<ImageOutput> imgOutputList = new ArrayList<ImageOutput>();
	  	for(int k=0; k < ndOutputList.getLength(); k++){
	  		Node ndOutput = ndOutputList.item(k);
	  		ImageOutput imgOutput = new ImageOutput();
	  		Output output = new Output();
	  		String format = (String)xpath.evaluate("@format",ndOutput,XPathConstants.STRING);
	  		if(format != null && format.contains("/")){
	  			format = format.substring(format.indexOf("/")+1);
	  		}
	  		output.setFormat(format);
	  		String content = (String)xpath.evaluate("@content",ndOutput,XPathConstants.STRING);
	  		output.setContent(content);
	  		String height = (String)xpath.evaluate("@height",ndOutput,XPathConstants.STRING);
	  		output.setHeight(height);
	  		String width = (String)xpath.evaluate("@width",ndOutput,XPathConstants.STRING);
	  		output.setWidth(width);
	  		imgOutput.setOutput(output);
	  		Node ndBbox = (Node)xpath.evaluate("xls:BBoxContext",ndOutput,XPathConstants.NODE);
	  		if(ndBbox != null){
	  			BBoxContext bbox = new BBoxContext();
	  			NodeList ndPos = (NodeList)xpath.evaluate("gml:pos",ndBbox,XPathConstants.NODESET);
	  			if(ndPos != null){
		    			for(int i=0; i<ndPos.getLength();i++) {
		    				Node pos = ndPos.item(i);
		    				String[] parts = pos.getTextContent().split(" ");
		    				if(i==0){	    					
		    					bbox.setMinX(parts[0]);
		    					bbox.setMinY(parts[1]);
		    				}else if(i==1){
		    					bbox.setMaxX(parts[0]);
		    					bbox.setMaxY(parts[1]);
		    				}
		    			}
	  			}
	  			imgOutput.setBboxContext(bbox);
	  		}
	  		imgOutputList.add(imgOutput);
	  	}
  		params.setImgOutputList(imgOutputList);
  		
  	}
  	
  	Node ndBaseMap = (Node)xpath.evaluate("xls:Basemap",ndReq,XPathConstants.NODE);
  	if(ndBaseMap != null){
  		BaseMap baseMap = new BaseMap();
  		baseMap.setFilter((String)xpath.evaluate("@filter",ndBaseMap,XPathConstants.STRING));  	 		
  		NodeList ndLayerList = (NodeList)xpath.evaluate("xls:Layer",ndBaseMap,XPathConstants.NODESET);
  	  	if(ndLayerList != null){
  	  		ArrayList<Layer> layerList = new ArrayList<Layer>();
  		  	for(int k=0; k < ndLayerList.getLength(); k++){
  		  		Layer layer = new Layer();  		  	
  		  		Node ndLayer = ndLayerList.item(k);
  		  		layer.setName((String)xpath.evaluate("@name",ndLayer,XPathConstants.STRING));
  		  	    Node ndStyle = (Node)xpath.evaluate("xls:Style/xls:Name",ndLayer,XPathConstants.NODE);
  		  	    if(ndStyle != null){
  		  	       layer.setStyleName(ndStyle.getTextContent());	
  		  	    }
  		  	    layerList.add(layer);
  		  	}
  		baseMap.setLayers(layerList);
  	  }
  	  params.setBaseMap(baseMap);
  	}
    return params;
  }

  /**
   * This method implements the doService() interface method
   *  bbox=-19259918.6611802%2C-2249065.94284002%2C-5607368.27645687%2C14909877.1636428
	&bboxSR=&layers=1%2C3&layerdefs=&size=600%2C600&imageSR=&format=jpg&transparent=false&dpi=&f=image";
   */
	private void executeRequest(OperationContext context) throws Throwable {
		long t1 = System.currentTimeMillis();
		
		PortrayMapParams params = context.getRequestOptions().getPortrayMapOptions();
		
		// Object[] json = new Object[2];

		String srvCfg = context.getRequestContext().getApplicationConfiguration()
	  	  .getCatalogConfiguration().getParameters().getValue("openls.portrayMap");
		
		ArrayList<ImageOutput> imgOutputList = params.getImgOutputList();
		for(ImageOutput imgOutput:imgOutputList){

			// MAKE THE JSON REQUEST
			String sMapRequest = makeExportMapRequest(context,imgOutput);
			sMapRequest = srvCfg + "/export?" + sMapRequest; 
	
			// SET THE BOUNDING BOX
			/*BoundingBox envelope = new BoundingBox();
			BBoxContext bboxCtx = imgOutput.getBboxContext();
			envelope.setMinX(bboxCtx.getMinX());
			envelope.setMinY(bboxCtx.getMinY());
			envelope.setMaxX(bboxCtx.getMaxX());
			envelope.setMaxY(bboxCtx.getMaxY());
			json[1] = envelope;*/
			
			//response.setPointList(((ISMapRequest) m_request).getAcetatePoints());
			imgOutput.setUrl(sMapRequest);
			LOGGER.info("Map URL = " + sMapRequest);
		}

		//BoundingBox bbox = (BoundingBox) json[1];

		/*if (m_srsName != null && !m_srsName.equalsIgnoreCase(""))
			bbox.setBoxSrs(((ISMapRequest) getRequest()).getSrsName());

		response.setEnvelope(bbox);
		response.setMapImageType(((ISMapRequest) m_request).getImageType());
		response.setMapImageWidth(Integer.parseInt(((ISMapRequest) m_request)
				.getImageWidth()));
		response.setMapImageHeight(Integer.parseInt(((ISMapRequest) m_request)
				.getImageHeight()));
		response.setVersion(m_version);
		setResponse(response);*/

		long t2 = System.currentTimeMillis();
		LOGGER.info("PERFORMANCE: " + (t2 - t1) + " ms spent performing service");
	}

	/**
	 * This method creates the export map request from a MapRequest object.
	 */
	private String makeExportMapRequest(OperationContext context,ImageOutput imgOutput) throws Throwable {

		PortrayMapParams params = context.getRequestOptions().getPortrayMapOptions();
		
		String requestParameters = "";
	//	m_version = request.getRequestVersion();
	//	m_srsName = request.getSrsName();

		/*
		 * use transparent background or not transparent = true | false
		 */
		if (params.getTransparent() != null
				&& !params.getTransparent().equalsIgnoreCase(""))
			requestParameters += "transparent="
					+ params.getTransparent().toLowerCase() + "&";

		BaseMap baseMap = params.getBaseMap();
		if(baseMap != null){
			String filter = Val.chkStr(baseMap.getFilter());
			if(filter.length() > 0){
				filter = filter.toLowerCase();
				filter = (filter == "exclude" ? "hide:" : "show:");
			}
			ArrayList<Layer> layers = baseMap.getLayers();
			GetPortrayMapCapabilitiesProvider gpmc = new GetPortrayMapCapabilitiesProvider();		
			gpmc.executeRequest(context);
			GetPortrayMapCapabilitiesParams gpmcParams = context.getRequestOptions().getGetPortrayMapCapabilitiesOptions();
			HashMap<String,String> layerMap = gpmcParams.getLayerMap();
			
			if(layers != null && layerMap != null){
				StringBuffer sb = new StringBuffer();
				for(int i=0; i<layers.size(); i++){
					Layer layer = layers.get(i);
					String name = Val.chkStr(layer.getName());
					if(name.length() > 0){
						if(sb.length() == 0){
							sb.append(filter).append(layerMap.get(name));
						}else{
							sb.append(",").append(layerMap.get(name));
						}
					}
				}
				if(sb.length() > 0){
					requestParameters += "layers=" + sb.toString() + "&";
				}
			}
		}
		
		
		
		
		Output output = imgOutput.getOutput();
		/*
		 * set the output format format = png | png8 | png24 | jpg | pdf | bmp | gif
		 * | svg | png32
		 */
		requestParameters += "format=" + output.getFormat() + "&";

		/*
		 * set the output size size = <width>, <height>
		 */
		requestParameters += "size=" + output.getWidth() + ","
				+ output.getHeight() + "&";

		BBoxContext bboxCtx = imgOutput.getBboxContext();
		String srsName = params.getSrsName();
		/*
		 * set the output coordinate reference system imageSR = <wellknown id>
		 */
		if (srsName != null && !srsName.equalsIgnoreCase("")) {			
			requestParameters += "imageSR=" + srsName + "&";
		}

		/*
		 * set the coordinate reference system for the bounding box bboxSR =
		 * <wellknown id>
		 * 
		 * use imageSR if the bboxSR is not set
		 */
		if (bboxCtx != null
				&& srsName == null)
			requestParameters += "bboxSR=4326"
					+ "&";
		else
			requestParameters += "bboxSR=4326&";

		/*
		 * set the bounding box bbox = <xmin>, <ymin>, <xmax>, <ymax>
		 * 
		 * request may contain a bbox, a center point and scale, or a center point
		 * and radius
		 */
		requestParameters += "bbox=";

		if (bboxCtx != null) {
			requestParameters += bboxCtx.getMinX();
			requestParameters += "," + bboxCtx.getMinY();
			requestParameters += "," + bboxCtx.getMaxX();
			requestParameters += "," + bboxCtx.getMaxY();

		} else {
			// add else part for center point

			/*if (bboxCtx. != null && request.getScale() > 0) {
				// center point and scale provided
				double widthScale = Double.parseDouble(request.getImageWidth()) / 2
						/ 96 * request.getScale();
				widthScale = Units.convert(widthScale, 2, 0);
				double heightScale = Double.parseDouble(request.getImageHeight()) / 2
						/ 96 * request.getScale();
				heightScale = Units.convert(heightScale, 2, 0);
				requestParameters += (request.getCenterPoint().getX() - widthScale);
				requestParameters += ","
						+ (request.getCenterPoint().getY() - heightScale);
				requestParameters += ","
						+ (request.getCenterPoint().getX() + widthScale);
				requestParameters += ","
						+ (request.getCenterPoint().getY() + heightScale);

			} else if (request.getCenterPoint() != null
					&& request.getRadius() != null) {
				// center point and radius provided
				requestParameters += (request.getCenterPoint().getX() - Integer
						.parseInt(request.getRadius()));
				requestParameters += ","
						+ (request.getCenterPoint().getY() - Integer.parseInt(request
								.getRadius()));
				requestParameters += ","
						+ (request.getCenterPoint().getX() + Integer.parseInt(request
								.getRadius()));
				requestParameters += ","
						+ (request.getCenterPoint().getY() + Integer.parseInt(request
								.getRadius()));

			} else {*/
				// all else failed: default to world as extent
				requestParameters += "-180,-90,180,90";
			//}
		}
		requestParameters += "&";

		/*
		 * set the layers to include layers= [show | hide | include |
		 * exclude]:layerId1,layerId2
		 */
		// TO DO

		/*
		 * close the request parameters with asking for the image as output as
		 * opposed to an HTML page...
		 */
		requestParameters += "&f=image";

		LOGGER.info("*******Map Request = " + requestParameters);
		return requestParameters;
	}
  
}
