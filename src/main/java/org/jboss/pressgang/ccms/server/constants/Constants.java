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

package org.jboss.pressgang.ccms.server.constants;

public class Constants {

    /**
     * A Topic ID that no topic should ever match
     */
    public static final String NULL_TOPIC_ID_STRING = "-1";

    /**
     * A Topic ID that no topic should ever match
     */
    public static final Integer NULL_TOPIC_ID = -1;

    /**
     * The base URL from which the REST interface can be accessed
     */
    public static final String BASE_REST_PATH = "/rest";
    /**
     * Query strings passed in as path segments always use this as the prefix
     */
    public static final String QUERY_PATHSEGMENT_PREFIX = "query";
    /**
     * The length of a string that contains a SHA 256 hash
     */
    public static final int SHA_256_HASH_LENGTH = 64;
    /**
     * This is the maximum number of topics to return when finding similar topics
     */
    public static final int MAX_NUMBER_SIMILAR_TOPICS = 100;
}
