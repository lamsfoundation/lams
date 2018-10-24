package io.undertow.servlet;

import java.util.Locale;
import java.nio.file.Path;
import java.lang.IllegalStateException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.net.MalformedURLException;
import javax.annotation.Generated;
import java.lang.String;
import java.io.IOException;
import java.lang.Exception;
import javax.servlet.ServletException;
import java.lang.RuntimeException;
import io.undertow.servlet.api.DeploymentManager.State;
import java.lang.NullPointerException;
import java.util.Arrays;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

/**
 * Warning this class consists of generated code.
 */
@Generated(value = "org.jboss.logging.processor.generator.model.MessageBundleImplementor", date = "2018-08-15T12:43:14+1000")
public class UndertowServletMessages_$bundle implements UndertowServletMessages, Serializable {
    private static final long serialVersionUID = 1L;
    protected UndertowServletMessages_$bundle() {}
    public static final UndertowServletMessages_$bundle INSTANCE = new UndertowServletMessages_$bundle();
    protected Object readResolve() {
        return INSTANCE;
    }
    private static final Locale LOCALE = Locale.ROOT;
    protected Locale getLoggingLocale() {
        return LOCALE;
    }
    private static final String paramCannotBeNull1 = "UT010000: %s cannot be null";
    protected String paramCannotBeNull1$str() {
        return paramCannotBeNull1;
    }
    @Override
    public final IllegalArgumentException paramCannotBeNull(final String param) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), paramCannotBeNull1$str(), param));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String paramCannotBeNull3 = "UT010001: %s cannot be null for %s named %s";
    protected String paramCannotBeNull3$str() {
        return paramCannotBeNull3;
    }
    @Override
    public final IllegalArgumentException paramCannotBeNull(final String param, final String componentType, final String name) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), paramCannotBeNull3$str(), param, componentType, name));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String canOnlyRemoveDeploymentsWhenUndeployed = "UT010002: Deployments can only be removed when in undeployed state, but state was %s";
    protected String canOnlyRemoveDeploymentsWhenUndeployed$str() {
        return canOnlyRemoveDeploymentsWhenUndeployed;
    }
    @Override
    public final IllegalStateException canOnlyRemoveDeploymentsWhenUndeployed(final State state) {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), canOnlyRemoveDeploymentsWhenUndeployed$str(), state));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String getReaderAlreadyCalled = "UT010003: Cannot call getInputStream(), getReader() already called";
    protected String getReaderAlreadyCalled$str() {
        return getReaderAlreadyCalled;
    }
    @Override
    public final IllegalStateException getReaderAlreadyCalled() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), getReaderAlreadyCalled$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String getInputStreamAlreadyCalled = "UT010004: Cannot call getReader(), getInputStream() already called";
    protected String getInputStreamAlreadyCalled$str() {
        return getInputStreamAlreadyCalled;
    }
    @Override
    public final IllegalStateException getInputStreamAlreadyCalled() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), getInputStreamAlreadyCalled$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String getWriterAlreadyCalled = "UT010005: Cannot call getOutputStream(), getWriter() already called";
    protected String getWriterAlreadyCalled$str() {
        return getWriterAlreadyCalled;
    }
    @Override
    public final IllegalStateException getWriterAlreadyCalled() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), getWriterAlreadyCalled$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String getOutputStreamAlreadyCalled = "UT010006: Cannot call getWriter(), getOutputStream() already called";
    protected String getOutputStreamAlreadyCalled$str() {
        return getOutputStreamAlreadyCalled;
    }
    @Override
    public final IllegalStateException getOutputStreamAlreadyCalled() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), getOutputStreamAlreadyCalled$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String twoServletsWithSameMapping = "UT010007: Two servlets specified with same mapping %s";
    protected String twoServletsWithSameMapping$str() {
        return twoServletsWithSameMapping;
    }
    @Override
    public final IllegalArgumentException twoServletsWithSameMapping(final String path) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), twoServletsWithSameMapping$str(), path));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String headerCannotBeConvertedToDate = "UT010008: Header %s cannot be converted to a date";
    protected String headerCannotBeConvertedToDate$str() {
        return headerCannotBeConvertedToDate;
    }
    @Override
    public final IllegalArgumentException headerCannotBeConvertedToDate(final String header) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), headerCannotBeConvertedToDate$str(), header));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String servletMustImplementServlet = "UT010009: Servlet %s of type %s does not implement javax.servlet.Servlet";
    protected String servletMustImplementServlet$str() {
        return servletMustImplementServlet;
    }
    @Override
    public final IllegalArgumentException servletMustImplementServlet(final String name, final Class<? extends javax.servlet.Servlet> servletClass) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), servletMustImplementServlet$str(), name, servletClass));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String componentMustHaveDefaultConstructor = "UT010010: %s of type %s must have a default constructor";
    protected String componentMustHaveDefaultConstructor$str() {
        return componentMustHaveDefaultConstructor;
    }
    @Override
    public final IllegalArgumentException componentMustHaveDefaultConstructor(final String componentType, final Class<?> componentClass) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), componentMustHaveDefaultConstructor$str(), componentType, componentClass));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String filterMustImplementFilter = "UT010011: Filter %s of type %s does not implement javax.servlet.Filter";
    protected String filterMustImplementFilter$str() {
        return filterMustImplementFilter;
    }
    @Override
    public final IllegalArgumentException filterMustImplementFilter(final String name, final Class<? extends javax.servlet.Filter> filterClass) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), filterMustImplementFilter$str(), name, filterClass));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String listenerMustImplementListenerClass = "UT010012: Listener class %s must implement at least one listener interface";
    protected String listenerMustImplementListenerClass$str() {
        return listenerMustImplementListenerClass;
    }
    @Override
    public final IllegalArgumentException listenerMustImplementListenerClass(final Class<?> listenerClass) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), listenerMustImplementListenerClass$str(), listenerClass));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String couldNotInstantiateComponent = "UT010013: Could not instantiate %s";
    protected String couldNotInstantiateComponent$str() {
        return couldNotInstantiateComponent;
    }
    @Override
    public final ServletException couldNotInstantiateComponent(final String name, final Exception e) {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), couldNotInstantiateComponent$str(), name), e);
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String cannotLoadClass = "UT010014: Could not load class %s";
    protected String cannotLoadClass$str() {
        return cannotLoadClass;
    }
    @Override
    public final RuntimeException cannotLoadClass(final String className, final Exception e) {
        final RuntimeException result = new RuntimeException(String.format(getLoggingLocale(), cannotLoadClass$str(), className), e);
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String deleteFailed = "UT010015: Could not delete file %s";
    protected String deleteFailed$str() {
        return deleteFailed;
    }
    @Override
    public final IOException deleteFailed(final Path file) {
        final IOException result = new IOException(String.format(getLoggingLocale(), deleteFailed$str(), file));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String notAMultiPartRequest = "UT010016: Not a multi part request";
    protected String notAMultiPartRequest$str() {
        return notAMultiPartRequest;
    }
    @Override
    public final ServletException notAMultiPartRequest() {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), notAMultiPartRequest$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String asyncNotStarted = "UT010018: Async not started";
    protected String asyncNotStarted$str() {
        return asyncNotStarted;
    }
    @Override
    public final IllegalStateException asyncNotStarted() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), asyncNotStarted$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String responseAlreadyCommited = "UT010019: Response already commited";
    protected String responseAlreadyCommited$str() {
        return responseAlreadyCommited;
    }
    @Override
    public final IllegalStateException responseAlreadyCommited() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), responseAlreadyCommited$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String contentHasBeenWritten = "UT010020: Content has been written";
    protected String contentHasBeenWritten$str() {
        return contentHasBeenWritten;
    }
    @Override
    public final IllegalStateException contentHasBeenWritten() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), contentHasBeenWritten$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String pathMustStartWithSlash = "UT010021: Path %s must start with a /";
    protected String pathMustStartWithSlash$str() {
        return pathMustStartWithSlash;
    }
    @Override
    public final MalformedURLException pathMustStartWithSlash(final String path) {
        final MalformedURLException result = new MalformedURLException(String.format(getLoggingLocale(), pathMustStartWithSlash$str(), path));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String sessionIsInvalid = "UT010022: Session is invalid";
    protected String sessionIsInvalid$str() {
        return sessionIsInvalid;
    }
    @Override
    public final IllegalStateException sessionIsInvalid() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), sessionIsInvalid$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String requestWasNotOriginalOrWrapper = "UT010023: Request %s was not original or a wrapper";
    protected String requestWasNotOriginalOrWrapper$str() {
        return requestWasNotOriginalOrWrapper;
    }
    @Override
    public final IllegalArgumentException requestWasNotOriginalOrWrapper(final ServletRequest request) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), requestWasNotOriginalOrWrapper$str(), request));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String responseWasNotOriginalOrWrapper = "UT010024: Response %s was not original or a wrapper";
    protected String responseWasNotOriginalOrWrapper$str() {
        return responseWasNotOriginalOrWrapper;
    }
    @Override
    public final IllegalArgumentException responseWasNotOriginalOrWrapper(final ServletResponse response) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), responseWasNotOriginalOrWrapper$str(), response));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String asyncRequestAlreadyDispatched = "UT010025: Async request already dispatched";
    protected String asyncRequestAlreadyDispatched$str() {
        return asyncRequestAlreadyDispatched;
    }
    @Override
    public final IllegalStateException asyncRequestAlreadyDispatched() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), asyncRequestAlreadyDispatched$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String startAsyncNotAllowed = "UT010026: Async is not supported for this request, as not all filters or Servlets were marked as supporting async";
    protected String startAsyncNotAllowed$str() {
        return startAsyncNotAllowed;
    }
    @Override
    public final IllegalStateException startAsyncNotAllowed() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), startAsyncNotAllowed$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String notImplemented = "UT010027: Not implemented";
    protected String notImplemented$str() {
        return notImplemented;
    }
    @Override
    public final IllegalStateException notImplemented() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), notImplemented$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String asyncAlreadyStarted = "UT010028: Async processing already started";
    protected String asyncAlreadyStarted$str() {
        return asyncAlreadyStarted;
    }
    @Override
    public final IllegalStateException asyncAlreadyStarted() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), asyncAlreadyStarted$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String streamIsClosed = "UT010029: Stream is closed";
    protected String streamIsClosed$str() {
        return streamIsClosed;
    }
    @Override
    public final IOException streamIsClosed() {
        final IOException result = new IOException(String.format(getLoggingLocale(), streamIsClosed$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String userAlreadyLoggedIn = "UT010030: User already logged in";
    protected String userAlreadyLoggedIn$str() {
        return userAlreadyLoggedIn;
    }
    @Override
    public final ServletException userAlreadyLoggedIn() {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), userAlreadyLoggedIn$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String loginFailed = "UT010031: Login failed";
    protected String loginFailed$str() {
        return loginFailed;
    }
    @Override
    public final ServletException loginFailed() {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), loginFailed$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String authenticationFailed = "UT010032: Authenticationfailed";
    protected String authenticationFailed$str() {
        return authenticationFailed;
    }
    @Override
    public final ServletException authenticationFailed() {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), authenticationFailed$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String noSession = "UT010033: No session";
    protected String noSession$str() {
        return noSession;
    }
    @Override
    public final IllegalStateException noSession() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), noSession$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String streamNotInAsyncMode = "UT010034: Stream not in async mode";
    protected String streamNotInAsyncMode$str() {
        return streamNotInAsyncMode;
    }
    @Override
    public final IllegalStateException streamNotInAsyncMode() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), streamNotInAsyncMode$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String streamNotReady = "UT010035: Stream in async mode was not ready for IO operation";
    protected String streamNotReady$str() {
        return streamNotReady;
    }
    @Override
    public final IllegalStateException streamNotReady() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), streamNotReady$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String listenerAlreadySet = "UT010036: Listener has already been set";
    protected String listenerAlreadySet$str() {
        return listenerAlreadySet;
    }
    @Override
    public final IllegalStateException listenerAlreadySet() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), listenerAlreadySet$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String noWebSocketHandler = "UT010038: No web socket handler was provided to the web socket servlet";
    protected String noWebSocketHandler$str() {
        return noWebSocketHandler;
    }
    @Override
    public final ServletException noWebSocketHandler() {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), noWebSocketHandler$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String unknownAuthenticationMechanism = "UT010039: Unknown authentication mechanism %s";
    protected String unknownAuthenticationMechanism$str() {
        return unknownAuthenticationMechanism;
    }
    @Override
    public final RuntimeException unknownAuthenticationMechanism(final String mechName) {
        final RuntimeException result = new RuntimeException(String.format(getLoggingLocale(), unknownAuthenticationMechanism$str(), mechName));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String moreThanOneDefaultErrorPage = "UT010040: More than one default error page %s and %s";
    protected String moreThanOneDefaultErrorPage$str() {
        return moreThanOneDefaultErrorPage;
    }
    @Override
    public final IllegalStateException moreThanOneDefaultErrorPage(final String defaultErrorPage, final String location) {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), moreThanOneDefaultErrorPage$str(), defaultErrorPage, location));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String servletContextAlreadyInitialized = "UT010041: The servlet context has already been initialized, you can only call this method from a ServletContainerInitializer or a ServletContextListener";
    protected String servletContextAlreadyInitialized$str() {
        return servletContextAlreadyInitialized;
    }
    @Override
    public final IllegalStateException servletContextAlreadyInitialized() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), servletContextAlreadyInitialized$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String cannotCallFromProgramaticListener = "UT010042: This method cannot be called from a servlet context listener that has been added programatically";
    protected String cannotCallFromProgramaticListener$str() {
        return cannotCallFromProgramaticListener;
    }
    @Override
    public final UnsupportedOperationException cannotCallFromProgramaticListener() {
        final UnsupportedOperationException result = new UnsupportedOperationException(String.format(getLoggingLocale(), cannotCallFromProgramaticListener$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String cannotAddServletContextListener = "UT010043: Cannot add servlet context listener from a programatically added listener";
    protected String cannotAddServletContextListener$str() {
        return cannotAddServletContextListener;
    }
    @Override
    public final IllegalArgumentException cannotAddServletContextListener() {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), cannotAddServletContextListener$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String listenerCannotBeNull = "UT010044: listener cannot be null";
    protected String listenerCannotBeNull$str() {
        return listenerCannotBeNull;
    }
    @Override
    public final NullPointerException listenerCannotBeNull() {
        final NullPointerException result = new NullPointerException(String.format(getLoggingLocale(), listenerCannotBeNull$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String sslCannotBeCombinedWithAnyOtherMethod = "UT010045: SSL cannot be combined with any other method";
    protected String sslCannotBeCombinedWithAnyOtherMethod$str() {
        return sslCannotBeCombinedWithAnyOtherMethod;
    }
    @Override
    public final IllegalArgumentException sslCannotBeCombinedWithAnyOtherMethod() {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), sslCannotBeCombinedWithAnyOtherMethod$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String couldNotFindContextToDispatchTo = "UT010046: No servlet context at %s to dispatch to";
    protected String couldNotFindContextToDispatchTo$str() {
        return couldNotFindContextToDispatchTo;
    }
    @Override
    public final IllegalArgumentException couldNotFindContextToDispatchTo(final String originalContextPath) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), couldNotFindContextToDispatchTo$str(), originalContextPath));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String nullName = "UT010047: Name was null";
    protected String nullName$str() {
        return nullName;
    }
    @Override
    public final NullPointerException nullName() {
        final NullPointerException result = new NullPointerException(String.format(getLoggingLocale(), nullName$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String invalidRequestResponseType = "UT010048: Can only handle HTTP type of request / response: %s / %s";
    protected String invalidRequestResponseType$str() {
        return invalidRequestResponseType;
    }
    @Override
    public final IllegalArgumentException invalidRequestResponseType(final ServletRequest request, final ServletResponse response) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), invalidRequestResponseType$str(), request, response));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String asyncRequestAlreadyReturnedToContainer = "UT010049: Async request already returned to container";
    protected String asyncRequestAlreadyReturnedToContainer$str() {
        return asyncRequestAlreadyReturnedToContainer;
    }
    @Override
    public final IllegalStateException asyncRequestAlreadyReturnedToContainer() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), asyncRequestAlreadyReturnedToContainer$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String filterNotFound = "UT010050: Filter %s used in filter mapping %s not found";
    protected String filterNotFound$str() {
        return filterNotFound;
    }
    @Override
    public final IllegalStateException filterNotFound(final String filterName, final String mapping) {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), filterNotFound$str(), filterName, mapping));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String deploymentStopped = "UT010051: Deployment %s has stopped";
    protected String deploymentStopped$str() {
        return deploymentStopped;
    }
    @Override
    public final ServletException deploymentStopped(final String deployment) {
        final ServletException result = new ServletException(String.format(getLoggingLocale(), deploymentStopped$str(), deployment));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String headerNameWasNull = "UT010052: Header name was null";
    protected String headerNameWasNull$str() {
        return headerNameWasNull;
    }
    @Override
    public final NullPointerException headerNameWasNull() {
        final NullPointerException result = new NullPointerException(String.format(getLoggingLocale(), headerNameWasNull$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String noConfidentialPortAvailable = "UT010053: No confidential port is available to redirect the current request.";
    protected String noConfidentialPortAvailable$str() {
        return noConfidentialPortAvailable;
    }
    @Override
    public final IllegalStateException noConfidentialPortAvailable() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), noConfidentialPortAvailable$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String couldNotCreateFactory = "UT010054: Unable to create an instance factory for %s";
    protected String couldNotCreateFactory$str() {
        return couldNotCreateFactory;
    }
    @Override
    public final RuntimeException couldNotCreateFactory(final String className, final Exception e) {
        final RuntimeException result = new RuntimeException(String.format(getLoggingLocale(), couldNotCreateFactory$str(), className), e);
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String listenerIsNotStarted = "UT010055: Listener is not started";
    protected String listenerIsNotStarted$str() {
        return listenerIsNotStarted;
    }
    @Override
    public final IllegalStateException listenerIsNotStarted() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), listenerIsNotStarted$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String pathWasNotSet = "UT010056: path was not set";
    protected String pathWasNotSet$str() {
        return pathWasNotSet;
    }
    @Override
    public final IllegalStateException pathWasNotSet() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), pathWasNotSet$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String multipartConfigNotPresent = "UT010057: multipart config was not present on Servlet";
    protected String multipartConfigNotPresent$str() {
        return multipartConfigNotPresent;
    }
    @Override
    public final IllegalStateException multipartConfigNotPresent() {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), multipartConfigNotPresent$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String servletNameNull = "UT010058: Servlet name cannot be null";
    protected String servletNameNull$str() {
        return servletNameNull;
    }
    @Override
    public final IllegalArgumentException servletNameNull() {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), servletNameNull$str()));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String paramCannotBeNullNPE = "UT010059: Param %s cannot be null";
    protected String paramCannotBeNullNPE$str() {
        return paramCannotBeNullNPE;
    }
    @Override
    public final NullPointerException paramCannotBeNullNPE(final String name) {
        final NullPointerException result = new NullPointerException(String.format(getLoggingLocale(), paramCannotBeNullNPE$str(), name));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String trailersNotSupported = "UT010060: Trailers not supported for this request due to %s";
    protected String trailersNotSupported$str() {
        return trailersNotSupported;
    }
    @Override
    public final IllegalStateException trailersNotSupported(final String reason) {
        final IllegalStateException result = new IllegalStateException(String.format(getLoggingLocale(), trailersNotSupported$str(), reason));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
    private static final String invalidMethodForPushRequest = "UT010061: Invalid method for push request %s";
    protected String invalidMethodForPushRequest$str() {
        return invalidMethodForPushRequest;
    }
    @Override
    public final IllegalArgumentException invalidMethodForPushRequest(final String method) {
        final IllegalArgumentException result = new IllegalArgumentException(String.format(getLoggingLocale(), invalidMethodForPushRequest$str(), method));
        final StackTraceElement[] st = result.getStackTrace();
        result.setStackTrace(Arrays.copyOfRange(st, 1, st.length));
        return result;
    }
}
