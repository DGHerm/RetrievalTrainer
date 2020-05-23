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


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;

import de.herm_detlef.java.application.ApplicationConstants;
import de.herm_detlef.java.application.mvc.model.ExerciseItem;
import org.xml.sax.SAXException;

/* @formatter:off */

/**
 * TODO
 * @author Detlef G. Herm, 2016-09-26
 * @since 1.0
 *
 */
class Import {

    private static ArrayList< ExerciseItem > exerciseItemList;
    private static ExerciseItem              exerciseItem;
    private static boolean                   isQuestionPart = false;
    private static boolean                   isAnswerPart   = false;
    private static boolean                   isSolutionPart = false;

    public static ArrayList<ExerciseItem> importExerciseItemListFromFile(String filename) throws JDOMException, IOException, SAXException {

        exerciseItemList = new ArrayList<>();

        Document doc = createDocument( filename );
        Element catalogElement = doc.getRootElement();
        createNode( catalogElement );

        return exerciseItemList;
    }

    private static Document createDocument( String filename ) throws JDOMException, IOException {
        
        try ( InputStream in = Import.class.getResourceAsStream( ApplicationConstants.XML_SCHEMA_DEFINITION ) ) {
            XMLReaderJDOMFactory schemafac = new XMLReaderXSDFactory( new StreamSource(in) );
            SAXBuilder builder = new SAXBuilder(schemafac);
            File xmlFile = new File(filename);
            return builder.build( xmlFile );// XML validation happens here
        }
    }

    private static void createNode( Element child ) throws JDOMException, SAXException {

        List< Element > children = child.getChildren();

        if ( children.isEmpty() ) {

            final TAG tag = TAG.valueOf( child.getName() );

            switch ( tag ) {
            case ID:
                exerciseItem.setItemId( Integer.parseInt( child.getTextTrim() ) );
                break;
            case TEXT: {
                final String str = child.getTextTrim();
                if ( isQuestionPart ) {
                    exerciseItem.addQuestionText( str );
                } else if ( isAnswerPart ) {
                    Attribute mark = child.getAttribute( ApplicationConstants.NAME_OF_XML_ATTRIBUTE_ANSWER_TEXT_MARK );
                    if ( mark != null ) {
                        exerciseItem.addAnswerText( str, mark.getBooleanValue() );
                    } else {
                        exerciseItem.addAnswerText( str, false );
                    }

                } else if ( isSolutionPart ) {
                    exerciseItem.addSolutionText( str );
                }
                break;
            }
            case CODE:
                if ( isQuestionPart ) {
                    exerciseItem.addQuestionCode( child.getTextTrim() );
                }
                break;
            case TEXT2:
                if ( isQuestionPart ) {
                    exerciseItem.addQuestionText2( child.getTextTrim() );
                }
                break;
            case CATALOG:
                throw new SAXException( String.format( "xml node %s has no children", tag.name() ) );
            default:
                throw new SAXException( String.format( "unexpected xml tag %s", tag.name() ) );
            }

            return;
        }

        for ( Element aChild : children ) {

            final TAG tag = TAG.valueOf( aChild.getName() );

            switch ( tag ) {
            case ITEM:
                exerciseItem = new ExerciseItem();
                exerciseItemList.add( exerciseItem );
                break;
            case QUESTION:
                signalQuestion();
                break;
            case SINGLE_CHOICE_ANSWER:
                signalSingleChoiceAnswer();
                exerciseItem.createSingleChoiceModel();
                break;
            case MULTIPLE_CHOICE_ANSWER:
                signalMultipleChoiceAnswer();
                exerciseItem.createMultipleChoiceModel();
                break;
            case SOLUTION:
                signalSolution();
                break;
            case ID:
            case TEXT:
            case CODE:
            case TEXT2:
                break;
            default:
                throw new SAXException( String.format( "unexpected xml tag %s", tag.name() ) );
            }

            createNode( aChild );
        }
    }

    private static void signalSolution() throws SAXException {

        setSelector( TAG.SOLUTION );
    }

    private static void signalMultipleChoiceAnswer() throws SAXException {

        setSelector( TAG.MULTIPLE_CHOICE_ANSWER );
    }

    private static void signalSingleChoiceAnswer() throws SAXException {

        setSelector( TAG.SINGLE_CHOICE_ANSWER );
    }

    private static void signalQuestion() throws SAXException {

        setSelector( TAG.QUESTION );
    }

    private static void setSelector( TAG selector ) throws SAXException {

        switch ( selector ) {
        case QUESTION:
            isQuestionPart = true;
            isAnswerPart = false;
            isSolutionPart = false;
            break;
        case SINGLE_CHOICE_ANSWER:
        case MULTIPLE_CHOICE_ANSWER:
            isQuestionPart = false;
            isAnswerPart = true;
            isSolutionPart = false;
            break;
        case SOLUTION:
            isQuestionPart = false;
            isAnswerPart = false;
            isSolutionPart = true;
            break;
        default:
            throw new SAXException( String.format( "unexpected xml tag %s", selector.name() ) );
        }
    }
}
