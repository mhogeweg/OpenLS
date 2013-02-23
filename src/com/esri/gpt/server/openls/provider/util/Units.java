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


public class Units {
    public static final int DECIMAL_DEGREES     = 0;
    public static final int CENTIMETERS         = 1;
    public static final int INCHES              = 2;
    public static final int FEET                = 3;
    public static final int MILES               = 4;
    public static final int METERS              = 5;
    public static final int KILOMETERS          = 6;
    public static final int MM                  = 7;
    public static final int UNKNOWN             = 8;

    public static final String[] unitStrings = {"DECIMAL_DEGREES",
                                                "CENTIMETERS",
                                                "INCHES",
                                                "FEET",
                                                "MILES",
                                                "METERS",
                                                "KILOMETERS",
                                                "MM",
                                                "UNKNOWN"
                                               };

    /*
    * Conversion constants
    *
    * Snyder p. 51, second to the last paragraph, says that 1 meter equals
    * exactly 39.37 inches, and other values shall be derived from that.
    */
    public final static double inchesPerMeter = 39.37;
    public final static double metersPerInch = (1.0 / inchesPerMeter);
    public final static double cmPerInch = (100.0 / inchesPerMeter);
    public final static double mmPerInch = (1000.0 / inchesPerMeter);
    public final static double inchesPerFoot = 12.0;
    public final static double inchesPerCm = (1.0 / cmPerInch);
    public final static double feetPerMile = 5280.0;
    public final static double inchesPerMile = (inchesPerFoot * feetPerMile);

    /**
    * Is a mapUnit supproted or not ?
    */
    public static int getMapUnitAsInt(String mapUnits){

        int units;
        if (mapUnits.equalsIgnoreCase("decimal_degrees") || mapUnits.equalsIgnoreCase("decimal_degree")
            || mapUnits.equalsIgnoreCase("degree")|| mapUnits.equalsIgnoreCase("degrees"))
             units = Units.DECIMAL_DEGREES;
        else if (mapUnits.equalsIgnoreCase("centimeters") || mapUnits.equalsIgnoreCase("centimeter"))
             units = Units.CENTIMETERS;
        else if (mapUnits.equalsIgnoreCase("inches") || mapUnits.equalsIgnoreCase("inch"))
             units = Units.INCHES;
        else if (mapUnits.equalsIgnoreCase("feet"))
             units = Units.FEET;
        else if (mapUnits.equalsIgnoreCase("miles") || mapUnits.equalsIgnoreCase("mile"))
             units = Units.MILES;
        else if (mapUnits.equalsIgnoreCase("meters") || mapUnits.equalsIgnoreCase("meter"))
             units = Units.METERS;
        else if (mapUnits.equalsIgnoreCase("kilometers") || mapUnits.equalsIgnoreCase("km"))
             units = Units.KILOMETERS;
        else if (mapUnits.equalsIgnoreCase("mm"))
             units = Units.MM;
        else
             units = Units.UNKNOWN;

        return units;
    }

    /**
    * Is a mapUnit supproted or not ?
    */
    public static boolean isMapUnitSupported(int mapUnit){
       if (mapUnit == 0 ||(mapUnit >=3 && mapUnit <= 6)) return true;
       else return false;
    }

    /**
    *  Converts a mapscale to cartographic scale for the given map unit.
    * mapScale is in decimal degrees
    */
    public static long convertMapScaleToCartScale(
        double mapScale,
        int mapUnit,
        double dpi
    ) {
        double cartScale = convert(mapScale, mapUnit, INCHES) * dpi;
        return (long)Math.round(cartScale);
   }

    /**
    *  Converts a mapscale to cartographic scale for the given map unit
    */
    public static long convertMapScaleToCartScale(
        double mapScale,
        String unit,
        double dpi
    ) {
        return convertMapScaleToCartScale(mapScale,stringUnitToInt(unit), dpi);
    }

    /**
    * converts the given cart scale to the map scale
    */
    public static double convertCartScaleToMapScale(
        long cartScale,
        int mapUnit,
        double dpi
    ) {
	return convert((double)cartScale / dpi, INCHES, mapUnit);
    }

    /**
    * converts the given cart scale to the map scale
    */
    public static double convertCartScaleToMapScale(
        long cartScale,
        String mapUnit,
        double dpi
    ) {
        return convertCartScaleToMapScale(cartScale,stringUnitToInt(mapUnit), dpi);
    }

    /**
    *  Returns the name of the unit for the given integer type
    *  "unknown" is returned if the type does not exist.
    */
    public static String intUnitToString(int type) {
        if (type >= 0 && type < unitStrings.length)
            return unitStrings[type];

        return "unknown";
    }

    /**
    *  Returns the type of the unit with the given name.
    *  -1 is returned if the name does not exist.
    */
    public static int stringUnitToInt(String name) {
        for (int i = 0; i < unitStrings.length; ++i)
            if (name.equalsIgnoreCase(unitStrings[i]))
                return i;

        return -1;
    }

    /*
    * Converts decimal degrees to meters
    */
    private static double DegreesToMeters(double degrees)
    {
      // This is a bastardization of the algorithm used in DegreePtsToMeters
      // for use with a distance instead of two points.  It is only accurate
      // at the equator, and gets less accurate the farther you get from the
      // equator.

      long numHalfCircles = (long)(degrees / 180);
      double remainder = degrees - (numHalfCircles*180);
      double L = (remainder * (3.14159265358979323846 / 180.0)) / 2;
      double sL = Math.sin(L);
      double cL = Math.cos(L);
      return (20015077.0 * (double) numHalfCircles)
               + (12741994.0 * Math.atan(Math.sqrt((sL*sL)/(cL*cL))));
    }

    /*
    * Converts meters to decimal degrees
    */
    private static double MetersToDegrees(double meters)
    {
      // This routine converts a distance in meters along the equator
      // to the equivalent number of decimal degrees.  This number is
      // only valid along the equator and assumes the GRS 80 spheroid.
      // The constant used here is from the ArcView code base in meas.c.
      return (meters * 0.000008993220293);
    }

    /*
    * Converts the value from meters to the specified units
    */
    private static double FromMeters(double meters, int nUnits)
    {
      switch ( nUnits )
        {
        case INCHES:
          return (meters * inchesPerMeter);
        case FEET:
          return ((meters * inchesPerMeter) / inchesPerFoot);
        case MILES:
          return ((meters * inchesPerMeter) / inchesPerMile);
        case MM:
          return (meters * 1000.0);
        case METERS:
          return meters;
        case KILOMETERS:
          return (meters / 1000.0);
        case DECIMAL_DEGREES:
          return MetersToDegrees(meters);
        default:
          break;
        }

      return meters;
    }

    /*
    * Converts the value from the specified units to meters
    */
    private static double ToMeters(double value, int nUnits)
    {
      switch ( nUnits )
        {
        case INCHES:
          return (value * metersPerInch);
        case FEET:
          return ((value * inchesPerFoot) * metersPerInch);
        case MILES:
          return ((value * inchesPerMile) * metersPerInch);
        case MM:
          return (value / 1000.0);
        case METERS:
          return value;
        case KILOMETERS:
          return (value * 1000.0);
        case DECIMAL_DEGREES:
          return DegreesToMeters(value);
        default:
          break;
        }

      return value;
    }

    /*
    * Conversion
    */
    public static double convert( double value, int nFromUnits, int nToUnits )
    {
      if ( nFromUnits == nToUnits )
        return value;

      if ( nFromUnits == UNKNOWN || nToUnits == UNKNOWN )
        return value;

      return FromMeters( ToMeters(value, nFromUnits), nToUnits );
    }
}



