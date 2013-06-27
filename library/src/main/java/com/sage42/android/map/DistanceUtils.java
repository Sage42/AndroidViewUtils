package com.sage42.android.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Copyright (C) 2013- Sage 42 App Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Some formulas taken from http://www.movable-type.co.uk/scripts/latlong.html 
 * 
 * @author Corey Scott (corey.scott@sage42.com)
 *
 */
public class DistanceUtils
{
    private static final int    METERS_IN_KM = 1000;

    private static final double EARTH_RADIUS = 6371.0d;

    private DistanceUtils()
    {
        // enforcing singleton
        super();
    }

    /*
     * This is an approximation of the actual earth distance
     */
    public static double distanceInMetersFromLatLngs(final LatLng origin, final LatLng destination)
    {
        final double degreesToKilometers = (Math.PI * 6371) / 180; // NOSONAR
        final double diffLat = origin.latitude - destination.latitude;
        final double diffLng = origin.longitude - destination.longitude;

        // calc distance in meters
        return degreesToKilometers * (Math.sqrt((diffLat * diffLat) + (diffLng * diffLng)))
                * DistanceUtils.METERS_IN_KM;
    }

    /**
     * REF: http://worldwind31.arc.nasa.gov/svn/trunk/WorldWind/src/gov/nasa/worldwind/geom/LatLon.java
     * 
     * Computes the azimuth angle (clockwise from North) that points from the first location to the second location.
     * This angle can be used as the starting azimuth for a great circle arc that begins at the first location, and
     * passes through the second location.
     *
     * @param origin LatLon of the first location
     * @param p2 LatLon of the second location
     *
     * @return Angle that points from the first location to the second location.
     */
    public static double getBearing(final LatLng origin, final LatLng destination)
    {
        final double lat1 = Math.toRadians(origin.latitude);
        final double lon1 = Math.toRadians(origin.longitude);
        final double lat2 = Math.toRadians(destination.latitude);
        final double lon2 = Math.toRadians(destination.longitude);

        if (lat1 == lat2 && lon1 == lon2)
        {
            return 0d;
        }

        if (lon1 == lon2)
        {
            return lat1 > lat2 ? 180d : 0d;
        }

        // Taken from "Map Projections - A Working Manual", page 30, equation 5-4b.
        // The atan2() function is used in place of the traditional atan(y/x) to simplify the case when x==0.
        final double y = Math.cos(lat2) * Math.sin(lon2 - lon1);
        final double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        final double azimuthRadians = Math.atan2(y, x);

        return Double.isNaN(azimuthRadians) ? 0d : Math.toDegrees(azimuthRadians);
    }

    public static LatLng getLatLngFromBearingAndDistance(final LatLng origin, final double bearingInDegrees,
            final double distanceInKM)
    {
        final double originLat = Math.toRadians(origin.latitude);
        final double originLong = Math.toRadians(origin.longitude);
        final double bearing = Math.toRadians(bearingInDegrees);

        final double latDest = Math.asin((Math.sin(originLat) * Math.cos(distanceInKM / DistanceUtils.EARTH_RADIUS))
                + (Math.cos(originLat) * Math.sin(distanceInKM / DistanceUtils.EARTH_RADIUS) * Math.cos(bearing)));
        final double lngDest = originLong
                + Math.atan2(
                        Math.sin(bearing) * Math.sin(distanceInKM / DistanceUtils.EARTH_RADIUS) * Math.cos(originLat),
                        Math.cos(distanceInKM / DistanceUtils.EARTH_RADIUS) - (Math.sin(originLat) * Math.sin(latDest)));

        return new LatLng(Math.toDegrees(latDest), Math.toDegrees(lngDest));
    }

    /**
     * 
     * 
     * @param viewWidthInDistance width of the map view in meters
     * @param screenWidthInPx width of the screen (in px)
     * @param clusterIconWidth with of the marker icon for clusted icon  (in px)
     * @param padding padding to add around the marker icon (in px)
     * @return
     */
    public static double minDistance(final double viewWidthInDistance, final int screenWidthInPx,
            final float clusterIconWidth, final int padding)
    {
        return (viewWidthInDistance / screenWidthInPx) * (clusterIconWidth + (padding * 2));
    }
}
