/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.constants;

/**
 * Constants that relate to values in the database.
 */
public class ServiceConstants {
    /**
     * The default value for the minimum characters in topic keywords.
     */
    public static final int KEYWORD_MINIMUM_WORD_LENGTH_DEFAULT = 5;
    /**
     * The Integer Constant ID that defines the minimum characters in topic keywords.
     */
    public static final int KEYWORD_MINIMUM_WORD_LENGTH_INT_CONSTANT_ID = 2;
    /**
     * The default value that defines the minimum number of times a keyword has to appear in any document.
     */
    public static final int KEYWORD_MINIMUM_DOCUMENT_FREQUENCY_DEFAULT = 2;
    /**
     * The Integer Constant ID that defines the minimum number of times a keyword has to appear in any document.
     */
    public static final int KEYWORD_MINIMUM_DOCUMENT_FREQUENCY_INT_CONSTANT_ID = 3;
    /**
     * The default value that defines the maximum number of keywords to find in a topic.
     */
    public static final int KEYWORD_MAX_QUERY_TERMS_INT_DEFAULT = 10;
    /**
     * The Integer Constant ID that defines the maximum number of keywords to find in a topic.
     */
    public static final int KEYWORD_MAX_QUERY_TERMS_INT_CONSTANT_ID = 4;
    /**
     * The default value that defines the minimum number of times a keyword has to appear in the document.
     */
    public static final int KEYWORD_MINIMUM_TERM_FREQUENCY_DEFAULT = 2;
    /**
     * The Integer Constant ID that defines the minimum number of times a keyword has to appear in the document.
     */
    public static final int KEYWORD_MINIMUM_TERM_FREQUENCY_INT_CONSTANT_ID = 5;
    /**
     * The default value that defines the maximum number of documents that a keyword can appear in.
     */
    public static final int KEYWORD_MAXIMUM_DOCUMENT_FREQUENCY_PERCENT_DEFAULT = 10;
    /**
     * The Integer Constant ID that defines the maximum number of documents that a keyword can appear in.
     */
    public static final int KEYWORD_MAXIMUM_DOCUMENT_FREQUENCY_PERCENT_INT_CONSTANT_ID = 6;
    /**
     * The string constant that defines the addition stop words.
     */
    public static final int KEYWORDS_STOPWORDS_STRING_CONSTANT_ID = 73;
}
