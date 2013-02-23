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
import java.util.HashMap;

import com.esri.gpt.server.openls.provider.components.Params;

/**
 * Options associated with a XLS DirectoryParams request.
 * <p>
 */
public class DirectoryParams extends Params {
  
  /** instance variables ====================================================== */
	private String maximumResponses="5"; 
	private String requestID="";
	private HashMap<String,Object> poiLocations = null;
	private HashMap<String,String> poiProperties = null;
	private ArrayList<POIContext> poiContexts = null;
  /** constructors ============================================================ */
  
  /** Default constructor */
  public DirectoryParams() {
    super();
  }

  /** properties ============================================================== */
  
/**
 * @return the maximumResponses
 */
public String getMaximumResponses() {
	return maximumResponses;
}

/**
 * @return the poiContexts
 */
public ArrayList<POIContext> getPoiContexts() {
	return poiContexts;
}

/**
 * @param poiContexts the poiContexts to set
 */
public void setPoiContexts(ArrayList<POIContext> poiContexts) {
	this.poiContexts = poiContexts;
}

/**
 * @param maximumResponses the maximumResponses to set
 */
public void setMaximumResponses(String maximumResponses) {
	this.maximumResponses = maximumResponses;
}

/**
 * @return the requestID
 */
public String getRequestID() {
	return requestID;
}

/**
 * @param requestID the requestID to set
 */
public void setRequestID(String requestID) {
	this.requestID = requestID;
}

/**
 * @return the poiLocations
 */
public HashMap<String, Object> getPoiLocations() {
	return poiLocations;
}

/**
 * @param poiLocations the poiLocations to set
 */
public void setPoiLocations(HashMap<String, Object> poiLocations) {
	this.poiLocations = poiLocations;
}

/**
 * @return the poiProperties
 */
public HashMap<String, String> getPoiProperties() {
	return poiProperties;
}

/**
 * @param poiProperties the poiProperties to set
 */
public void setPoiProperties(HashMap<String, String> poiProperties) {
	this.poiProperties = poiProperties;
}
    
}
