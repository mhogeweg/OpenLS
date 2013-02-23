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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;

import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.openls.provider.components.IOperationProvider;
import com.esri.gpt.server.openls.provider.components.IProviderFactory;
import com.esri.gpt.server.openls.provider.components.IResponseGenerator;
import com.esri.gpt.server.openls.provider.components.OperationContext;
import com.esri.gpt.server.openls.provider.services.geocode.GeocodeParams;
import com.esri.gpt.server.openls.provider.services.geocode.GeocodeProvider;
import com.esri.gpt.server.openls.provider.util.BBoxContext;
import com.esri.gpt.server.openls.provider.util.GeocodedAddress;
import com.esri.gpt.server.openls.provider.util.GmlPos;
import com.esri.gpt.server.openls.provider.util.ImageOutput;
import com.esri.gpt.server.openls.provider.util.Output;
import com.esri.gpt.server.openls.provider.util.Point;

/**
 * Provides the Openls DetermineRoute operation.
 */
public class DetermineRouteProvider implements IOperationProvider {

	/**
	 * class variables =========================================================
	 */

	/** The Logger. */
	private static Logger LOGGER = Logger
			.getLogger(DetermineRouteProvider.class.getName());

	/**
	 * constructors ============================================================
	 */

	/** Default constructor */
	public DetermineRouteProvider() {
		super();
	}

	/**
	 * methods =================================================================
	 */
	/**
	 * Executes a parsed operation request.
	 * 
	 * @param context
	 *            the operation context
	 * @throws Exception
	 *             if a processing exception occurs
	 */
	private void generateResponse(OperationContext context) throws Exception {

		// initialize
		LOGGER.finer("Executing xls:DetermineRoute request...");
		IProviderFactory factory = context.getProviderFactory();

		// generate the response
		IResponseGenerator generator = factory.makeResponseGenerator(context);
		if (generator == null) {
			String msg = "IProviderFactory.makeResponseGenerator: instantiation failed.";
			LOGGER.log(Level.SEVERE, msg);
			throw new OwsException(msg);
		} else {
			generator.generateResponse(context);
		}
	}

	/**
	 * Handles a URL based request (HTTP GET).
	 * 
	 * @param context
	 *            the operation context
	 * @param request
	 *            the HTTP request
	 * @throws Exception
	 *             if a processing exception occurs
	 */
	public void handleGet(OperationContext context, HttpServletRequest request)
			throws Exception {
	}

	/**
	 * Handles an XML based request (normally HTTP POST).
	 * 
	 * @param context
	 *            the operation context
	 * @param root
	 *            the root node
	 * @param xpath
	 *            an XPath to enable queries (properly configured with name
	 *            spaces)
	 * @throws Exception
	 */
	public void handleXML(OperationContext context, Node root, XPath xpath)
			throws Exception {

		// initialize
		LOGGER.finer("Handling xls:DetermineRouteProvider request XML...");
		String locator = "xls:DetermineRouteRequest";

		// service and version are parsed by the parent RequestHandler
		Node ndReq = (Node) xpath.evaluate(locator, root, XPathConstants.NODE);
		if (ndReq != null) {
			parseRequest(context, ndReq, xpath);
		}
		// TODO requestId
		try {
			executeRequest(context);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		generateResponse(context);
	}

	/**
	 * 
	 * @param context
	 * @param ndReq
	 * @param xpath
	 * @throws Exception
	 */
	private void parseRequest(OperationContext context, Node ndReq, XPath xpath)
			throws Exception {
		DetermineRouteParams routeParams = new DetermineRouteParams();
		Node ndRoutePlan = (Node) xpath.evaluate("xls:RoutePlan", ndReq,
				XPathConstants.NODE);
		if (ndRoutePlan != null) {
			RoutePlan routePlan = new RoutePlan();
			Node ndRoutePreference = (Node) xpath.evaluate(
					"xls:RoutePreference", ndRoutePlan, XPathConstants.NODE);
			if (ndRoutePreference != null) {
				routePlan
						.setRoutePreference(ndRoutePreference.getTextContent());
			}
			Node ndWayPointList = (Node) xpath.evaluate("xls:WayPointList",
					ndRoutePlan, XPathConstants.NODE);
			if (ndWayPointList != null) {
				HashMap<String, Point> wayPointList = new HashMap<String, Point>();
				Node ndStartPoint = (Node) xpath.evaluate("xls:StartPoint",
						ndWayPointList, XPathConstants.NODE);
				if (ndStartPoint != null) {
					Node ndStartPOI = (Node) xpath.evaluate("xls:POI",
							ndStartPoint, XPathConstants.NODE);
					if (ndStartPOI != null) {
						ndStartPoint = ndStartPOI;
					}
					Node ndStartPos = (Node) xpath.evaluate(
							"xls:Position/gml:Point/gml:pos", ndStartPoint,
							XPathConstants.NODE);
					Point point = new Point();
					if (ndStartPos != null) {
						String[] coords = ndStartPos.getTextContent()
								.split(" ");
						point.setX(coords[0]);
						point.setY(coords[1]);
					} else {
						geocode(context, ndStartPoint, xpath, point);
					}
					wayPointList.put("start", point);
				}
				Node ndViaPoint = (Node) xpath.evaluate("xls:ViaPoint",
						ndWayPointList, XPathConstants.NODE);
				if (ndViaPoint != null) {
					Node ndViaPOI = (Node) xpath.evaluate("xls:POI",
							ndStartPoint, XPathConstants.NODE);
					if (ndViaPOI != null) {
						ndViaPoint = ndViaPOI;
					}
					Point point = new Point();
					Node ndViaPos = (Node) xpath.evaluate(
							"xls:Position/gml:Point/gml:pos", ndViaPoint,
							XPathConstants.NODE);
					if (ndViaPos != null) {
						String[] coords = ndViaPos.getTextContent().split(" ");
						point.setX(coords[0]);
						point.setY(coords[1]);
					} else {
						geocode(context, ndViaPoint, xpath, point);
					}
					wayPointList.put("via", point);
				}
				Node ndEndPoint = (Node) xpath.evaluate("xls:EndPoint",
						ndWayPointList, XPathConstants.NODE);
				if (ndEndPoint != null) {
					Node ndEndPOI = (Node) xpath.evaluate("xls:POI",
							ndEndPoint, XPathConstants.NODE);
					if (ndEndPOI != null) {
						ndEndPoint = ndEndPOI;
					}
					Node ndEndPos = (Node) xpath.evaluate(
							"xls:Position/gml:Point/gml:pos", ndEndPoint,
							XPathConstants.NODE);
					Point point = new Point();
					if (ndEndPos != null) {
						String[] coords = ndEndPos.getTextContent().split(" ");
						point.setX(coords[0]);
						point.setY(coords[1]);
					} else {
						geocode(context, ndEndPoint, xpath, point);
					}
					wayPointList.put("end", point);
				}
				routePlan.setWayPointList(wayPointList);
			}

			Node ndAvoidList = (Node) xpath.evaluate("xls:AvoidList",
					ndRoutePlan, XPathConstants.NODE);
			if (ndAvoidList != null) {
				Node ndAvoidPOI = (Node) xpath.evaluate("xls:POI", ndAvoidList,
						XPathConstants.NODE);
				if (ndAvoidPOI != null) {
					ndAvoidList = ndAvoidPOI;
				}
				HashMap<String, Point> avoidPointList = new HashMap<String, Point>();
				Node ndStartPos = (Node) xpath.evaluate(
						"xls:Position/gml:Point/gml:pos", ndAvoidList,
						XPathConstants.NODE);
				Point point = new Point();
				if (ndStartPos != null) {
					String[] coords = ndStartPos.getTextContent().split(" ");
					point.setX(coords[0]);
					point.setY(coords[1]);
				} else {
					geocode(context, ndAvoidList, xpath, point);
				}
				avoidPointList.put("avoid", point);
				routePlan.setAvoidPointList(avoidPointList);
			}

			routeParams.setRoutePlan(routePlan);
		}
		Node ndRouteInst = (Node) xpath.evaluate(
				"xls:RouteInstructionsRequest", ndReq, XPathConstants.NODE);
		if (ndRouteInst != null) {
			RouteInstructionsRequest routeInst = new RouteInstructionsRequest();
			String format = (String) xpath.evaluate("@format", ndRouteInst,
					XPathConstants.STRING);
			String provideGeometry = (String) xpath.evaluate(
					"@provideGeometry", ndRouteInst, XPathConstants.STRING);
			routeInst.setFormat(format);
			routeInst.setProvideGeometry(provideGeometry);
			routeParams.setRouteInstructions(routeInst);
		}
		Node ndRouteMap = (Node) xpath.evaluate("xls:RouteMapRequest", ndReq,
				XPathConstants.NODE);
		if (ndRouteMap != null) {
			RouteMap routeMap = new RouteMap();
			Node ndOutput = (Node) xpath.evaluate("xls:Output", ndRouteMap,
					XPathConstants.NODE);
			if (ndOutput != null) {
				ImageOutput imgOutput = new ImageOutput();
				Output output = new Output();
				String format = (String) xpath.evaluate("@format", ndOutput,
						XPathConstants.STRING);
				String width = (String) xpath.evaluate("@width", ndOutput,
						XPathConstants.STRING);
				String height = (String) xpath.evaluate("@height", ndOutput,
						XPathConstants.STRING);
				output.setFormat(format);
				output.setWidth(width);
				output.setHeight(height);
				imgOutput.setOutput(output);
				routeMap.setImageOutput(imgOutput);
			}
			routeParams.setRouteMap(routeMap);
		}
		context.getRequestOptions().setDetermineRouteParams(routeParams);

	}

	/**
	 * Geocodes address
	 * @param context
	 * @param ndReq
	 * @param xpath
	 * @param point
	 * @throws Exception
	 */
	private void geocode(OperationContext context, Node ndReq, XPath xpath,
			Point point) throws Exception {
		GeocodeProvider geocodeProvider = new GeocodeProvider();
		GeocodeParams reqParams = new GeocodeParams();
		geocodeProvider.parseRequest(reqParams, ndReq, xpath);
		try {
			ArrayList<GeocodedAddress> ga = geocodeProvider.executeRequest(
					context, reqParams);
			point.setX(ga.get(0).getX());
			point.setY(ga.get(0).getY());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * Executes route request
	 * @param context
	 * @throws Throwable
	 */
	private void executeRequest(OperationContext context) throws Throwable {
		long t1 = System.currentTimeMillis();

		String srvCfg = context.getRequestContext()
				.getApplicationConfiguration().getCatalogConfiguration()
				.getParameters().getValue("openls.determineRoute");

		String srvCfgMapService = context.getRequestContext()
				.getApplicationConfiguration().getCatalogConfiguration()
				.getParameters().getValue("openls.determineRoute.mapService");

		DetermineRouteParams routeParams = context.getRequestOptions()
				.getDetermineRouteParams();

		RoutePlan rtPlan = routeParams.getRoutePlan();
		HashMap<String, Point> barriers = rtPlan.getAvoidPointList();
		GeocodedAddress[] avoidAddr = null;
		if (barriers != null) {
			avoidAddr = new GeocodedAddress[barriers.size()];
			Set<String> keys = barriers.keySet();
			Iterator<String> iter = keys.iterator();
			int cnt = 0;
			while (iter.hasNext()) {
				String key = iter.next();
				Point barrier = (Point) barriers.get(key);
				GeocodedAddress ga = new GeocodedAddress();
				ga.setX(barrier.getX());
				ga.setY(barrier.getY());
				avoidAddr[cnt] = ga;
				cnt++;
			}
		}

		HashMap<String, Point> points = rtPlan.getWayPointList();
		GeocodedAddress[] gcAddr = new GeocodedAddress[points.size()];
		Point start = points.get("start");
		GeocodedAddress gaStart = new GeocodedAddress();
		gaStart.setX(start.getX());
		gaStart.setY(start.getY());
		gcAddr[0] = gaStart;

		Point via = points.get("via");
		GeocodedAddress gaVia = new GeocodedAddress();
		gaVia.setX(via.getX());
		gaVia.setY(via.getY());
		gcAddr[1] = gaVia;

		Point end = points.get("start");
		GeocodedAddress gaEnd = new GeocodedAddress();
		gaEnd.setX(end.getX());
		gaEnd.setY(end.getY());
		gcAddr[2] = gaEnd;

		String sRouteRequest = makeRouteRequest(gcAddr, avoidAddr);

		// SUBMIT THE REQUEST
		String sUrl = srvCfg + "/solve?" + sRouteRequest;

		LOGGER.info("REQUEST=\n" + sUrl);

		URL url = new URL(sUrl);
		URLConnection conn = url.openConnection();

		// Get the response
		String line = "";
		String sResponse = "";

		InputStream is = conn.getInputStream();

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader rd = new BufferedReader(isr);
		while ((line = rd.readLine()) != null) {
			sResponse += line;
		}
		rd.close();
		url = null;

		DetermineRouteParams determineRouteParams = parseRouteResponse(sResponse);

		if (routeParams.getRouteInstructions().getFormat().equals("text/plain")) {

			// rte.setInstSwitch(true);
		}

		determineRouteParams.setRouteMap(routeParams.getRouteMap());
		makeExportMapRequest(determineRouteParams, srvCfgMapService);

		context.getRequestOptions().setDetermineRouteParams(
				determineRouteParams);
		long t2 = System.currentTimeMillis();
		LOGGER.info("PERFORMANCE: " + (t2 - t1)
				+ " ms required to perform service");
	}

	// http://tasks.arcgisonline.com/ArcGIS/rest/services/NetworkAnalysis/ESRI_Route_NA/NAServer/Route/solve?
	// stops=-122.4079,+37.78356;+-122.404,+37.782&barriers=&outSR=4326&ignoreInvalidLocations=true&
	// accumulateAttributeNames=&impedanceAttributeName=Time&restrictionAttributeNames=OneWay,Avoid+Passenger+Ferries,Non-routeable+Segments&
	// attributeParameterValues=&restrictUTurns=esriNFSBAllowBacktrack&useHierarchy=true&returnDirections=true&returnRoutes=true&returnStops=false&
	// returnBarriers=false&directionsLanguage=en_US&outputLines=esriNAOutputLineTrueShapeWithMeasure&findBestSequence=false&preserveFirstStop=true&
	// preserveLastStop=true&useTimeWindows=false&startTime=&
	// outputGeometryPrecision=&outputGeometryPrecisionUnits=esriUnknownUnits&directionsTimeAttributeName=Time&directionsLengthUnits=esriNAUMiles&f=json
	/**
	 * Builds route request string
	 */
	private String makeRouteRequest(GeocodedAddress[] gcAdd,
			GeocodedAddress[] avoidAdd) {
		StringBuffer req = new StringBuffer();
		if (gcAdd.length > 0) {
			req.append("stops=");
			for (int i = 0; i < gcAdd.length; i++) {
				req.append(gcAdd[i].getX()).append(",").append(gcAdd[i].getY())
						.append(";");
			}
		}
		if (avoidAdd != null && avoidAdd.length > 0) {
			req.append("&barriers=");
			for (int i = 0; i < avoidAdd.length; i++) {
				req.append(avoidAdd[i].getX()).append(",")
						.append(avoidAdd[i].getY()).append(";");
			}
		}

		req.append("&outSR=4326&ignoreInvalidLocations=true&")
				.append("accumulateAttributeNames=&impedanceAttributeName=Time&restrictionAttributeNames=OneWay,Avoid+Passenger+Ferries,Non-routeable+Segments&")
				.append("attributeParameterValues=&restrictUTurns=esriNFSBAllowBacktrack&useHierarchy=true&returnDirections=true&returnRoutes=true&returnStops=false&")
				.append("returnBarriers=false&directionsLanguage=en_US&outputLines=esriNAOutputLineTrueShapeWithMeasure&findBestSequence=false&preserveFirstStop=true&")
				.append("preserveLastStop=true&useTimeWindows=false&startTime=&")
				.append("outputGeometryPrecision=&outputGeometryPrecisionUnits=esriUnknownUnits&directionsTimeAttributeName=Time&directionsLengthUnits=esriNAUMiles&f=json");

		return req.toString();
	}

	/**
	 * Parses route response
	 * @param sResponse
	 * @return
	 * @throws Throwable
	 */
	@SuppressWarnings("unused")
	private DetermineRouteParams parseRouteResponse(String sResponse)
			throws Throwable {
		DetermineRouteParams routeParams = null;
		try {
			routeParams = new DetermineRouteParams();
			JSONObject jsonResp = new JSONObject(sResponse);

			String xResponse = "<?xml version='1.0'?><response>"
					+ org.json.XML.toString(jsonResp) + "</response>";

			LOGGER.info("XML from JSON = " + xResponse);

			RouteInstructionsList instList = new RouteInstructionsList();

			String coords = "";
			if (jsonResp != null) {

				JSONObject routes = jsonResp.getJSONObject("routes");
				if (routes != null) {
					JSONArray routesFeatures = routes.getJSONArray("features");
					JSONObject geometry = routesFeatures.getJSONObject(0)
							.getJSONObject("geometry");
					JSONArray paths = geometry.getJSONArray("paths")
							.getJSONArray(0);

					for (int i = 0; i < paths.length(); i++) {
						GmlPos pos = new GmlPos();
						pos.setPoint(paths.getString(i).replace("[", "")
								.replace("]", "").replace(",", " "));
					}
				}

				JSONArray directions = jsonResp.getJSONArray("directions");
				JSONObject direction = directions.getJSONObject(0);
				if (directions != null) {

					JSONObject summary = direction.getJSONObject("summary");
					RouteSummary rteSummary = new RouteSummary();
					rteSummary.setTotalTime(summary.getString("totalTime"));
					rteSummary.setTotalDistance(summary
							.getString("totalLength"));
					rteSummary.setTotalDriveTime(summary
							.getString("totalDriveTime"));
					String routeName = direction.getString("routeName");
					String desc = direction.getString("summary");

					coords = summary.getString("envelope");
					if (coords != null && !coords.equals(""))
						rteSummary.setBbox(new BBoxContext(coords));

					JSONObject envelope = summary.getJSONObject("envelope");

					String minx = envelope.getString("xmin");
					String miny = envelope.getString("ymin");
					String maxx = envelope.getString("xmax");
					String maxy = envelope.getString("ymax");

					rteSummary.setBbox(new BBoxContext(minx, miny, maxx, maxy));

					if (rteSummary.getBbox() == null)
						rteSummary.setBbox(new BBoxContext(minx, miny, maxx,
								maxy));

					JSONArray directionFeatures = direction
							.getJSONArray("features");
					ArrayList<RouteInstruction> routeInstructionList = new ArrayList<RouteInstruction>();
					for (int j = 0; j < directionFeatures.length(); j++) {
						JSONObject featuresArray = directionFeatures
								.getJSONObject(j);
						JSONObject attributes = featuresArray
								.getJSONObject("attributes");
						String compressedGeom = featuresArray
								.getString("compressedGeometry");
						RouteInstruction inst = new RouteInstruction();
						inst.setDistance((attributes.getString("length")));
						inst.setDuration(attributes.getString("time"));
						inst.setInstruction(attributes.getString("text"));
						// inst.(new BBoxContext());

						routeInstructionList.add(inst);
					}

					instList.setRouteInstrunctionList(routeInstructionList);
					routeParams.setRouteSummary(rteSummary);
					routeParams.setRouteInstList(instList);
				}
			}
		} finally {
		}
		return routeParams;
	}
    /**
     * This method creates the export map request from a MapRequest object.
     * @param determineRouteParams
     * @param srvCfgMapService
     * @throws Throwable
     */
	private void makeExportMapRequest(
			DetermineRouteParams determineRouteParams, String srvCfgMapService)
			throws Throwable {

		ImageOutput imgOutput = determineRouteParams.getRouteMap()
				.getImageOutput();
		Output output = imgOutput.getOutput();
		determineRouteParams
				.getRouteMap()
				.getImageOutput()
				.setBboxContext(
						determineRouteParams.getRouteSummary().getBbox());

		// ISMapRequest request = (ISMapRequest) getRequest();

		String requestParameters = "";
		// m_version = request.getRequestVersion();
		// m_srsName = request.getSrsName();

		/*
		 * use transparent background or not transparent = true | false
		 */
		/*
		 * if (request.getTransparent() != null &&
		 * !request.getTransparent().equalsIgnoreCase("")) requestParameters +=
		 * "transparent=" + request.getTransparent().toLowerCase() + "&";
		 */

		/*
		 * set the output format format = png | png8 | png24 | jpg | pdf | bmp |
		 * gif | svg | png32
		 */
		requestParameters += "format=" + output.getFormat() + "&";

		/*
		 * set the output size size = <width>, <height>
		 */
		requestParameters += "size=" + output.getWidth() + ","
				+ output.getHeight() + "&";

		/*
		 * set the output coordinate reference system imageSR = <wellknown id>
		 */
		/*
		 * if (m_srsName != null && !m_srsName.equalsIgnoreCase("")) {
		 * routeReq.getBoundingBox().setBoxSrs( ((ISMapRequest)
		 * getRequest()).getSrsName()); requestParameters += "imageSR=" +
		 * m_srsName + "&"; }
		 */

		/*
		 * set the coordinate reference system for the bounding box bboxSR =
		 * <wellknown id>
		 * 
		 * use imageSR if the bboxSR is not set
		 */
		if (imgOutput.getBboxContext() != null)
			requestParameters += "bboxSR=4326" // +
												// imgOutput.getBoundingBox().getBoxSrs()
					+ "&";
		else
			requestParameters += "bboxSR=4326&";

		/*
		 * set the bounding box bbox = <xmin>, <ymin>, <xmax>, <ymax>
		 * 
		 * request may contain a bbox, a center point and scale, or a center
		 * point and radius
		 */
		requestParameters += "bbox=";
		BBoxContext bbox = imgOutput.getBboxContext();
		if (bbox != null) {

			if (bbox.getMinX() != 0.0 && bbox.getMaxY() != 0.0) {

				requestParameters += bbox.getMinX();
				requestParameters += "," + bbox.getMinY();
				requestParameters += "," + bbox.getMaxX();
				requestParameters += "," + bbox.getMaxY();
			} else {
				requestParameters += bbox.getMinX();
				requestParameters += "," + bbox.getMinY();
				requestParameters += "," + bbox.getMaxX();
				requestParameters += "," + bbox.getMaxY();
			}

		}

		/*
		 * } else { // add else part for /* if (request.getCenterPoint() != null
		 * && request.getScale() > 0) { // center point and scale provided
		 * double widthScale = Double.parseDouble(routeReq.getImageWidth()) / 2
		 * / 96 * request.getScale(); widthScale = Units.convert(widthScale, 2,
		 * 0); double heightScale =
		 * Double.parseDouble(routeReq.getImageHeight()) / 2 / 96 *
		 * request.getScale(); heightScale = Units.convert(heightScale, 2, 0);
		 * requestParameters += (request.getCenterPoint().getX() - widthScale);
		 * requestParameters += "," + (request.getCenterPoint().getY() -
		 * heightScale); requestParameters += "," +
		 * (request.getCenterPoint().getX() + widthScale); requestParameters +=
		 * "," + (request.getCenterPoint().getY() + heightScale);
		 * 
		 * } else if (request.getCenterPoint() != null && request.getRadius() !=
		 * null) { // center point and radius provided requestParameters +=
		 * (request.getCenterPoint().getX() - Integer
		 * .parseInt(request.getRadius())); requestParameters += "," +
		 * (request.getCenterPoint().getY() - Integer.parseInt(request
		 * .getRadius())); requestParameters += "," +
		 * (request.getCenterPoint().getX() + Integer.parseInt(request
		 * .getRadius())); requestParameters += "," +
		 * (request.getCenterPoint().getY() + Integer.parseInt(request
		 * .getRadius()));
		 * 
		 * } else { // all else failed: default to world as extent
		 * requestParameters += "-180,-90,180,90"; } }
		 */
		requestParameters += "&";

		/*
		 * set the layers to include layers= [show | hide | include |
		 * exclude]:layerId1,layerId2
		 */
		// TO DO

		/*
		 * close the request parameters with asking for the image as output as
		 * opposed to an HTML page...
		 */
		requestParameters += "&f=image";

		LOGGER.info("*******Map Request = " + requestParameters);

		imgOutput.setUrl(srvCfgMapService + "/export?"
				+ Val.escapeXml(requestParameters));
	}

}
