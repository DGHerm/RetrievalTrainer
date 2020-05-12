package de.herm_detlef.java.application.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import java.lang.Math;

/* @formatter:off */

public class TextAreaHelper extends TextArea {

    private final String                        text;

    private ScrollPane                          sp;

    private final DoubleProperty                viewportHeightProperty = new SimpleDoubleProperty();

    private boolean                             once = false;


    private TextAreaHelper( String text ) {
        this.text = text;
    }

    public static TextAreaHelper create( String text ) {

        TextAreaHelper textAreaHelper = new TextAreaHelper( text );

        textAreaHelper.initialize();

        return textAreaHelper;
    }

    private void initialize() {

        boundsInLocalProperty().addListener( (obj, oldValue, newValue) -> {

            // earliest point in time to lookup for the instance of class ScrollPane
            if ( sp == null ) {
                sp = (ScrollPane) lookup( ".scroll-pane" );

                if ( sp == null ) {
                    assert false;
                    return;
                }

                setStyle( "-fx-background-color: transparent; -fx-border-color: transparent" );
                sp.setStyle( "-fx-background-color: transparent; -fx-border-color: transparent" );
                sp.getContent().setStyle( "-fx-background-color: transparent; -fx-border-color: transparent" );


                sp.setVbarPolicy( ScrollPane.ScrollBarPolicy.NEVER );
                sp.applyCss();
                sp.viewportBoundsProperty().addListener( this::viewportBoundsPropertyChangeListener );
            }

            if ( text != null && ! once ) {

                // stretch the node vertically to fit the content

                once = true;

                //setText( text );// sets the caret to before the first char of text
                //paste();// OK

                replaceText( 0, 0, text );// sets the caret to after the last char of the text
                                          // and calls the private method 'updateContent'

                //end();// sets the caret to after the last char of the text
                        // and calls the private method 'updateContent'
                layout();
                return;
            }
        });

        setWrapText( true );
        setMinHeight( 32.0 );
        setMaxHeight( 500.0 );
        setPrefHeight( 32.0 );
    }

    private void viewportBoundsPropertyChangeListener( ObservableValue< ? extends Bounds > obj, Bounds oldValue, Bounds newValue ) {

        Bounds b = sp.viewportBoundsProperty().getValue();

        if ( b == null ) {
            assert false;
            return;
        }

        assert newValue == null || (newValue.getMinY() == b.getMinY());

        double viewportHeight = b.getHeight() + Math.abs( newValue != null ? newValue.getMinY() : b.getMinY() );

        if ( Math.abs(viewportHeightProperty.doubleValue() - viewportHeight) < 0.01 ) {
            sp.setVisible( true );
            return;
        }

        BoundingBox bBox = new BoundingBox( 0.0,
                                            0.0,
                                            0.0,
                                            b.getWidth(),
                                            viewportHeight,
                                            b.getDepth() );

        sp.viewportBoundsProperty().set(
            ( BoundingBox ) bBox );

        double m = Math.ceil( viewportHeight / getMinHeight() ) * getMinHeight();
        setPrefHeight( m );
        sp.setVisible( true );
    }
}


