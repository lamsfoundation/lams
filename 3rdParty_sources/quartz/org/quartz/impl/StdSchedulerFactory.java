/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package org.quartz.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerListener;
import org.quartz.core.JobRunShellFactory;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.core.SchedulingContext;
import org.quartz.ee.jta.JTAJobRunShellFactory;
import org.quartz.ee.jta.UserTransactionHelper;
import org.quartz.impl.jdbcjobstore.JobStoreSupport;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.InstanceIdGenerator;
import org.quartz.spi.JobFactory;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.ThreadPool;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.quartz.utils.JNDIConnectionProvider;
import org.quartz.utils.PoolingConnectionProvider;
import org.quartz.utils.PropertiesParser;

/**
 * <p>
 * An implementation of <code>{@link org.quartz.SchedulerFactory}</code> that
 * does all of it's work of creating a <code>QuartzScheduler</code> instance
 * based on the contenents of a <code>Properties</code> file.
 * </p>
 * 
 * <p>
 * By default a properties file named "quartz.properties" is loaded from the
 * 'current working directory'. If that fails, then the "quartz.properties"
 * file located (as a resource) in the org/quartz package is loaded. If you
 * wish to use a file other than these defaults, you must define the system
 * property 'org.quartz.properties' to* point to the file you want.
 * </p>
 * 
 * <p>
 * See the sample properties files that are distributed with Quartz for
 * information about the various settings available within the file.
 * </p>
 * 
 * <p>
 * Alternativly, you can explicitly initialize the factory by calling one of
 * the <code>initialize(xx)</code> methods before calling <code>getScheduler()</code>.
 * </p>
 * 
 * <p>
 * Instances of the specified <code>{@link org.quartz.spi.JobStore}</code>,
 * <code>{@link org.quartz.spi.ThreadPool}</code>, classes will be created
 * by name, and then any additional properties specified for them in the config
 * file will be set on the instance by calling an equivalent 'set' method. For
 * example if the properties file contains the property 'org.quartz.jobStore.
 * myProp = 10' then after the JobStore class has been instantiated, the method
 * 'setMyProp()' will be called on it. Type conversion to primitive Java types
 * (int, long, float, double, boolean, and String) are performed before calling
 * the propertie's setter method.
 * </p>
 * 
 * @author James House
 * @author Anthony Eden
 * @author Mohammad Rezaei
 */
public class StdSchedulerFactory implements SchedulerFactory {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String PROPERTIES_FILE = "org.quartz.properties";

    public static final String PROP_SCHED_INSTANCE_NAME = "org.quartz.scheduler.instanceName";

    public static final String PROP_SCHED_INSTANCE_ID = "org.quartz.scheduler.instanceId";

    public static final String PROP_SCHED_INSTANCE_ID_GENERATOR_CLASS = "org.quartz.scheduler.instanceIdGenerator.class";
    
    public static final String PROP_SCHED_THREAD_NAME = "org.quartz.scheduler.threadName";
    
    public static final String PROP_SCHED_RMI_EXPORT = "org.quartz.scheduler.rmi.export";

    public static final String PROP_SCHED_RMI_PROXY = "org.quartz.scheduler.rmi.proxy";

    public static final String PROP_SCHED_RMI_HOST = "org.quartz.scheduler.rmi.registryHost";

    public static final String PROP_SCHED_RMI_PORT = "org.quartz.scheduler.rmi.registryPort";

    public static final String PROP_SCHED_RMI_SERVER_PORT = "org.quartz.scheduler.rmi.serverPort";

    public static final String PROP_SCHED_RMI_CREATE_REGISTRY = "org.quartz.scheduler.rmi.createRegistry";

    public static final String PROP_SCHED_WRAP_JOB_IN_USER_TX = "org.quartz.scheduler.wrapJobExecutionInUserTransaction";

    public static final String PROP_SCHED_USER_TX_URL = "org.quartz.scheduler.userTransactionURL";

    public static final String PROP_SCHED_IDLE_WAIT_TIME = "org.quartz.scheduler.idleWaitTime";

    public static final String PROP_SCHED_DB_FAILURE_RETRY_INTERVAL = "org.quartz.scheduler.dbFailureRetryInterval";

    public static final String PROP_SCHED_CLASS_LOAD_HELPER_CLASS = "org.quartz.scheduler.classLoadHelper.class";

    public static final String PROP_SCHED_JOB_FACTORY_CLASS = "org.quartz.scheduler.jobFactory.class";

    public static final String PROP_SCHED_JOB_FACTORY_PREFIX = "org.quartz.scheduler.jobFactory";

    public static final String PROP_SCHED_CONTEXT_PREFIX = "org.quartz.context.key";

    public static final String PROP_THREAD_POOL_PREFIX = "org.quartz.threadPool";

    public static final String PROP_THREAD_POOL_CLASS = "org.quartz.threadPool.class";

    public static final String PROP_JOB_STORE_PREFIX = "org.quartz.jobStore";

    public static final String PROP_JOB_STORE_CLASS = "org.quartz.jobStore.class";

    public static final String PROP_JOB_STORE_USE_PROP = "org.quartz.jobStore.useProperties";

    public static final String PROP_DATASOURCE_PREFIX = "org.quartz.dataSource";

    public static final String PROP_CONNECTION_PROVIDER_CLASS = "connectionProvider.class";

    public static final String PROP_DATASOURCE_DRIVER = "driver";

    public static final String PROP_DATASOURCE_URL = "URL";

    public static final String PROP_DATASOURCE_USER = "user";

    public static final String PROP_DATASOURCE_PASSWORD = "password";

    public static final String PROP_DATASOURCE_MAX_CONNECTIONS = "maxConnections";

    public static final String PROP_DATASOURCE_VALIDATION_QUERY = "validationQuery";

    public static final String PROP_DATASOURCE_JNDI_URL = "jndiURL";

    public static final String PROP_DATASOURCE_JNDI_ALWAYS_LOOKUP = "jndiAlwaysLookup";

    public static final String PROP_DATASOURCE_JNDI_INITIAL = "java.naming.factory.initial";

    public static final String PROP_DATASOURCE_JNDI_PROVDER = "java.naming.provider.url";

    public static final String PROP_DATASOURCE_JNDI_PRINCIPAL = "java.naming.security.principal";

    public static final String PROP_DATASOURCE_JNDI_CREDENTIALS = "java.naming.security.credentials";

    public static final String PROP_PLUGIN_PREFIX = "org.quartz.plugin";

    public static final String PROP_PLUGIN_CLASS = "class";

    public static final String PROP_JOB_LISTENER_PREFIX = "org.quartz.jobListener";

    public static final String PROP_TRIGGER_LISTENER_PREFIX = "org.quartz.triggerListener";

    public static final String PROP_LISTENER_CLASS = "class";

    public static final String DEFAULT_INSTANCE_ID = "NON_CLUSTERED";

    public static final String AUTO_GENERATE_INSTANCE_ID = "AUTO";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private SchedulerException initException = null;

    private String propSrc = null;

    private PropertiesParser cfg;

    //  private Scheduler scheduler;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public StdSchedulerFactory() {
    }

    public StdSchedulerFactory(Properties props) throws SchedulerException {
        initialize(props);
    }

    public StdSchedulerFactory(String fileName) throws SchedulerException {
        initialize(fileName);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static Log getLog() {
        return LogFactory.getLog(StdSchedulerFactory.class);
    }

    /**
     * <p>
     * Initialize the <code>{@link org.quartz.SchedulerFactory}</code> with
     * the contenents of a <code>Properties</code> file.
     * </p>
     * 
     * <p>
     * By default a properties file named "quartz.properties" is loaded from
     * the 'current working directory'. If that fails, then the
     * "quartz.properties" file located (as a resource) in the org/quartz
     * package is loaded. If you wish to use a file other than these defaults,
     * you must define the system property 'org.quartz.properties' to point to
     * the file you want.
     * </p>
     * 
     * <p>
     * System properties (envrionment variables, and -D definitions on the
     * command-line when running the JVM) over-ride any properties in the
     * loaded file.
     * </p>
     */
    public void initialize() throws SchedulerException {
        // short-circuit if already initialized
        if (cfg != null) return;
        if (initException != null) throw initException;

        String requestedFile = System.getProperty(PROPERTIES_FILE);
        String propFileName = requestedFile != null ? requestedFile
                : "quartz.properties";
        File propFile = new File(propFileName);

        Properties props = new Properties();

        if (propFile.exists()) {
            try {
                if (requestedFile != null) propSrc = "specified file: '"
                        + requestedFile + "'";
                else
                    propSrc = "default file in current working dir: 'quartz.properties'";

                props.load(new BufferedInputStream(new FileInputStream(
                        propFileName)));

                initialize(overRideWithSysProps(props));
            } catch (IOException ioe) {
                initException = new SchedulerException("Properties file: '"
                        + propFileName + "' could not be read.", ioe);
                throw initException;
            }
        } else if (requestedFile != null) {
            InputStream in = 
                Thread.currentThread().getContextClassLoader().getResourceAsStream(requestedFile);

            if(in == null) {
            initException = new SchedulerException("Properties file: '"
                    + requestedFile + "' could not be found.");
            throw initException;
            }
            
            propSrc = "specified file: '" + requestedFile + "' in the class resource path.";
            
            try {
                props.load(new BufferedInputStream(in));
                initialize(overRideWithSysProps(props));
            } catch (IOException ioe) {
                initException = new SchedulerException("Properties file: '"
                        + requestedFile + "' could not be read.", ioe);
                throw initException;
            }
            
        } else {
            propSrc = "default resource file in Quartz package: 'quartz.properties'";

            InputStream in = getClass().getClassLoader().getResourceAsStream(
                    "quartz.properties");

            if (in == null)
                    in = getClass().getClassLoader().getResourceAsStream(
                            "/quartz.properties");
            if (in == null)
                    in = getClass().getClassLoader().getResourceAsStream(
                            "org/quartz/quartz.properties");
            if (in == null) {
                initException = new SchedulerException(
                        "Default quartz.properties not found in class path");
                throw initException;
            }
            try {
                props.load(in);
            } catch (IOException ioe) {
                initException = new SchedulerException(
                        "Resource properties file: 'org/quartz/quartz.properties' "
                                + "could not be read from the classpath.", ioe);
                throw initException;
            }

            initialize(overRideWithSysProps(props));
        }
    }

    private Properties overRideWithSysProps(Properties props) {

        Properties sysProps = System.getProperties();
        props.putAll(sysProps);

        return props;
    }

    /**
     * <p>
     * Initialize the <code>{@link org.quartz.SchedulerFactory}</code> with
     * the contenents of the <code>Properties</code> file with the given
     * name.
     * </p>
     */
    public void initialize(String filename) throws SchedulerException {
        // short-circuit if already initialized
        if (cfg != null) return;
        if (initException != null) throw initException;

        InputStream is = null;
        Properties props = new Properties();

        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
         
        try {
            if(is != null) {
                is = new BufferedInputStream(is);
                propSrc = "the specified file : '" + filename + "' from the class resource path.";
            }
            else {
            is = new BufferedInputStream(new FileInputStream(filename));
            propSrc = "the specified file : '" + filename + "'";
            }
            props.load(is);
        } catch (IOException ioe) {
            initException = new SchedulerException("Properties file: '"
                    + filename + "' could not be read.", ioe);
            throw initException;
        }

        initialize(props);
    }

    /**
     * <p>
     * Initialize the <code>{@link org.quartz.SchedulerFactory}</code> with
     * the contenents of the <code>Properties</code> file opened with the
     * given <code>InputStream</code>.
     * </p>
     */
    public void initialize(InputStream propertiesStream)
            throws SchedulerException {
        // short-circuit if already initialized
        if (cfg != null) return;
        if (initException != null) throw initException;

        Properties props = new Properties();

        if (propertiesStream != null) {
            try {
                props.load(propertiesStream);
                propSrc = "an externally opened InputStream.";
            } catch (IOException e) {
                initException = new SchedulerException(
                        "Error loading property data from InputStream", e);
                throw initException;
            }
        } else {
            initException = new SchedulerException(
                    "Error loading property data from InputStream - InputStream is null.");
            throw initException;
        }

        initialize(props);
    }

    /**
     * <p>
     * Initialize the <code>{@link org.quartz.SchedulerFactory}</code> with
     * the contenents of the given <code>Properties</code> object.
     * </p>
     */
    public void initialize(Properties props) throws SchedulerException {
        if (propSrc == null)
                propSrc = "an externally provided properties instance.";

        this.cfg = new PropertiesParser(props);
    }

    /**
     *  
     */
    private Scheduler instantiate() throws SchedulerException {
        if (cfg == null) initialize();

        if (initException != null) throw initException;

        JobStore js = null;
        ThreadPool tp = null;
        QuartzScheduler qs = null;
        SchedulingContext schedCtxt = null;
        DBConnectionManager dbMgr = null;
        String instanceIdGeneratorClass = null;
        Properties tProps = null;
        String userTXLocation = null;
        boolean wrapJobInTx = false;
        boolean autoId = false;
        long idleWaitTime = -1;
        long dbFailureRetry = -1;
        String classLoadHelperClass;
        String jobFactoryClass;

        SchedulerRepository schedRep = SchedulerRepository.getInstance();

        // Get Scheduler Properties
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        String schedName = cfg.getStringProperty(PROP_SCHED_INSTANCE_NAME,
                "QuartzScheduler");

        String threadName = cfg.getStringProperty(PROP_SCHED_THREAD_NAME,
                schedName + "_QuartzSchedulerThread");
        
        String schedInstId = cfg.getStringProperty(PROP_SCHED_INSTANCE_ID,
                DEFAULT_INSTANCE_ID);

        if (schedInstId.equals(AUTO_GENERATE_INSTANCE_ID)) {
            autoId = true;
            instanceIdGeneratorClass = cfg.getStringProperty(
                    PROP_SCHED_INSTANCE_ID_GENERATOR_CLASS,
                    "org.quartz.simpl.SimpleInstanceIdGenerator");
        }
        
        userTXLocation = cfg.getStringProperty(PROP_SCHED_USER_TX_URL,
                userTXLocation);
        if (userTXLocation != null && userTXLocation.trim().length() == 0)
                userTXLocation = null;

        classLoadHelperClass = cfg.getStringProperty(
                PROP_SCHED_CLASS_LOAD_HELPER_CLASS,
                "org.quartz.simpl.CascadingClassLoadHelper");
        wrapJobInTx = cfg.getBooleanProperty(PROP_SCHED_WRAP_JOB_IN_USER_TX,
                wrapJobInTx);

        jobFactoryClass = cfg.getStringProperty(
                PROP_SCHED_JOB_FACTORY_CLASS, null);

        idleWaitTime = cfg.getLongProperty(PROP_SCHED_IDLE_WAIT_TIME,
                idleWaitTime);
        dbFailureRetry = cfg.getLongProperty(
                PROP_SCHED_DB_FAILURE_RETRY_INTERVAL, dbFailureRetry);

        boolean rmiExport = cfg
                .getBooleanProperty(PROP_SCHED_RMI_EXPORT, false);
        boolean rmiProxy = cfg.getBooleanProperty(PROP_SCHED_RMI_PROXY, false);
        String rmiHost = cfg
                .getStringProperty(PROP_SCHED_RMI_HOST, "localhost");
        int rmiPort = cfg.getIntProperty(PROP_SCHED_RMI_PORT, 1099);
        int rmiServerPort = cfg.getIntProperty(PROP_SCHED_RMI_SERVER_PORT, -1);
        String rmiCreateRegistry = cfg.getStringProperty(
                PROP_SCHED_RMI_CREATE_REGISTRY,
                QuartzSchedulerResources.CREATE_REGISTRY_NEVER);

        Properties schedCtxtProps = cfg.getPropertyGroup(PROP_SCHED_CONTEXT_PREFIX, true);

        // If Proxying to remote scheduler, short-circuit here...
        // ~~~~~~~~~~~~~~~~~~
        if (rmiProxy) {

            if (autoId)  
                schedInstId = DEFAULT_INSTANCE_ID;
                
            schedCtxt = new SchedulingContext();
            schedCtxt.setInstanceId(schedInstId);

            String uid = QuartzSchedulerResources.getUniqueIdentifier(
                    schedName, schedInstId);

            RemoteScheduler remoteScheduler = new RemoteScheduler(schedCtxt,
                    uid, rmiHost, rmiPort);

            schedRep.bind(remoteScheduler);

            return remoteScheduler;
        }

        // Create class load helper
        ClassLoadHelper loadHelper = null;
        try {
            loadHelper = (ClassLoadHelper) loadClass(classLoadHelperClass)
                    .newInstance();
        } catch (Exception e) {
            throw new SchedulerConfigException(
                    "Unable to instantiate class load helper class: "
                            + e.getMessage(), e);
        }
        loadHelper.initialize();
        
        JobFactory jobFactory = null;
        if(jobFactoryClass != null) {
            try {
                jobFactory = (JobFactory) loadHelper.loadClass(jobFactoryClass)
                        .newInstance();
            } catch (Exception e) {
                throw new SchedulerConfigException(
                        "Unable to instantiate JobFactory class: "
                                + e.getMessage(), e);
            }

            tProps = cfg.getPropertyGroup(PROP_SCHED_JOB_FACTORY_PREFIX, true);
            try {
                setBeanProps(jobFactory, tProps);
            } catch (Exception e) {
                initException = new SchedulerException("JobFactory class '"
                        + jobFactoryClass + "' props could not be configured.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
        }        
        
        InstanceIdGenerator instanceIdGenerator = null;
        if(instanceIdGeneratorClass != null) {
            try {
                instanceIdGenerator = (InstanceIdGenerator) loadHelper.loadClass(instanceIdGeneratorClass)
                    .newInstance();
            } catch (Exception e) {
                throw new SchedulerConfigException(
                        "Unable to instantiate InstanceIdGenerator class: "
                        + e.getMessage(), e);
            }
        }               
        
        // Get ThreadPool Properties
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        String tpClass = cfg.getStringProperty(PROP_THREAD_POOL_CLASS, null);

        if (tpClass == null) {
            initException = new SchedulerException(
                    "ThreadPool class not specified. ",
                    SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }

        try {
            tp = (ThreadPool) loadHelper.loadClass(tpClass).newInstance();
        } catch (Exception e) {
            initException = new SchedulerException("ThreadPool class '"
                    + tpClass + "' could not be instantiated.", e);
            initException
                    .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        tProps = cfg.getPropertyGroup(PROP_THREAD_POOL_PREFIX, true);
        try {
            setBeanProps(tp, tProps);
        } catch (Exception e) {
            initException = new SchedulerException("ThreadPool class '"
                    + tpClass + "' props could not be configured.", e);
            initException
                    .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }

        // Get JobStore Properties
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        String jsClass = cfg.getStringProperty(PROP_JOB_STORE_CLASS,
                RAMJobStore.class.getName());

        if (jsClass == null) {
            initException = new SchedulerException(
                    "JobStore class not specified. ",
                    SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }

        try {
            js = (JobStore) loadHelper.loadClass(jsClass).newInstance();
        } catch (Exception e) {
            initException = new SchedulerException("JobStore class '" + jsClass
                    + "' could not be instantiated.", e);
            initException
                    .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }
        tProps = cfg.getPropertyGroup(PROP_JOB_STORE_PREFIX, true);
        try {
            setBeanProps(js, tProps);
        } catch (Exception e) {
            initException = new SchedulerException("JobStore class '" + jsClass
                    + "' props could not be configured.", e);
            initException
                    .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
            throw initException;
        }

        if (js instanceof org.quartz.impl.jdbcjobstore.JobStoreSupport) {
            ((org.quartz.impl.jdbcjobstore.JobStoreSupport) js)
				.setInstanceId(schedInstId);
            ((org.quartz.impl.jdbcjobstore.JobStoreSupport) js)
            	.setInstanceName(schedName);
        }
        
        // Set up any DataSources
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        String[] dsNames = cfg.getPropertyGroups(PROP_DATASOURCE_PREFIX);
        for (int i = 0; i < dsNames.length; i++) {
            PropertiesParser pp = new PropertiesParser(cfg.getPropertyGroup(
                    PROP_DATASOURCE_PREFIX + "." + dsNames[i], true));

            String cpClass = pp.getStringProperty(PROP_CONNECTION_PROVIDER_CLASS, null);

            // custom connectionProvider...
            if(cpClass != null) {
                ConnectionProvider cp = null;
                try {
                    cp = (ConnectionProvider) loadHelper.loadClass(cpClass).newInstance();
                } catch (Exception e) {
                    initException = new SchedulerException("ConnectionProvider class '" + cpClass
                            + "' could not be instantiated.", e);
                    initException
                            .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                    throw initException;
                }

                try {
                    // remove the class name, so it isn't attempted to be set
                    pp.getUnderlyingProperties().remove(
                            PROP_CONNECTION_PROVIDER_CLASS);
                    
                    setBeanProps(cp, pp.getUnderlyingProperties());
                } catch (Exception e) {
                    initException = new SchedulerException("ConnectionProvider class '" + cpClass
                            + "' props could not be configured.", e);
                    initException
                            .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                    throw initException;
                }

                dbMgr = DBConnectionManager.getInstance();
                dbMgr.addConnectionProvider(dsNames[i], cp);
            }
            else {
                String dsDriver = pp
                .getStringProperty(PROP_DATASOURCE_DRIVER, null);
                String dsURL = pp.getStringProperty(PROP_DATASOURCE_URL, null);
                boolean dsAlwaysLookup = pp.getBooleanProperty(
                        PROP_DATASOURCE_JNDI_ALWAYS_LOOKUP, false);
                String dsUser = pp.getStringProperty(PROP_DATASOURCE_USER, "");
                String dsPass = pp.getStringProperty(PROP_DATASOURCE_PASSWORD, "");
                int dsCnt = pp.getIntProperty(PROP_DATASOURCE_MAX_CONNECTIONS, 10);
                String dsJndi = pp
                        .getStringProperty(PROP_DATASOURCE_JNDI_URL, null);
                String dsJndiInitial = pp.getStringProperty(
                        PROP_DATASOURCE_JNDI_INITIAL, null);
                String dsJndiProvider = pp.getStringProperty(
                        PROP_DATASOURCE_JNDI_PROVDER, null);
                String dsJndiPrincipal = pp.getStringProperty(
                        PROP_DATASOURCE_JNDI_PRINCIPAL, null);
                String dsJndiCredentials = pp.getStringProperty(
                        PROP_DATASOURCE_JNDI_CREDENTIALS, null);
                String dsValidation = pp.getStringProperty(
                        PROP_DATASOURCE_VALIDATION_QUERY, null);
        
                if (dsJndi != null) {
                    Properties props = null;
                    if (null != dsJndiInitial || null != dsJndiProvider
                            || null != dsJndiPrincipal || null != dsJndiCredentials) {
                        props = new Properties();
                        if (dsJndiInitial != null)
                                props.put(PROP_DATASOURCE_JNDI_INITIAL,
                                        dsJndiInitial);
                        if (dsJndiProvider != null)
                                props.put(PROP_DATASOURCE_JNDI_PROVDER,
                                        dsJndiProvider);
                        if (dsJndiPrincipal != null)
                                props.put(PROP_DATASOURCE_JNDI_PRINCIPAL,
                                        dsJndiPrincipal);
                        if (dsJndiCredentials != null)
                                props.put(PROP_DATASOURCE_JNDI_CREDENTIALS,
                                        dsJndiCredentials);
                    }
                    JNDIConnectionProvider cp = new JNDIConnectionProvider(dsJndi,
                            props, dsAlwaysLookup);
                    dbMgr = DBConnectionManager.getInstance();
                    dbMgr.addConnectionProvider(dsNames[i], cp);
                } else {
                    if (dsDriver == null) {
                        initException = new SchedulerException(
                                "Driver not specified for DataSource: "
                                        + dsNames[i]);
                        throw initException;
                    }
                    if (dsURL == null) {
                        initException = new SchedulerException(
                                "DB URL not specified for DataSource: "
                                        + dsNames[i]);
                        throw initException;
                    }
                    try {
                        PoolingConnectionProvider cp = new PoolingConnectionProvider(
                                dsDriver, dsURL, dsUser, dsPass, dsCnt,
                                dsValidation);
                        dbMgr = DBConnectionManager.getInstance();
                        dbMgr.addConnectionProvider(dsNames[i], cp);
                    } catch (SQLException sqle) {
                        initException = new SchedulerException(
                                "Could not initialize DataSource: " + dsNames[i],
                                sqle);
                        throw initException;
                    }
                }
                
            }
            
        }

        // Set up any SchedulerPlugins
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        String[] pluginNames = cfg.getPropertyGroups(PROP_PLUGIN_PREFIX);
        SchedulerPlugin[] plugins = new SchedulerPlugin[pluginNames.length];
        for (int i = 0; i < pluginNames.length; i++) {
            Properties pp = cfg.getPropertyGroup(PROP_PLUGIN_PREFIX + "."
                    + pluginNames[i], true);

            String plugInClass = pp.getProperty(PROP_PLUGIN_CLASS, null);

            if (plugInClass == null) {
                initException = new SchedulerException(
                        "SchedulerPlugin class not specified for plugin '"
                                + pluginNames[i] + "'",
                        SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            SchedulerPlugin plugin = null;
            try {
                plugin = (SchedulerPlugin)
                        loadHelper.loadClass(plugInClass).newInstance();
            } catch (Exception e) {
                initException = new SchedulerException(
                        "SchedulerPlugin class '" + plugInClass
                                + "' could not be instantiated.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            try {
                setBeanProps(plugin, pp);
            } catch (Exception e) {
                initException = new SchedulerException(
                        "JobStore SchedulerPlugin '" + plugInClass
                                + "' props could not be configured.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            plugins[i] = plugin;
        }

        // Set up any JobListeners
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        Class[] strArg = new Class[] { String.class };
        String[] jobListenerNames = cfg.getPropertyGroups(PROP_JOB_LISTENER_PREFIX);
        JobListener[] jobListeners = new JobListener[jobListenerNames.length];
        for (int i = 0; i < jobListenerNames.length; i++) {
            Properties lp = cfg.getPropertyGroup(PROP_JOB_LISTENER_PREFIX + "."
                    + jobListenerNames[i], true);
            
            String listenerClass = lp.getProperty(PROP_LISTENER_CLASS, null);

            if (listenerClass == null) {
                initException = new SchedulerException(
                        "JobListener class not specified for listener '"
                                + jobListenerNames[i] + "'",
                        SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            JobListener listener = null;
            try {
               listener = (JobListener)
                       loadHelper.loadClass(listenerClass).newInstance();
            } catch (Exception e) {
                initException = new SchedulerException(
                        "JobListener class '" + listenerClass
                                + "' could not be instantiated.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            try {
                Method nameSetter = listener.getClass().getMethod("setName", strArg);
                if(nameSetter != null)
                    nameSetter.invoke(listener, new Object[] {jobListenerNames[i] } );
                setBeanProps(listener, lp);
            } catch (Exception e) {
                initException = new SchedulerException(
                        "JobListener '" + listenerClass
                                + "' props could not be configured.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            jobListeners[i] = listener;
        }
               
        // Set up any TriggerListeners
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        String[] triggerListenerNames = cfg.getPropertyGroups(PROP_TRIGGER_LISTENER_PREFIX);
        TriggerListener[] triggerListeners = new TriggerListener[triggerListenerNames.length];
        for (int i = 0; i < triggerListenerNames.length; i++) {
            Properties lp = cfg.getPropertyGroup(PROP_TRIGGER_LISTENER_PREFIX + "."
                    + triggerListenerNames[i], true);
            
            String listenerClass = lp.getProperty(PROP_LISTENER_CLASS, null);

            if (listenerClass == null) {
                initException = new SchedulerException(
                        "TriggerListener class not specified for listener '"
                                + triggerListenerNames[i] + "'",
                        SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            TriggerListener listener = null;
            try {
               listener = (TriggerListener)
                       loadHelper.loadClass(listenerClass).newInstance();
            } catch (Exception e) {
                initException = new SchedulerException(
                        "TriggerListener class '" + listenerClass
                                + "' could not be instantiated.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            try {
                Method nameSetter = listener.getClass().getMethod("setName", strArg);
                if(nameSetter != null)
                    nameSetter.invoke(listener, new Object[] {triggerListenerNames[i] } );
                setBeanProps(listener, lp);
            } catch (Exception e) {
                initException = new SchedulerException(
                        "TriggerListener '" + listenerClass
                                + "' props could not be configured.", e);
                initException
                        .setErrorCode(SchedulerException.ERR_BAD_CONFIGURATION);
                throw initException;
            }
            triggerListeners[i] = listener;
        }
                
        
        // Fire everything up
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        JobRunShellFactory jrsf = null; // Create correct run-shell factory...
        UserTransactionHelper userTxHelper = null;

        if (wrapJobInTx)
                userTxHelper = new UserTransactionHelper(userTXLocation);

        if (wrapJobInTx) jrsf = new JTAJobRunShellFactory(userTxHelper);
        else
            jrsf = new StdJobRunShellFactory();

        if (autoId) {
            try {
                schedInstId = DEFAULT_INSTANCE_ID;
                if (js instanceof org.quartz.impl.jdbcjobstore.JobStoreSupport) {
                    if(((org.quartz.impl.jdbcjobstore.JobStoreSupport) js) 
                        .isClustered()) {
                        schedInstId = instanceIdGenerator.generateInstanceId();                    
                    }
                }
            } catch (Exception e) {
                getLog().error("Couldn't generate instance Id!", e);
                throw new IllegalStateException(
                        "Cannot run without an instance id.");
            }
        }

        if (js instanceof JobStoreSupport) {
            JobStoreSupport jjs = (JobStoreSupport) js;
            jjs.setInstanceId(schedInstId);
            jjs.setDbRetryInterval(dbFailureRetry);
        }
        
        QuartzSchedulerResources rsrcs = new QuartzSchedulerResources();
        rsrcs.setName(schedName);
        rsrcs.setThreadName(threadName);        
        rsrcs.setInstanceId(schedInstId);
        rsrcs.setJobRunShellFactory(jrsf);

        if (rmiExport) {
            rsrcs.setRMIRegistryHost(rmiHost);
            rsrcs.setRMIRegistryPort(rmiPort);
            rsrcs.setRMIServerPort(rmiServerPort);
            rsrcs.setRMICreateRegistryStrategy(rmiCreateRegistry);
        }

        rsrcs.setThreadPool(tp);
        if(tp instanceof SimpleThreadPool)
          ((SimpleThreadPool)tp).setThreadNamePrefix(schedName + "_Worker");
        tp.initialize();
        
        rsrcs.setJobStore(js);

        schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(rsrcs.getInstanceId());

        qs = new QuartzScheduler(rsrcs, schedCtxt, idleWaitTime, dbFailureRetry);

        //    if(usingJSCMT)
        //      qs.setSignalOnSchedulingChange(false); // TODO: fixed? (don't need
        // this any more?)

        // Create Scheduler ref...
        Scheduler scheduler = instantiate(rsrcs, qs);
        
        // set job factory if specified
        if(jobFactory != null)
            qs.setJobFactory(jobFactory);

        // add plugins
        for (int i = 0; i < plugins.length; i++) {
            plugins[i].initialize(pluginNames[i], scheduler);
            qs.addSchedulerPlugin(plugins[i]);
        }
        
        // add listeners
        for (int i = 0; i < jobListeners.length; i++) {
            qs.addGlobalJobListener(jobListeners[i]);
        }
        for (int i = 0; i < triggerListeners.length; i++) {
            qs.addGlobalTriggerListener(triggerListeners[i]);
        }
        
        // set scheduler context data...
        Iterator itr = schedCtxtProps.keySet().iterator();
        while(itr.hasNext()) {
            String key = (String) itr.next();
            String val = schedCtxtProps.getProperty(key);
            
            scheduler.getContext().put(key, val);
        }
        
        // fire up job store, and runshell factory

        js.initialize(loadHelper, qs.getSchedulerSignaler());

        jrsf.initialize(scheduler, schedCtxt);

        getLog().info(
                "Quartz scheduler '" + scheduler.getSchedulerName()
                        + "' initialized from " + propSrc);

        getLog().info("Quartz scheduler version: " + qs.getVersion());

        // prevents the repository from being garbage collected
        qs.addNoGCObject(schedRep);
        // prevents the db manager from being garbage collected
        if (dbMgr != null) qs.addNoGCObject(dbMgr);

        schedRep.bind(scheduler);

        return scheduler;
    }

    protected Scheduler instantiate(QuartzSchedulerResources rsrcs, QuartzScheduler qs) {
        SchedulingContext schedCtxt = new SchedulingContext();
        schedCtxt.setInstanceId(rsrcs.getInstanceId());
        
        Scheduler scheduler = new StdScheduler(qs, schedCtxt);
        return scheduler;
    }
    

    private void setBeanProps(Object obj, Properties props)
            throws NoSuchMethodException, IllegalAccessException,
            java.lang.reflect.InvocationTargetException,
            IntrospectionException, SchedulerConfigException {
        props.remove("class");

        BeanInfo bi = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propDescs = bi.getPropertyDescriptors();
        PropertiesParser pp = new PropertiesParser(props);

        java.util.Enumeration keys = props.keys();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            String c = name.substring(0, 1).toUpperCase(Locale.US);
            String methName = "set" + c + name.substring(1);

            java.lang.reflect.Method setMeth = getSetMethod(methName, propDescs);

            try {
                if (setMeth == null)
                        throw new NoSuchMethodException(
                                "No setter for property '" + name + "'");

                Class[] params = setMeth.getParameterTypes();
                if (params.length != 1)
                        throw new NoSuchMethodException(
                                "No 1-argument setter for property '" + name
                                        + "'");

                if (params[0].equals(int.class)) {
                    setMeth.invoke(obj, new Object[]{new Integer(pp
                            .getIntProperty(name))});
                } else if (params[0].equals(long.class)) {
                    setMeth.invoke(obj, new Object[]{new Long(pp
                            .getLongProperty(name))});
                } else if (params[0].equals(float.class)) {
                    setMeth.invoke(obj, new Object[]{new Float(pp
                            .getFloatProperty(name))});
                } else if (params[0].equals(double.class)) {
                    setMeth.invoke(obj, new Object[]{new Double(pp
                            .getDoubleProperty(name))});
                } else if (params[0].equals(boolean.class)) {
                    setMeth.invoke(obj, new Object[]{new Boolean(pp
                            .getBooleanProperty(name))});
                } else if (params[0].equals(String.class)) {
                    setMeth.invoke(obj,
                            new Object[]{pp.getStringProperty(name)});
                } else
                    throw new NoSuchMethodException(
                            "No primitive-type setter for property '" + name
                                    + "'");
            } catch (NumberFormatException nfe) {
                throw new SchedulerConfigException("Could not parse property '"
                        + name + "' into correct data type: " + nfe.toString());
            }
        }
    }

    private java.lang.reflect.Method getSetMethod(String name,
            PropertyDescriptor[] props) {
        for (int i = 0; i < props.length; i++) {
            java.lang.reflect.Method wMeth = props[i].getWriteMethod();

            if (wMeth != null && wMeth.getName().equals(name)) return wMeth;
        }

        return null;
    }

    private Class loadClass(String className) throws ClassNotFoundException {

        try {
            return Thread.currentThread().getContextClassLoader().loadClass(
                    className);
        } catch (ClassNotFoundException e) {
            return getClass().getClassLoader().loadClass(className);
        }
    }
    
    private String getSchedulerName() {
        return cfg.getStringProperty(PROP_SCHED_INSTANCE_NAME,
                "QuartzScheduler");
    }

    private String getSchedulerInstId() {
        return cfg.getStringProperty(PROP_SCHED_INSTANCE_ID,
                DEFAULT_INSTANCE_ID);
    }

    /**
     * <p>
     * Returns a handle to the Scheduler produced by this factory.
     * </p>
     * 
     * <p>
     * If one of the <code>initialize</code> methods has not be previously
     * called, then the default (no-arg) <code>initialize()</code> method
     * will be called by this method.
     * </p>
     */
    public Scheduler getScheduler() throws SchedulerException {
        if (cfg == null) initialize();

        SchedulerRepository schedRep = SchedulerRepository.getInstance();

        Scheduler sched = schedRep.lookup(getSchedulerName());

        if (sched != null) {
            if (sched.isShutdown()) schedRep.remove(getSchedulerName());
            else
                return sched;
        }

        sched = instantiate();

        return sched;
    }

    /**
     * <p>
     * Returns a handle to the default Scheduler, creating it if it does not
     * yet exist.
     * </p>
     * 
     * @see #initialize()
     */
    public static Scheduler getDefaultScheduler() throws SchedulerException {
        StdSchedulerFactory fact = new StdSchedulerFactory();

        return fact.getScheduler();
    }

    /**
     * <p>
     * Returns a handle to the Scheduler with the given name, if it exists (if
     * it has already been instantiated).
     * </p>
     */
    public Scheduler getScheduler(String schedName) throws SchedulerException {
        return SchedulerRepository.getInstance().lookup(schedName);
    }

    /**
     * <p>
     * Returns a handle to all known Schedulers (made by any
     * StdSchedulerFactory instance.).
     * </p>
     */
    public Collection getAllSchedulers() throws SchedulerException {
        return SchedulerRepository.getInstance().lookupAll();
    }

}
