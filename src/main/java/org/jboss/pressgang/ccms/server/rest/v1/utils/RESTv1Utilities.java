package org.jboss.pressgang.ccms.server.rest.v1.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.exceptions.CustomConstraintViolationException;
import org.jboss.pressgang.ccms.provider.exception.ProviderException;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.server.rest.v1.EntityCache;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.NotFoundException;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RESTv1Utilities {
    private static final Logger LOG = LoggerFactory.getLogger(RESTv1Utilities.class);

    public static <U extends AuditedEntity, T extends RESTBaseEntityV1<T, ?, ?>> U findEntity(final EntityManager entityManager,
            EntityCache entityCache, final T restEntity, final Class<U> databaseClass) {
        if (restEntity.getId() == null) {
            // If the id is null than there is no possible way a matching entity could be found, so return null
            return null;
        } else if (restEntity.getId() < 0 && entityCache.containsRESTEntity(restEntity)) {
            // If the id is less than zero than it is a new entity, so get the matching entity from the cache
            return (U) entityCache.get(restEntity);
        } else {
            // At this stage the id will be positive, so get the entity from the Persistence Context
            return entityManager.find(databaseClass, restEntity.getId());
        }
    }

    /**
     * Process an Error/Exception and generate a RESTEasy Exception based on the error/exception produced.
     *
     * @param ex The Error/Exception to be processed.
     * @return A RESTEasy Exception containing the details of the Error.
     */
    public static Failure processError(final Throwable ex) {
        return processError(null, ex);
    }

    /**
     * Process an Error/Exception and generate a RESTEasy Exception based on the error/exception produced.
     *
     * @param transaction The transaction to handle rolling back changes.
     * @param ex          The Error/Exception to be processed.
     * @return A RESTEasy Exception containing the details of the Error.
     */
    public static Failure processError(final UserTransaction transaction, final Throwable ex) {
        LOG.error("Failed to process REST request", ex);

        // Rollback if a transaction is active
        try {
            if (transaction != null) {
                /*
                    Rolling back only active transactions leads to "Error checking for a transaction" and
                    "Transaction is not active" errors.

                    From http://techblogs.agiledigital.com.au/2013/01/03/jboss-as-7-1-transaction-reaping/

                        Transaction information is stored in the thread context. Each connector can potentially share the
                        same transaction – even across requests. The transaction is removed from the thread context when it
                        is committed or rolled back.

                        When the transaction is reaped, it is marked as rollback only. This changes the status of the
                        transaction to STATUS_ROLLING_BACK – and then shortly thereafter STATUS_ROLLED_BACK. However, the
                        transaction is not actually rolled back or removed from the context of the thread. It will not be
                        removed until utx.commit() or utx.rollback() is called.

                        The base servlet would never attempt to commit or rollback the transaction because:

                        (utx.getStatus() == Status.STATUS_ACTIVE)

                        will always be false after the transaction reaper has caused the status to change to ROLLED_BACK.
                        Consequently, the transaction was never removed from the thread context and an attempt would be made
                        by TxConnectionManagerImpl (seen in the stack trace above) to re-use it. Since it had been marked
                        ROLLED_BACK it could not be re-used and an exception was thrown.
                 */
                final int status = transaction.getStatus();
                if (status != Status.STATUS_NO_TRANSACTION) {
                    transaction.rollback();
                }
            }
        } catch (Throwable e) {
            return new InternalServerErrorException(e);
        }

        // We need to do some unwrapping of exception first
        Throwable cause = ex;
        while (cause != null) {
            if (cause == cause.getCause()) {
                // sometimes this can be an circular reference
                break;
            } else if (cause instanceof Failure) {
                return (Failure) cause;
            } else if (cause instanceof EntityNotFoundException) {
                return new NotFoundException(cause);
            } else if (cause instanceof ValidationException || cause instanceof CustomConstraintViolationException || cause instanceof
                    org.hibernate.exception.ConstraintViolationException || cause instanceof RollbackException) {
                break;
            } else if (cause instanceof PersistenceException) {
                if (cause.getCause() != null && cause.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                    cause = cause.getCause();
                } else {
                    break;
                }
            } else if (cause instanceof ProviderException) {
                if (cause != null && (cause instanceof ValidationException || cause instanceof PersistenceException || cause instanceof
                        CustomConstraintViolationException)) {
                    cause = cause.getCause();
                } else {
                    break;
                }
            } else if (cause instanceof BatchUpdateException) {
                cause = ((SQLException) cause).getNextException();
            } else {
                cause = cause.getCause();
            }
        }

        // This is a Persistence exception with information
        if (cause instanceof ConstraintViolationException) {
            final ConstraintViolationException e = (ConstraintViolationException) cause;
            final StringBuilder stringBuilder = new StringBuilder();

            // Construct a "readable" message outlining the validation errors
            for (ConstraintViolation invalidValue : e.getConstraintViolations())
                stringBuilder.append(invalidValue.getMessage()).append("\n");

            return new BadRequestException(stringBuilder.toString(), cause);
        } else if (cause instanceof EntityNotFoundException) {
            return new NotFoundException(cause);
        } else if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
            return new BadRequestException(cause.getMessage());
        } else if (cause instanceof ValidationException || cause instanceof CustomConstraintViolationException) {
            return new BadRequestException(cause);
        } else if (cause instanceof RollbackException) {
            return new BadRequestException(
                    "This is most likely caused by the fact that two users are trying to save the same entity at the same time.\n" + "You" +
                            " can try saving again, or reload the entity to see if there were any changes made in the background.", cause);
        } else if (cause instanceof ProviderException) {
            if (cause instanceof org.jboss.pressgang.ccms.provider.exception.NotFoundException) {
                throw new NotFoundException(cause);
            } else if (cause instanceof org.jboss.pressgang.ccms.provider.exception.InternalServerErrorException) {
                throw new InternalServerErrorException(cause);
            } else if (cause instanceof org.jboss.pressgang.ccms.provider.exception.BadRequestException) {
                throw new BadRequestException(cause);
            } else if (cause instanceof org.jboss.pressgang.ccms.provider.exception.UnauthorisedException) {
                throw new UnauthorizedException(cause);
            }
        }

        // If it's not some validation error then it must be an internal error.
        return new InternalServerErrorException(ex);
    }
}
