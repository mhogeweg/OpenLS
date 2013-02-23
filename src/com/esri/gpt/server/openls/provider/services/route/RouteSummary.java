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

import com.esri.gpt.server.openls.provider.util.BBoxContext;

public class RouteSummary {
	private BBoxContext bbox;
	private String totalTime;
	private String totalDistance;
	private String totalDriveTime;

	/**
	 * @return the bbox
	 */
	public BBoxContext getBbox() {
		return bbox;
	}
	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(BBoxContext bbox) {
		this.bbox = bbox;
	}
	/**
	 * @return the totalTime
	 */
	public String getTotalTime() {
		return totalTime;
	}
	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	/**
	 * @return the totalDistance
	 */
	public String getTotalDistance() {
		return totalDistance;
	}
	/**
	 * @param totalDistance the totalDistance to set
	 */
	public void setTotalDistance(String totalDistance) {
		this.totalDistance = totalDistance;
	}
	/**
	 * @param totalDriveTime the totalDriveTime to set
	 */
	public void setTotalDriveTime(String totalDriveTime) {
		this.totalDriveTime = totalDriveTime;
	}
	/**
	 * @return the totalDriveTime
	 */
	public String getTotalDriveTime() {
		return totalDriveTime;
	}

}
