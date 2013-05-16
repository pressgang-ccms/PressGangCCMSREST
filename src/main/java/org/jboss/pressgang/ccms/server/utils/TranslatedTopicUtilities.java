package org.jboss.pressgang.ccms.server.utils;

import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.server.zanata.ZanataPullTopicThread;
import org.jboss.pressgang.ccms.server.zanata.ZanataPushTopicThread;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.structures.Pair;

public class TranslatedTopicUtilities {
    public static void pullFromZanata(final TranslatedTopic translatedTopic) {
        final ZanataPullTopicThread zanataPullTopicThread = new ZanataPullTopicThread(
                CollectionUtilities.toArrayList(translatedTopic.getTranslatedTopicId()));
        final Thread thread = new Thread(zanataPullTopicThread);
        thread.start();
    }

    public static void pushToZanata(final TranslatedTopic translatedTopic) {
        final Pair<Integer, Integer> topicIdRevisionPair = new Pair<Integer, Integer>(translatedTopic.getTopicId(), translatedTopic.getTopicRevision());
        final List<Pair<Integer, Integer>> topicIdRevisionPairs = new ArrayList<Pair<Integer, Integer>>();
        topicIdRevisionPairs.add(topicIdRevisionPair);
        
        final ZanataPushTopicThread zanataPushTopicThread = new ZanataPushTopicThread(topicIdRevisionPairs, false);
        final Thread thread = new Thread(zanataPushTopicThread);
        thread.start();
    }
}
