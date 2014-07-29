/*
  Copyright 2011-2014 Red Hat

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.utils;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Properties;

import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TranslatedTopicUtilities {
    private static final Logger log = LoggerFactory.getLogger(TranslatedTopicUtilities.class);

    /**
     * Process a Translated Topics XML and reformats the XML using the formatting String Constant rules.
     *
     * @param entityManager       An open EntityManager instance to lookup formatting constants.
     * @param translatedTopicData The TranslatedTopic to process the XML for.
     */
    public static void processXML(final EntityManager entityManager, final TranslatedTopicData translatedTopicData) {
        // Get the XML elements that require special formatting/processing
        final StringConstants xmlElementsProperties = entityManager.find(StringConstants.class,
                EntitiesConfig.getInstance().getXMLFormattingElementsStringConstantId());

        // Load the String Constants as Properties
        final Properties prop = new Properties();
        try {
            prop.load(new StringReader(xmlElementsProperties.getConstantValue()));
        } catch (IOException ex) {
            log.error("The XML Elements Properties file couldn't be loaded as a property file", ex);
        }
        // Find the XML elements that need formatting for different display rules.
        final String verbatimElementsString = prop.getProperty(CommonConstants.VERBATIM_XML_ELEMENTS_PROPERTY_KEY);
        final String inlineElementsString = prop.getProperty(CommonConstants.INLINE_XML_ELEMENTS_PROPERTY_KEY);
        final String contentsInlineElementsString = prop.getProperty(CommonConstants.CONTENTS_INLINE_XML_ELEMENTS_PROPERTY_KEY);

        final ArrayList<String> verbatimElements = verbatimElementsString == null ? new ArrayList<String>() : CollectionUtilities
                .toArrayList(
                        verbatimElementsString.split("[\\s]*,[\\s]*"));

        final ArrayList<String> inlineElements = inlineElementsString == null ? new ArrayList<String>() : CollectionUtilities
                .toArrayList(
                        inlineElementsString.split("[\\s]*,[\\s]*"));

        final ArrayList<String> contentsInlineElements = contentsInlineElementsString == null ? new ArrayList<String>() :
                CollectionUtilities.toArrayList(
                        contentsInlineElementsString.split("[\\s]*,[\\s]*"));

        // Check we have something to process
        if (!isNullOrEmpty(translatedTopicData.getTranslatedXml())) {
            Document doc = null;
            try {
                doc = XMLUtilities.convertStringToDocument(translatedTopicData.getTranslatedXml());
            } catch (SAXException ex) {
                // Do nothing with this as it's handled later by the validator
            } catch (Exception ex) {
                log.warn("An Error occurred transforming a XML String to a DOM Document", ex);
            }
            if (doc != null) {
                // Convert the document to a String applying the XML Formatting property rules
                translatedTopicData.setTranslatedXml(
                        XMLUtilities.convertNodeToString(doc, verbatimElements, inlineElements, contentsInlineElements, true));
            }
        }

        // Check we have some additional xml to process
        if (!isNullOrEmpty(translatedTopicData.getTranslatedAdditionalXml())) {
            Document doc = null;
            try {
                doc = XMLUtilities.convertStringToDocument(translatedTopicData.getTranslatedAdditionalXml());
            } catch (SAXException ex) {
                // Do nothing with this as it's handled later by the validator
            } catch (Exception ex) {
                log.warn("An Error occurred transforming a XML String to a DOM Document", ex);
            }
            if (doc != null) {
                // Convert the document to a String applying the XML Formatting property rules
                translatedTopicData.setTranslatedAdditionalXml(
                        XMLUtilities.convertNodeToString(doc, verbatimElements, inlineElements, contentsInlineElements, true));
            }
        }
    }
}
