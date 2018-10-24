package io.undertow.servlet;

import java.util.Locale;
import java.io.Serializable;
import javax.annotation.Generated;
import org.jboss.logging.DelegatingBasicLogger;
import javax.servlet.UnavailableException;
import java.lang.String;
import java.io.IOException;
import org.jboss.logging.Logger;
import java.util.Date;
import java.lang.Exception;
import org.jboss.logging.BasicLogger;
import java.lang.Throwable;
import java.lang.Object;
import java.util.Arrays;
import java.lang.IllegalArgumentException;


import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.WARN;

/**
 * Warning this class consists of generated code.
 */
@Generated(value = "org.jboss.logging.processor.generator.model.MessageLoggerImplementor", date = "2018-08-15T12:43:14+1000")
public class UndertowServletLogger_$logger extends DelegatingBasicLogger implements UndertowServletLogger, BasicLogger, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FQCN = UndertowServletLogger_$logger.class.getName();
    public UndertowServletLogger_$logger(final Logger log) {
        super(log);
    }
    private static final Locale LOCALE = Locale.ROOT;
    protected Locale getLoggingLocale() {
        return LOCALE;
    }
    @Override
    public final void stoppingServletDueToPermanentUnavailability(final String servlet, final UnavailableException e) {
        super.log.logf(FQCN, ERROR, e, stoppingServletDueToPermanentUnavailability$str(), servlet);
    }
    private static final String stoppingServletDueToPermanentUnavailability = "UT015002: Stopping servlet %s due to permanent unavailability";
    protected String stoppingServletDueToPermanentUnavailability$str() {
        return stoppingServletDueToPermanentUnavailability;
    }
    @Override
    public final void stoppingServletUntilDueToTemporaryUnavailability(final String name, final Date till, final UnavailableException e) {
        super.log.logf(FQCN, ERROR, e, stoppingServletUntilDueToTemporaryUnavailability$str(), name, till);
    }
    private static final String stoppingServletUntilDueToTemporaryUnavailability = "UT015003: Stopping servlet %s till %s due to temporary unavailability";
    protected String stoppingServletUntilDueToTemporaryUnavailability$str() {
        return stoppingServletUntilDueToTemporaryUnavailability;
    }
    @Override
    public final void errorInvokingListener(final String method, final Class<?> listenerClass, final Throwable t) {
        super.log.logf(FQCN, ERROR, t, errorInvokingListener$str(), method, listenerClass);
    }
    private static final String errorInvokingListener = "UT015005: Error invoking method %s on listener %s";
    protected String errorInvokingListener$str() {
        return errorInvokingListener;
    }
    @Override
    public final void ioExceptionDispatchingAsyncEvent(final IOException e) {
        super.log.logf(FQCN, ERROR, e, ioExceptionDispatchingAsyncEvent$str());
    }
    private static final String ioExceptionDispatchingAsyncEvent = "UT015006: IOException dispatching async event";
    protected String ioExceptionDispatchingAsyncEvent$str() {
        return ioExceptionDispatchingAsyncEvent;
    }
    @Override
    public final void servletStackTracesAll(final String deploymentName) {
        super.log.logf(FQCN, WARN, null, servletStackTracesAll$str(), deploymentName);
    }
    private static final String servletStackTracesAll = "UT015007: Stack trace on error enabled for deployment %s, please do not enable for production use";
    protected String servletStackTracesAll$str() {
        return servletStackTracesAll;
    }
    @Override
    public final void failedtoLoadPersistentSessions(final Exception e) {
        super.log.logf(FQCN, WARN, e, failedtoLoadPersistentSessions$str());
    }
    private static final String failedtoLoadPersistentSessions = "UT015008: Failed to load development mode persistent sessions";
    protected String failedtoLoadPersistentSessions$str() {
        return failedtoLoadPersistentSessions;
    }
    @Override
    public final void failedToPersistSessionAttribute(final String attributeName, final Object value, final String sessionID, final Exception e) {
        super.log.logf(FQCN, WARN, e, failedToPersistSessionAttribute$str(), attributeName, value, sessionID);
    }
    private static final String failedToPersistSessionAttribute = "UT015009: Failed to persist session attribute %s with value %s for session %s";
    protected String failedToPersistSessionAttribute$str() {
        return failedToPersistSessionAttribute;
    }
    @Override
    public final void failedToPersistSessions(final Exception e) {
        super.log.logf(FQCN, WARN, e, failedToPersistSessions$str());
    }
    private static final String failedToPersistSessions = "UT015010: Failed to persist sessions";
    protected String failedToPersistSessions$str() {
        return failedToPersistSessions;
    }
    @Override
    public final void errorGeneratingErrorPage(final String originalErrorPage, final Object originalException, final int code, final Throwable cause) {
        super.log.logf(FQCN, ERROR, cause, errorGeneratingErrorPage$str(), originalErrorPage, originalException, code);
    }
    private static final String errorGeneratingErrorPage = "UT015012: Failed to generate error page %s for original exception: %s. Generating error page resulted in a %s.";
    protected String errorGeneratingErrorPage$str() {
        return errorGeneratingErrorPage;
    }
    @Override
    public final void errorReadingRewriteConfiguration(final IOException e) {
        super.log.logf(FQCN, ERROR, e, errorReadingRewriteConfiguration$str());
    }
    private static final String errorReadingRewriteConfiguration = "UT015014: Error reading rewrite configuration";
    protected String errorReadingRewriteConfiguration$str() {
        return errorReadingRewriteConfiguration;
    }
    private static final String invalidRewriteConfiguration = "UT015015: Error reading rewrite configuration: %s";
    protected String invalidRewriteConfiguration$str() {
        return invalidRewriteConfiguration;
    }
    @Override
    public final IllegalArgumentException invalidRewriteConfiguration(final String line) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), invalidRewriteConfiguration$str(), line));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String invalidRewriteMap = "UT015016: Invalid rewrite map class: %s";
    protected String invalidRewriteMap$str() {
        return invalidRewriteMap;
    }
    @Override
    public final IllegalArgumentException invalidRewriteMap(final String className) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), invalidRewriteMap$str(), className));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String invalidRewriteFlags2 = "UT015017: Error reading rewrite flags in line %s as %s";
    protected String invalidRewriteFlags2$str() {
        return invalidRewriteFlags2;
    }
    @Override
    public final IllegalArgumentException invalidRewriteFlags(final String line, final String flags) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), invalidRewriteFlags2$str(), line, flags));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String invalidRewriteFlags1 = "UT015018: Error reading rewrite flags in line %s";
    protected String invalidRewriteFlags1$str() {
        return invalidRewriteFlags1;
    }
    @Override
    public final IllegalArgumentException invalidRewriteFlags(final String line) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), invalidRewriteFlags1$str(), line));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    @Override
    public final void failedToDestroy(final Object object, final Throwable t) {
        super.log.logf(FQCN, ERROR, t, failedToDestroy$str(), object);
    }
    private static final String failedToDestroy = "UT015019: Failed to destroy %s";
    protected String failedToDestroy$str() {
        return failedToDestroy;
    }
    @Override
    public final void unsecuredMethodsOnPath(final String path, final java.util.Set<String> missing) {
        super.log.logf(FQCN, WARN, null, unsecuredMethodsOnPath$str(), path, missing);
    }
    private static final String unsecuredMethodsOnPath = "UT015020: Path %s is secured for some HTTP methods, however it is not secured for %s";
    protected String unsecuredMethodsOnPath$str() {
        return unsecuredMethodsOnPath;
    }
    @Override
    public final void failureDispatchingAsyncEvent(final Throwable t) {
        super.log.logf(FQCN, ERROR, t, failureDispatchingAsyncEvent$str());
    }
    private static final String failureDispatchingAsyncEvent = "UT015021: Failure dispatching async event";
    protected String failureDispatchingAsyncEvent$str() {
        return failureDispatchingAsyncEvent;
    }
}
