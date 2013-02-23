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

import java.util.HashMap;

import com.esri.gpt.server.openls.provider.util.Point;

public class RoutePlan {

	private String routePreference = "Fastest";
	private HashMap <String,Point> wayPointList= null;
	private HashMap <String,Point> avoidPointList= null;
	/**
	 * @return the routePreference
	 */
	public String getRoutePreference() {
		return routePreference;
	}
	/**
	 * @param routePreference the routePreference to set
	 */
	public void setRoutePreference(String routePreference) {
		this.routePreference = routePreference;
	}
	/**
	 * @return the wayPointList
	 */
	public HashMap<String, Point> getWayPointList() {
		return wayPointList;
	}
	/**
	 * @param wayPointList the wayPointList to set
	 */
	public void setWayPointList(HashMap<String, Point> wayPointList) {
		this.wayPointList = wayPointList;
	}
	/**
	 * @param avoidPointList the avoidPointList to set
	 */
	public void setAvoidPointList(HashMap <String,Point> avoidPointList) {
		this.avoidPointList = avoidPointList;
	}
	/**
	 * @return the avoidPointList
	 */
	public HashMap <String,Point> getAvoidPointList() {
		return avoidPointList;
	}


}
