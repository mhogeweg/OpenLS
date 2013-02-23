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
package com.esri.gpt.server.openls.provider.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Point {
	@SuppressWarnings("unused")
	static private Log logger = LogFactory.getLog(Point.class);
	private String m_strX;
	private String m_strY;
	private double m_X = Double.MAX_VALUE;
	private double m_Y = Double.MAX_VALUE;
	private String m_overlaySrs = "";

	public Point() {

	}

	public Point(double p_X, double p_Y) {
		setX(p_X);
		setY(p_Y);
	}

	public Point(String p_strX, String p_strY) throws NumberFormatException {
		Double.valueOf(p_strX);
		Double.valueOf(p_strY);
		setX(p_strX);
		setY(p_strY);
	}

	public void setX(double p_X) {
		m_X = p_X;
		m_strX = "" + p_X;
	}

	public void setY(double p_Y) {
		m_Y = p_Y;
		m_strY = "" + p_Y;
	}

	public void setX(String p_strX) {
		m_strX = p_strX;
		m_X = Double.valueOf(p_strX).doubleValue();
	}

	public void setY(String p_strY) {
		m_strY = p_strY;
		m_Y = Double.valueOf(p_strY).doubleValue();
	}

	public double getX() {
		return m_X;
	}

	public String getStrX() {
		return m_strX;
	}

	public double getY() {
		return m_Y;
	}

	public String getStrY() {
		return m_strY;
	}

	public void setCoord(String p_coord) {
		m_strX = p_coord.substring(0, p_coord.indexOf(","));
		m_X = Double.parseDouble(m_strX);
		m_strY = p_coord.substring(p_coord.indexOf(",") + 1, p_coord.length());
		m_Y = Double.parseDouble(m_strY);
	}

	public String getCoord() {
		return m_strX + "," + m_strY;
	}

	public void setGmlPos(String p_coord) {
		m_strX = p_coord.substring(0, p_coord.indexOf(" "));
		m_X = Double.parseDouble(m_strX);
		m_strY = p_coord.substring(p_coord.indexOf(" ") + 1, p_coord.length());
		m_Y = Double.parseDouble(m_strY);
	}

	public String getGmlPos() {
		return m_strX + " " + m_strY;
	}

	public void setOverlaySrs(String srs) {
		if (srs.indexOf("#") != -1)
			srs = srs.substring(srs.indexOf("#") + 1);
		m_overlaySrs = srs;
	}

	public String getOverlaySrs() {
		return m_overlaySrs;
	}

}