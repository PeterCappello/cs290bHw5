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
    static final private Integer    EMPTY = -1;
    static final private double[][] CITIES = ClientEuclideanTsp.CITIES;
//    {
//        { 0, 0 },
//        { 0, 1 },
//        { 0, 2 },
//        { 0, 3 }
//    };
    
           final private List<Deque<Integer>> nearestNeighbors;
                 private double lowerBound;
    
    public LowerBoundNearestNeighbors()
    {
        nearestNeighbors = initializeNearestNeighbors();
        lowerBound = initializeLowerBound();
    }
    
    public LowerBoundNearestNeighbors( final List<Deque<Integer>> nearestNeighbors, final double lowerBound ) 
    {
        List<Deque<Integer>> copyNearestNeighbors = new ArrayList<>();
        for ( int city = 0; city < nearestNeighbors.size(); city++ )
        {
//            Deque<Integer> deque = new ArrayDeque<>( nearestNeighbors.get( city ) );
//            System.out.println(">>> nearestNeighbors.get( " + city + " ): " + nearestNeighbors.get( city ) );
            Deque<Integer> deque = new ArrayDeque<>();
            Integer[] array = nearestNeighbors.get( city ).toArray(new Integer[0]);
            for ( int neighbor = 0; neighbor < array.length; neighbor++ )
            {
                deque.add( array[ neighbor ] ); 
            }
//            System.out.println(">>> COPY: " + deque );
            copyNearestNeighbors.add( deque );
        }
        this.nearestNeighbors = copyNearestNeighbors;
        
        //!! check that copy == original
        this.lowerBound = lowerBound;
        // not true when 1 or more elements of 1 or mre deques have been removed.  What is true?
        assert this.lowerBound == initializeLowerBound() : lowerBound + " " + this.lowerBound + " " +  initializeLowerBound();
    }
    
    @Override
    public double initializeLowerBound()
    {
        double bound = 0.0;
        for ( int city = 0; city < CITIES.length; city++ )
        {
            final Deque<Integer> deque = nearestNeighbors.get( city );
            bound += distance( CITIES[ city ], CITIES[ deque.peekFirst() ] );
            bound += distance( CITIES[ city ], CITIES[ deque.peekLast()  ] );
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
            for ( int neighbor = 0; neighbor < CITIES.length; neighbor++ )
            {
                if ( neighbor != city )
                {
                    if ( cityNearestNeighbors.peekFirst().equals( EMPTY ) || distance( CITIES[ city ], CITIES[ neighbor ] ) < distance( CITIES[ city ], CITIES[ cityNearestNeighbors.peekFirst() ] ) )
                    {
                        cityNearestNeighbors.removeLast();
                        cityNearestNeighbors.addFirst( neighbor );
                    }
                    else if ( cityNearestNeighbors.peekLast().equals( EMPTY ) || distance( CITIES[ city ], CITIES[ neighbor ] ) < distance( CITIES[ city ], CITIES[ cityNearestNeighbors.peekLast() ] ) )
                    {
                        cityNearestNeighbors.removeLast();
                        cityNearestNeighbors.addLast( neighbor );
                    }
                }
            }
            System.out.println("city: " + city + " deque: " + cityNearestNeighbors );
            assert ! cityNearestNeighbors.peekFirst().equals( EMPTY );
            assert ! cityNearestNeighbors.peekLast().equals(  EMPTY );
            assert distance( CITIES[ city ], CITIES[ cityNearestNeighbors.peekFirst() ] ) <= distance( CITIES[ city ], CITIES[ cityNearestNeighbors.peekLast() ] );
            neighbors.add( cityNearestNeighbors );
        }
        return neighbors;
    }

    @Override
    public double lowerBound() { return lowerBound; }

    @Override
    public void update( final Integer city1, final Integer city2 ) 
    {
//        System.out.println("update: city1: " + city1 + " city2: " + city2 );
        updateEndpoint( city1, city2 );
        updateEndpoint( city2, city1 );
    }
    
    public void updateEndpoint( final Integer city, final Integer newEndpoint)
    {
        System.out.println("updateEndpoint: city: " + city + " newEndpoint: " + newEndpoint + "\tBEFORE: deque: " + nearestNeighbors.get( city ) );
        if ( newEndpoint.equals( nearestNeighbors.get( city ).peekFirst() ) )
        {
            System.out.println("\t\t\t\t\tAFTER: deque: " + nearestNeighbors.get( city ) + " == FIRST: city: " + city + " new-old endpoints " + newEndpoint + " == " + nearestNeighbors.get( city ).peekFirst() );
            nearestNeighbors.get( city ).removeFirst();
        }
        else if ( newEndpoint.equals( nearestNeighbors.get( city ).peekLast() ) )
        {
            System.out.println("\t\t\t\t\tAFTER: deque.size(): " + nearestNeighbors.get( city ) + " == LAST: " + city + " new-old endpoints " + newEndpoint + " == " + nearestNeighbors.get( city ).peekLast() );
            nearestNeighbors.get( city ).removeLast();
        }
        else // it is a endpoint different from this city's 2 nearest neighbors
        {
            Integer oldEndpoint = nearestNeighbors.get( city ).removeLast();
            double deltaDistance = distance( CITIES[ city ], CITIES[ newEndpoint ] ) - distance( CITIES[ city ], CITIES[ oldEndpoint ] );
            lowerBound += deltaDistance / 2.0;
            System.out.println("\t\t\t\t\tAFTER: deque.size(): " + nearestNeighbors.get( city ) + " oldEndpoint: " + oldEndpoint + " increment: " + deltaDistance );
            assert deltaDistance >= 0.0;
        }
    }
    
    public static void main( String[] args ) throws Exception
    {
        LowerBoundNearestNeighbors lowerBoundNearestNeighbors = new LowerBoundNearestNeighbors();
        lowerBoundNearestNeighbors.initializeLowerBound();
        for ( int city = 0; city < lowerBoundNearestNeighbors.nearestNeighbors.size(); city++ )
        {
            Deque<Integer> deque = lowerBoundNearestNeighbors.nearestNeighbors.get( city );
            System.out.println("City " + city + ": " + deque.peekFirst() + " " + deque.peekLast() );
        }
        System.out.println("Lower bound: " + lowerBoundNearestNeighbors.lowerBound + " expecting 5.");
    }

    @Override
    public LowerBound clone() 
    {
        return new LowerBoundNearestNeighbors( nearestNeighbors, lowerBound );
    }
}
