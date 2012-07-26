package org.jboss.pressgangccms.restserver.constants;

public final class Constants
{
	/* The following database key constants are defined in import.sql */
	
	/** The Content Specification tag ID */
	public static final Integer CONTENT_SPEC_TAG_ID = 4;
	/** The Concept tag ID */
	public static final Integer CONCEPT_TAG_ID = 1;
	/** The Concept tag name */
	public static final String CONCEPT_TAG_NAME = "Concept";
	/** The Task tag ID */
	public static final Integer TASK_TAG_ID = 2;
	/** The Task tag name */
	public static final String TASK_TAG_NAME = "Task";
	/** The Reference tag ID */
	public static final Integer REFERENCE_TAG_ID = 3;
	/** The Reference tag name */
	public static final String REFERENCE_TAG_NAME = "Reference";	
	/** The string constant that is used as a reference template */
	public static final Integer REFERENCE_TOPIC_STRINGCONSTANTID = 2;
	/** The string constant that is used as a task template */
	public static final Integer TASK_TOPIC_STRINGCONSTANTID = 1;
	/** The string constant that is used as a concept template */
	public static final Integer CONCEPT_TOPIC_STRINGCONSTANTID = 3;

	/**
	 * The default size to use for batch fetching.
	 */
	public static final int DEFAULT_BATCH_SIZE = 15;
	
	public static final String PROPERTY_TAG_SELECT_ITEM_VALUE_PREFIX = "PropertyTag";
	public static final String UNCATEGORISED_PROPERTY_TAG_CATEGORY_SELECT_ITEM_VALUE = "UncategorisedPropertyTagCategory";
	public static final String PROPERTY_TAG_CATEGORY_SELECT_ITEM_VALUE_PREFIX = "PropertyTagCategory";
	public static final String UNCATEGORISED_PROPERTY_TAG_CATEGORY_SELECT_ITEM_LABEL = "Uncategorised";
	public static final String PROPERTY_TAG_SELECT_LABEL_PREFIX = "- ";
	
	/**
	 * The URL variable prefix to indicate the internal logic of a category (and
	 * optionally also specify a project)
	 */
	public static final String CATEORY_INTERNAL_LOGIC = "catint";
	/**
	 * The URL variable prefix to indicate the external logic of a category (and
	 * optionally also specify a project)
	 */
	public static final String CATEORY_EXTERNAL_LOGIC = "catext";
	/**
	 * The value (as used in the FilterTag database TagState field) the
	 * indicates that a tag should be matched
	 */
	public static final int MATCH_TAG_STATE = 1;
	/**
	 * The value (as used in the FilterTag database TagState field) the
	 * indicates that a tag should be excluded
	 */
	public static final int NOT_MATCH_TAG_STATE = 0;
	/**
	 * The value (as used in the FilterTag database TagState field) the
	 * indicates that a tag should be excluded
	 */
	public static final int GROUP_TAG_STATE = 2;
	/**
	 * The value (as used in the FilterCategory database CategoryState field)
	 * the indicates that a category has an internal "and" state
	 */
	public static final int CATEGORY_INTERNAL_AND_STATE = 0;
	/**
	 * The value (as used in the FilterCategory database CategoryState field)
	 * the indicates that a category has an internal "or" state
	 */
	public static final int CATEGORY_INTERNAL_OR_STATE = 1;
	/**
	 * The value (as used in the FilterCategory database CategoryState field)
	 * the indicates that a category has an external "and" state
	 */
	public static final int CATEGORY_EXTERNAL_AND_STATE = 2;
	/**
	 * The value (as used in the FilterCategory database CategoryState field)
	 * the indicates that a category has an external "or" state
	 */
	public static final int CATEGORY_EXTERNAL_OR_STATE = 3;
	/** 
	 * The default internal category logic state 
	 */
	public static final int CATEGORY_INTERNAL_DEFAULT_STATE = CATEGORY_INTERNAL_OR_STATE;
	/** 
	 * The default external category logic state 
	 */
	public static final int CATEGORY_EXTERNAL_DEFAULT_STATE = CATEGORY_EXTERNAL_AND_STATE;
	
	/** 
	 * The SQL logic keyword to use when two conditions need to be and'ed 
	 */
	public static final String AND_LOGIC = "And";
	/** 
	 * The SQL logic keyword to use when two conditions need to be or'ed 
	 */
	public static final String OR_LOGIC = "Or";
}
