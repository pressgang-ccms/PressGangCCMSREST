package org.jboss.pressgang.ccms.server.utils;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Status;
import javax.transaction.TransactionManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jboss.pressgang.ccms.model.TopicSourceUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicSourceURLTitleThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicSourceURLTitleThread.class);

    private EntityManagerFactory entityManagerFactory;
    private TransactionManager transactionManager;
    private List<TopicSourceUrl> topicSourceUrls;

    public TopicSourceURLTitleThread(final List<TopicSourceUrl> topicSourceUrls) throws NamingException {
        entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
        transactionManager = JNDIUtilities.lookupJBossTransactionManager();
        this.topicSourceUrls = topicSourceUrls;
    }

    @Override
    public void run() {
        EntityManager entityManager = null;
        try {
            final List<TopicSourceUrl> updatedTopicSourceUrls = setTopicSourceUrlTitles(topicSourceUrls);

            // Make sure we have something to process
            if (updatedTopicSourceUrls.isEmpty()) {
                return;
            }

            // Start a transaction
            transactionManager.begin();

            // Get the Entity Manager
            entityManager = entityManagerFactory.createEntityManager();

            // Merge the changes
            for (final TopicSourceUrl topicSourceUrl : updatedTopicSourceUrls) {
                entityManager.merge(topicSourceUrl);
            }

            // Flush and commit the changes
            entityManager.flush();
            transactionManager.commit();
        } catch (Exception ex) {
            LOGGER.error("Unable to set Topic Source Urls", ex);
            try {
                if (transactionManager != null) {
                    final int status = transactionManager.getStatus();
                    if (status != Status.STATUS_ROLLING_BACK && status != Status.STATUS_ROLLEDBACK && status != Status
                            .STATUS_NO_TRANSACTION) {
                        transactionManager.rollback();
                    }
                }
            } catch (Throwable e) {
                LOGGER.error("Failed to rollback the transaction", e);
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    protected List<TopicSourceUrl> setTopicSourceUrlTitles(final List<TopicSourceUrl> topicSourceUrls) {
        final List<TopicSourceUrl> updatedTopicSourceUrls = new ArrayList<TopicSourceUrl>();
        for (final TopicSourceUrl topicSourceUrl : topicSourceUrls) {
            if (topicSourceUrl.getTitle() == null || topicSourceUrl.getTitle().trim().isEmpty() && (topicSourceUrl.getSourceUrl() !=
                    null && !topicSourceUrl.getSourceUrl().trim().isEmpty())) {
                setTitle(topicSourceUrl);
                updatedTopicSourceUrls.add(topicSourceUrl);
            }
        }

        return updatedTopicSourceUrls;
    }

    protected void setTitle(final TopicSourceUrl topicSourceUrl) {
        try {
            // Some common string replacements to make in the titles
            final Map<String, String> replaceList = new HashMap<String, String>();
            replaceList.put("&nbsp;", " ");
            replaceList.put("&amp;", "&");

            // create an instance of HtmlCleaner
            final HtmlCleaner cleaner = new HtmlCleaner();

            // clean the source url
            final TagNode node = cleaner.clean(new URL(topicSourceUrl.getSourceUrl()));

            // find the first title node
            final TagNode title = node.findElementByName("title", true);

            if (title != null) {
                // clean up the title
                String titleText = title.getText().toString();

                for (final String replace : replaceList.keySet())
                    titleText = titleText.replaceAll(replace, replaceList.get(replace));

                titleText = titleText.trim();

                // assign it to the entity
                topicSourceUrl.setTitle(titleText);
            }
        } catch (final IOException ex) {
            LOGGER.error("Probably a problem with HTMLCleaner", ex);
        }
    }
}
