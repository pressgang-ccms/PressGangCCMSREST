package org.jboss.pressgang.ccms.server.utils;

/**
 * Constants that relate to values in the database.
 */
public class ServiceConstants {
    /**
     * The Integer Constant ID that defines the minimum characters in topic keywords.
     */
    public static final int KEYWORD_MINIMUM_WORD_LENGTH_INT_CONSTANT_ID = 2;
    /**
     * The default value for the minimum characters in topic keywords.
     */
    public static final int KEYWORD_MINIMUM_WORD_LENGTH_DEFAULT = 5;
    /**
     * The default value that defines the minimum number of times a keyword has to appear in the document.
     */
    public static final int KEYWORD_MINIMUM_TERM_FREQUENCY_DEFAULT = 2;
    /**
     * The Integer Constant ID that defines the minimum number of times a keyword has to appear in the document.
     */
    public static final int KEYWORD_MINIMUM_TERM_FREQUENCY_INT_CONSTANT_ID = 4;
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
     * The default value that defines the maximum number of documents that a keyword can appear in.
     */
    public static final int KEYWORD_MAXIMUM_DOCUMENT_FREQUENCY_PERCENT_DEFAULT = 10;
    /**
     * The Integer Constant ID that defines the maximum number of documents that a keyword can appear in.
     */
    public static final int KEYWORD_MAXIMUM_DOCUMENT_FREQUENCY_PERCENT_INT_CONSTANT_ID = 6;
}
