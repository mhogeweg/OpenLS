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

import com.esri.gpt.server.openls.provider.components.Params;
import com.esri.gpt.server.openls.provider.util.BaseMap;
import com.esri.gpt.server.openls.provider.util.ImageOutput;

public class PortrayMapParams extends Params {

	private ArrayList<ImageOutput> imgOutputList;
	private BaseMap baseMap;
	private String transparent;
	private String srsName;
	private String  requestId;

	
	/**
	 * @param transparent the transparent to set
	 */
	public void setTransparent(String transparent) {
		this.transparent = transparent;
	}
	/**
	 * @return the transparent
	 */
	public String getTransparent() {
		return transparent;
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
	 * @param imgOutput the imgOutput to set
	 */
	public void setImgOutputList(ArrayList<ImageOutput> imgOutput) {
		this.imgOutputList = imgOutput;
	}
	/**
	 * @return the imgOutput
	 */
	public ArrayList<ImageOutput> getImgOutputList() {
		return imgOutputList;
	}
	/**
	 * @param baseMap the baseMap to set
	 */
	public void setBaseMap(BaseMap baseMap) {
		this.baseMap = baseMap;
	}
	/**
	 * @return the baseMap
	 */
	public BaseMap getBaseMap() {
		return baseMap;
	}
}
