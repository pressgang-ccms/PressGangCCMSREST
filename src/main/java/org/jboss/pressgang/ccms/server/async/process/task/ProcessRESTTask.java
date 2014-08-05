/*
  Copyright 2011-2014 Red Hat, Inc

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

package org.jboss.pressgang.ccms.server.async.process.task;

import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.contentspec.SpecTopic;
import org.jboss.pressgang.ccms.provider.DataProviderFactory;
import org.jboss.pressgang.ccms.provider.TopicProvider;
import org.jboss.pressgang.ccms.rest.v1.query.RESTTopicQueryBuilderV1;
import org.jboss.pressgang.ccms.utils.structures.Pair;

public abstract class ProcessRESTTask<T> extends ProcessTask<T> {
    private static int MAX_DOWNLOAD_SIZE = 400;

    /**
     * Download all the topics that are to be used during processing from the
     * parsed Content Specification.
     */
    public void downloadAllTopics(final DataProviderFactory providerFactory, final ContentSpec contentSpec) {
        final TopicProvider topicProvider = providerFactory.getProvider(TopicProvider.class);
        final List<SpecTopic> specTopics = contentSpec.getSpecTopics();
        final List<Integer> topicIds = new ArrayList<Integer>();
        final List<Pair<Integer, Integer>> revisionTopicIds = new ArrayList<Pair<Integer, Integer>>();

        // populate the topicIds and revisionTopicIds
        for (final SpecTopic specTopic : specTopics) {
            if (specTopic.getRevision() == null) {
                topicIds.add(specTopic.getDBId());
            } else {
                revisionTopicIds.add(new Pair<Integer, Integer>(specTopic.getDBId(), specTopic.getRevision()));
            }
        }

        // Check if a maximum revision was specified for processing
        if (!topicIds.isEmpty()) {
            // Download the list of topics in one go to reduce I/O overhead
            getLogger().info("Attempting to download all the latest topics...");
            final RESTTopicQueryBuilderV1 queryBuilder = new RESTTopicQueryBuilderV1();
            if (topicIds.size() > MAX_DOWNLOAD_SIZE) {
                int start = 0;
                while (start < topicIds.size()) {
                    final List<Integer> subList = topicIds.subList(start, Math.min(start + MAX_DOWNLOAD_SIZE, topicIds.size()));
                    queryBuilder.setTopicIds(subList);
                    topicProvider.getTopicsWithQuery(queryBuilder.getQuery());

                    start += MAX_DOWNLOAD_SIZE;
                }
            } else {
                queryBuilder.setTopicIds(topicIds);
                topicProvider.getTopicsWithQuery(queryBuilder.getQuery());
            }
        }

        if (!revisionTopicIds.isEmpty()) {
            downloadRevisionTopics(topicProvider, revisionTopicIds);
        }
    }

    /**
     * Download the Topics from the REST API that specify a revision.
     *
     * @param referencedRevisionTopicIds The Set of topic ids and revision to download.
     */
    public void downloadRevisionTopics(final TopicProvider topicProvider,
            final List<Pair<Integer, Integer>> referencedRevisionTopicIds) {
        getLogger().info("Attempting to download all the revision topics...");

        final int showPercent = 10;
        final float total = referencedRevisionTopicIds.size();
        float current = 0;
        int lastPercent = 0;

        for (final Pair<Integer, Integer> topicToRevision : referencedRevisionTopicIds) {
            // If we want to update the revisions then we should get the latest topic and not the revision
            topicProvider.getTopic(topicToRevision.getFirst(), topicToRevision.getSecond());

            ++current;
            final int percent = Math.round(current / total * 100);
            if (percent - lastPercent >= showPercent) {
                lastPercent = percent;
                getLogger().info(String.format("\tDownloading revision topics %d%% Done", percent));
            }
        }
    }
}
