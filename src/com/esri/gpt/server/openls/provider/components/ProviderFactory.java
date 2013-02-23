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
import javax.servlet.http.HttpServletRequest;

import com.esri.gpt.framework.context.RequestContext;
import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.provider.components.ICqlParser;
import com.esri.gpt.server.csw.provider.components.IFilterParser;
import com.esri.gpt.server.csw.provider.components.IQueryEvaluator;
import com.esri.gpt.server.csw.provider.components.ISortByParser;
import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.csw.provider.components.ServiceProperties;
import com.esri.gpt.server.csw.provider.components.SupportedParameter;
import com.esri.gpt.server.csw.provider.components.SupportedParameters;
import com.esri.gpt.server.csw.provider.components.SupportedValues;
import com.esri.gpt.server.openls.provider.services.geocode.GeocodeProvider;
import com.esri.gpt.server.openls.provider.services.geocode.GeocodeResponse;
import com.esri.gpt.server.openls.provider.services.map.GetPortrayMapCapabilitiesProvider;
import com.esri.gpt.server.openls.provider.services.map.GetPortrayMapCapabilitiesResponse;
import com.esri.gpt.server.openls.provider.services.map.PortrayMapProvider;
import com.esri.gpt.server.openls.provider.services.map.PortrayMapResponse;
import com.esri.gpt.server.openls.provider.services.poi.DirectoryProvider;
import com.esri.gpt.server.openls.provider.services.poi.DirectoryResponse;
import com.esri.gpt.server.openls.provider.services.reversegeocode.ReverseGeocodeProvider;
import com.esri.gpt.server.openls.provider.services.reversegeocode.ReverseGeocodeResponse;
import com.esri.gpt.server.openls.provider.services.route.DetermineRouteProvider;
import com.esri.gpt.server.openls.provider.services.route.DetermineRouteResponse;



/**
 * Instantiates components associated with the execution of a 
 * requested CSW operation against the local catalog.
 */
public class ProviderFactory implements IProviderFactory {
     
  /** constructors ============================================================ */
  
  /** Default constructor */
  public ProviderFactory() {}
  
  /** main ==================================================================== */
  
    
  /** methods ================================================================= */
    
  
  /**
   * Makes an operation provider for a given operation name.
   * @param context the operation context
   * @param operationName the operation name
   * @return the operation provider
   * @throws OwsException if the method is unsupported
   */
  public IOperationProvider makeOperationProvider(OperationContext context, 
                                                  String operationName) 
    throws OwsException {
    
    ServiceProperties svcProps = context.getServiceProperties();
    SupportedParameters parameters = svcProps.getSupportedParameters();
    SupportedValues values;
    IOperationProvider opProvider = null;
    
    // output format
    values = new SupportedValues("application/xml,text/xml",",");
    parameters.add(new SupportedParameter(XlsConstants.Parameter_OutputFormat,values));
    
 // PortrayMapRequest
    if (operationName.equalsIgnoreCase("GetPortrayMapCapabilitiesRequest")) {
    	GetPortrayMapCapabilitiesProvider gcp = new GetPortrayMapCapabilitiesProvider();
         opProvider = gcp;
      
   // PortrayMapRequest
    } else if (operationName.equalsIgnoreCase("PortrayMapRequest")) {
      PortrayMapProvider pmp = new PortrayMapProvider();
      opProvider = pmp;
      
    // DetermineRouteRequest
    } else if (operationName.equals("DetermineRouteRequest")) {
      DetermineRouteProvider drp = new DetermineRouteProvider();
      opProvider = drp;
      
      values = new SupportedValues("XMLSCHEMA,http://www.w3.org/XML/Schema",",");
      parameters.add(new SupportedParameter(XlsConstants.Parameter_SchemaLanguage,values));
      
    // DirectoryRequest
    } else if (operationName.equals("DirectoryRequest")) {
      opProvider = new DirectoryProvider();
      
      values = new SupportedValues(
          "http://www.isotc211.org/2005/gmd",",");
      parameters.add(new SupportedParameter(XlsConstants.Parameter_OutputSchema,values));    
      
    // GeocodeRequest
    } else if (operationName.equals("GeocodeRequest")) { 
      GeocodeProvider grp = new GeocodeProvider();
      opProvider = grp;
      
      values = new SupportedValues(
          "http://www.isotc211.org/2005/gmd",",");
      parameters.add(new SupportedParameter(XlsConstants.Parameter_OutputSchema,values));
      
      // query type names              
      values = new SupportedValues("1.1.0,1.1",",");
      parameters.add(new SupportedParameter(XlsConstants.Parameter_ConstraintVersion,values));
      
      // ReverseGeocodeRequest
    } else if (operationName.equals("ReverseGeocodeRequest")) { 
      ReverseGeocodeProvider tp = new ReverseGeocodeProvider();
      opProvider = tp;
      
      values = new SupportedValues("1.1.0,1.1",",");
      parameters.add(new SupportedParameter(XlsConstants.Parameter_ConstraintVersion,values));      
    }
        
    return opProvider;
  }
  
  /**
   * Makes a provider for documents in their original XML schema.
   * @param context the operation context
   * @return the original XML provider
   * @throws OwsException if the method is unsupported
   */
  public IOriginalXmlProvider makeOriginalXmlProvider(OperationContext context) 
    throws OwsException {
    return null;
  }
 
    
  /**
   * Makes a CSW request handler.
   * @param request the HTTP servlet request
   * @param cswSubContextPath the HTTP sub-context path associated with the CSW service
   * @param resourceFilePrefix the path prefix for XML/XSLT resource files
   * @return the request handler
   */
  public RequestHandler makeRequestHandler(HttpServletRequest request,
									          RequestContext requestContext,
									          String cswSubContextPath,
									          String resourceFilePrefix) {
    
    // make the operation context
    OperationContext context = new OperationContext();
    context.setProviderFactory(this);
    context.setRequestContext(requestContext);
        
    // set the service properties
    ServiceProperties svcProps = new ServiceProperties();
    context.setServiceProperties(svcProps);
  
    svcProps.setCswSubContextPath(cswSubContextPath);
    svcProps.setResourceFilePrefix(resourceFilePrefix);
        
    // supported parameters
    SupportedParameters parameters = context.getServiceProperties().getSupportedParameters();
    SupportedValues values;
    
    // supported service name and versions
    values = new SupportedValues("XLS",",");
    parameters.add(new SupportedParameter(XlsConstants.Parameter_Service,values));
    values = new SupportedValues("1.1.0,1.1",",");
    parameters.add(new SupportedParameter(XlsConstants.Parameter_Version,values));
    
    // supported operations
    values = new SupportedValues(
        "GetPortrayMapCapabilities,PortrayMap,Route,Directory,Geocode,ReverseGeocode",",");
    parameters.add(new SupportedParameter(XlsConstants.Parameter_OperationName,values));
    
    // make and return the request handler
    RequestHandler handler = new RequestHandler();
    handler.setOperationContext(context);
    return handler;
  }
     
  /**
   * Makes an appropriate CSW operation response generator.
   * @param context the operation context
   * @return the response generator
   * @throws OwsException if the method is unsupported
   */
  public IResponseGenerator makeResponseGenerator(OperationContext context)
    throws OwsException {
    String opName = Val.chkStr(context.getOperationName());
    
    if (opName.equalsIgnoreCase(XlsConstants.Operation_GetPortrayMapCapabilities)) {
        return new GetPortrayMapCapabilitiesResponse();
        
    }else if (opName.equalsIgnoreCase(XlsConstants.Operation_PortrayMap)) {
      return new PortrayMapResponse();
      
    } else if (opName.equalsIgnoreCase(XlsConstants.Operation_Route)) {
      return new DetermineRouteResponse();
      
    } else if (opName.equalsIgnoreCase(XlsConstants.Operation_Directory)) {
      return new DirectoryResponse();
      
    } else if (opName.equalsIgnoreCase(XlsConstants.Operation_ReverseGeocode)) {     
        return new ReverseGeocodeResponse();          
    } else if (opName.equalsIgnoreCase(XlsConstants.Operation_Geocode)) {
      return new GeocodeResponse();      
    }
    return null;
  }
  
   
  /**
   * Instantiates a CSW request handler the local catalog.
   * @param requestContext the active request context
   * @return the request handler
   */
  public static RequestHandler newHandler(RequestContext requestContext) {
    IProviderFactory factory = ProviderFactory.newFactory();
    return factory.makeRequestHandler(null,requestContext,"","");
  }
  
  /**
   * Instantiates a CSW provider factory for the local catalog.
   * <p/>
   * By default, a new instance of 
   * com.esri.gpt.server.csw.provider.local.ProviderFactory is returned.
   * <p/>
   * @return the provider factory
   */
  public static IProviderFactory newFactory() {    
      return new ProviderFactory(); 
  }

@Override
public ICqlParser makeCqlParser(OperationContext context, String version)
		throws OwsException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public IFilterParser makeFilterParser(OperationContext context, String version)
		throws OwsException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public IQueryEvaluator makeQueryEvaluator(OperationContext context)
		throws OwsException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public ISortByParser makeSortByParser(OperationContext context)
		throws OwsException {
	// TODO Auto-generated method stub
	return null;
}  
}
