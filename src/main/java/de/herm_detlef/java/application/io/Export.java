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

package de.herm_detlef.java.application.io;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.model.ExerciseItem;
import de.herm_detlef.java.application.model.ExerciseItem.AnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.ItemPart;
import de.herm_detlef.java.application.model.ExerciseItem.MultipleChoiceAnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.QuestionCode;
import de.herm_detlef.java.application.model.ExerciseItem.QuestionText;
import de.herm_detlef.java.application.model.ExerciseItem.QuestionText2;
import de.herm_detlef.java.application.model.ExerciseItem.SingleChoiceAnswerText;
import de.herm_detlef.java.application.model.ExerciseItem.SolutionText;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Export {

    private static XMLReaderJDOMFactory schemafac;

    private static boolean              isQuestionPart = false;
    private static boolean              isAnswerPart   = false;

    static {

        try {
            InputStream in = Import.class.getResourceAsStream( ApplicationConstants.XML_SCHEMA_DEFINITION ); 
            schemafac = new XMLReaderXSDFactory( new StreamSource( in ) );

        } catch ( JDOMException e ) {
            Utilities.showErrorMessage(
                e.getClass().getSimpleName(),
                e.getMessage() );
            e.printStackTrace();
        }
    }

    public static void exportExerciseItemListToFile(CommonData commonData,
                                                    File file ) {

        commonData.markSelectedAnswerPartItems();

        final Document doc = createJdomDocument( commonData.getExerciseItemListMaster() );
        assert doc != null;

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat( Format.getPrettyFormat() );

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( 1_000_000 );
            xmlOutput.output( doc, outputStream );
            ByteArrayInputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray() );
            validateDocument( inputStream );

            xmlOutput.output( doc, new FileWriter( file.getCanonicalPath() ) );

            commonData.savedExerciseItemListMaster();
            commonData.setRecentlySavedFile( file );
            commonData.setRecentlyOpenedFile( file );

            commonData.getExerciseItemListInitialMaster().clear();
            commonData.getExerciseItemListInitialMaster().addAll( commonData.getExerciseItemListMaster() );

        } catch ( IOException | JDOMException e ) {
            Utilities.showErrorMessage( e.getClass().getSimpleName(), e.getMessage() );
            // e.printStackTrace();
        }

    }

    private static void validateDocument( ByteArrayInputStream inputStream ) throws JDOMException, IOException {

        assert schemafac != null;

        SAXBuilder builder = new SAXBuilder( schemafac );

        // XML validation against schema definition happens here:
        builder.build( inputStream );
    }

    private static Document createJdomDocument( ObservableList< ExerciseItem > exerciseItemList ) {

        Namespace ns = Namespace.getNamespace( ApplicationConstants.XML_NAMESPACE );
        Element catalog = new Element( TAG.CATALOG.name(), ns );
        Document doc = new Document( catalog );
        Namespace xsi = Namespace.getNamespace( "xsi", ApplicationConstants.XML_SCHEMA_INSTANCE );
        doc.getRootElement().addNamespaceDeclaration( xsi );
        doc.getRootElement().setAttribute(
            "schemaLocation",
            ApplicationConstants.XML_SCHEMA_DEFINITION,
            xsi );

        int count = 0;
        for ( ExerciseItem exItem : exerciseItemList ) {

            Element item = new Element( TAG.ITEM.name(), ns );
            catalog.addContent( item );

            Element id = new Element( TAG.ID.name(), ns );
            id.addContent( String.valueOf( ++count ) );
            item.addContent( id );

            Element element = null;

            for ( ItemPart itemPart : exItem.getExerciseItemParts() ) {
                Object obj = itemPart.get();

                if ( obj == null ) {
                    // assert false : String.format(
                    // "%s",
                    // itemPart.getClass() ); // TODO
                    continue;
                }

                if ( itemPart instanceof QuestionText
                     || itemPart instanceof QuestionText2 ) {
                    isAnswerPart = false;

                    if ( !isQuestionPart ) {
                        element = new Element( TAG.QUESTION.name(), ns );
                        item.addContent( element );
                        isQuestionPart = true;
                    }

                    if ( element == null ) continue;

                    if ( itemPart instanceof QuestionText ) {
                        Element text = new Element( TAG.TEXT.name(), ns );
                        element.addContent( text );
                        text.addContent( itemPart.getStr().get() );
                    }

                    if ( itemPart instanceof QuestionText2 ) {
                        Element text = new Element( TAG.TEXT2.name(), ns );
                        element.addContent( text );
                        text.addContent( itemPart.getStr().get() );
                    }
                }

                else if ( itemPart instanceof QuestionCode ) {
                    isAnswerPart = false;

                    if ( !isQuestionPart ) {
                        element = new Element( TAG.QUESTION.name(), ns );
                        item.addContent( element );
                        isQuestionPart = true;
                    }

                    if ( element == null ) continue;

                    Element code = new Element( TAG.CODE.name(), ns );
                    element.addContent( code );

                    code.addContent( itemPart.getStr().get() );
                }

                else if ( itemPart instanceof SingleChoiceAnswerText ) {
                    isQuestionPart = false;

                    if ( !isAnswerPart ) {
                        element = new Element( TAG.SINGLE_CHOICE_ANSWER.name(), ns );
                        item.addContent( element );
                        isAnswerPart = true;
                    }

                    if ( element == null ) continue;

                    Element answer = new Element( TAG.TEXT.name(), ns );
                    element.addContent( answer );

                    if ( ( ( AnswerText ) itemPart ).isMarked() ) {
                        Attribute mark = new Attribute( "mark", "true" );
                        answer.setAttribute( mark );
                    }

                    answer.addContent( ( ( SingleChoiceAnswerText ) itemPart ).getStr().get() );
                }

                else if ( itemPart instanceof MultipleChoiceAnswerText ) {
                    isQuestionPart = false;

                    if ( !isAnswerPart ) {
                        element = new Element( TAG.MULTIPLE_CHOICE_ANSWER.name(), ns );
                        item.addContent( element );
                        isAnswerPart = true;
                    }

                    if ( element == null ) continue;

                    Element answer = new Element( TAG.TEXT.name(), ns );
                    element.addContent( answer );

                    if ( ( ( AnswerText ) itemPart ).isMarked() ) {
                        Attribute mark = new Attribute( "mark", "true" );
                        answer.setAttribute( mark );
                    }

                    answer.addContent( ( ( MultipleChoiceAnswerText ) itemPart ).getStr().get() );
                }

                else if ( itemPart instanceof SolutionText ) {
                    isQuestionPart = false;
                    isAnswerPart = false;


                    element = new Element( TAG.SOLUTION.name(), ns );
                    item.addContent( element );

                    Element solution = new Element( TAG.TEXT.name(), ns );
                    element.addContent( solution );

                    solution.addContent( ( ( SolutionText ) itemPart ).getStr().get() );
                }

                if ( element == null ) {
                    assert false;
                    return null;
                }
            }
        }

        return doc;
    }

    public static boolean validateCurrentExerciseItem( CommonData commonData, boolean showErrorMessageJDOM ) {

        final ObservableList< ExerciseItem > exerciseItemList = FXCollections.observableArrayList();

        exerciseItemList.add( commonData.getCurrentExerciseItem() );

        Document doc = createJdomDocument( exerciseItemList );

        XMLOutputter xmlOutput = new XMLOutputter();

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream( 1_000_000 );

            xmlOutput.output( doc, outputStream );

            ByteArrayInputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray() );

            validateDocument( inputStream );

        } catch ( JDOMException e ) {

            if ( !showErrorMessageJDOM )
                return false;

            Utilities.showErrorMessage( e.getClass().getSimpleName(), e.getMessage() );
            //e.printStackTrace();
            return false;

        } catch ( IOException e ) {

            Utilities.showErrorMessage( e.getClass().getSimpleName(), e.getMessage() );
            //e.printStackTrace();
            return false;
        }

        return true;
    }
}
