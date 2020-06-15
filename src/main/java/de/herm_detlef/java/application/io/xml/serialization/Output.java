/*
 *   Copyright 2016 Detlef Gregor Herm
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package de.herm_detlef.java.application.io.xml.serialization;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.herm_detlef.java.application.io.xml.TAG;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.herm_detlef.java.application.CommonData;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.AnswerText;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.ItemPart;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.MultipleChoiceAnswerText;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.QuestionCode;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.QuestionText;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.QuestionText2;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.SingleChoiceAnswerText;
import de.herm_detlef.java.application.mvc.model.ExerciseItem.SolutionText;
import de.herm_detlef.java.application.utilities.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static de.herm_detlef.java.application.ApplicationConstants.*;

/* @formatter:off */

/**
 * TODO
 *
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
public class Output {

    private static boolean isQuestionPart = false;
    private static boolean isAnswerPart   = false;

    private Output() {}

    public static void writeExerciseItemListToFile( CommonData commonData, File file ) throws JDOMException, IOException {

        commonData.markSelectedAnswerPartItems();

        List< ExerciseItem > exerciseItems = commonData.getExerciseItemListMaster();

        writeExerciseItemListToFile( exerciseItems, file );

        commonData.savedExerciseItemListMaster();
        commonData.setRecentlySavedFile(file);
        commonData.setRecentlyOpenedFile(file);

        commonData.getExerciseItemListInitialMaster().clear();
        commonData.getExerciseItemListInitialMaster().addAll(commonData.getExerciseItemListMaster());
    }

    public static void writeExerciseItemListToFile( List< ExerciseItem > exerciseItems, File file ) throws IOException, JDOMException {

        final Document doc = createJdomDocument( exerciseItems );
        if (DEBUG) assert doc != null;

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat( Format.getPrettyFormat() );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( 1_000_000);
        xmlOutput.output(doc, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream( outputStream.toByteArray());
        validateDocument(inputStream);

        xmlOutput.output(doc, new FileWriter( file.getCanonicalPath()));
    }

    private static void validateDocument( ByteArrayInputStream inputStream ) throws JDOMException, IOException {

        if (DEBUG) assert XML_READER_JDOM_FACTORY != null;

        SAXBuilder builder = new SAXBuilder( XML_READER_JDOM_FACTORY );

        // XML validation against schema definition happens here:
        builder.build( inputStream );
    }

    private static Document createJdomDocument( List< ExerciseItem > exerciseItemList ) {

        Namespace ns = Namespace.getNamespace( XML_NAMESPACE );
        Element catalog = new Element( TAG.CATALOG.name(), ns );
        Document doc = new Document( catalog );
        Namespace xsi = Namespace.getNamespace( "xsi", XML_SCHEMA_INSTANCE );
        doc.getRootElement().addNamespaceDeclaration( xsi );
        doc.getRootElement().setAttribute("schemaLocation", XML_SCHEMA_DEFINITION, xsi );

        int count = 0;// FIXME
        for ( ExerciseItem exItem : exerciseItemList ) {

            Element item = new Element( TAG.ITEM.name(), ns );
            catalog.addContent( item );

            Element id = new Element( TAG.ID.name(), ns );
            id.addContent( String.valueOf( ++count ) );// FIXME
            item.addContent( id );

            Element element = null;

            for ( ItemPart itemPart : exItem.getExerciseItemParts() ) {
                Object obj = itemPart.get();

                if ( obj == null ) {
                     if (DEBUG) assert false : String.format( "%s", itemPart.getClass() );
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

                    answer.addContent( itemPart.getStr().get() );
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

                    answer.addContent( itemPart.getStr().get() );
                }

                else if ( itemPart instanceof SolutionText ) {
                    isQuestionPart = false;
                    isAnswerPart = false;


                    element = new Element( TAG.SOLUTION.name(), ns );
                    item.addContent( element );

                    Element solution = new Element( TAG.TEXT.name(), ns );
                    element.addContent( solution );

                    solution.addContent( itemPart.getStr().get() );
                }

                if ( element == null ) {
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
            if (DEBUG) e.printStackTrace();
            return false;

        } catch ( IOException e ) {

            Utilities.showErrorMessage( e.getClass().getSimpleName(), e.getMessage() );
            if (DEBUG) e.printStackTrace();
            return false;
        }

        return true;
    }
}
