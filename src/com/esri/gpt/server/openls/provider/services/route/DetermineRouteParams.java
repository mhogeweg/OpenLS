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

/**
 * Options associated with a CSW DescribeRecord request.
 */
public class DetermineRouteParams {
  
  /** instance variables ====================================================== */
  private String requestId;
  private RoutePlan routePlan;
  private RouteInstructionsRequest routeInstructions;
  private RouteMap routeMap;
  private RouteSummary routeSummary;
  private RouteInstructionsList routeInstList;
	
  /** constructors ============================================================ */
  
  /** properties ============================================================== */

  
  /** Default constructor */
  public DetermineRouteParams() {
    super();
  }

/**
 * @return the routeSummary
 */
public RouteSummary getRouteSummary() {
	return routeSummary;
}

/**
 * @param routeSummary the routeSummary to set
 */
public void setRouteSummary(RouteSummary routeSummary) {
	this.routeSummary = routeSummary;
}

/**
 * @return the routeInstList
 */
public RouteInstructionsList getRouteInstList() {
	return routeInstList;
}

/**
 * @param routeInstList the routeInstList to set
 */
public void setRouteInstList(RouteInstructionsList routeInstList) {
	this.routeInstList = routeInstList;
}

/**
 * @return the requestId
 */
public String getRequestId() {
	return requestId;
}

/**
 * @param requestId the requestId to set
 */
public void setRequestId(String requestId) {
	this.requestId = requestId;
}

/**
 * @return the routeMap
 */
public RouteMap getRouteMap() {
	return routeMap;
}

/**
 * @param routeMap the routeMap to set
 */
public void setRouteMap(RouteMap routeMap) {
	this.routeMap = routeMap;
}

/**
 * @return the routePlan
 */
public RoutePlan getRoutePlan() {
	return routePlan;
}

/**
 * @param routePlan the routePlan to set
 */
public void setRoutePlan(RoutePlan routePlan) {
	this.routePlan = routePlan;
}

/**
 * @return the routeInstructions
 */
public RouteInstructionsRequest getRouteInstructions() {
	return routeInstructions;
}

/**
 * @param routeInstructions the routeInstructions to set
 */
public void setRouteInstructions(RouteInstructionsRequest routeInstructions) {
	this.routeInstructions = routeInstructions;
}

}
