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

import java.util.ArrayList;

public class RouteInstructionsList {

	private String language;
	private ArrayList<RouteInstruction> routeInstructionList = null;
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * @return the routeInstrunctionList
	 */
	public ArrayList<RouteInstruction> getRouteInstructionList() {
		return routeInstructionList;
	}
	/**
	 * @param routeInstrunctionList the routeInstrunctionList to set
	 */
	public void setRouteInstrunctionList(
			ArrayList<RouteInstruction> routeInstructionList) {
		this.routeInstructionList = routeInstructionList;
	}


}
