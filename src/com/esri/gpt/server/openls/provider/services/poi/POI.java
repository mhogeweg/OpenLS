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

import com.esri.gpt.server.openls.provider.util.Point;

public class POI {
	private String poiName = "";
	private String id = "";
	private Point location = null;
	private POIInfoList poiAttributeList = null;
	/**
	 * @return the poiName
	 */
	public String getPoiName() {
		return poiName;
	}
	/**
	 * @return the poiAttributeList
	 */
	public POIInfoList getPoiAttributeList() {
		return poiAttributeList;
	}
	/**
	 * @param poiName the poiName to set
	 */
	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * @param poiInfoList the poiAttributeList to set
	 */
	public void setPoiAttributeList(POIInfoList poiInfoList) {
		this.poiAttributeList = poiInfoList;
	}


}
