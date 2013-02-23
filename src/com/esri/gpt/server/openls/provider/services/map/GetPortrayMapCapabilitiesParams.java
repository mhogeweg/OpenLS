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

import com.esri.gpt.server.openls.provider.components.Params;

public class GetPortrayMapCapabilitiesParams extends Params {

	private HashMap<String,String> layerMap;
	private ArrayList<String> styleList;
	private ArrayList<String> formatList;
	private ArrayList<String> srsList;
	private String srsName;
	private String  requestId;
	
	
	/**
	 * @return the styleList
	 */
	public ArrayList<String> getStyleList() {
		return styleList;
	}
	/**
	 * @param styleList the styleList to set
	 */
	public void setStyleList(ArrayList<String> styleList) {
		this.styleList = styleList;
	}
	/**
	 * @return the formatList
	 */
	public ArrayList<String> getFormatList() {
		return formatList;
	}
	/**
	 * @param formatList the formatList to set
	 */
	public void setFormatList(ArrayList<String> formatList) {
		this.formatList = formatList;
	}
	/**
	 * @return the srsList
	 */
	public ArrayList<String> getSrsList() {
		return srsList;
	}
	/**
	 * @param srsList the srsList to set
	 */
	public void setSrsList(ArrayList<String> srsList) {
		this.srsList = srsList;
	}
	/**
	 * @param srsName the srsName to set
	 */
	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}
	/**
	 * @return the srsName
	 */
	public String getSrsName() {
		return srsName;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param layerList the layerList to set
	 */
	public void setLayerMap(HashMap<String,String> layerList) {
		this.layerMap = layerList;
	}
	/**
	 * @return the layerList
	 */
	public HashMap<String,String> getLayerMap() {
		return layerMap;
	}
	
}
