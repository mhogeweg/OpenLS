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
package com.esri.gpt.server.openls.provider.services.reversegeocode;

import com.esri.gpt.server.openls.provider.components.Params;
import com.esri.gpt.server.openls.provider.util.GeocodedAddress;

public class ReverseGeocodeParams extends Params {

	private String lat;
	private String lng;
	private String preference;
	private String  requestId;
	private GeocodedAddress gAddr;

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
	 * @param gAddr the gAddr to set
	 */
	public void setgAddr(GeocodedAddress gAddr) {
		this.gAddr = gAddr;
	}

	/**
	 * @return the gAddr
	 */
	public GeocodedAddress getgAddr() {
		return gAddr;
	}


	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getPreference() {
		return preference;
	}
	public void setPreference(String preference) {
		this.preference = preference;
	}
}
