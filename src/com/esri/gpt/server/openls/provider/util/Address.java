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

public class Address {

	static final String emptyString = "";
	private String m_street = null;
	private String mr_street = null;
	private String m_buildingNumber = emptyString;
	private String m_intersection = null;
	private String m_oldintersection = null;
	private String m_municipality = emptyString;
	private String m_countrySubdivision = emptyString;
	private String m_postalCode = emptyString;
	private String m_country = emptyString;
	private String m_placeType;
	private String m_place;

	private java.util.Vector<String> streetVec;
	private int count = 0;

	public Address() {
		streetVec = new java.util.Vector<String>();
	}

	public Address(String p_street, String p_municipality,
			String p_countrySubdivision, String p_postalCode, String p_country) {
		setStreet(p_street);
		setMunicipality(p_municipality);
		setCountrySubdivision(p_countrySubdivision);
		setPostalCode(p_postalCode);
		setCountry(p_country);
		streetVec = new java.util.Vector<String>();
	}

	public Address(String p_street, String p_intersection,
			String p_municipality, String p_countrySubdivision,
			String p_postalCode, String p_country) {
		this(p_street, p_municipality, p_countrySubdivision, p_postalCode,
				p_country);
		setIntersection(p_intersection);
	}

	public void setStreet(String p_street) {
		if (p_street != null)
			m_street = p_street;
		// else m_street = emptyString;
	}

	public void setBuildingNumber(String p_buildingNumber) {
		if (p_buildingNumber != null)
			m_buildingNumber = p_buildingNumber;
		else
			m_buildingNumber = emptyString;
	}

	public void setMunicipality(String p_municipality) {
		if (p_municipality != null)
			m_municipality = p_municipality;
		else
			m_municipality = emptyString;
	}

	public void setCountrySubdivision(String p_countrySubdivision) {
		if (p_countrySubdivision != null)
			m_countrySubdivision = p_countrySubdivision;
		else
			m_countrySubdivision = emptyString;
	}

	public void setPostalCode(String p_postalCode) {
		if (p_postalCode != null)
			m_postalCode = p_postalCode;
		else
			m_postalCode = emptyString;
	}

	public void setCountry(String p_country) {
		if (p_country != null)
			m_country = p_country;
		else
			m_country = emptyString;
	}

	public void setIntersection(String p_intersection) {
		if (p_intersection != null)
			m_intersection = p_intersection;
	}

	public String getStreet() {
		return m_street;
	}

	public String getBuildingNumber() {
		return m_buildingNumber;
	}

	public String getMunicipality() {
		return m_municipality;
	}

	public String getCountrySubdivision() {
		return m_countrySubdivision;
	}

	public String getPostalCode() {
		return m_postalCode;
	}

	public String getCountry() {
		return m_country;
	}

	public String getIntersection() {
		return m_intersection;
	}

	public void setPlaceType(String p_type) {
		if (m_place == null) {
			m_placeType = p_type;
			return;
		}
		if (p_type.equals("Municipality"))
			setMunicipality(m_place);
		else if (p_type.equals("CountrySubdivision"))
			setCountrySubdivision(m_place);
		else if (p_type.equals("Country"))
			setCountry(m_place);
		m_placeType = null;
	}

	public void setPlace(String p_place) {
		if (m_placeType == null) {
			m_place = p_place;
			return;
		}
		if (m_placeType.equals("Municipality"))
			setMunicipality(p_place);
		else if (m_placeType.equals("CountrySubdivision"))
			setCountrySubdivision(p_place);
		else if (m_placeType.equals("Country"))
			setCountry(p_place);
		m_placeType = null;
	}

	public void setStreetVec(String p_street) {
		streetVec.add(p_street);
		if (count == 0)
			m_street = p_street;
		else
			m_intersection = p_street;
		count++;
	}

	public java.util.Vector<String> getStreetVec() {
		return streetVec;
	}

	public String getRevStreet() {
		return mr_street;
	}

	public void setRevStreet(String r_street) {
		if (r_street != null)
			mr_street = r_street;
	}

	public String getOldIntersection() {
		return m_oldintersection;
	}

	public void setOldIntersection(String p_intersection) {
		m_oldintersection = p_intersection;
	}

	public void setFreeFormAddr(String addressStr) {
		this.setStreet(addressStr.substring(0, addressStr.indexOf(",") - 1));
		this.setMunicipality(addressStr.substring(addressStr.indexOf(",") + 1,
				addressStr.lastIndexOf(",") - 1));
		this.setCountrySubdivision(addressStr.substring(addressStr
				.lastIndexOf(",") + 1));
	}

}