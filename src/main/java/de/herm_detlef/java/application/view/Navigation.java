/*
 *  Copyright 2016 Detlef Gregor Herm
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.herm_detlef.java.application.view;


import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Supplier;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.Remote;
import de.herm_detlef.java.application.controller.navigation.NavigationController;
import de.herm_detlef.java.application.model.ExerciseItem;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 */
public class Navigation {

    private static Navigation                                            left, right;
    private static ListIterator< ExerciseItem >                          iter;
    private static Node                                                  root;
    private static Function< Integer, ExerciseItem >                     preparation;

    private final CommonData                                             commonData;
    private final boolean                                                forwards;

    private final Function< ListIterator< ? >, Boolean >                 hasNextItem;
    private final Function< ListIterator< ? >, Integer >                 nextItemIndex;
    private final Function< ListIterator< ExerciseItem >, ExerciseItem > nextItem;
    private final Function< ListIterator< ? >, Boolean >                 hasPreviousItem;
    private final Supplier< Button >                                     jumpLastBtn;
    private final Supplier< Button >                                     jumpFirstBtn;
    private final Supplier< Button >                                     stepNextBtn;
    private final Supplier< Button >                                     stepPrevBtn;

    private Navigation( CommonData commonData,
                        Remote     remote,
                        boolean    forwards,
                        Button     first,
                        Button     previous,
                        Button     next,
                        Button     last ) {

        this.commonData = commonData;
        this.forwards   = forwards;

        remote.setNavigation( this );

        hasNextItem     = ( forwards ) ?     trav -> trav.hasNext() : trav -> trav.hasPrevious();
        nextItemIndex   = ( forwards ) ?   trav -> trav.nextIndex() : trav -> trav.previousIndex();
        nextItem        = ( forwards ) ?        trav -> trav.next() : trav -> trav.previous();
        hasPreviousItem = ( forwards ) ? trav -> trav.hasPrevious() : trav -> trav.hasNext();
        jumpFirstBtn    = ( forwards ) ?                () -> first : () -> last;
        stepPrevBtn     = ( forwards ) ?             () -> previous : () -> next;
        stepNextBtn     = ( forwards ) ?                 () -> next : () -> previous;
        jumpLastBtn     = ( forwards ) ?                 () -> last : () -> first;
    }

    public static void init( CommonData                        commonData,
                             Remote                            remote,
                             NavigationController              navi,
                             Function< Integer, ExerciseItem > preparation,
                             Parent                            root ) {

        final Button first    = navi.getFirst();
        final Button previous = navi.getPrevious();
        final Button next     = navi.getNext();
        final Button last     = navi.getLast();

        // TODO exception handling
        if ( commonData.getExerciseItemListShuffledSubset().size() == 0 ) {
            assert false;
            return;
        }

        Navigation.iter        = commonData.getExerciseItemListShuffledSubset().listIterator();
        Navigation.root        = root;
        Navigation.preparation = preparation;

        left = new Navigation( commonData,
                               remote,
                               false,
                               first,
                               previous,
                               next,
                               last );

        right = new Navigation( commonData,
                                remote,
                                true,
                                first,
                                previous,
                                next,
                                last );

        // adjust iterator to actual 'currentExerciseItem':

        while ( Navigation.iter.hasPrevious() ) {
            Navigation.iter.previous();
        }

        while ( Navigation.iter.hasNext() ) {
            if ( Navigation.iter.next() == commonData.getCurrentExerciseItem() ) {
                break;
            }
        }
    }

    public static Navigation leftDirection() {

        return left;
    }

    public static Navigation rightDirection() {

        return right;
    }

    public boolean hasNextStep() {

        return hasNextItem.apply( iter );
    }

    public void step() {

        assert commonData.getExerciseItemListShuffledSubset().size() > 0;

        try {
            root.setDisable( true );

            if ( hasNextItem.apply( iter ) ) {
                int i = nextItemIndex.apply( iter );
                ExerciseItem item = nextItem.apply( iter );

                if ( commonData.getCurrentExerciseItem() != item ) {
                    commonData.setCurrentExerciseItem( item );
                    preparation.apply( i );
                } else
                    if ( hasNextItem.apply( iter ) ) {
                        i = nextItemIndex.apply( iter );
                        commonData.setCurrentExerciseItem( nextItem.apply( iter ) );
                        preparation.apply( i );
                    }

                if ( !hasNextItem.apply( iter ) ) {
                    jumpLastBtn.get().setDisable( true );
                    stepNextBtn.get().setDisable( true );
                }

                if ( hasPreviousItem.apply( iter ) ) {
                    stepPrevBtn.get().setDisable( false );
                    jumpFirstBtn.get().setDisable( false );
                }
            }
        } finally {
            root.setDisable( false );
        }
    }

    public void jumpEnd() {

        assert commonData.getExerciseItemListShuffledSubset().size() > 0;

        try {
            root.setDisable( true ); // avoid screen flickering

            while ( hasNextItem.apply( iter ) ) {
                nextItem.apply( iter );
            }

            commonData.setCurrentExerciseItem(
                preparation.apply( forwards ? commonData.getExerciseItemListShuffledSubset().size() - 1 : 0 ) );

            jumpFirstBtn.get().setDisable( false );
            stepPrevBtn.get().setDisable( false );
            stepNextBtn.get().setDisable( true );
            jumpLastBtn.get().setDisable( true );

        } finally {
            root.setDisable( false );
        }
    }
}
