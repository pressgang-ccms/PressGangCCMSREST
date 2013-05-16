package org.jboss.pressgang.ccms.server.utils;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;

public class Constants {
    /**
     * The system property that defines the login message
     */
    public static final String LOGIN_MESSAGE_SYSTEM_PROPERTY = "topicIndex.loginMessage";
    /**
     * The system property that defines the XML elements that should be serialized verbatim
     */
    public static final String KERBEROS_ENABLED_SYSTEM_PROPERTY = "topicIndex.kerberosEnabled";
    /**
     * The system property that determines if topics should be rendered into HTML
     */
    public static final String ENABLE_RENDERING_PROPERTY = "topicIndex.rerenderTopic";
    /**
     * The system property that defines the STOMP message queue that skynet should send topic rendering requests to
     */
    public static final String STOMP_MESSAGE_SERVER_TOPIC_RENDER_QUEUE_SYSTEM_PROPERTY = "topicIndex.stompMessageServerRenderTopicQueue";
    /**
     * The system property that defines the STOMP message queue that skynet should send topic rendering requests to
     */
    public static final String STOMP_MESSAGE_SERVER_TRANSLATED_TOPIC_RENDER_QUEUE_SYSTEM_PROPERTY = "topicIndex.stompMessageServerRenderTranslatedTopicQueue";

    public static final String PROPERTY_TAG_SELECT_ITEM_VALUE_PREFIX = "PropertyTag";
    public static final String PROPERTY_TAG_SELECT_LABEL_PREFIX = "- ";
    public static final String PROPERTY_TAG_CATEGORY_SELECT_ITEM_VALUE_PREFIX = "PropertyTagCategory";
    public static final String UNCATEGORISED_PROPERTY_TAG_CATEGORY_SELECT_ITEM_LABEL = "Uncategorised";
    public static final String UNCATEGORISED_PROPERTY_TAG_CATEGORY_SELECT_ITEM_VALUE = "UncategorisedPropertyTagCategory";

    /**
     * When bulk adding property tags that are unique, a GUID will be proceeded by this message to ensure that the value of the
     * tag is initially unique.
     */
    public static final String UNIQUE_PROPERTY_TAG_PREFIX = "Unique Property Tag";

    /**
     * The name given to the tab that shows entities that didn't match any grouping tags
     */
    public static final String UNGROUPED_RESULTS_TAB_NAME = "Ungrouped Results";

    /**
     * The string to be returned by the VersionedEntityHome.persist() function if a concurrent edit has been detected
     */
    public static final String CONCURRENT_EDIT = "concurrentEdit";

    /**
     * The ID given to the tab that shows entities that didn't match any grouping tags
     */
    public static final Integer UNGROUPED_RESULTS_TAB_ID = -1;

    /**
     * A Topic ID that no topic should ever match
     */
    public static final String NULL_TOPIC_ID_STRING = "-1";

    /**
     * A Topic ID that no topic should ever match
     */
    public static final Integer NULL_TOPIC_ID = -1;

    /** The message saved by SkynetExceptionUtilities when a precondition fails */
    public static final String PRECONDITION_CHECK_FAILED_MESSAGE = "The method failed a precondition check";

    /** The generic error message to display to the user */
    public static final String GENERIC_ERROR_INSTRUCTIONS = "Please log out, log back in and try again. If the problem persists, please log a bug.";

    /** The base URL from which the REST interface can be accessed */
    public static final String BASE_REST_PATH = "/rest";

    /** The default number of elements to be shown by an EntityQuery object */
    public static final int DEFAULT_PAGING_SIZE = 25;

    /**
     * The default number of elements to be shown by an EntityQuery object that has to access Envers data (which is slow)
     */
    public static final int DEFAULT_ENVERS_PAGING_SIZE = 15;

    /**
     * The "Common" project includes any tags that are not assigned to any other project. The Common project has not setup in
     * the database anywhere, but when processing the data structures that contain the list of projects we need a name, which is
     * defined in this constant.
     */
    public static final String COMMON_PROJECT_NAME = "Common";
    /** This is the ID for the Common project */
    public static final Integer COMMON_PROJECT_ID = -1;
    /** This is the description for the Common project */
    public static final String COMMON_PROJECT_DESCRIPTION = "This project holds tags that are not assigned to any other project";
    /**
     * This is the host name of the live SQL server. This value is used to provide a label in the top bar when modifying live
     * data.
     */
    public static final String LIVE_SQL_SERVER = "jboss-eap.bne.redhat.com";
    /** The SQL logic keyword to use when two conditions need to be and'ed */
    public static final String AND_LOGIC = "And";
    /** The SQL logic keyword to use when two conditions need to be or'ed */
    public static final String OR_LOGIC = "Or";
    /** The default logic to be applied to tags within a category */
    public static final String DEFAULT_INTERNAL_LOGIC = OR_LOGIC;
    /** The default logic to be applied between categories */
    public static final String DEFAULT_EXTERNAL_LOGIC = AND_LOGIC;
    /** The default internal category logic state */
    public static final int CATEGORY_INTERNAL_DEFAULT_STATE = CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE;
    /** The default external category logic state */
    public static final int CATEGORY_EXTERNAL_DEFAULT_STATE = CommonFilterConstants.CATEGORY_EXTERNAL_AND_STATE;
    /** The default logic to be applied to the search fields */
    public static final String LOGIC_FILTER_VAR_DEFAULT_VALUE = "and";

    /**
     * The file name for the DocBook DTD schema. This is used when matching and providing XML resources
     */
    public static final String DOCBOOK_DTD = "docbook.dtd";
    /**
     * The file name for the RocBook DTD schema. This is used when matching and providing XML resources
     */
    public static final String ROCBOOK_DTD = "rocbook.dtd";

    /** The Concept tag ID */
    public static final Integer CONCEPT_TAG_ID = 5;
    /** The Concept tag name */
    public static final String CONCEPT_TAG_NAME = "Concept";
    /** The Conceptual Overview tag ID */
    public static final Integer CONCEPTUALOVERVIEW_TAG_ID = 93;
    /** The Conceptual Overview tag name */
    public static final String CONCEPTUALOVERVIEW_TAG_NAME = "Overview";
    /** The Reference tag ID */
    public static final Integer REFERENCE_TAG_ID = 6;
    /** The Reference tag name */
    public static final String REFERENCE_TAG_NAME = "Reference";
    /** The Task tag ID */
    public static final Integer TASK_TAG_ID = 4;
    /** The Task tag name */
    public static final String TASK_TAG_NAME = "Task";
    /** The Content Specification tag ID */
    public static final Integer CONTENT_SPEC_TAG_ID = 268;
    /** The Content Specification tag name */
    public static final String CONTENT_SPEC_TAG_NAME = "Content Specfication";

    /** The Added By Property Tag ID */
    public static final Integer ADDED_BY_PROPERTY_TAG_ID = 14;
    /** The First Name Property Tag ID */
    public static final Integer FIRST_NAME_PROPERTY_TAG_ID = 1;
    /** The Last Name Property Tag ID */
    public static final Integer LAST_NAME_PROPERTY_TAG_ID = 2;
    /** The Email Address Property Tag ID */
    public static final Integer EMAIL_PROPERTY_TAG_ID = 3;
    /** The Organization Property Tag ID */
    public static final Integer ORGANIZATION_PROPERTY_TAG_ID = 18;
    /** The Organization Division Property Tag ID */
    public static final Integer ORG_DIVISION_PROPERTY_TAG_ID = 19;
    /** The DTD Property Tag ID */
    public static final Integer DTD_PROPERTY_TAG_ID = 16;
    /** The Content Specification Type Property Tag ID */
    public static final Integer CSP_TYPE_PROPERTY_TAG_ID = 17;

    /**
     * The ID for the inherit relationship type, as defined in the RoleToRoleRelationship table
     */
    public static final Integer INHERIT_ROLE_RELATIONSHIP_TYPE = 1;
}
