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

public class POIContext implements Comparable<Object> {

	private double distance;
	private POI point = null;
	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	/**
	 * @return the point
	 */
	public POI getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(POI point) {
		this.point = point;
	}
	@Override
	public int compareTo(Object o) {
		if(this.distance < ((POIContext)o).distance){
			return 0;
		}else{
			return 1;
		}
	}


}
