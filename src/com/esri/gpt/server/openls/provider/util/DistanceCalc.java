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

/*
 * This computer program software is proprietary to Environmental Systems  
 * Research Institute Inc. and has been copyrighted in their name. It shall
 * not be copied, used, or otherwise disclosed to others without the       
 * written authorization of Environmental Systems Research Institute Inc.  
 * (C) Copyright Environmental Systems Research Institute Inc. 2001
 */

/*
 *  This class maintains complete information about a given field
 */

public class DistanceCalc {
	// class variables
	public static double c_inchesPerMeter = 39.37;
	public static double c_metersPerInch = (1.0 / c_inchesPerMeter);
	public static double c_cmPerInch = (100.0 / c_inchesPerMeter);
	public static double c_mmPerInch = (1000.0 / c_inchesPerMeter);
	public static double c_inchesPerCm = (1.0 / c_cmPerInch);
	public static double c_inchesPerFoot = 12.0;
	public static double c_feetPerYard = 3.0;
	public static double c_inchesPerYard = (c_inchesPerFoot * c_feetPerYard);
	public static double c_feetPerMile = 5280.0;
	public static double c_feetPerNauticalMile = 6080.27;
	public static double c_inchesPerMile = (c_inchesPerFoot * c_feetPerMile);
	public static double c_inchesPerNauticalMile = (c_inchesPerFoot * c_feetPerNauticalMile);
	public static double c_defaultSemiMajorAxis = 6378137.0; // Use WGS84
																// spheroid
	public static double c_defaultFlattening = 0.003352813;

	public static double getApproxMeters(double x1, double y1, double x2,
			double y2) {
		return (Units.convert(getApproxMiles(x1, y1, x2, y2), Units.MILES,
				Units.METERS));
	}

	public static double getApproxMeters(Point point1, Point point2) {
		return (getApproxMeters(point1.getX(), point1.getY(), point2.getX(),
				point2.getY()));
	}

	@SuppressWarnings("unused")
	public static double getApproxMiles(double x1, double y1, double x2,
			double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double edist = Math.sqrt(dx * dx + dy * dy);

		double phi1 = Math.toRadians(y1);
		double phi2 = Math.toRadians(y2);
		double lam1 = Math.toRadians(x1);
		double lam2 = Math.toRadians(x2);

		double d_lam = lam2 - lam1;

		double cos_phi1 = Math.abs(phi1) == (Math.PI / 2.0) ? 0.0 : Math
				.cos(phi1);
		double sin_phi1 = Math.sin(phi1);
		double cos_phi2 = Math.abs(phi2) == (Math.PI / 2.0) ? 0.0 : Math
				.cos(phi2);
		double sin_phi2 = Math.sin(phi2);

		double cos_dlam = Math.abs(d_lam) == (Math.PI / 2.0) ? 0.0 : Math
				.cos(d_lam);
		double sin_dlam = Math.abs(d_lam) == Math.PI ? 0.0 : Math.sin(d_lam);

		double A = Math.sin((phi2 - phi1) / 2.0);
		double B = Math.sin(d_lam / 2.0);
		double sigma = 2.0 * Math.asin(Math.sqrt(A * A + cos_phi1 * cos_phi2
				* B * B));
		double dist3 = ((sigma * c_defaultSemiMajorAxis) * c_inchesPerMeter)
				/ c_inchesPerMile;
		return (dist3);
	}

	/**
	 * Method Name: distancePointLine Parameters : Point (input location), Point
	 * (line starting location), Point (line end location) Returns : Minimum
	 * distance between input location point from the line segment. Description:
	 * Following technique is used to find the shortest distance from a point to
	 * a line or line segment. The equation of a line defined through two points
	 * P1 (x1,y1) and P2 (x2,y2) is P = P1 + u (P2 - P1) The point P3 (x3,y3) is
	 * closest to the line at the tangent to the line which passes through P3,
	 * that is, the dot product of the tangent and line is 0, thus (P3 - P) dot
	 * (P2 - P1) = 0 Substituting the equation of the line gives [P3 - P1 - u(P2
	 * - P1)] dot (P2 - P1) = 0 Solving this gives the value of u we get, u =
	 * ((x3-x1)(x2-x1)+(y3-y1)*(y2-y1))/||(p2-p1)(p2-p1) Substituting this into
	 * the equation of the line gives the point of intersection (x,y) of the
	 * tangent as x = x1 + u (x2 - x1) y = y1 + u (y2 - y1) The distance
	 * therefore between the point P3 and the line is the distance between (x,y)
	 * above and P3.
	 */
	public static double distancePointLine(Point location, Point lineStart,
			Point lineEnd) {

		Point intersection = getIntersection(location, lineStart, lineEnd);
		return (getApproxMiles(location.getX(), location.getY(),
				intersection.getX(), intersection.getY()));
	}

	public static double getLineMagnitude(Point lineStart, Point lineEnd) {
		double vectorX = lineEnd.getX() - lineStart.getX();
		double vectorY = lineEnd.getY() - lineStart.getY();
		double mag = vectorX * vectorX + vectorY * vectorY;
		return (java.lang.Math.sqrt(mag));
	}

	public static double getLineMagnitude(double x1, double y1, double x2,
			double y2) {
		double vectorX = x2 - x1;
		double vectorY = y2 - y1;
		double mag = vectorX * vectorX + vectorY * vectorY;
		return (java.lang.Math.sqrt(mag));
	}

	public static Point getIntersection(Point location, Point lineStart,
			Point lineEnd) {
		Point intersection = new Point();

		double lineMag = getLineMagnitude(lineEnd, lineStart);
		double U = (((location.getX() - lineStart.getX()) * (lineEnd.getX() - lineStart
				.getX())) + ((location.getY() - lineStart.getY()) * (lineEnd
				.getY() - lineStart.getY())))
				/ (lineMag * lineMag);
		double X = lineStart.getX() + U * (lineEnd.getX() - lineStart.getX());
		double Y = lineStart.getY() + U * (lineEnd.getY() - lineStart.getY());
		if (U < 0.0f || U > 1.0f) {
			double d1 = getApproxMiles(location.getX(), location.getY(),
					lineStart.getX(), lineStart.getY());
			double d2 = getApproxMiles(location.getX(), location.getY(),
					lineEnd.getX(), lineEnd.getY());
			if (d1 < d2) {
				intersection.setX(lineStart.getX());
				intersection.setY(lineStart.getY());
			} else {
				intersection.setX(lineEnd.getX());
				intersection.setY(lineEnd.getY());
			}
		} else {
			intersection.setX(X);
			intersection.setY(Y);
		}
		return (intersection);
	}

	public static double distancePointLineMeters(Point location,
			Point lineStart, Point lineEnd) {

		Point intersection = getIntersectionMeters(location, lineStart, lineEnd);
		return (getApproxMeters(location.getX(), location.getY(),
				intersection.getX(), intersection.getY()));
	}

	public static Point getIntersectionMeters(Point location, Point lineStart,
			Point lineEnd) {
		Point intersection = new Point();

		double lineMag = getLineMagnitude(lineEnd, lineStart);
		double U = (((location.getX() - lineStart.getX()) * (lineEnd.getX() - lineStart
				.getX())) + ((location.getY() - lineStart.getY()) * (lineEnd
				.getY() - lineStart.getY())))
				/ (lineMag * lineMag);
		double X = lineStart.getX() + U * (lineEnd.getX() - lineStart.getX());
		double Y = lineStart.getY() + U * (lineEnd.getY() - lineStart.getY());
		if (U < 0.0f || U > 1.0f) {
			double d1 = getApproxMeters(location.getX(), location.getY(),
					lineStart.getX(), lineStart.getY());
			double d2 = getApproxMeters(location.getX(), location.getY(),
					lineEnd.getX(), lineEnd.getY());
			if (d1 < d2) {
				intersection.setX(lineStart.getX());
				intersection.setY(lineStart.getY());
			} else {
				intersection.setX(lineEnd.getX());
				intersection.setY(lineEnd.getY());
			}
		} else {
			intersection.setX(X);
			intersection.setY(Y);
		}
		return (intersection);
	}
}