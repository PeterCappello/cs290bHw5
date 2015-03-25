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
package applications.euclideantsp;

import clients.ClientEuclideanTsp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import static util.EuclideanGraph.distance;

/**
 *
 * @author Peter Cappello
 */
final public class LowerBoundNearestNeighbors implements LowerBound 
{
    static final private Integer EMPTY = -1;
    static final private double[][] CITIES = ClientEuclideanTsp.CITIES;
    
           final private List<Deque<Integer>> nearestNeighbors;
                 private  double lowerBound;
    
    public LowerBoundNearestNeighbors()
    {
        nearestNeighbors = initializeNearestNeighbors();
        lowerBound = initializeLowerBound();
    }
    
    @Override
    public double initializeLowerBound()
    {
        double bound = 0.0;
        for ( Deque<Integer> neighbors : nearestNeighbors )
        {
            bound += neighbors.peekFirst();
            bound += neighbors.peekLast();
        }
        return bound / 2.0;
    }
    
    private List<Deque<Integer>> initializeNearestNeighbors()
    {
        final List<Deque<Integer>> neighbors = new ArrayList<>();
        for ( int city = 0; city < CITIES.length; city++ )
        {
            Deque<Integer> cityNearestNeighbors = new ArrayDeque<>();
            cityNearestNeighbors.add( EMPTY );
            cityNearestNeighbors.add( EMPTY );
            for ( int neighbor = 0; neighbor < CITIES.length; city++ )
            {
                if ( neighbor != city )
                {
                    if ( distance( CITIES[ city ], CITIES[ neighbor ] ) < cityNearestNeighbors.peekFirst() )
                    {
                        cityNearestNeighbors.removeLast();
                        cityNearestNeighbors.addFirst( neighbor );
                    }
                    else if ( distance( CITIES[ city ], CITIES[ neighbor ] ) < cityNearestNeighbors.peekLast() )
                    {
                        cityNearestNeighbors.removeLast();
                        cityNearestNeighbors.addLast( neighbor );
                    }
                }
            }
            assert ! cityNearestNeighbors.peekFirst().equals( EMPTY )
                && ! cityNearestNeighbors.peekLast().equals(  EMPTY )
                && cityNearestNeighbors.peekFirst() < cityNearestNeighbors.peekLast();
        }
        return neighbors;
    }

    @Override
    public double lowerBound() { return lowerBound; }

    @Override
    public void update( final Integer city1, final Integer city2 ) 
    {
        updateEndpoint( city1, city2 );
        updateEndpoint( city2, city1 );
    }
    
    public void updateEndpoint( final Integer city, final Integer newEndpoint)
    {
        Integer oldEndpoint = nearestNeighbors.get( city ).removeFirst();
        lowerBound += (  distance( CITIES[ city ], CITIES[ newEndpoint ] )
                       - distance( CITIES[ city ], CITIES[ oldEndpoint ] ) 
                      ) / 2.0;
    }
}
