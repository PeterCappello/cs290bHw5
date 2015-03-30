/*
 * The MIT License
 *
 * Copyright 2015 petercappello.
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
import java.util.ArrayList;
import java.util.List;
import static util.EuclideanGraph.tourDistance;

/**
 *
 * @author petercappello
 */
final public class LowerBoundPartialTour implements LowerBound
{
    static final private double[][] CITIES = ClientEuclideanTsp.CITIES;
    
           final private List<Integer> partialTour;
                 private double lowerBound;
    
    public LowerBoundPartialTour( final List<Integer> partialTour )
    {
        this.partialTour = new ArrayList( partialTour );
        lowerBound = initializeLowerBound();
    }
    
    @Override
    public double initializeLowerBound() { return tourDistance( CITIES, partialTour ); }

    @Override
    public double cost() { return lowerBound; }

//    @Override
//    public void update( final Integer city, final Integer newCity ) 
//    {
//        partialTour.add( newCity );
////        cost = initializeLowerBound();
//        lowerBound = parentTask.lowerBound // compute cost in O(1) time using parentTask.cost
//                - distance( CITIES[ 0 ], CITIES[ partialTour.get( partialTour.size() - 1 ) ] )
//                + distance( CITIES[ 0 ], CITIES[ newCity ] )
//                + distance( CITIES[ partialTour.get( partialTour.size() - 1 ) ], CITIES[ newCity ] );
//    }

//    @Override
//    public LowerBound clone() { return new LowerBoundPartialTour( newPartialTour ); }

    @Override
    public LowerBound make( TaskEuclideanTsp parentTask, Integer newCity ) 
    {
        List<Integer> newPartialTour = parentTask.tour();
        newPartialTour.add( newCity );
        return new LowerBoundPartialTour( newPartialTour );
    }
}
