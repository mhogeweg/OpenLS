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
package com.esri.gpt.server.openls.provider.components;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import com.esri.gpt.server.openls.provider.services.geocode.GeocodeParams;
import com.esri.gpt.server.openls.provider.services.map.GetPortrayMapCapabilitiesParams;
import com.esri.gpt.server.openls.provider.services.map.PortrayMapParams;
import com.esri.gpt.server.openls.provider.services.poi.DirectoryParams;
import com.esri.gpt.server.openls.provider.services.reversegeocode.ReverseGeocodeParams;
import com.esri.gpt.server.openls.provider.services.route.DetermineRouteParams;

/**
 * Options associated with a CSW operation request.
 */
public class RequestOptions {
  
  /** instance variables ====================================================== */
  private Map<String,Object>    additionalOptions = new HashMap<String,Object>();
  private GetPortrayMapCapabilitiesParams getPortrayMapCapabilitiesOptions = new GetPortrayMapCapabilitiesParams();
  private PortrayMapParams      portrayMapOptions = new PortrayMapParams();
  private DetermineRouteParams 	determineRouteParams = new DetermineRouteParams();
  private DirectoryParams       directoryOptions = new DirectoryParams();  
  private GeocodeParams 		geocodeOptions = new GeocodeParams();
  private ReverseGeocodeParams  reverseGeocodeOptions = new ReverseGeocodeParams();
  private Document              requestDom;
  private String                requestXml;
  /** constructors ============================================================ */
  
  /** Default constructor */
  public RequestOptions() {
    super();
  }
  
  /** properties ============================================================== */
  
  public DetermineRouteParams getDetermineRouteParams() {
		return determineRouteParams;
	}

	public void setDetermineRouteParams(DetermineRouteParams determineRouteParams) {
		this.determineRouteParams = determineRouteParams;
	}

	public DirectoryParams getDirectoryOptions() {
		return directoryOptions;
	}

	public void setDirectoryOptions(DirectoryParams directoryOptions) {
		this.directoryOptions = directoryOptions;
	}

	public GeocodeParams getGeocodeOptions() {
		return geocodeOptions;
	}

	public void setGeocodeOptions(GeocodeParams geocodeOptions) {
		this.geocodeOptions = geocodeOptions;
	}

	public ReverseGeocodeParams getReverseGeocodeOptions() {
		return reverseGeocodeOptions;
	}

	public void setReverseGeocodeOptions(ReverseGeocodeParams reverseGeocodeOptions) {
		this.reverseGeocodeOptions = reverseGeocodeOptions;
	}

	
  /**
   * Gets the free form map of additional options.
   * @return the additional options
   */
  public Map<String,Object> getAdditionalOptions() {
    return this.additionalOptions;
  }
  /**
   * Sets the free form map of additional options.
   * @param additionalOptions the additional options
   */
  public void setAdditionalOptions(Map<String,Object> additionalOptions) {
    this.additionalOptions = additionalOptions;
  }

  /**
   * Gets the DescribeRecord request options.
   * @return the DescribeRecord options
   */
  public DetermineRouteParams getDescribeRecordOptions() {
    return this.determineRouteParams;
  }
  /**
   * Sets the DescribeRecord request options.
   * @param options the DescribeRecord options
   */
  public void setDescribeRecordOptions(DetermineRouteParams options) {
    this.determineRouteParams = options;
  }
    
  
  /**
   * Gets the XML request document.
   * @return the XML request document (can be null)
   */
  public Document getRequestDom() {
    return this.requestDom;
  }
  /**
   * Sets the XML request document.
   * @param requestDom the XML request document
   */
  public void setRequestDom(Document requestDom) {
    this.requestDom = requestDom;
  }
    
  /**
   * Gets the request XML.
   * @return the request XML (can be null)
   */
  public String getRequestXml() {
    return this.requestXml;
  }
  /**
   * Sets the request XML.
   * @param xml the request XML (can be null)
   */
  public void setRequestXml(String xml) {
    this.requestXml = xml;
  }

/**
 * @param portrayMapOptions the portrayMapOptions to set
 */
public void setPortrayMapOptions(PortrayMapParams portrayMapOptions) {
	this.portrayMapOptions = portrayMapOptions;
}

/**
 * @return the portrayMapOptions
 */
public PortrayMapParams getPortrayMapOptions() {
	return portrayMapOptions;
}

/**
 * @param getPortrayMapCapabilitiesOptions the getPortrayMapCapabilitiesOptions to set
 */
public void setGetPortrayMapCapabilitiesOptions(
		GetPortrayMapCapabilitiesParams getPortrayMapCapabilitiesOptions) {
	this.getPortrayMapCapabilitiesOptions = getPortrayMapCapabilitiesOptions;
}

/**
 * @return the getPortrayMapCapabilitiesOptions
 */
public GetPortrayMapCapabilitiesParams getGetPortrayMapCapabilitiesOptions() {
	return getPortrayMapCapabilitiesOptions;
}

            
}
