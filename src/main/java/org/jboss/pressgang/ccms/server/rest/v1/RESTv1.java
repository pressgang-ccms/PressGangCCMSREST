package org.jboss.pressgang.ccms.server.rest.v1;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.threadpool.DefaultThreadPool;
import org.apache.commons.threadpool.ThreadPool;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jboss.pressgang.ccms.contentspec.processor.SnapshotProcessor;
import org.jboss.pressgang.ccms.contentspec.processor.structures.SnapshotOptions;
import org.jboss.pressgang.ccms.filter.BlobConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.CategoryFieldFilter;
import org.jboss.pressgang.ccms.filter.ContentSpecFieldFilter;
import org.jboss.pressgang.ccms.filter.ContentSpecNodeFieldFilter;
import org.jboss.pressgang.ccms.filter.FileFieldFilter;
import org.jboss.pressgang.ccms.filter.FilterFieldFilter;
import org.jboss.pressgang.ccms.filter.ImageFieldFilter;
import org.jboss.pressgang.ccms.filter.IntegerConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.ProcessFieldFilter;
import org.jboss.pressgang.ccms.filter.ProjectFieldFilter;
import org.jboss.pressgang.ccms.filter.PropertyTagCategoryFieldFilter;
import org.jboss.pressgang.ccms.filter.PropertyTagFieldFilter;
import org.jboss.pressgang.ccms.filter.RoleFieldFilter;
import org.jboss.pressgang.ccms.filter.StringConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.TagFieldFilter;
import org.jboss.pressgang.ccms.filter.TopicFieldFilter;
import org.jboss.pressgang.ccms.filter.TranslatedContentSpecFieldFilter;
import org.jboss.pressgang.ccms.filter.TranslatedContentSpecNodeFieldFilter;
import org.jboss.pressgang.ccms.filter.TranslatedTopicFieldFilter;
import org.jboss.pressgang.ccms.filter.UserFieldFilter;
import org.jboss.pressgang.ccms.filter.builder.BlobConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.CategoryFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ContentSpecFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ContentSpecNodeFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.FileFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.FilterFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ImageFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.IntegerConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ProcessFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ProjectFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.PropertyTagCategoryFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.PropertyTagFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.RoleFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.StringConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TagFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TranslatedContentSpecFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TranslatedContentSpecNodeFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TranslatedTopicDataFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.UserFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.File;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.ImageFile;
import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.model.MinHashXOR;
import org.jboss.pressgang.ccms.model.Process;
import org.jboss.pressgang.ccms.model.ProcessType;
import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToProcess;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.provider.DBProviderFactory;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTBlobConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTIntegerConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProcessInformationCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTRoleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTStringConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTUserCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTextContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.components.ComponentTopicV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTProcessInformationV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerEntitiesV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTServerSettingsV1;
import org.jboss.pressgang.ccms.rest.v1.elements.RESTSystemStatsV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBlobConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTIntegerConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTMatchedFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTMatchedImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTMatchedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTRoleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTLogDetailsV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTextContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTXMLFormat;
import org.jboss.pressgang.ccms.rest.v1.entities.wrapper.IntegerWrapper;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataDetails;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTBaseInterfaceV1;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTInterfaceAdvancedV1;
import org.jboss.pressgang.ccms.server.async.ProcessManager;
import org.jboss.pressgang.ccms.server.async.process.PGProcess;
import org.jboss.pressgang.ccms.server.async.process.task.TestTask;
import org.jboss.pressgang.ccms.server.constants.Constants;
import org.jboss.pressgang.ccms.server.rest.v1.base.BaseRESTv1;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.thread.RESTRunnableWithTransaction;
import org.jboss.pressgang.ccms.server.rest.v1.utils.ProcessHelper;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.ContentSpecUtilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.pressgang.ccms.server.utils.ProviderUtilities;
import org.jboss.pressgang.ccms.server.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.pressgang.ccms.utils.common.HashUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.common.ZipUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.jboss.resteasy.specimpl.PathSegmentImpl;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.NotFoundException;
import org.jboss.resteasy.util.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jppf.JPPFException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.zanata.common.LocaleId;

/**
 * The PressGang REST interface implementation
 */
@Path(Constants.BASE_REST_PATH + "/1")
public class RESTv1 extends BaseRESTv1 implements RESTBaseInterfaceV1, RESTInterfaceAdvancedV1 {
    private static final Logger log = LoggerFactory.getLogger(RESTv1.class);
    private static final int BATCH_SIZE = 20;
    private static final int THREAD_POOL_SIZE = 5;
    private static final ThreadPool THREAD_POOL = new DefaultThreadPool(THREAD_POOL_SIZE);

    @Inject
    private ProcessManager processManager;
    @Inject
    private ProcessHelper processHelper;

    /* UTILITY FUNCTIONS */
    public RESTSystemStatsV1 getJSONSysInfo() {
        final AuditReader reader = AuditReaderFactory.get(entityManager);
        final Number revision = reader.getRevisionNumberForDate(new Date(Long.MAX_VALUE));
        final Date revisionDate = reader.getRevisionDate(revision);

        final RESTSystemStatsV1 retValue = new RESTSystemStatsV1();
        retValue.setLastRevision(revision.intValue());
        retValue.setLastRevisionDate(revisionDate);

        return retValue;
    }

    @Override
    public Map<Integer, Integer> getMinHashes(final String xml) {
        final List<MinHashXOR> minHashXORs = entityManager.createQuery(MinHashXOR.SELECT_ALL_QUERY).getResultList();
        return org.jboss.pressgang.ccms.model.utils.TopicUtilities.getMinHashes(xml, minHashXORs);
    }

    @Override
    public RESTTopicCollectionV1 getSimilarTopics(final String xml, final String expand, final Float threshold) {

        if (threshold == null) {
            throw new IllegalArgumentException(
                    "threshold must be between " + org.jboss.pressgang.ccms.model.constants.Constants.MIN_DOCUMENT_SIMILARITY + " and " +
                            org.jboss.pressgang.ccms.model.constants.Constants.MAX_DOCUMENT_SIMILARITY);
        }

        if (threshold < org.jboss.pressgang.ccms.model.constants.Constants.MIN_DOCUMENT_SIMILARITY || threshold > org.jboss.pressgang
                .ccms.model.constants.Constants.MAX_DOCUMENT_SIMILARITY) {
            throw new IllegalArgumentException(
                    "threshold must be between " + org.jboss.pressgang.ccms.model.constants.Constants.MIN_DOCUMENT_SIMILARITY + " and " +
                            org.jboss.pressgang.ccms.model.constants.Constants.MAX_DOCUMENT_SIMILARITY);
        }

        final Map<Integer, Integer> minHashes = getMinHashes(xml);

        final List<Integer> topics = org.jboss.pressgang.ccms.model.utils.TopicUtilities.getMatchingMinHash(entityManager, minHashes,
                threshold);

        if (topics != null) {
            final String topicIds = CollectionUtilities.toSeperatedString(topics, ",");

            final PathSegment filter = new PathSegmentImpl(Constants.QUERY_PATHSEGMENT_PREFIX, false);
            filter.getMatrixParameters().add(CommonFilterConstants.TOPIC_IDS_FILTER_VAR, topicIds);

            return getJSONTopicsWithQuery(filter, expand);
        } else {
            return new RESTTopicCollectionV1();
        }
    }

    @Override
    public void recalculateMinHashXORs(final String confirmation) {
        try {
            // Start a Transaction
            transaction.begin();

            // Join the transaction we just started
            entityManager.joinTransaction();

            /*
                Confirmation needs to equal the current minhash with func id 1.
                This is because any topics saved after the minhashes are regenerated can not
                be compared to any that were saved before. This check ensures that you *really*
                wanted to regenerate the minhash xor values.
            */
            final MinHashXOR firstMinHash = entityManager.find(MinHashXOR.class, 1);
            final String firstMinHashAsString = firstMinHash == null ? "" : firstMinHash.getMinHashXOR().toString();
            final String confirmationString = confirmation == null ? "" : confirmation;
            if (firstMinHashAsString.equals(confirmationString)) {

                final Random randomGenerator = new Random();

                // The first min hash is not XORed.
                for (int i = 1; i < org.jboss.pressgang.ccms.model.constants.Constants.NUM_MIN_HASHES; ++i) {
                    final int random = randomGenerator.nextInt();
                    MinHashXOR minHashXOR = entityManager.find(MinHashXOR.class, i);
                    if (minHashXOR == null) {
                        minHashXOR = new MinHashXOR();
                        minHashXOR.setMinHashXORFuncId(i);
                    }

                    minHashXOR.setMinHashXOR(random);

                    entityManager.persist(minHashXOR);

                    // Do batch updating by flushing the changes every 100 updates.
                    if (i % BATCH_SIZE == 0) {
                        entityManager.flush();
                    }
                }
            } else {
                throw new BadRequestException(
                        "The request body needs to equal the existing MinHashXOR with id 1, " + "or be empty if there is no existing " +
                                "MinHashXOR with id 1.");
            }

            // Flush the changes to the database and commit the transaction
            entityManager.flush();
            transaction.commit();
        } catch (final Throwable ex) {
            throw RESTv1Utilities.processError(transaction, ex);
        }
    }

    @Override
    public void recalculateMinHash() {
        recalculateMinHashes(false);
    }

    @Override
    public void recalculateMissingMinHash() {
        recalculateMinHashes(true);
    }

    private void recalculateMinHashes(final boolean missingOnly) {
        try {
            final String topicQuery = "SELECT topic.topicId FROM Topic as Topic WHERE NOT topic.topicXML IS " +
                    "NULL" + (missingOnly ? " AND SIZE(topic.minHashes) != " + org.jboss.pressgang.ccms.model.constants.Constants
                    .NUM_MIN_HASHES : "");

            final List<MinHashXOR> minHashXORs = entityManager.createQuery(MinHashXOR.SELECT_ALL_QUERY).getResultList();
            final List<Integer> topics = entityManager.createQuery(topicQuery).getResultList();

            // Since there are a lot of topics to process there is a high chance it'll hit the timeout,
            // so break the transactions into smaller chunks
            for (int i = 0; i < topics.size(); i += BATCH_SIZE) {

                final int startTopic = i;

                // lets just wrap up some code in a Runnable
                THREAD_POOL.invokeLater(new RESTRunnableWithTransaction() {
                    public void doWork(final EntityManager em, final UserTransaction transaction) {
                        for (int j = startTopic; j < topics.size() && j < startTopic + BATCH_SIZE; ++j) {

                            final Topic topic = em.find(Topic.class, topics.get(j));
                            boolean topicChanged = org.jboss.pressgang.ccms.model.utils.TopicUtilities.recalculateMinHash(topic, minHashXORs);

                            // Handle topics that have invalid titles.
                            if (topic.getTopicTitle() == null || topic.getTopicTitle().trim().isEmpty()) {
                                topic.setTopicTitle("Placeholder");
                                topicChanged = true;
                            }

                            if (topicChanged) {
                                em.persist(topic);
                            }
                        }
                    }
                });
            }
        } catch (final Throwable ex) {
            throw RESTv1Utilities.processError(transaction, ex);
        }
    }

    @Override
    public void recalculateMissingContentHash() {
        try {
            final String topicQuery = "SELECT topic.topicId FROM Topic as Topic WHERE NOT topic.topicXML = '' AND NOT topic.topicXML IS " +
                    "NULL AND (topic.topicContentHash IS NULL OR topic.topicContentHash = '')";

            final List<Integer> topics = entityManager.createQuery(topicQuery).getResultList();

            final String imageQuery = "SELECT languageimage.languageImageId FROM LanguageImage as LanguageImage WHERE NOT languageimage" +
                    ".imageData IS NULL AND (languageimage.imageContentHash IS NULL OR languageimage.imageContentHash = '')";

            final List<Integer> images = entityManager.createQuery(imageQuery).getResultList();

            final String fileQuery = "SELECT languagefile.languageFileId FROM LanguageFile as LanguageFile WHERE NOT languagefile.fileData IS NULL" +
                    " AND (languagefile.fileContentHash IS NULL OR languagefile.fileContentHash = '')" ;

            final List<Integer> files = entityManager.createQuery(fileQuery).getResultList();


            // Since there are a lot of topics to process there is a high chance it'll hit the timeout,
            // so break the transactions into smaller chunks
            for (int i = 0; i < topics.size(); i += BATCH_SIZE) {

                final int startTopic = i;

                // lets just wrap up some code in a Runnable
                THREAD_POOL.invokeLater(new RESTRunnableWithTransaction() {
                    public void doWork(final EntityManager em, final UserTransaction transaction) {
                        for (int j = startTopic; j < topics.size() && j < startTopic + BATCH_SIZE; ++j) {

                            final Topic topic = em.find(Topic.class, topics.get(j));

                            // make some change to allow the entity to be saved
                            topic.setTopicContentHash(new char[Constants.SHA_256_HASH_LENGTH]);

                            // persisting the topic will regenerate the content hash in
                            // @prepersist
                            em.persist(topic);
                        }
                    }
                });
            }

            for (int i = 0; i < images.size(); i += BATCH_SIZE) {

                final int start = i;

                // lets just wrap up some code in a Runnable
                THREAD_POOL.invokeLater(new RESTRunnableWithTransaction() {
                    public void doWork(final EntityManager em, final UserTransaction transaction) {
                        for (int j = start; j < images.size() && j < start + BATCH_SIZE; ++j) {

                            final LanguageImage image = em.find(LanguageImage.class, images.get(j));

                            // make some change to allow the entity to be saved
                            image.setImageContentHash(new char[Constants.SHA_256_HASH_LENGTH]);

                            // persisting the topic will regenerate the content hash in
                            // @prepersist
                            em.persist(image);
                        }
                    }
                });
            }

            for (int i = 0; i < files.size(); i += BATCH_SIZE) {

                final int start = i;

                // lets just wrap up some code in a Runnable
                THREAD_POOL.invokeLater(new RESTRunnableWithTransaction() {
                    public void doWork(final EntityManager em, final UserTransaction transaction) {
                        for (int j = start; j < files.size() && j < start + BATCH_SIZE; ++j) {

                            final LanguageFile file = em.find(LanguageFile.class, files.get(j));

                            // make some change to allow the entity to be saved
                            file.setFileContentHash(new char[Constants.SHA_256_HASH_LENGTH]);

                            // persisting the topic will regenerate the content hash in
                            // @prepersist
                            em.persist(file);
                        }
                    }
                        
                });
            }
        } catch (final Throwable ex) {
            throw RESTv1Utilities.processError(transaction, ex);
        }
    }

    /* SYSTEM FUNCTIONS */
    @Override
    public void reIndexLuceneDatabase() {
        try {
            final Session session = (Session) entityManager.getDelegate();
            final FullTextSession fullTextSession = Search.getFullTextSession(session);
            fullTextSession.createIndexer().start();
        } catch (final Exception ex) {
            log.error("An error reindexing the Lucene database", ex);
        }
    }

    @Override
    public ExpandDataTrunk getJSONExpandTrunkExample() {
        final ExpandDataTrunk expand = new ExpandDataTrunk();
        final ExpandDataTrunk collection = new ExpandDataTrunk(new ExpandDataDetails("collectionname"));
        final ExpandDataTrunk subCollection = new ExpandDataTrunk(new ExpandDataDetails("subcollection"));

        collection.setBranches(CollectionUtilities.toArrayList(subCollection));
        expand.setBranches(CollectionUtilities.toArrayList(collection));

        return expand;
    }

    @Override
    public IntegerWrapper holdXML(final String xml) {
        if (xml == null) {
            throw new BadRequestException("The xml parameter cannot be null");
        }

        // Parse the XML to make sure it's XML
        Document doc = null;
        try {
            doc = XMLUtilities.convertStringToDocument(xml);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }

        // Make sure the input was XML by seeing if it could be parsed
        if (doc == null) {
            throw new BadRequestException("The input XML is not valid XML content");
        }

        // Add the xml to the cache and return the ID
        final IntegerWrapper retValue = new IntegerWrapper();
        retValue.value = xmlEchoCache.addXML(xml);

        return retValue;
    }

    @Override
    public String echoXML(final Integer id, final String xml) {
        if (id == null && xml == null) throw new BadRequestException("The id parameter field cannot be null");

        if (id != null) {
            final String foundXML = xmlEchoCache.getXML(id);
            if (foundXML == null) {
                throw new NotFoundException("No XML exists for the specified id");
            } else {
                return foundXML;
            }
        } else {
            Document doc = null;
            try {
                doc = XMLUtilities.convertStringToDocument(xml);
            } catch (Exception e) {
                throw new BadRequestException(e);
            }

            if (doc == null) {
                throw new BadRequestException("The input XML is not valid XML content");
            } else {
                return xml;
            }
        }
    }

    /* APPLICATION SETTING FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPApplicationSettings(@QueryParam("callback") String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONServerSettings()));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTServerSettingsV1 getJSONServerSettings() {
        final ExpandDataTrunk expand = new ExpandDataTrunk();
        expand.setBranches(Arrays.asList(
                new ExpandDataTrunk(new ExpandDataDetails(RESTServerSettingsV1.UNDEFINED_SETTINGS_NAME)),
                new ExpandDataTrunk(new ExpandDataDetails(RESTServerSettingsV1.ZANATA_SETTINGS_NAME)),
                new ExpandDataTrunk(new ExpandDataDetails(RESTServerEntitiesV1.UNDEFINED_ENTITIES_NAME))
        ));

        return serverSettingsFactory.createRESTEntityFromObject(applicationConfig, getBaseUrl(), RESTv1Constants.JSON_URL, expand);
    }

    @Override
    public RESTServerSettingsV1 updateJSONServerSettings(final RESTServerSettingsV1 dataObject) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        // Update the settings
        serverSettingsFactory.updateObjectFromRESTEntity(applicationConfig, dataObject);

        // Return the new settings
        return getJSONServerSettings();
    }

    /* BLOBCONSTANTS FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPBlobConstant(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPBlobConstantRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstantRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    public String getJSONPBlobConstants(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPBlobConstantsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTBlobConstantV1 getJSONBlobConstant(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(BlobConstants.class, blobConstantFactory, id, expand);
    }

    @Override
    public RESTBlobConstantV1 getJSONBlobConstantRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(BlobConstants.class, blobConstantFactory, id, revision, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 getJSONBlobConstants(final String expand) {
        return getJSONResources(RESTBlobConstantCollectionV1.class, BlobConstants.class, blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 getJSONBlobConstantsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTBlobConstantCollectionV1.class, query.getMatrixParameters(),
                BlobConstantFilterQueryBuilder.class, new BlobConstantFieldFilter(), blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTBlobConstantV1 updateJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(BlobConstants.class, dataObject, blobConstantFactory, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 updateJSONBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, dataObjects, blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTBlobConstantV1 createJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(BlobConstants.class, dataObject, blobConstantFactory, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 createJSONBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, dataObjects, blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTBlobConstantV1 deleteJSONBlobConstant(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(BlobConstants.class, blobConstantFactory, id, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 deleteJSONBlobConstants(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, blobConstantFactory, dbEntityIds,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* RAW FUNCTIONS */
    @Override
    public byte[] getRAWBlobConstant(@PathParam("id") Integer id) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final BlobConstants entity = getEntity(BlobConstants.class, id);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getConstantName());
        return entity.getConstantValue();
    }

    @Override
    public byte[] getRAWBlobConstantRevision(@PathParam("id") Integer id, @PathParam("rev") Integer revision) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        final BlobConstants entity = getEntity(BlobConstants.class, id, revision);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getConstantName());
        return entity.getConstantValue();
    }

    /* PROJECT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPProject(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProject(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjectRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjectRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjects(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjects(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjectsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjectsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTProjectV1 getJSONProject(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Project.class, projectFactory, id, expand);
    }

    @Override
    public RESTProjectV1 getJSONProjectRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Project.class, projectFactory, id, revision, expand);
    }

    @Override
    public RESTProjectCollectionV1 getJSONProjects(final String expand) {
        return getJSONResources(RESTProjectCollectionV1.class, Project.class, projectFactory, RESTv1Constants.PROJECTS_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTProjectCollectionV1 getJSONProjectsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTProjectCollectionV1.class, query.getMatrixParameters(), ProjectFilterQueryBuilder.class,
                new ProjectFieldFilter(), projectFactory, RESTv1Constants.PROJECTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTProjectV1 updateJSONProject(final String expand, final RESTProjectV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Project.class, dataObject, projectFactory, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 updateJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTProjectCollectionV1.class, Project.class, dataObjects, projectFactory,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTProjectV1 createJSONProject(final String expand, final RESTProjectV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Project.class, dataObject, projectFactory, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 createJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTProjectCollectionV1.class, Project.class, dataObjects, projectFactory,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTProjectV1 deleteJSONProject(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Project.class, projectFactory, id, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 deleteJSONProjects(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTProjectCollectionV1.class, Project.class, projectFactory, dbEntityIds,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROPERYTAG FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPPropertyTag(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTag(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTagRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTagRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTags(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTags(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTagsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTagsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTPropertyTagV1 getJSONPropertyTag(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(PropertyTag.class, propertyTagFactory, id, expand);
    }

    @Override
    public RESTPropertyTagV1 getJSONPropertyTagRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(PropertyTag.class, propertyTagFactory, id, revision, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 getJSONPropertyTags(final String expand) {
        return getJSONResources(RESTPropertyTagCollectionV1.class, PropertyTag.class, propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 getJSONPropertyTagsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTPropertyTagCollectionV1.class, query.getMatrixParameters(),
                PropertyTagFilterQueryBuilder.class, new PropertyTagFieldFilter(), propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyTagV1 updateJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(PropertyTag.class, dataObject, propertyTagFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 updateJSONPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, dataObjects, propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyTagV1 createJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(PropertyTag.class, dataObject, propertyTagFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 createJSONPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, dataObjects, propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyTagV1 deleteJSONPropertyTag(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(PropertyTag.class, propertyTagFactory, id, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 deleteJSONPropertyTags(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, propertyTagFactory, dbEntityIds,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROPERYCATEGORY FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPPropertyCategory(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategory(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategoryRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategoryRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategories(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategories(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategoriesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategoriesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTPropertyCategoryV1 getJSONPropertyCategory(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(PropertyTagCategory.class, propertyCategoryFactory, id, expand);
    }

    @Override
    public RESTPropertyCategoryV1 getJSONPropertyCategoryRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(PropertyTagCategory.class, propertyCategoryFactory, id, revision, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 getJSONPropertyCategories(final String expand) {
        return getJSONResources(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 getJSONPropertyCategoriesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTPropertyCategoryCollectionV1.class, query.getMatrixParameters(),
                PropertyTagCategoryFilterQueryBuilder.class, new PropertyTagCategoryFieldFilter(), propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyCategoryV1 updateJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(PropertyTagCategory.class, dataObject, propertyCategoryFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 updateJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, dataObjects, propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryV1 createJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(PropertyTagCategory.class, dataObject, propertyCategoryFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 createJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, dataObjects, propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryV1 deleteJSONPropertyCategory(final Integer id, final String message, final Integer flag,
            final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(PropertyTagCategory.class, propertyCategoryFactory, id, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 deleteJSONPropertyCategories(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, propertyCategoryFactory, dbEntityIds,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    /* ROLE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPRole(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRole(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRoleRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRoleRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRoles(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRoles(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRolesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRolesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTRoleV1 getJSONRole(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Role.class, roleFactory, id, expand);
    }

    @Override
    public RESTRoleV1 getJSONRoleRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Role.class, roleFactory, id, expand);
    }

    @Override
    public RESTRoleCollectionV1 getJSONRoles(final String expand) {
        return getJSONResources(RESTRoleCollectionV1.class, Role.class, roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTRoleCollectionV1 getJSONRolesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTRoleCollectionV1.class, query.getMatrixParameters(), RoleFilterQueryBuilder.class,
                new RoleFieldFilter(), roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTRoleV1 updateJSONRole(final String expand, final RESTRoleV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Role.class, dataObject, roleFactory, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 updateJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTRoleCollectionV1.class, Role.class, dataObjects, roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTRoleV1 createJSONRole(final String expand, final RESTRoleV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Role.class, dataObject, roleFactory, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 createJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTRoleCollectionV1.class, Role.class, dataObjects, roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTRoleV1 deleteJSONRole(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Role.class, roleFactory, id, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 deleteJSONRoles(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTRoleCollectionV1.class, Role.class, roleFactory, dbEntityIds, RESTv1Constants.ROLES_EXPANSION_NAME,
                expand, logDetails);
    }

    /* TRANSLATEDTOPIC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTranslatedTopic(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopic(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopicRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopicRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopics(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopics(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopicsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopicsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTranslatedTopicV1 getJSONTranslatedTopic(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(TranslatedTopicData.class, translatedTopicFactory, id, expand);
    }

    @Override
    public RESTTranslatedTopicV1 getJSONTranslatedTopicRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(TranslatedTopicData.class, translatedTopicFactory, id, revision, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 getJSONTranslatedTopicsWithQuery(PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTranslatedTopicCollectionV1.class, query.getMatrixParameters(),
                TranslatedTopicDataFilterQueryBuilder.class, new TranslatedTopicFieldFilter(), translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 getJSONTranslatedTopics(final String expand) {
        return getJSONResources(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedTopicV1 updateJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(TranslatedTopicData.class, dataObject, translatedTopicFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 updateJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, dataObjects, translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicV1 createJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(TranslatedTopicData.class, dataObject, translatedTopicFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 createJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, dataObjects, translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicV1 deleteJSONTranslatedTopic(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(TranslatedTopicData.class, translatedTopicFactory, id, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 deleteJSONTranslatedTopics(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, translatedTopicFactory, dbEntityIds,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    /* STRINGCONSTANT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPStringConstant(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstantRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstantRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstants(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstantsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTStringConstantV1 getJSONStringConstant(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(StringConstants.class, stringConstantFactory, id, expand);
    }

    @Override
    public RESTStringConstantV1 getJSONStringConstantRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(StringConstants.class, stringConstantFactory, id, revision, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 getJSONStringConstants(final String expand) {
        return getJSONResources(RESTStringConstantCollectionV1.class, StringConstants.class, stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 getJSONStringConstantsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTStringConstantCollectionV1.class, query.getMatrixParameters(),
                StringConstantFilterQueryBuilder.class, new StringConstantFieldFilter(), stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTStringConstantV1 updateJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(StringConstants.class, dataObject, stringConstantFactory, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 updateJSONStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, dataObjects, stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTStringConstantV1 createJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(StringConstants.class, dataObject, stringConstantFactory, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 createJSONStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, dataObjects, stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTStringConstantV1 deleteJSONStringConstant(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(StringConstants.class, stringConstantFactory, id, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 deleteJSONStringConstants(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, stringConstantFactory, dbEntityIds,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* USER FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPUser(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUser(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUserRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUserRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUsers(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUsers(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUsersWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUsersWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTUserV1 getJSONUser(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(User.class, userFactory, id, expand);
    }

    @Override
    public RESTUserV1 getJSONUserRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(User.class, userFactory, id, revision, expand);
    }

    @Override
    public RESTUserCollectionV1 getJSONUsers(final String expand) {
        return getJSONResources(RESTUserCollectionV1.class, User.class, userFactory, RESTv1Constants.USERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTUserCollectionV1 getJSONUsersWithQuery(final PathSegment query, final String expand) {
        return this.getJSONResourcesFromQuery(RESTUserCollectionV1.class, query.getMatrixParameters(), UserFilterQueryBuilder.class,
                new UserFieldFilter(), userFactory, RESTv1Constants.USERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTUserV1 updateJSONUser(final String expand, final RESTUserV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(User.class, dataObject, userFactory, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 updateJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTUserCollectionV1.class, User.class, dataObjects, userFactory, RESTv1Constants.USERS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTUserV1 createJSONUser(final String expand, final RESTUserV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(User.class, dataObject, userFactory, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 createJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTUserCollectionV1.class, User.class, dataObjects, userFactory, RESTv1Constants.USERS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTUserV1 deleteJSONUser(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(User.class, userFactory, id, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 deleteJSONUsers(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");
        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTUserCollectionV1.class, User.class, userFactory, dbEntityIds, RESTv1Constants.USERS_EXPANSION_NAME,
                expand, logDetails);
    }

    /* TAG FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTag(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTag(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTagRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTagRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTags(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTags(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTagsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTagsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTagV1 getJSONTag(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Tag.class, tagFactory, id, expand);
    }

    @Override
    public RESTTagV1 getJSONTagRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Tag.class, tagFactory, id, revision, expand);
    }

    @Override
    public RESTTagCollectionV1 getJSONTags(final String expand) {
        return getJSONResources(RESTTagCollectionV1.class, Tag.class, tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTagCollectionV1 getJSONTagsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTagCollectionV1.class, query.getMatrixParameters(), TagFilterQueryBuilder.class,
                new TagFieldFilter(), tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTagV1 updateJSONTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Tag.class, dataObject, tagFactory, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 updateJSONTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTagCollectionV1.class, Tag.class, dataObjects, tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTTagV1 createJSONTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Tag.class, dataObject, tagFactory, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 createJSONTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTagCollectionV1.class, Tag.class, dataObjects, tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTTagV1 deleteJSONTag(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Tag.class, tagFactory, id, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 deleteJSONTags(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTagCollectionV1.class, Tag.class, tagFactory, dbEntityIds, RESTv1Constants.TAGS_EXPANSION_NAME,
                expand, logDetails);
    }

    /* CATEGORY FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPCategory(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategory(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategoryRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategoryRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategories(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategories(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategoriesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategoriesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTCategoryCollectionV1 getJSONCategories(final String expand) {
        return getJSONResources(RESTCategoryCollectionV1.class, Category.class, categoryFactory, RESTv1Constants.CATEGORIES_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTCategoryV1 getJSONCategory(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Category.class, categoryFactory, id, expand);
    }

    @Override
    public RESTCategoryV1 getJSONCategoryRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Category.class, categoryFactory, id, revision, expand);
    }

    @Override
    public RESTCategoryCollectionV1 getJSONCategoriesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTCategoryCollectionV1.class, query.getMatrixParameters(), CategoryFilterQueryBuilder.class,
                new CategoryFieldFilter(), categoryFactory, RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCategoryV1 updateJSONCategory(final String expand, final RESTCategoryV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Category.class, dataObject, categoryFactory, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 updateJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTCategoryCollectionV1.class, Category.class, dataObjects, categoryFactory,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCategoryV1 createJSONCategory(final String expand, final RESTCategoryV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Category.class, dataObject, categoryFactory, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 createJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTCategoryCollectionV1.class, Category.class, dataObjects, categoryFactory,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCategoryV1 deleteJSONCategory(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Category.class, categoryFactory, id, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 deleteJSONCategories(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTCategoryCollectionV1.class, Category.class, categoryFactory, dbEntityIds,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    /* IMAGE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPImage(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImage(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImageRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImageRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImages(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImages(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImagesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImagesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTImageV1 getJSONImage(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(ImageFile.class, imageFactory, id, expand);
    }

    @Override
    public RESTImageV1 getJSONImageRevision(final Integer id, Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(ImageFile.class, imageFactory, id, revision, expand);
    }

    @Override
    public RESTImageCollectionV1 getJSONImages(final String expand) {
        return getJSONResources(RESTImageCollectionV1.class, ImageFile.class, imageFactory, RESTv1Constants.IMAGES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTImageCollectionV1 getJSONImagesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTImageCollectionV1.class, query.getMatrixParameters(), ImageFilterQueryBuilder.class,
                new ImageFieldFilter(), imageFactory, RESTv1Constants.IMAGES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTImageV1 updateJSONImage(final String expand, final RESTImageV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(ImageFile.class, dataObject, imageFactory, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 updateJSONImages(final String expand, final RESTImageCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTImageCollectionV1.class, ImageFile.class, dataObjects, imageFactory,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTImageV1 createJSONImage(final String expand, final RESTImageV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(ImageFile.class, dataObject, imageFactory, expand, logDetails);
    }

    @Override
    public RESTMatchedImageV1 createOrMatchJSONImage(final String expand, final RESTImageV1 dataObject, final String message,
            final Integer flag, final String userId) {
        // make sure the incoming topic has specified some xml
        if (dataObject.getConfiguredParameters().indexOf(RESTImageV1.LANGUAGEIMAGES_NAME) == -1 ||
                dataObject.getLanguageImages_OTM() == null ||
                dataObject.getLanguageImages_OTM().getItems().size() == 0) {
            throw new BadRequestException(
                    "The image to be created or matched needs to have at least one language image added and defined to the configured " +
                            "parameters.");
        }

        final StringBuilder query = new StringBuilder("SELECT imageFile FROM ImageFile as ImageFile WHERE");

        /*
            Find any existing image whose language images are a match for the ones supplied. In this case we ignore the
            presence of additional language images, so the returned image may already have some translations.
         */
        int count = 0;
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final RESTLanguageImageCollectionItemV1 restLanguageImageCollectionItemV1 : dataObject.getLanguageImages_OTM().getItems()) {
            if (count != 0) {
                query.append(" AND");
            }
            query.append(" EXISTS " +
                    "(SELECT languageImage FROM LanguageImage as LanguageImage " +
                    "WHERE languageImage.imageFile.imageFileId = imageFile.imageFileId " +
                    "AND languageImage.imageContentHash = :hash" + count + " " +
                    "AND languageImage.locale = :locale" + count + ")");

            // Add the bind parameters
            final String hash = HashUtilities.generateSHA256(restLanguageImageCollectionItemV1.getItem().getImageData());
            parameters.put("hash" + count, hash.toCharArray());
            parameters.put("locale" + count, restLanguageImageCollectionItemV1.getItem().getLocale());

            // increase the count
            ++count;
        }

        // Create the query
        final Query query1 = entityManager.createQuery(query.toString());

        // Add the bind parameters to the query
        for (final Map.Entry<String, Object> parameter : parameters.entrySet()) {
            query1.setParameter(parameter.getKey(), parameter.getValue());
        }

        // Execute the query
        final List<ImageFile> images = query1.getResultList();

        if (images.size() != 0) {
            // we have at least one image with identical language images, so return that
            return new RESTMatchedImageV1(getJSONImage(images.get(0).getId(), expand), true);
        } else {
            // we have no matching topics, so create a new one
            return new RESTMatchedImageV1(createJSONImage(expand, dataObject, message, flag, userId), false);
        }
    }

    @Override
    public RESTImageCollectionV1 createJSONImages(final String expand, final RESTImageCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTImageCollectionV1.class, ImageFile.class, dataObjects, imageFactory,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTImageV1 deleteJSONImage(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(ImageFile.class, imageFactory, id, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 deleteJSONImages(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTImageCollectionV1.class, ImageFile.class, imageFactory, dbEntityIds,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    /* RAW FUNCTIONS */
    @Override
    public Response getRAWImage(final Integer id, final String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final ImageFile entity = getEntity(ImageFile.class, id);
        final String fixedLocale = locale == null ? ApplicationConfig.getInstance().getDefaultLocale() : locale;

        // Try and find the locale specified first
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (fixedLocale.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        // If the specified locale can't be found then use the default
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (ApplicationConfig.getInstance().getDefaultLocale().equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        throw new BadRequestException("No image exists for the " + fixedLocale + " locale.");
    }

    @Override
    public Response getRAWImageRevision(final Integer id, final Integer revision, final String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        final ImageFile entity = getEntity(ImageFile.class, id, revision);
        final String fixedLocale = locale == null ? ApplicationConfig.getInstance().getDefaultLocale() : locale;

        // Try and find the locale specified first
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (fixedLocale.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        // If the specified locale can't be found then use the default
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (ApplicationConfig.getInstance().getDefaultLocale().equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        throw new BadRequestException("No image exists for the " + fixedLocale + " locale.");
    }

    @Override
    public Response getRAWImageThumbnail(final Integer id, final String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final ImageFile entity = getEntity(ImageFile.class, id);
        final String fixedLocale = locale == null ? ApplicationConfig.getInstance().getDefaultLocale() : locale;

        try {
            LanguageImage foundLanguageImage = null;

            // Try and find the locale specified first
            for (final LanguageImage languageImage : entity.getLanguageImages()) {
                if (fixedLocale.equalsIgnoreCase(languageImage.getLocale())) {
                    foundLanguageImage = languageImage;
                    break;
                }
            }

            if (foundLanguageImage == null) {
                // If the specified locale can't be found then use the default */
                for (final LanguageImage languageImage : entity.getLanguageImages()) {
                    if (ApplicationConfig.getInstance().getDefaultLocale().equalsIgnoreCase(languageImage.getLocale())) {
                        foundLanguageImage = languageImage;
                        break;
                    }
                }
            }

            if (foundLanguageImage != null) {
                byte[] base64Thumbnail = foundLanguageImage.getThumbnailData();
                final String mimeType = foundLanguageImage.getMimeType();
                if (mimeType.equals("image/svg+xml")) {
                    return Response.ok(Base64.decode(base64Thumbnail), "image/jpg").build();
                } else {
                    return Response.ok(Base64.decode(base64Thumbnail), mimeType).build();
                }
            } else {
                throw new BadRequestException("No image exists for the " + fixedLocale + " locale.");
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }

    /* TOPIC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTopicsWithQuery(PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopicsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public Response getJSONTopicXMLWithXSL(
            @Context final Request req,
            @PathParam("id") final Integer id,
            @QueryParam("includeTitle") final Boolean includeTitle,
            @QueryParam("csNodeId") final Integer contentSpecContext) {

        //Create cache control header
        final CacheControl cc = new CacheControl();
        //Set max age to one year
        cc.setMaxAge(31536000);

        final Topic topic = entityManager.find(Topic.class, id);

        CSNode node = null;
        if (contentSpecContext != null) {
            node = entityManager.find(CSNode.class, contentSpecContext);
        }

        // Calculate the ETag on last modified date of user resource
        final EntityTag etag = new EntityTag(EnversUtilities.getLatestRevision(entityManager, topic).toString() +
                (node == null ? "" : ":" + node.getId() + "-" + EnversUtilities.getLatestRevision(entityManager, node)));


        // Verify if it matched with etag available in http request
        final Response.ResponseBuilder rb = req.evaluatePreconditions(etag);

        // If the supplied etag matches the etag we generated, return
        // a unmodifed response.

        if (rb != null) {
            return rb.cacheControl(cc).tag(etag).build();
        }

        if (topic == null) throw new NotFoundException("No topic was found with an ID of " + id);

        final String xml = topic.getTopicXML();
        try {
            final String retValue = addXSLToTopicXML(xml, includeTitle, node);
            return Response.ok(retValue).cacheControl(cc).tag(etag).build();
        } catch (final SAXException ex) {
            throw new InternalServerErrorException("The topic has invalid XML");
        }
    }

    @Override
    public Response getJSONTopicRevisionXMLWithXSL(
            @Context final Request req,
            @PathParam("id") final Integer id,
            @PathParam("rev") final Integer revision,
            @QueryParam("includeTitle") final Boolean includeTitle,
            @QueryParam("csNodeId") final Integer contentSpecContext) {

        //Create cache control header
        final CacheControl cc = new CacheControl();
        //Set max age to one year
        cc.setMaxAge(31536000);

        CSNode node = null;
        if (contentSpecContext != null) {
            node = entityManager.find(CSNode.class, contentSpecContext);
        }

        // Calculate the ETag on last modified date of user resource
        final EntityTag etag = new EntityTag(revision.toString() +
                (node == null ? "" : ":" + node.getId() + "-" + EnversUtilities.getLatestRevision(entityManager, node)));

        // Verify if it matched with etag available in http request
        final Response.ResponseBuilder rb = req.evaluatePreconditions(etag);

        // If the supplied etag matches the etag we generated (in the case if the client
        // supplies any etag it will match, because a revision never changes), return
        // a unmodifed response.

        if (rb != null) {
            return rb.cacheControl(cc).tag(etag).build();
        }

        final Topic topic = getEntity(Topic.class, id, revision);

        if (topic == null) throw new NotFoundException("No topic was found with an ID of " + id);

        final String xml = topic.getTopicXML();
        try {
            final String retValue = addXSLToTopicXML(xml, includeTitle, node);
            return Response.ok(retValue).cacheControl(cc).tag(etag).build();
        } catch (final SAXException ex) {
            throw new InternalServerErrorException("The topic has invalid XML");
        }
    }

    @Override
    public String getJSONPTopics(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopics(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopic(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopic(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopicRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopicRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTopicCollectionV1 getJSONTopics(final String expand) {
        return getJSONResources(RESTTopicCollectionV1.class, Topic.class, topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTopicCollectionV1 getJSONTopicsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTopicCollectionV1.class, query.getMatrixParameters(), TopicFilterQueryBuilder.class,
                new TopicFieldFilter(), topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    /**
     * http://localhost:8080/pressgang-ccms/rest/1/topics/get/svg/query;topicIncludedInSpec=13968/chart;chartTagGroup=4;chartTagGroup=5;
     * chartTagGroup=6;chartTitle=Topic%20Types
     *
     * @param query The query settings. Same ones as getJSONTopicsWithQuery accespts
     * @param chart The charting settings.
     * @return A SVG chart image
     */
    @Override
    public String getSVGTopicsWithQuery(final PathSegment query, final PathSegment chart) {
        /* The stream to hold the output */
        final StringWriter output = new StringWriter();

        try {
            final String expand = "{\"branches\":[" +
                    "{\"trunk\":{\"name\": \"" + RESTv1Constants.TOPICS_EXPANSION_NAME + "\"}, \"branches\": [" +
                    "{\"trunk\":{\"name\": \"" + RESTTopicV1.TAGS_NAME + "\"}}" +
                    "]}" +
                    "]}";

            final RESTTopicCollectionV1 topics = getJSONResourcesFromQuery(RESTTopicCollectionV1.class, query.getMatrixParameters(),
                    TopicFilterQueryBuilder.class, new TopicFieldFilter(), topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);

            final MultivaluedMap<String, String> chartingOptions = chart.getMatrixParameters();
            final Map<Integer, String> tagNames = new HashMap<Integer, String>();
            final Map<Integer, AtomicInteger> tagCounts = new HashMap<Integer, AtomicInteger>();

            String chartTitle = "PressGang CCMS Chart";
            String chartType = RESTv1Constants.CHART_PIE_TYPE;
            boolean showTitle = true;
            boolean showLegend = true;

            /*
                Loop through the options and use them to set the chart options
             */
            for (final String option : chartingOptions.keySet()) {
                final List<String> values = chartingOptions.get(option);

                if (RESTv1Constants.CHART_TAG_GROUP.equals(option)) {

                    for (final String value : values) {
                        try {
                            final Integer tagId = Integer.parseInt(value);
                            if (!tagCounts.containsKey(tagId)) {
                                tagCounts.put(tagId, new AtomicInteger(0));
                            }
                        } catch (final NumberFormatException ex) {
                            log.error(value + " is not a valid tag id");
                        }
                    }
                } else if (RESTv1Constants.CHART_TITLE.equals(option)) {
                    if (values.size() != 0) {
                        chartTitle = values.get(0);
                    }
                } else if (RESTv1Constants.CHART_TYPE.equals(option)) {
                    if (values.size() != 0) {
                        if (RESTv1Constants.CHART_PIE_TYPE.equals(values.get(0))) {
                            chartType = RESTv1Constants.CHART_PIE_TYPE;
                        } else if (RESTv1Constants.CHART_BAR_TYPE.equals(values.get(0))) {
                            chartType = RESTv1Constants.CHART_BAR_TYPE;
                        } else if (RESTv1Constants.CHART_PIE3D_TYPE.equals(values.get(0))) {
                            chartType = RESTv1Constants.CHART_PIE3D_TYPE;
                        }
                    }
                } else if (RESTv1Constants.CHART_SHOW_LEGEND.equals(option)) {
                    if (values.size() != 0) {
                        showLegend = Boolean.parseBoolean(values.get(0));
                    }
                } else {
                    log.error(option + " is not a valid chart option");
                }
            }

            for (final Integer tagId : tagCounts.keySet()) {
                for (final RESTTopicCollectionItemV1 topic : topics.getItems()) {
                    for (final RESTTagCollectionItemV1 tag : topic.getItem().getTags().getItems()) {
                        if (tagId.equals(tag.getItem().getId())) {
                            if (!tagNames.containsKey(tagId)) {
                                tagNames.put(tagId, tag.getItem().getName());
                            }

                            tagCounts.get(tagId).incrementAndGet();
                        }
                    }
                }
            }

            JFreeChart jFreeChart = null;

            if (RESTv1Constants.CHART_PIE_TYPE.equals(chartType)) {
                /* Define the data range for SVG Pie Chart */
                final DefaultPieDataset mySvgPieChartData = new DefaultPieDataset();

                for (final Integer tagId : tagNames.keySet()) {
                    mySvgPieChartData.setValue(tagNames.get(tagId), tagCounts.get(tagId).get());
                }

                /* This method returns a JFreeChart object back to us */
                jFreeChart = ChartFactory.createPieChart(chartTitle, mySvgPieChartData, showLegend, false, false);
            } else if (RESTv1Constants.CHART_PIE3D_TYPE.equals(chartType)) {
                /* Define the data range for SVG Pie Chart */
                final DefaultPieDataset mySvgPieChartData = new DefaultPieDataset();

                for (final Integer tagId : tagNames.keySet()) {
                    mySvgPieChartData.setValue(tagNames.get(tagId), tagCounts.get(tagId).get());
                }

                /* This method returns a JFreeChart object back to us */
                jFreeChart = ChartFactory.createPieChart3D(chartTitle, mySvgPieChartData, showLegend, false, false);
                ((PiePlot3D) jFreeChart.getPlot()).setForegroundAlpha(0.6f);
            } else if (RESTv1Constants.CHART_BAR_TYPE.equals(chartType)) {
                /* Define the data range for SVG Pie Chart */
                final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();

                for (final Integer tagId : tagNames.keySet()) {
                    categoryDataset.addValue(tagCounts.get(tagId).get(), tagNames.get(tagId), "");
                }

                /* This method returns a JFreeChart object back to us */
                jFreeChart = ChartFactory.createBarChart(chartTitle, "Tags", "Topics", categoryDataset, PlotOrientation.VERTICAL,
                        showLegend, false, false);
            }

            if (jFreeChart != null) {
                /* Our logical Pie chart is ready at this step. We can now write the chart as SVG using Batik */
                /* Get DOM Implementation */
                final DOMImplementation mySVGDOM = GenericDOMImplementation.getDOMImplementation();

                /* create Document object */
                final String svgNS = "http://www.w3.org/2000/svg";
                final Document document = mySVGDOM.createDocument(svgNS, "svg", null);

                /* Create SVG Generator */
                final SVGGraphics2D my_svg_generator = new SVGGraphics2D(document);

                /* Render chart as SVG 2D Graphics object */
                jFreeChart.draw(my_svg_generator, new Rectangle2D.Double(0, 0, 640, 480), null);

                /* Write output to file */
                my_svg_generator.stream(output);
            }


            return output.toString();

        } catch (final Exception ex) {
            throw new InternalServerErrorException(ex);
        } finally {
            try {
                // not required, but good practice
                output.close();
            } catch (IOException e) {
                //
            }
        }
    }

    @Override
    public Feed getATOMTopicsWithQuery(PathSegment query, final String expand) {
        final RESTTopicCollectionV1 topics = getJSONTopicsWithQuery(query, expand);
        return convertTopicsIntoFeed(topics, "Topic Query (" + topics.getSize() + " items)");
    }

    @Override
    public RESTTopicV1 getJSONTopic(final Integer id, final String expand) {
        assert id != null : "The id parameter can not be null";

        return getJSONResource(Topic.class, topicFactory, id, expand);
    }

    @Override
    public RESTTopicV1 getJSONTopicRevision(final Integer id, final Integer revision, final String expand) {
        assert id != null : "The id parameter can not be null";
        assert revision != null : "The revision parameter can not be null";

        return getJSONResource(Topic.class, topicFactory, id, revision, expand);
    }

    @Override
    public RESTTopicV1 updateJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Topic.class, dataObject, topicFactory, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 updateJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTopicCollectionV1.class, Topic.class, dataObjects, topicFactory,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTopicV1 createJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Topic.class, dataObject, topicFactory, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 createJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTopicCollectionV1.class, Topic.class, dataObjects, topicFactory,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTopicV1 deleteJSONTopic(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Topic.class, topicFactory, id, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 deleteJSONTopics(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTopicCollectionV1.class, Topic.class, topicFactory, dbEntityIds,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTMatchedTopicV1 createOrMatchJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message,
            final Integer flag, final String userId) {
        // make sure the incoming topic has specified some xml
        if (dataObject.getConfiguredParameters().indexOf(RESTTopicV1.XML_NAME) == -1 ||
                dataObject.getXml() == null ||
                dataObject.getXml().trim().length() == 0) {
            throw new BadRequestException(
                    "The topic to be created or matched needs to have the XML field populated and added to the configured parameters.");
        }

        try {
            final Document doc = XMLUtilities.convertStringToDocument(dataObject.getXml());
            if (doc != null) {

                /*
                    We need to get the XML as it would be saved if this were a new topic. This is so we can get
                    an accurate hash of the content to compare to existing topics.

                    So we follow the logic that would be applied if this were a new topic
                    (TopicV1Factory.syncDBEntityWithRESTEntityFirstPass()). If the topic is a normal
                    topic (i.e. not an abstract or revision history) and it has a title field specified, then
                    the title of the section is overwritten.
                 */
                if (TopicUtilities.isTopicNormalTopic(dataObject)) {
                    if (dataObject.getConfiguredParameters().indexOf(RESTTopicV1.TITLE_NAME) != -1) {
                        DocBookUtilities.setSectionTitle(TopicUtilities.getTopicXMLDocBookVersion(dataObject), dataObject.getTitle(), doc);
                    }
                }

                final String processedXML = TopicUtilities.processXML(entityManager, doc);
                final String shaHash = HashUtilities.generateSHA256(processedXML);

                final List<Topic> topics = entityManager.createQuery(
                        "SELECT topic FROM Topic as Topic WHERE topic.topicContentHash = '" + shaHash + "'").getResultList();

                if (topics.size() != 0) {
                    for (final Topic topic : topics) {
                        /*
                            The locale is either unspecified or is a match.
                         */
                        if (dataObject.getConfiguredParameters().indexOf(RESTTopicV1.LOCALE_NAME) == -1 ||
                                topic.getTopicLocale().equals(dataObject.getLocale())) {
                            return new RESTMatchedTopicV1(getJSONTopic(topic.getId(), expand), true);
                        }
                    }
                }

                // we have no matching topics, so create a new one
                return new RESTMatchedTopicV1(createJSONTopic(expand, dataObject, message, flag, userId), false);

            } else {
                throw new BadRequestException("The topic to be created or matched needs to have valid XML.");
            }
        } catch (final SAXException ex) {
            throw new BadRequestException("The topic to be created or matched needs to have valid XML.");
        }
    }

    // XML TOPIC FUNCTIONS

    @Override
    public RESTTopicCollectionV1 getXMLTopics(final String expand) {
        return getXMLResources(RESTTopicCollectionV1.class, Topic.class, topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTopicV1 getXMLTopic(final Integer id) {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, topicFactory, id, null);
    }

    @Override
    public RESTTopicV1 getXMLTopicRevision(final Integer id, final Integer revision) {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, topicFactory, id, revision, null);
    }

    @Override
    public String getXMLTopicXML(final Integer id) {
        assert id != null : "The id parameter can not be null";

        final RESTTopicV1 entity = getXMLResource(Topic.class, topicFactory, id, null);
        // TODO Account for other topic types
        if (entity.getXmlFormat() != null) {
            if (entity.getXmlFormat() == RESTXMLFormat.DOCBOOK_45) {
                return DocBookUtilities.addDocBook45Doctype(entity.getXml(), null, "section");
            } else if (entity.getXmlFormat() == RESTXMLFormat.DOCBOOK_50) {
                return DocBookUtilities.addDocBook50Namespace(entity.getXml(), "section");
            }
        }

        return entity.getXml();
    }

    @Override
    public String updateXMLTopicXML(Integer id, String xml, String message, Integer flag, String userId) {
        if (xml == null) throw new BadRequestException("The xml parameter can not be null");
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        // Create the topic data object that will be saved
        final RESTTopicV1 topic = new RESTTopicV1();
        topic.setId(id);
        topic.explicitSetXml(xml);

        RESTTopicV1 updatedTopic = updateJSONEntity(Topic.class, topic, topicFactory, "", logDetails);
        return updatedTopic == null ? null : updatedTopic.getXml();
    }

    @Override
    public String getXMLTopicRevisionXML(final Integer id, final Integer revision) {
        assert id != null : "The id parameter can not be null";
        assert revision != null : "The revision parameter can not be null";

        final RESTTopicV1 entity = getXMLResource(Topic.class, topicFactory, id, revision, null);
        // TODO Account for other topic types
        if (entity.getXmlFormat() != null) {
            if (entity.getXmlFormat() == RESTXMLFormat.DOCBOOK_45) {
                return DocBookUtilities.addDocBook45Doctype(entity.getXml(), null, "section");
            } else if (entity.getXmlFormat() == RESTXMLFormat.DOCBOOK_50) {
                return DocBookUtilities.addDocBook50Namespace(entity.getXml(), "section");
            }
        }

        return entity.getXml();
    }

    @Override
    public String getXMLTopicXMLContained(final Integer id, final String containerName) {
        assert id != null : "The id parameter can not be null";
        assert containerName != null : "The containerName parameter can not be null";

        return ComponentTopicV1.returnXMLWithNewContainer(getXMLResource(Topic.class, topicFactory, id, null), containerName);
    }

    @Override
    public String getXMLTopicXMLNoContainer(final Integer id, final Boolean includeTitle) {
        assert id != null : "The id parameter can not be null";

        final String retValue = ComponentTopicV1.returnXMLWithNoContainer(getXMLResource(Topic.class, topicFactory, id, null),
                includeTitle);
        return retValue;
    }

    // CSV TOPIC FUNCTIONS

    @Override
    public String getCSVTopics() {
        final List<Topic> topicList = getAllEntities(Topic.class);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=Topics.csv");

        return TopicUtilities.getCSVForTopics(entityManager, topicList);
    }

    @Override
    public String getCSVTopicsWithQuery(@PathParam("query") PathSegment query) {
        final CriteriaQuery<Topic> criteriaQuery = getEntitiesFromQuery(query.getMatrixParameters(),
                new TopicFilterQueryBuilder(entityManager), new TopicFieldFilter());
        final List<Topic> topicList = entityManager.createQuery(criteriaQuery).getResultList();

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=Topics.csv");

        return TopicUtilities.getCSVForTopics(entityManager, topicList);
    }

    // ZIP TOPIC FUNCTIONS

    @Override
    public byte[] getZIPTopics() {
        final List<Topic> topicList = getAllEntities(Topic.class);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=XML.zip");

        return TopicUtilities.getZIPTopicXMLDump(topicList);
    }

    @Override
    public byte[] getZIPTopicsWithQuery(PathSegment query) {
        final CriteriaQuery<Topic> criteriaQuery = getEntitiesFromQuery(query.getMatrixParameters(),
                new TopicFilterQueryBuilder(entityManager), new TopicFieldFilter());
        final List<Topic> topicList = entityManager.createQuery(criteriaQuery).getResultList();

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=XML.zip");

        return TopicUtilities.getZIPTopicXMLDump(topicList);
    }

    /* FILTERS FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPFilter(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilter(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFilterRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilterRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFilters(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilters(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFiltersWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFiltersWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTFilterV1 getJSONFilter(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Filter.class, filterFactory, id, expand);
    }

    @Override
    public RESTFilterV1 getJSONFilterRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Filter.class, filterFactory, id, revision, expand);
    }

    @Override
    public RESTFilterCollectionV1 getJSONFilters(final String expand) {
        return getJSONResources(RESTFilterCollectionV1.class, Filter.class, filterFactory, RESTv1Constants.FILTERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFilterCollectionV1 getJSONFiltersWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTFilterCollectionV1.class, query.getMatrixParameters(), FilterFilterQueryBuilder.class,
                new FilterFieldFilter(), filterFactory, RESTv1Constants.FILTERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFilterV1 updateJSONFilter(final String expand, final RESTFilterV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Filter.class, dataObject, filterFactory, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 updateJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTFilterCollectionV1.class, Filter.class, dataObjects, filterFactory,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTFilterV1 createJSONFilter(final String expand, final RESTFilterV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Filter.class, dataObject, filterFactory, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 createJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTFilterCollectionV1.class, Filter.class, dataObjects, filterFactory,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTFilterV1 deleteJSONFilter(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Filter.class, filterFactory, id, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 deleteJSONFilters(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTFilterCollectionV1.class, Filter.class, filterFactory, dbEntityIds,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    /* INTEGERCONSTANT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPIntegerConstant(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstantRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstantRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstants(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstantsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTIntegerConstantV1 getJSONIntegerConstant(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(IntegerConstants.class, integerConstantFactory, id, expand);
    }

    @Override
    public RESTIntegerConstantV1 getJSONIntegerConstantRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(IntegerConstants.class, integerConstantFactory, id, revision, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 getJSONIntegerConstants(final String expand) {
        return getJSONResources(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 getJSONIntegerConstantsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTIntegerConstantCollectionV1.class, query.getMatrixParameters(),
                IntegerConstantFilterQueryBuilder.class, new IntegerConstantFieldFilter(), integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTIntegerConstantV1 updateJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(IntegerConstants.class, dataObject, integerConstantFactory, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 updateJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, dataObjects, integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantV1 createJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(IntegerConstants.class, dataObject, integerConstantFactory, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 createJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, dataObjects, integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantV1 deleteJSONIntegerConstant(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(IntegerConstants.class, integerConstantFactory, id, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 deleteJSONIntegerConstants(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, integerConstantFactory, dbEntityIds,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* CONTENT SPEC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPContentSpec(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpec(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecs(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecs(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTContentSpecV1 getJSONContentSpec(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(ContentSpec.class, contentSpecFactory, id, expand);
    }

    @Override
    public RESTContentSpecV1 getJSONContentSpecRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(ContentSpec.class, contentSpecFactory, id, revision, expand);
    }

    @Override
    public RESTContentSpecCollectionV1 getJSONContentSpecs(final String expand) {
        return getJSONResources(RESTContentSpecCollectionV1.class, ContentSpec.class, contentSpecFactory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTContentSpecCollectionV1 getJSONContentSpecsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTContentSpecCollectionV1.class, query.getMatrixParameters(),
                ContentSpecFilterQueryBuilder.class, new ContentSpecFieldFilter(), contentSpecFactory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTContentSpecV1 updateJSONContentSpec(final String expand, final RESTContentSpecV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(ContentSpec.class, dataObject, contentSpecFactory, expand, logDetails);
    }

    @Override
    public RESTContentSpecCollectionV1 updateJSONContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTContentSpecCollectionV1.class, ContentSpec.class, dataObjects, contentSpecFactory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTContentSpecV1 createJSONContentSpec(final String expand, final RESTContentSpecV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(ContentSpec.class, dataObject, contentSpecFactory, expand, logDetails);
    }

    @Override
    public RESTContentSpecCollectionV1 createJSONContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTContentSpecCollectionV1.class, ContentSpec.class, dataObjects, contentSpecFactory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTContentSpecV1 deleteJSONContentSpec(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(ContentSpec.class, contentSpecFactory, id, expand, logDetails);
    }

    @Override
    public RESTContentSpecCollectionV1 deleteJSONContentSpecs(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTContentSpecCollectionV1.class, ContentSpec.class, contentSpecFactory, dbEntityIds,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    /* ADDITIONAL CONTENT SPEC FUNCTIONS */

    @Override
    public String getTEXTContentSpec(final Integer id) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        try {
            return ContentSpecUtilities.getContentSpecText(id, entityManager);
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(null, e);
        }
    }

    @Override
    public String getTEXTContentSpecRevision(final Integer id, final Integer revision) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        try {
            return ContentSpecUtilities.getContentSpecText(id, revision, entityManager);
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(null, e);
        }
    }

    @Override
    public String updateTEXTContentSpec(final Integer id, final String contentSpec, final Boolean strictTitles, final String message,
            final Integer flag, final String userId) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (contentSpec == null) throw new BadRequestException("The contentSpec parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateTEXTContentSpecFromString(id, contentSpec, strictTitles, logDetails);
    }

    @Override
    public String createTEXTContentSpec(final String contentSpec, final Boolean strictTitles, final String message, final Integer flag,
            final String userId) {
        if (contentSpec == null) throw new BadRequestException("The contentSpec parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createTEXTContentSpecFromString(contentSpec, strictTitles, logDetails);
    }

    @Override
    public RESTTextContentSpecV1 getJSONTextContentSpec(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(ContentSpec.class, textContentSpecFactory, id, expand);
    }

    @Override
    public RESTTextContentSpecV1 getJSONTextContentSpecRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(ContentSpec.class, textContentSpecFactory, id, revision, expand);
    }

    @Override
    public RESTTextContentSpecCollectionV1 getJSONTextContentSpecs(final String expand) {
        return getJSONResources(RESTTextContentSpecCollectionV1.class, ContentSpec.class, textContentSpecFactory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTextContentSpecCollectionV1 getJSONTextContentSpecsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTextContentSpecCollectionV1.class, query.getMatrixParameters(),
                ContentSpecFilterQueryBuilder.class, new ContentSpecFieldFilter(), textContentSpecFactory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTextContentSpecV1 updateJSONTextContentSpec(final String expand, final RESTTextContentSpecV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONContentSpecFromString(dataObject, logDetails, expand);
    }

    @Override
    public RESTTextContentSpecV1 createJSONTextContentSpec(final String expand, final RESTTextContentSpecV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONContentSpecFromString(dataObject, logDetails, expand);
    }

    @Override
    public byte[] getZIPContentSpecs() {
        final PathSegment pathSegment = new PathSegmentImpl("query;", false);
        return getZIPContentSpecsWithQuery(pathSegment);
    }

    @Override
    public byte[] getZIPContentSpecsWithQuery(final PathSegment query) {
        response.getOutputHeaders().putSingle("Content-Disposition", "filename=ContentSpecs.zip");

        final CriteriaQuery<ContentSpec> contentSpecQuery = getEntitiesFromQuery(query.getMatrixParameters(),
                new ContentSpecFilterQueryBuilder(entityManager), new ContentSpecFieldFilter());
        final List<ContentSpec> contentSpecs = entityManager.createQuery(contentSpecQuery).getResultList();

        final HashMap<String, byte[]> files = new HashMap<String, byte[]>();
        try {
            for (final ContentSpec entity : contentSpecs) {
                final String contentSpec = ContentSpecUtilities.getContentSpecText(entity.getId(), null, entityManager);
                files.put(entity.getId() + ".contentspec", contentSpec.getBytes("UTF-8"));
            }

            return ZipUtilities.createZip(files);
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(null, e);
        }
    }

    @Override
    public RESTContentSpecV1 createJSONContentSpecSnapshot(final Integer id, final String expand, final boolean useLatestRevisions,
            final String message, final Integer flag, final String userId) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final String snapshot = previewTEXTContentSpecSnapshot(id, useLatestRevisions);
        final RESTTextContentSpecV1 textContentSpec = new RESTTextContentSpecV1();
        textContentSpec.explicitSetText(snapshot);
        final RESTTextContentSpecV1 contentSpec = createJSONTextContentSpec("", textContentSpec, message, flag, userId);
        return getJSONContentSpec(contentSpec.getId(), expand);
    }

    @Override
    public String previewTEXTContentSpecSnapshot(final Integer id, final boolean useLatestRevisions) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        // Make sure that the content spec doesn't have a failure, as if it doesn't we can't do a preview as we need a valid spec
        final ContentSpec entity = getEntity(ContentSpec.class, id);
        if (!isNullOrEmpty(entity.getFailedContentSpec())) {
            throw new BadRequestException("The content spec has failures and therefore a snapshot cannot be generated");
        }

        // Convert the entity into a ContentSpec object that can be manipulated
        final DBProviderFactory providerFactory = ProviderUtilities.getDBProviderFactory(entityManager);
        final org.jboss.pressgang.ccms.contentspec.ContentSpec contentSpec = ContentSpecUtilities.getContentSpec(id, null, providerFactory);

        // Setup the processing options
        final SnapshotOptions snapshotOptions = new SnapshotOptions();
        snapshotOptions.setAddRevisions(true);
        snapshotOptions.setUpdateRevisions(useLatestRevisions);

        // Create the snapshot
        final SnapshotProcessor snapshotProcessor = new SnapshotProcessor(providerFactory);
        snapshotProcessor.processContentSpec(contentSpec, snapshotOptions);
        return contentSpec.toString();
    }

    @Override
    public RESTTextContentSpecV1 createJSONTextContentSpecSnapshot(final Integer id, final String expand, final boolean useLatestRevisions,
            final String message, final Integer flag, final String userId) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final String snapshot = previewTEXTContentSpecSnapshot(id, useLatestRevisions);
        final RESTTextContentSpecV1 textContentSpec = new RESTTextContentSpecV1();
        textContentSpec.explicitSetText(snapshot);
        return createJSONTextContentSpec(expand, textContentSpec, message, flag, userId);
    }

    /* CONTENT SPEC NODE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPContentSpecNode(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNode(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecNodeRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNodeRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecNodes(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNodes(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecNodesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNodesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTCSNodeV1 getJSONContentSpecNode(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(CSNode.class, csNodeFactory, id, expand);
    }

    @Override
    public RESTCSNodeV1 getJSONContentSpecNodeRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(CSNode.class, csNodeFactory, id, revision, expand);
    }

    @Override
    public RESTCSNodeCollectionV1 getJSONContentSpecNodes(final String expand) {
        return getJSONResources(RESTCSNodeCollectionV1.class, CSNode.class, csNodeFactory, RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTCSNodeCollectionV1 getJSONContentSpecNodesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTCSNodeCollectionV1.class, query.getMatrixParameters(), ContentSpecNodeFilterQueryBuilder.class,
                new ContentSpecNodeFieldFilter(), csNodeFactory, RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand);
    }

//    @Override
//    public RESTCSNodeV1 updateJSONContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String message,
//            final Integer flag, final String userId) {
//        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
//        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");
//
//        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
//        return updateJSONEntity(CSNode.class, dataObject, csNodeFactory, expand, logDetails);
//    }
//
//    @Override
//    public RESTCSNodeCollectionV1 updateJSONContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
//            final String message, final Integer flag, final String userId) {
//        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
//        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");
//
//        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
//        return updateJSONEntities(RESTCSNodeCollectionV1.class, CSNode.class, dataObjects, csNodeFactory,
//                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand, logDetails);
//    }
//
//    @Override
//    public RESTCSNodeV1 createJSONContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String message,
//            final Integer flag, final String userId) {
//        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
//
//        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
//        return createJSONEntity(CSNode.class, dataObject, csNodeFactory, expand, logDetails);
//    }
//
//    @Override
//    public RESTCSNodeCollectionV1 createJSONContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
//            final String message, final Integer flag, final String userId) {
//        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
//        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");
//
//        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
//        return createJSONEntities(RESTCSNodeCollectionV1.class, CSNode.class, dataObjects, csNodeFactory,
//                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand, logDetails);
//    }
//
//    @Override
//    public RESTCSNodeV1 deleteJSONContentSpecNode(final Integer id, final String message, final Integer flag, final String userId,
//            final String expand) {
//        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");
//
//        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
//        return deleteJSONEntity(CSNode.class, csNodeFactory, id, expand, logDetails);
//    }
//
//    @Override
//    public RESTCSNodeCollectionV1 deleteJSONContentSpecNodes(final PathSegment ids, final String message, final Integer flag,
//            final String userId, final String expand) {
//        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");
//
//        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
//        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
//        return deleteJSONEntities(RESTCSNodeCollectionV1.class, CSNode.class, csNodeFactory, dbEntityIds,
//                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand, logDetails);
//    }

    /* TRANSLATED CONTENT SPEC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTranslatedContentSpec(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpec(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedContentSpecRevision(final Integer id, final Integer revision, final String expand,
            final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedContentSpecs(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecs(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedContentSpecsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTranslatedContentSpecV1 getJSONTranslatedContentSpec(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(TranslatedContentSpec.class, translatedContentSpecFactory, id, expand);
    }

    @Override
    public RESTTranslatedContentSpecV1 getJSONTranslatedContentSpecRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(TranslatedContentSpec.class, translatedContentSpecFactory, id, revision, expand);
    }

    @Override
    public RESTTranslatedContentSpecCollectionV1 getJSONTranslatedContentSpecs(final String expand) {
        return getJSONResources(RESTTranslatedContentSpecCollectionV1.class, TranslatedContentSpec.class, translatedContentSpecFactory,
                RESTv1Constants.TRANSLATED_CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedContentSpecCollectionV1 getJSONTranslatedContentSpecsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTranslatedContentSpecCollectionV1.class, query.getMatrixParameters(),
                TranslatedContentSpecFilterQueryBuilder.class, new TranslatedContentSpecFieldFilter(), translatedContentSpecFactory,
                RESTv1Constants.TRANSLATED_CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedContentSpecV1 updateJSONTranslatedContentSpec(final String expand, final RESTTranslatedContentSpecV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(TranslatedContentSpec.class, dataObject, translatedContentSpecFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedContentSpecCollectionV1 updateJSONTranslatedContentSpecs(final String expand,
            final RESTTranslatedContentSpecCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTranslatedContentSpecCollectionV1.class, TranslatedContentSpec.class, dataObjects,
                translatedContentSpecFactory, RESTv1Constants.TRANSLATED_CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedContentSpecV1 createJSONTranslatedContentSpec(final String expand, final RESTTranslatedContentSpecV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(TranslatedContentSpec.class, dataObject, translatedContentSpecFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedContentSpecCollectionV1 createJSONTranslatedContentSpecs(final String expand,
            final RESTTranslatedContentSpecCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTranslatedContentSpecCollectionV1.class, TranslatedContentSpec.class, dataObjects,
                translatedContentSpecFactory, RESTv1Constants.TRANSLATED_CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedContentSpecV1 deleteJSONTranslatedContentSpec(final Integer id, final String message, final Integer flag,
            final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(TranslatedContentSpec.class, translatedContentSpecFactory, id, expand, logDetails);
    }

    @Override
    public RESTTranslatedContentSpecCollectionV1 deleteJSONTranslatedContentSpecs(final PathSegment ids, final String message,
            final Integer flag, final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTranslatedContentSpecCollectionV1.class, TranslatedContentSpec.class, translatedContentSpecFactory,
                dbEntityIds, RESTv1Constants.TRANSLATED_CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    /* TRANSLATED CONTENT SPEC NODE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTranslatedContentSpecNode(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecNode(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedContentSpecNodeRevision(final Integer id, final Integer revision, final String expand,
            final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecNodeRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedContentSpecNodes(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecNodes(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedContentSpecNodesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedContentSpecNodesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTranslatedCSNodeV1 getJSONTranslatedContentSpecNode(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(TranslatedCSNode.class, translatedCSNodeFactory, id, expand);
    }

    @Override
    public RESTTranslatedCSNodeV1 getJSONTranslatedContentSpecNodeRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(TranslatedCSNode.class, translatedCSNodeFactory, id, revision, expand);
    }

    @Override
    public RESTTranslatedCSNodeCollectionV1 getJSONTranslatedContentSpecNodes(final String expand) {
        return getJSONResources(RESTTranslatedCSNodeCollectionV1.class, TranslatedCSNode.class, translatedCSNodeFactory,
                RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedCSNodeCollectionV1 getJSONTranslatedContentSpecNodesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTranslatedCSNodeCollectionV1.class, query.getMatrixParameters(),
                TranslatedContentSpecNodeFilterQueryBuilder.class, new TranslatedContentSpecNodeFieldFilter(), translatedCSNodeFactory,
                RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedCSNodeV1 updateJSONTranslatedContentSpecNode(final String expand, final RESTTranslatedCSNodeV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(TranslatedCSNode.class, dataObject, translatedCSNodeFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedCSNodeCollectionV1 updateJSONTranslatedContentSpecNodes(final String expand,
            final RESTTranslatedCSNodeCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTranslatedCSNodeCollectionV1.class, TranslatedCSNode.class, dataObjects, translatedCSNodeFactory,
                RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedCSNodeV1 createJSONTranslatedContentSpecNode(final String expand, final RESTTranslatedCSNodeV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(TranslatedCSNode.class, dataObject, translatedCSNodeFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedCSNodeCollectionV1 createJSONTranslatedContentSpecNodes(final String expand,
            final RESTTranslatedCSNodeCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTranslatedCSNodeCollectionV1.class, TranslatedCSNode.class, dataObjects, translatedCSNodeFactory,
                RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedCSNodeV1 deleteJSONTranslatedContentSpecNode(final Integer id, final String message, final Integer flag,
            final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(TranslatedCSNode.class, translatedCSNodeFactory, id, expand, logDetails);
    }

    @Override
    public RESTTranslatedCSNodeCollectionV1 deleteJSONTranslatedContentSpecNodes(final PathSegment ids, final String message,
            final Integer flag, final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTranslatedCSNodeCollectionV1.class, TranslatedCSNode.class, translatedCSNodeFactory, dbEntityIds,
                RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_EXPANSION_NAME, expand, logDetails);
    }

    /* FILE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPFile(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFile(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFileRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFileRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFiles(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFiles(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFilesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTFileV1 getJSONFile(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(File.class, fileFactory, id, expand);
    }

    @Override
    public RESTFileV1 getJSONFileRevision(final Integer id, Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(File.class, fileFactory, id, revision, expand);
    }

    @Override
    public RESTFileCollectionV1 getJSONFiles(final String expand) {
        return getJSONResources(RESTFileCollectionV1.class, File.class, fileFactory, RESTv1Constants.FILES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFileCollectionV1 getJSONFilesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTFileCollectionV1.class, query.getMatrixParameters(), FileFilterQueryBuilder.class,
                new FileFieldFilter(), fileFactory, RESTv1Constants.FILES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFileV1 updateJSONFile(final String expand, final RESTFileV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(File.class, dataObject, fileFactory, expand, logDetails);
    }

    @Override
    public RESTFileCollectionV1 updateJSONFiles(final String expand, final RESTFileCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTFileCollectionV1.class, File.class, dataObjects, fileFactory, RESTv1Constants.FILES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTFileV1 createJSONFile(final String expand, final RESTFileV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(File.class, dataObject, fileFactory, expand, logDetails);
    }

    @Override
    public RESTMatchedFileV1 createOrMatchJSONFile(final String expand, final RESTFileV1 dataObject, final String message, final Integer flag, final String userId) {
        // make sure the incoming topic has specified some xml
        if (dataObject.getConfiguredParameters().indexOf(RESTFileV1.LANGUAGE_FILES_NAME) == -1 ||
                dataObject.getLanguageFiles_OTM() == null ||
                dataObject.getLanguageFiles_OTM().getItems().size() == 0)
        {
            throw new BadRequestException("The file to be created or matched needs to have at least one language file added and defined to the configured parameters.");
        }

        final StringBuilder query = new StringBuilder("SELECT file FROM File as File WHERE");

        /*
            Find any existing image whose language images are a match for the ones supplied. In this case we ignore the
            presence of additional language images, so the returned image may already have some translations.
         */
        int count = 0;
        final Map<String, Object> parameters = new HashMap<String, Object>();
        for (final RESTLanguageFileCollectionItemV1 restLanguageFileCollectionItemV1 : dataObject.getLanguageFiles_OTM().getItems()) {
            if (count != 0) {
                query.append(" AND");
            }
            query.append(" EXISTS " +
                    "(SELECT languageFile FROM LanguageFile as LanguageFile " +
                    "WHERE languageFile.file.fileId = file.fileId " +
                    "AND languageFile.fileContentHash = :hash" + count + " " +
                    "AND languageFile.locale = :locale" + count + ")");

            // Add the bind parameters
            final String hash = HashUtilities.generateSHA256(restLanguageFileCollectionItemV1.getItem().getFileData());
            parameters.put("hash" + count, hash.toCharArray());
            parameters.put("locale" + count, restLanguageFileCollectionItemV1.getItem().getLocale());

            // increase the count
            ++count;
        }

        // Create the query
        final Query query1 = entityManager.createQuery(query.toString());

        // Add the bind parameters to the query
        for (final Map.Entry<String, Object> parameter : parameters.entrySet()) {
            query1.setParameter(parameter.getKey(), parameter.getValue());
        }

        // Execute the query
        final List<File> files = query1.getResultList();

        if (files.size() != 0) {
            // we have at least one image with identical language images, so return that
            return new RESTMatchedFileV1(getJSONFile(files.get(0).getId(), expand), true);
        }
        else {
            // we have no matching topics, so create a new one
            return new RESTMatchedFileV1(createJSONFile(expand, dataObject, message, flag, userId), false);
        }
    }

    @Override
    public RESTFileCollectionV1 createJSONFiles(final String expand, final RESTFileCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTFileCollectionV1.class, File.class, dataObjects, fileFactory, RESTv1Constants.FILES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTFileV1 deleteJSONFile(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(File.class, fileFactory, id, expand, logDetails);
    }

    @Override
    public RESTFileCollectionV1 deleteJSONFiles(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTFileCollectionV1.class, File.class, fileFactory, dbEntityIds, RESTv1Constants.FILES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public byte[] getRAWFile(@PathParam("id") Integer id, @QueryParam("lang") String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final File entity = getEntity(File.class, id);
        final String fixedLocale = locale == null ? ApplicationConfig.getInstance().getDefaultLocale() : locale;

        // Try and find the locale specified first
        for (final LanguageFile languageFile : entity.getLanguageFiles()) {
            if (fixedLocale.equalsIgnoreCase(languageFile.getLocale())) {
                response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getFileName());
                return languageFile.getFileData();
            }
        }

        // If the specified locale can't be found then use the default
        for (final LanguageFile languageFile : entity.getLanguageFiles()) {
            if (ApplicationConfig.getInstance().getDefaultLocale().equalsIgnoreCase(languageFile.getLocale())) {
                response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getFileName());
                return languageFile.getFileData();
            }
        }

        throw new BadRequestException("No file exists for the " + fixedLocale + " locale.");
    }

    @Override
    public byte[] getRAWFileRevision(@PathParam("id") Integer id, @PathParam("rev") Integer revision, @QueryParam("lang") String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        final File entity = getEntity(File.class, id, revision);
        final String fixedLocale = locale == null ? ApplicationConfig.getInstance().getDefaultLocale() : locale;

        // Try and find the locale specified first
        for (final LanguageFile languageFile : entity.getLanguageFiles()) {
            if (fixedLocale.equalsIgnoreCase(languageFile.getLocale())) {
                response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getFileName());
                return languageFile.getFileData();
            }
        }

        // If the specified locale can't be found then use the default
        for (final LanguageFile languageFile : entity.getLanguageFiles()) {
            if (ApplicationConfig.getInstance().getDefaultLocale().equalsIgnoreCase(languageFile.getLocale())) {
                response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getFileName());
                return languageFile.getFileData();
            }
        }

        throw new BadRequestException("No file exists for the " + fixedLocale + " locale.");
    }

    @Override
    public RESTProcessInformationV1 getJSONProcess(final String id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);
        final Process process = processHelper.getProcess(id);

        return processInformationFactory.createRESTEntityFromObject(process, getBaseUrl(), RESTv1Constants.JSON_URL, expandDataTrunk);
    }

    @Override
    public RESTProcessInformationCollectionV1 getJSONProcessesWithQuery(final PathSegment query, final String expand) {
        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get the Filter Entities
            final ProcessFilterQueryBuilder filterQueryBuilder = new ProcessFilterQueryBuilder(entityManager);
            final CriteriaQuery<Process> criteriaQuery = getEntitiesFromQuery(query.getMatrixParameters(), filterQueryBuilder,
                    new ProcessFieldFilter());

            // Order the results based on start time, since the primary key is a UUID it could be in any order
            criteriaQuery.orderBy(filterQueryBuilder.getCriteriaBuilder().desc(filterQueryBuilder.getCriteriaRoot().get("startTime")));

            // Create the Collection Class and populate it with data using the query result data
            final RESTProcessInformationCollectionV1 retValue = RESTElementCollectionFactory.create(RESTProcessInformationCollectionV1
                    .class, processInformationFactory, criteriaQuery, RESTv1Constants.PROCESSES_EXPANSION_NAME, RESTv1Constants.JSON_URL,
                    expandDataTrunk, getBaseUrl(), entityManager);

            return retValue;
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(null, e);
        }
    }

    @Override
    public RESTProcessInformationCollectionV1 getJSONProcesses(final String expand) {
        final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

        // Get all the entities, sorted on the starttime column
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Process> criteriaQuery = criteriaBuilder.createQuery(Process.class);
        final Root<Process> root = criteriaQuery.from(Process.class);
        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("startTime")));

        final TypedQuery<Process> query = entityManager.createQuery(criteriaQuery);
        final List<Process> processes = query.getResultList();

        return RESTElementCollectionFactory.create(RESTProcessInformationCollectionV1.class, processInformationFactory, processes,
                RESTv1Constants.PROCESSES_EXPANSION_NAME, RESTv1Constants.JSON_URL, expandDataTrunk, getBaseUrl());
    }

    @Override
    public RESTProcessInformationV1 stopJSONProcess(final String id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);
        final PGProcess process = processManager.getLiveProcess(id);
        if (process == null) {
            throw new BadRequestException("Process " + id + " isn't currently running or it doesn't exist");
        }

        // Try to cancel the process
        try {
            processManager.cancelProcess(id);
        } catch (IllegalStateException e) {
            throw new BadRequestException(e.getMessage());
        }

        return processInformationFactory.createRESTEntityFromObject(process.getDBEntity(), getBaseUrl(), RESTv1Constants.JSON_URL,
                expandDataTrunk);
    }

    @Override
    public RESTProcessInformationV1 pushContentSpecForTranslation(final Integer id, final String serverId, final String expand,
            final String name, final boolean contentSpecOnly, final String username, final String apikey) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        // Check that a push isn't already running
        if (processHelper.isProcessAlreadyRunning(id, ProcessType.TRANSLATION_PUSH)) {
            throw new BadRequestException("Content Spec " + id + " is already being pushed for translation");
        }

        try {
            // Unmarshall the expand string
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final Process process = processHelper.createZanataPushProcess(getBaseUrl(), id, name, contentSpecOnly, serverId, username, apikey);
            return processInformationFactory.createRESTEntityFromObject(process, getBaseUrl(), RESTv1Constants.JSON_URL, expandDataTrunk);
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(e);
        }
    }

    @Override
    public RESTProcessInformationV1 syncContentSpecTranslations(final Integer id, final String serverId, final String expand,
            final String name, final String locales, final String username, final String apikey) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        // Check that a sync isn't already running
        if (processHelper.isProcessAlreadyRunning(id, ProcessType.TRANSLATION_SYNC)) {
            throw new BadRequestException("Content Spec " + id + " already has its translations being synced");
        }

        // Create the sync task
        try {
            // Unmarshall the expand string
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Convert the locales
            final String[] localeArray = locales == null ? new String[0] : locales.split(",");
            final List<LocaleId> localeList = new ArrayList<LocaleId>();
            for (final String locale : localeArray) {
                localeList.add(LocaleId.fromJavaName(locale));
            }

            final Process process = processHelper.createZanataSyncProcess(getBaseUrl(), id, name, localeList, serverId, username, apikey);
            return processInformationFactory.createRESTEntityFromObject(process, getBaseUrl(), RESTv1Constants.JSON_URL, expandDataTrunk);
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(e);
        }
    }

    /*
     * TODO: Before the 1.5 release remove the two endpoints below. As they only exist for testing purposes.
     */

    @GET
    @Path("/process/test")
    @Produces(MediaType.APPLICATION_JSON)
    public RESTProcessInformationV1 testProcess() {
        // Create the sync task
        try {
            transaction.begin();
            entityManager.joinTransaction();

            // Create the process
            final PGProcess process = new PGProcess();
            process.setName("Test Process");
            // TODO add user who started the process
            // process.setStartedBy();
            process.addTask(new TestTask());

            // Save the newly created process entity and content spec mapping
            entityManager.persist(process.getDBEntity());
            entityManager.flush();

            // Start the process
            processManager.startProcess(process);

            transaction.commit();

            return processInformationFactory.createRESTEntityFromObject(process.getDBEntity(), getBaseUrl(), RESTv1Constants.JSON_URL,
                    null);
        } catch (JPPFException e) {
            throw RESTv1Utilities.processError(transaction, new InternalServerErrorException(e));
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(transaction, e);
        }
    }

    @GET
    @Path("/contentspec/test/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RESTProcessInformationV1 testContentSpecProcess(final @PathParam("id") Integer id) {
        // Create the sync task
        try {
            // Check that a sync isn't already running
            if (processHelper.isProcessAlreadyRunning(id, ProcessType.GENERIC)) {
                throw new BadRequestException("Content Spec " + id + " is already being tested.");
            }

            transaction.begin();
            entityManager.joinTransaction();

            final ContentSpec contentSpec = getEntity(ContentSpec.class, id);

            // Create the process
            final PGProcess process = new PGProcess();
            process.setName("Test Content Spec Process");
            // TODO add user who started the process
            // process.setStartedBy();
            process.addTask(new TestTask());

            // Add the process to the content spec
            final ContentSpecToProcess contentSpecToProcess = new ContentSpecToProcess();
            contentSpecToProcess.setProcess(process.getDBEntity());
            contentSpec.addProcess(contentSpecToProcess);

            // Save the newly created process entity and content spec mapping
            entityManager.persist(process.getDBEntity());
            entityManager.persist(contentSpecToProcess);
            entityManager.flush();

            // Start the process
            processManager.startProcess(process);

            transaction.commit();

            return processInformationFactory.createRESTEntityFromObject(process.getDBEntity(), getBaseUrl(), RESTv1Constants.JSON_URL,
                    null);
        } catch (JPPFException e) {
            throw RESTv1Utilities.processError(transaction, new InternalServerErrorException(e));
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(transaction, e);
        }
    }
}
