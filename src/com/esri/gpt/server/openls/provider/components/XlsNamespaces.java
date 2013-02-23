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
package com.esri.gpt.server.openls.provider.components;
import javax.xml.namespace.NamespaceContext;

import com.esri.gpt.catalog.schema.NamespaceContextImpl;
import com.esri.gpt.catalog.schema.Namespaces;

/**
 * Defines XLS name spaces.
 */
public class XlsNamespaces {
  
  /** class variables ========================================================= */
      
  /** URI XLS - "http://www.opengis.net/xls" */
  public static final String URI_XLS = "http://www.opengis.net/xls";
  
  /** URI DC - "http://purl.org/dc/elements/1.1/" */
  public static final String URI_DC = "http://purl.org/dc/elements/1.1/";
  
  /** URI dcmiBox - "http://dublincore.org/documents/2000/07/11/dcmi-box/" */
  public static final String URI_dcmiBox = "http://dublincore.org/documents/2000/07/11/dcmi-box/";
  
  /** URI DCT - "http://purl.org/dc/terms/" */
  public static final String URI_DCT = "http://purl.org/dc/terms/";
  
  /** URI GML - "http://www.opengis.net/gml" */
  public static final String URI_GML = "http://www.opengis.net/gml";
  
  /** URI INSPIRE_DS - "http://inspire.europa.eu/networkservice/discovery" */
  public static final String URI_INSPIRE_DS = "http://inspire.europa.eu/networkservice/discovery";
  
  /** URI OGC - "http://www.opengis.net/ogc" */
  public static final String URI_OGC = "http://www.opengis.net/ogc";
  
  /** URI OWS - "http://www.opengis.net/ows" */
  public static final String URI_OWS = "http://www.opengis.net/ows";

  /** URI SOAP - "http://www.w3.org/2001/12/soap-envelope" */
  public static final String URI_SOAP = "http://www.w3.org/2001/12/soap-envelope";
  
  /** URI SOAP_ENV - "http://schemas.xmlsoap.org/soap/envelope/" */
  public static final String URI_SOAP_ENV = "http://schemas.xmlsoap.org/soap/envelope/";
  
  /** URI_SOAP_2003_05 - "http://schemas.xmlsoap.org/soap/envelope/" */
  public static final String URI_SOAP_2003_05 = "http://www.w3.org/2003/05/soap-envelope";
  
  /** URI TCEXT - "http://www.conterra.de/catalog/ext" */
  public static final String URI_TCEXT = "http://www.conterra.de/catalog/ext";
  
  /** URI XLINK - "http://www.w3.org/1999/xlink" */
  public static final String URI_XLINK = "http://www.w3.org/1999/xlink";
  
  /** URI XSD - "http://www.w3.org/2001/XMLSchema" */
  public static final String URI_XSD = "http://www.w3.org/2001/XMLSchema";
      
  /** constructors ============================================================ */
      
  /** Default constructor. */
  public XlsNamespaces() {}
  
  /** methods ================================================================= */
  
  /**
   * Makes a context for XLS name spaces.
   * @return the name space context
   */
  public NamespaceContext makeNamespaceContext() {
    return new NamespaceContextImpl(makeNamespaces());
  }
  
  /**
   * Makes XLS name spaces.
   * @return the XLS name spaces
   */
  private Namespaces makeNamespaces() {
    Namespaces namespaces = new Namespaces();
    namespaces.add("xls",URI_XLS);
    namespaces.add("dc", URI_DC);
    namespaces.add("dct",URI_DCT);
    namespaces.add("gml",URI_GML);
    namespaces.add("inspire_ds",URI_INSPIRE_DS);
    namespaces.add("ogc",URI_OGC);
    namespaces.add("ows",URI_OWS);
    namespaces.add("soap",URI_SOAP);
    namespaces.add("SOAP-ENV",URI_SOAP_ENV);
    namespaces.add("soap_2003_05",URI_SOAP_2003_05);
    namespaces.add("tcExt",URI_TCEXT);
    namespaces.add("xlink",URI_XLINK);
    namespaces.add("xsd",URI_XSD);
    return namespaces;
  }

}
