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

public final class GeocodedAddress extends Point {
  private double m_score;
  private Address m_addr;
  private String m_placeType;
  private String m_place;
  private String m_matchType;

  public GeocodedAddress(String p_strX, String p_strY) throws Exception {
    super(p_strX, p_strY);
    m_addr = new Address();
  }

  public GeocodedAddress() {
    m_addr = new Address();
  }

  public void setScore(double m_score) {
		this.m_score = m_score;
	}
  
  public void setStreet(String p_street) {
    m_addr.setStreet(p_street);
  }

  public String getStreet() {
    return m_addr.getStreet();
  }

  public String getScore(){
    return "" + m_score;
  }

  public void setAddress(Address p_addr) {
    m_addr = p_addr;
  }

  public Address getAddress() {
    return m_addr;
  }

  public String getIntersectionStreet( ) {
    return m_addr.getIntersection();
  }
  public void setIntersectionStreet(String p_street) {
    m_addr.setStreet(p_street);

  }

  public void setPostal(String p_postalCode) {
    m_addr.setPostalCode(p_postalCode);
  }

  public String getPostal( ) {
    return m_addr.getPostalCode();
  }

  public void setPlaceType(String p_type) {
    if (m_place == null) {
      m_placeType = p_type;
      return;
    }
    if (p_type.equals("Municipality")) m_addr.setMunicipality(m_place);
    else if (p_type.equals("CountrySubdivision")) m_addr.setCountrySubdivision(m_place);
    else if (p_type.equals("Country")) m_addr.setCountry(m_place);
    m_placeType = null;
  }

  public void setPlace(String p_place) {
    if (m_placeType == null) {
      m_place = p_place;
      return;
    }
    if (m_placeType.equals("Municipality")) m_addr.setMunicipality(p_place);
    else if (m_placeType.equals("CountrySubdivision")) m_addr.setCountrySubdivision(p_place);
    else if (m_placeType.equals("Country")) m_addr.setCountry(p_place);
    m_placeType = null;
  }

  public String getPlace() {
    return m_addr.getStreet();
  }

  public String getCoordinates() {
    return getStrX() + "," + getStrY();
  }

  public String getMunicipality() {
    return m_addr.getMunicipality();
  }

  public String getMunicipalityAttribute() {
    return "Municipality";
  }

  public String getCountrySubdivision() {
    return m_addr.getCountrySubdivision();
  }

  public String getCountrySubdivisionAttribute() {
    return "CountrySubdivision";
   }

   public String getCountrySecondarySubdivisionAttribute() {
     return "CountrySecondarySubdivision";
   }

   public void setCountry(String p_country) {
     m_addr.setCountry(p_country);
   }

   public String getCountry() {
     return m_addr.getCountry();
   }

   public String getGmlCoord() {
      return getStrX() + " " + getStrY();
   }
   public void setRevStreet(String p_street) {
     m_addr.setRevStreet(p_street);

   }
   public String getRevStreet() {
     return m_addr.getRevStreet();
   }
   public String getIntersectStreet() {
     return m_addr.getStreet();
   }
   public String getIntersection( ) {
     if(!m_addr.getIntersection().equals(""))
       return m_addr.getIntersection();
     else return null;
   }
   public void setIntersection(String p_intersection ) {
     m_addr.setIntersection(p_intersection);
   }
   public String getOldIntersection( ) {
     if(!m_addr.getOldIntersection().equals(""))
       return m_addr.getOldIntersection();
     else return null;
   }
   public void setOldIntersection(String p_intersection ) {
     m_addr.setOldIntersection(p_intersection);
   }
   public void setMatchType(String type){
     m_matchType = type;
   }
   public String getMatchType(){
     return m_matchType;
   }
}
