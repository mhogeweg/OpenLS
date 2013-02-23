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

import java.util.Vector;

public class BBoxContext {

	protected double m_minx;
	protected double m_miny;
	protected double m_maxx;
	protected double m_maxy;
	private Vector<?> _pointVec;

	public BBoxContext() {
	}

	public BBoxContext(String p_minx, String p_miny, String p_maxx,
			String p_maxy) throws NumberFormatException {
		Double.valueOf(p_minx);
		Double.valueOf(p_miny);
		Double.valueOf(p_maxx);
		Double.valueOf(p_maxy);
		setMinX(p_minx);
		setMinY(p_miny);
		setMaxX(p_maxx);
		setMaxY(p_maxy);
	}

	public BBoxContext(double p_minx, double p_miny, double p_maxx,
			double p_maxy) {
		m_minx = p_minx;
		m_miny = p_miny;
		m_maxx = p_maxx;
		m_maxy = p_maxy;
	}

	public BBoxContext(String coords) {
		// TODO Auto-generated constructor stub
	}

	public void setMinX(double p_X) {
		m_minx = p_X;
	}

	public void setMinY(double p_Y) {
		m_miny = p_Y;
	}

	public void setMaxX(double p_X) {
		m_maxx = p_X;
	}

	public void setMaxY(double p_Y) {
		m_maxy = p_Y;
	}

	public void setMinX(String p_X) {
		m_minx = Double.valueOf(p_X).doubleValue();
	}

	public void setMinY(String p_Y) {
		m_miny = Double.valueOf(p_Y).doubleValue();
	}

	public void setMaxX(String p_X) {
		m_maxx = Double.valueOf(p_X).doubleValue();
	}

	public void setMaxY(String p_Y) {
		m_maxy = Double.valueOf(p_Y).doubleValue();
	}

	public void setUpperCorner(String p_coord) {
		m_minx = Double.parseDouble(p_coord.substring(0, p_coord.indexOf(",")));
		m_maxy = Double.parseDouble(p_coord.substring(p_coord.indexOf(",") + 1,
				p_coord.length()));
	}

	public void setLowerCorner(String p_coord) {
		m_maxx = Double.parseDouble(p_coord.substring(0, p_coord.indexOf(",")));
		m_miny = Double.parseDouble(p_coord.substring(p_coord.indexOf(",") + 1,
				p_coord.length()));
	}

	public String getUpperCorner() {
		return m_minx + "," + m_maxy;
	}

	public String getLowerCorner() {
		return m_maxx + "," + m_miny;
	}

	public double getMinX() {
		return m_minx;
	}

	public double getMinY() {
		return m_miny;
	}

	public double getMaxX() {
		return m_maxx;
	}

	public double getMaxY() {
		return m_maxy;
	}

	public String getStrMinX() {
		return "" + m_minx;
	}

	public String getStrMinY() {
		return "" + m_miny;
	}

	public String getStrMaxX() {
		return "" + m_maxx;
	}

	public String getStrMaxY() {
		return "" + m_maxy;
	}

	public void setPointVec(Vector<?> pointVec) {
		_pointVec = pointVec;
		GmlPos point = (GmlPos) _pointVec.get(0);
		String p_coord = point.getPoint();
		m_minx = Double.parseDouble(p_coord.substring(0, p_coord.indexOf(" ")));
		m_miny = Double.parseDouble(p_coord.substring(p_coord.indexOf(" ") + 1,
				p_coord.length()));
		point = (GmlPos) _pointVec.get(1);
		p_coord = point.getPoint();
		m_maxx = Double.parseDouble(p_coord.substring(0, p_coord.indexOf(" ")));
		m_maxy = Double.parseDouble(p_coord.substring(p_coord.indexOf(" ") + 1,
				p_coord.length()));
	}

	public Vector<?> getPointVec() {
		return _pointVec;
	}

}
