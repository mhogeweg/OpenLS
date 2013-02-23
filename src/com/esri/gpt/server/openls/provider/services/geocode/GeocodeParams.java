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
package com.esri.gpt.server.openls.provider.services.geocode;

import java.util.ArrayList;

import com.esri.gpt.server.openls.provider.components.Params;
import com.esri.gpt.server.openls.provider.util.Address;
import com.esri.gpt.server.openls.provider.util.GeocodedAddress;

public class GeocodeParams extends Params {

	private String srsName;
	private ArrayList<Address> addresses = new ArrayList<Address>();

	private String requestId;
	private ArrayList<GeocodedAddress> gAddrList;

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
	 * @param gAddrList the gAddrList to set
	 */
	public void setgAddrList(ArrayList<GeocodedAddress> gAddrList) {
		this.gAddrList = gAddrList;
	}

	/**
	 * @return the gAddrList
	 */
	public ArrayList<GeocodedAddress> getgAddrList() {
		return gAddrList;
	}
	public ArrayList<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
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
}
