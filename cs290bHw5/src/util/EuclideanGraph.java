/*
 * The MIT License
 *
 * Copyright 2015 Peter Cappello.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package util;

import applications.euclideantsp.TaskEuclideanTsp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Generate pseudorandom Euclidean graphs.
 * @author Peter Cappello
 */
public class EuclideanGraph 
{
    static final private int X = 0;
    static final private int Y = 1;
    
    /**
     *
     * @param numVertices
     * @param seed
     * @return
     */
    public static double[][] generateRandomGraph( int numVertices, long seed )
    {
        double[][] vertices = new double[ numVertices ][ 2 ];
        Random random = new Random( seed );
        for ( double[] vertex : vertices )
        {
            vertex[ X ] = random.nextDouble();
            vertex[ Y ] = random.nextDouble();
        }
        return vertices;
    }
    
    public static List<Integer> greedyTour( double[][] cities )
    {
        List<Integer> tour = new LinkedList<>();
        List<Integer> unvisitedCities = new ArrayList<>();
        for ( int unvisitedCity = 1; unvisitedCity < cities.length; unvisitedCity++ )
        {
            unvisitedCities.add( unvisitedCity );
        }
        tour.add( 0 );
        int baseCity = 0;
        while ( ! unvisitedCities.isEmpty() )
        {
            // select city that is nearest to me among the unvisited cities
            int nearestCity = -1;
            double nearestCityDistance = Double.MAX_VALUE;
            for ( Integer nextCity : unvisitedCities )
            {
                double nextCityDistance = distance( cities[ baseCity ], cities[ nextCity ] );
                if ( nextCityDistance < nearestCityDistance )
                {
                   nearestCity = nextCity;
                   nearestCityDistance = nextCityDistance;
                }
            }
            tour.add( nearestCity );
            unvisitedCities.remove( new Integer( nearestCity ) );
        }
        return tour;
    }
    
    public static double tourDistance( final double[][] cities, final List<Integer> tour )
    {
        double cost = 0.0;
        for ( int city = 0; city < tour.size() - 1; city ++ )
        {
            cost += distance( cities[ tour.get( city ) ], cities[ tour.get( city + 1 ) ] );
        }
        return cost + distance( cities[ tour.get( tour.size() - 1 ) ], cities[ 0 ] );
    }
    
    public static double distance( final double[] city1, final double[] city2 )
    {
        final double deltaX = city1[ 0 ] - city2[ 0 ];
        final double deltaY = city1[ 1 ] - city2[ 1 ];
        return Math.sqrt( deltaX * deltaX + deltaY * deltaY );
    }
}