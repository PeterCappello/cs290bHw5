/*
 * The MIT License
 *
 * Copyright 2016 petercappello.
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

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static util.EuclideanGraph.distance;
import static util.EuclideanGraph.tourDistance;

/**
 * Make graph of numCities cities in unit square.
 * @author Peter Cappello
 */
public class Graph 
{
    static public double[][] makeGraph( int numCities, int seed )
    {        
        Random random = new Random( seed );
        double[][] graph = new double[ numCities ][ 2 ];
        for ( int city = 0; city < numCities; city++ )
        {
            graph[ city ] = new double[] { random.nextFloat(), random.nextFloat() };
        }
        return graph;
    }
    
    public static List<Integer> greedyTour( double[][] cities )
    {
        List<Integer> tour = Arrays.asList( 0 );
        List<Integer> unvisitedCities = IntStream.range( 1, cities.length ).boxed().collect( Collectors.toList() );
        for ( int nearestCity = -1, baseCity = 0; ! unvisitedCities.isEmpty(); baseCity = nearestCity )
        {
            // select unvisited city that is nearest to baseCity 
            double nearestCityDistance = Double.MAX_VALUE;
            int nearestCityIndex = 0;
            final int numUnvisitedCities = unvisitedCities.size();
            for ( int i = 0; i < numUnvisitedCities; i++ )
            {
                Integer nextCity = unvisitedCities.get( i );
                double nextCityDistance = distance( cities[ baseCity ], cities[ nextCity ] );
                if ( nextCityDistance < nearestCityDistance )
                {
                   nearestCity = nextCity;
                   nearestCityDistance = nextCityDistance;
                   nearestCityIndex = i;
                }
            }
            unvisitedCities.remove( nearestCityIndex );
            tour.add( nearestCity );
        }
        Logger.getLogger( EuclideanGraph.class.getCanonicalName() )
              .log(Level.INFO, "\n\tTour: {0}\n\tCost: {1}", new Object[]{ tour, tourDistance( cities, tour ) } );
        return tour;
    }
}
