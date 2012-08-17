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
package org.quartz.xml;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.BeanPropertySetterRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Parses an XML file that declares Jobs and their schedules (Triggers).
 * 
 * The xml document must conform to the format defined in
 * "job_scheduling_data_1_2.dtd" or "job_scheduling_data_1_2.xsd"
 * 
 * After creating an instance of this class, you should call one of the <code>processFile()</code>
 * functions, after which you may call the <code>getScheduledJobs()</code>
 * function to get a handle to the defined Jobs and Triggers, which can then be
 * scheduled with the <code>Scheduler</code>. Alternatively, you could call
 * the <code>processFileAndScheduleJobs()</code> function to do all of this
 * in one step.
 * 
 * The same instance can be used again and again, with the list of defined Jobs
 * being cleared each time you call a <code>processFile</code> method,
 * however a single instance is not thread-safe.
 * 
 * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
 * @author James House
 */
public class JobSchedulingDataProcessor extends DefaultHandler {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String QUARTZ_PUBLIC_ID = "-//Quartz Enterprise Job Scheduler//DTD Job Scheduling Data 1.5//EN";

    public static final String QUARTZ_SYSTEM_ID = "http://www.opensymphony.com/quartz/xml/job_scheduling_data_1_5.dtd";
    
    public static final String QUARTZ_DTD = "/org/quartz/xml/job_scheduling_data_1_5.dtd";
    
    public static final String QUARTZ_NS = "http://www.opensymphony.com/quartz/JobSchedulingData";
    
    public static final String QUARTZ_SCHEMA = "http://www.opensymphony.com/quartz/xml/job_scheduling_data_1_5.xsd";
    
    public static final String QUARTZ_XSD = "/org/quartz/xml/job_scheduling_data_1_5.xsd";

    public static final String QUARTZ_SYSTEM_ID_DIR_PROP = "quartz.system.id.dir";

    public static final String QUARTZ_XML_FILE_NAME = "quartz_jobs.xml";

    public static final String QUARTZ_SYSTEM_ID_PREFIX = "jar:";

    protected static final String TAG_QUARTZ = "quartz";
    
    protected static final String TAG_OVERWRITE_EXISTING_JOBS = "overwrite-existing-jobs";

    protected static final String TAG_JOB_LISTENER = "job-listener";
    
    protected static final String TAG_CALENDAR = "calendar";
    
    protected static final String TAG_CLASS_NAME = "class-name";
    
    protected static final String TAG_DESCRIPTION = "description";

    protected static final String TAG_BASE_CALENDAR = "base-calendar";
    
    protected static final String TAG_MISFIRE_INSTRUCTION = "misfire-instruction";
    
    protected static final String TAG_CALENDAR_NAME = "calendar-name";

    protected static final String TAG_JOB = "job";

    protected static final String TAG_JOB_DETAIL = "job-detail";

    protected static final String TAG_NAME = "name";

    protected static final String TAG_GROUP = "group";

    protected static final String TAG_JOB_CLASS = "job-class";

    protected static final String TAG_JOB_LISTENER_REF = "job-listener-ref";
    
    protected static final String TAG_VOLATILITY = "volatility";

    protected static final String TAG_DURABILITY = "durability";

    protected static final String TAG_RECOVER = "recover";
    
    protected static final String TAG_JOB_DATA_MAP = "job-data-map";
    
    protected static final String TAG_ENTRY = "entry";
    
    protected static final String TAG_KEY = "key";
    
    protected static final String TAG_ALLOWS_TRANSIENT_DATA = "allows-transient-data";
    
    protected static final String TAG_VALUE = "value";

    protected static final String TAG_TRIGGER = "trigger";

    protected static final String TAG_SIMPLE = "simple";

    protected static final String TAG_CRON = "cron";

    protected static final String TAG_JOB_NAME = "job-name";

    protected static final String TAG_JOB_GROUP = "job-group";

    protected static final String TAG_START_TIME = "start-time";

    protected static final String TAG_END_TIME = "end-time";

    protected static final String TAG_REPEAT_COUNT = "repeat-count";

    protected static final String TAG_REPEAT_INTERVAL = "repeat-interval";

    protected static final String TAG_CRON_EXPRESSION = "cron-expression";

    protected static final String TAG_TIME_ZONE = "time-zone";

    /**
     * XML Schema dateTime datatype format.
     * <p>
     * See <a href="http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#dateTime">
     * http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/#dateTime</a>
     */
    protected static final String XSD_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";
    
    /**
     * Legacy DTD version 1.0 date format.
     */
    protected static final String DTD_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss a";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected Map scheduledJobs = new HashMap();

    protected List jobsToSchedule = new LinkedList();
    protected List calsToSchedule = new LinkedList();
    protected List listenersToSchedule = new LinkedList();
    
    protected Collection validationExceptions = new ArrayList();
    
    protected Digester digester;
    
    private boolean overWriteExistingJobs = true;
    
    private ThreadLocal schedLocal = new ThreadLocal();
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
     
    /**
     * Constructor for QuartzMetaDataProcessor.
     */
    public JobSchedulingDataProcessor() {
        this(true, true, true);
    }

    /**
     * Constructor for QuartzMetaDataProcessor.
     * 
     * @param useContextClassLoader whether or not to use the context class loader.
     * @param validating        whether or not to validate XML.
     * @param validatingSchema  whether or not to validate XML schema.
     */
    public JobSchedulingDataProcessor(boolean useContextClassLoader, boolean validating, boolean validatingSchema) {
        initDigester(useContextClassLoader, validating, validatingSchema);
    }

    /**
     * Initializes the digester.
     * 
     * @param useContextClassLoader whether or not to use the context class loader.
     * @param validating        whether or not to validate XML.
     * @param validatingSchema  whether or not to validate XML schema.
     */
    protected void initDigester(boolean useContextClassLoader, boolean validating, boolean validatingSchema) {
        digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setUseContextClassLoader(useContextClassLoader);
        digester.setValidating(validating);
        initSchemaValidation(validatingSchema);
        digester.setEntityResolver(this);
        digester.setErrorHandler(this);
        
        if(addCustomDigesterRules(digester))
            addDefaultDigesterRules(digester);
    }

    /**
     * Add the default set of digest rules
     */
    protected void addDefaultDigesterRules(Digester digester) {
        digester.addSetProperties(TAG_QUARTZ, TAG_OVERWRITE_EXISTING_JOBS, "overWriteExistingJobs");
        digester.addObjectCreate(TAG_QUARTZ + "/" + TAG_JOB_LISTENER, "jobListener","class-name");
        digester.addCallMethod(TAG_QUARTZ + "/" + TAG_JOB_LISTENER,"setName",1);
        digester.addCallParam(TAG_QUARTZ + "/" + TAG_JOB_LISTENER,0,"name");
        digester.addSetNext(TAG_QUARTZ + "/" + TAG_JOB_LISTENER,"addListenerToSchedule");        
        digester.addRuleSet(new CalendarRuleSet(TAG_QUARTZ + "/" + TAG_CALENDAR, "addCalendarToSchedule"));
        digester.addRuleSet(new CalendarRuleSet("*/" + TAG_BASE_CALENDAR, "setBaseCalendar"));
        digester.addObjectCreate(TAG_QUARTZ + "/" + TAG_JOB, JobSchedulingBundle.class);
        digester.addObjectCreate(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL, JobDetail.class);
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_NAME, "name");
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_GROUP, "group");
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_DESCRIPTION, "description");
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_CLASS, "jobClass");
        digester.addCallMethod(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_LISTENER_REF,"addJobListener",0 );
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_VOLATILITY, "volatility");
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_DURABILITY, "durability");
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_RECOVER, "requestsRecovery");
        digester.addObjectCreate(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_DATA_MAP, JobDataMap.class);
        digester.addSetProperties(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_DATA_MAP, TAG_ALLOWS_TRANSIENT_DATA, "allowsTransientData");
        digester.addCallMethod(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_DATA_MAP + "/" + TAG_ENTRY, "put", 2, new Class[] { Object.class, Object.class });
        digester.addCallParam(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_DATA_MAP + "/" + TAG_ENTRY + "/" + TAG_KEY, 0);
        digester.addCallParam(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_DATA_MAP + "/" + TAG_ENTRY + "/" + TAG_VALUE, 1);
        digester.addSetNext(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL + "/" + TAG_JOB_DATA_MAP, "setJobDataMap");
        digester.addSetNext(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_JOB_DETAIL, "setJobDetail");
        digester.addRuleSet(new TriggerRuleSet(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_SIMPLE, SimpleTrigger.class));
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_SIMPLE + "/" + TAG_REPEAT_COUNT, "repeatCount");
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_SIMPLE + "/" + TAG_REPEAT_INTERVAL, "repeatInterval");
        digester.addSetNext(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_SIMPLE, "addTrigger");
        digester.addRuleSet(new TriggerRuleSet(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_CRON, CronTrigger.class));
        digester.addBeanPropertySetter(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_CRON + "/" + TAG_CRON_EXPRESSION, "cronExpression");
        digester.addRule(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_CRON + "/" + TAG_TIME_ZONE, new SimpleConverterRule("timeZone", new TimeZoneConverter(), TimeZone.class));
        digester.addSetNext(TAG_QUARTZ + "/" + TAG_JOB + "/" + TAG_TRIGGER + "/" + TAG_CRON, "addTrigger");
        digester.addSetNext(TAG_QUARTZ + "/" + TAG_JOB, "addJobToSchedule");
    }
    
    /**
     * Template method provided as a hook for those who wish to extend this 
     * class and add more functionality.
     * 
     * This method is invoked after the Digester is instantiated, and before
     * the default set of rules are added.
     * 
     * @param digester
     * @return false, if the default rules should NOT be added
     */
    protected boolean addCustomDigesterRules(Digester digester) {
        // do nothing in base impl
        return true;
    }
    
    /**
     * Initializes the digester for XML Schema validation.
     * 
     * @param validating    whether or not to validate XML.
     */
    protected void initSchemaValidation(boolean validatingSchema) {
        if (validatingSchema) {
            String schemaUri = null;
            URL url = getClass().getResource(QUARTZ_XSD);
            if (url != null) {
                schemaUri = url.toExternalForm();
            }
            else {
                schemaUri = QUARTZ_SCHEMA;
            }
            digester.setSchema(schemaUri);
        }
    }

    protected static Log getLog() {
        return LogFactory.getLog(JobSchedulingDataProcessor.class);
    }

    /**
     * Returns whether to use the context class loader.
     * 
     * @return whether to use the context class loader.
     */
    public boolean getUseContextClassLoader() {
        return digester.getUseContextClassLoader();
    }
    
    /**
     * Sets whether to use the context class loader.
     * 
     * @param useContextClassLoader boolean.
     */
    public void setUseContextClassLoader(boolean useContextClassLoader) {
        digester.setUseContextClassLoader(useContextClassLoader);
    }

    /**
     * Returns whether to overwrite existing jobs.
     * 
     * @return whether to overwrite existing jobs.
     */
    public boolean getOverWriteExistingJobs() {
        return overWriteExistingJobs;
    }
    
    /**
     * Sets whether to overwrite existing jobs.
     * 
     * @param overWriteExistingJobs boolean.
     */
    public void setOverWriteExistingJobs(boolean overWriteExistingJobs) {
        this.overWriteExistingJobs = overWriteExistingJobs;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Process the xml file in the default location (a file named
     * "quartz_jobs.xml" in the current working directory).
     *  
     */
    public void processFile() throws Exception {
        processFile(QUARTZ_XML_FILE_NAME);
    }

    /**
     * Process the xml file named <code>fileName</code>.
     * 
     * @param fileName
     *          meta data file name.
     */
    public void processFile(String fileName) throws Exception {
        processFile(fileName, fileName);
    }

    /**
     * Process the xmlfile named <code>fileName</code> with the given system
     * ID.
     * 
     * @param fileName
     *          meta data file name.
     * @param systemId
     *          system ID.
     */
    public void processFile(String fileName, String systemId)
            throws ValidationException, ParserConfigurationException,
            SAXException, IOException, SchedulerException,
            ClassNotFoundException, ParseException {
        clearValidationExceptions();

        scheduledJobs.clear();
        jobsToSchedule.clear();
        calsToSchedule.clear();

        getLog().info("Parsing XML file: " + fileName +
                      " with systemId: " + systemId +
                      " validating: " + digester.getValidating() +
                      " validating schema: " + digester.getSchema());
        InputSource is = new InputSource(getInputStream(fileName));
        is.setSystemId(systemId);
        digester.push(this);
        digester.parse(is);

        maybeThrowValidationException();
    }
    
    /**
     * Process the xmlfile named <code>fileName</code> with the given system
     * ID.
     * 
     * @param stream
     *          an input stream containing the xml content.
     * @param systemId
     *          system ID.
     */
    public void processStream(InputStream stream, String systemId)
            throws ValidationException, ParserConfigurationException,
            SAXException, IOException, SchedulerException,
            ClassNotFoundException, ParseException {
        clearValidationExceptions();

        scheduledJobs.clear();
        jobsToSchedule.clear();
        calsToSchedule.clear();

        getLog().info("Parsing XML from stream with systemId: " + systemId +
                      " validating: " + digester.getValidating() +
                      " validating schema: " + digester.getSchema());
        InputSource is = new InputSource(stream);
        is.setSystemId(systemId);
        digester.push(this);
        digester.parse(is);

        maybeThrowValidationException();
    }
    
    /**
     * Process the xml file in the default location, and schedule all of the
     * jobs defined within it.
     *  
     */
    public void processFileAndScheduleJobs(Scheduler sched,
            boolean overWriteExistingJobs) throws SchedulerException, Exception {
        processFileAndScheduleJobs(QUARTZ_XML_FILE_NAME, sched,
                overWriteExistingJobs);
    }

    /**
     * Process the xml file in the given location, and schedule all of the
     * jobs defined within it.
     * 
     * @param fileName
     *          meta data file name.
     */
    public void processFileAndScheduleJobs(String fileName, Scheduler sched,
            boolean overWriteExistingJobs) throws Exception {
        processFileAndScheduleJobs(fileName, fileName, sched, overWriteExistingJobs);
    }
    
    /**
     * Process the xml file in the given location, and schedule all of the
     * jobs defined within it.
     * 
     * @param fileName
     *          meta data file name.
     */
    public void processFileAndScheduleJobs(String fileName, String systemId,
            Scheduler sched, boolean overWriteExistingJobs) throws Exception {
        schedLocal.set(sched);
        try {
            processFile(fileName, systemId);
        scheduleJobs(getScheduledJobs(), sched, overWriteExistingJobs);
        } finally {
            schedLocal.set(null);
        }
    }

    /**
     * Add the Jobs and Triggers defined in the given map of <code>JobSchedulingBundle</code>
     * s to the given scheduler.
     * 
     * @param jobBundles
     * @param sched
     * @param overWriteExistingJobs
     * @throws Exception
     */
    public void scheduleJobs(Map jobBundles, Scheduler sched,
            boolean overWriteExistingJobs) throws Exception {
        getLog().info("Scheduling " + jobsToSchedule.size() + " parsed jobs.");

        Iterator itr = calsToSchedule.iterator();
        while (itr.hasNext()) {
            CalendarBundle bndle = (CalendarBundle) itr.next();
            addCalendar(sched, bndle);
        }

        itr = jobsToSchedule.iterator();
        while (itr.hasNext()) {
            JobSchedulingBundle bndle = (JobSchedulingBundle) itr.next();
            scheduleJob(bndle, sched, overWriteExistingJobs);
        }
        
        itr = listenersToSchedule.iterator();
        while (itr.hasNext()) {
            JobListener listener = (JobListener) itr.next();
            getLog().info("adding listener "+listener.getName()+" of class "+listener.getClass().getName());
            sched.addJobListener(listener);
        }
        getLog().info(jobBundles.size() + " scheduled jobs.");        
    }

    /**
     * Returns a <code>Map</code> of scheduled jobs.
     * <p/>
     * The key is the job name and the value is a <code>JobSchedulingBundle</code>
     * containing the <code>JobDetail</code> and <code>Trigger</code>.
     * 
     * @return a <code>Map</code> of scheduled jobs.
     */
    public Map getScheduledJobs() {
        return Collections.unmodifiableMap(scheduledJobs);
    }

    /**
     * Returns a <code>JobSchedulingBundle</code> for the job name.
     * 
     * @param name
     *          job name.
     * @return a <code>JobSchedulingBundle</code> for the job name.
     */
    public JobSchedulingBundle getScheduledJob(String name) {
        return (JobSchedulingBundle) getScheduledJobs().get(name);
    }

    /**
     * Returns an <code>InputStream</code> from the fileName as a resource.
     * 
     * @param fileName
     *          file name.
     * @return an <code>InputStream</code> from the fileName as a resource.
     */
    protected InputStream getInputStream(String fileName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        InputStream is = cl.getResourceAsStream(fileName);

        return is;
    }
    
    /**
     * Schedules a given job and trigger (both wrapped by a <code>JobSchedulingBundle</code>).
     * 
     * @param job
     *          job wrapper.
     * @exception SchedulerException
     *              if the Job or Trigger cannot be added to the Scheduler, or
     *              there is an internal Scheduler error.
     */
    public void scheduleJob(JobSchedulingBundle job)
        throws SchedulerException {
        scheduleJob(job, (Scheduler) schedLocal.get(), getOverWriteExistingJobs());
    }

    
    public void addJobToSchedule(JobSchedulingBundle job)
    {
        jobsToSchedule.add(job);
    }

    public void addCalendarToSchedule(CalendarBundle cal)
    {
        calsToSchedule.add(cal);
    }

    public void addListenerToSchedule(JobListener listener)
    {
        listenersToSchedule.add(listener);
    }
    
    /**
     * Schedules a given job and trigger (both wrapped by a <code>JobSchedulingBundle</code>).
     * 
     * @param job
     *          job wrapper.
     * @param sched
     *          job scheduler.
     * @param localOverWriteExistingJobs
     *          locally overwrite existing jobs.
     * @exception SchedulerException
     *              if the Job or Trigger cannot be added to the Scheduler, or
     *              there is an internal Scheduler error.
     */
    public void scheduleJob(JobSchedulingBundle job, Scheduler sched, boolean localOverWriteExistingJobs)
            throws SchedulerException {
        if ((job != null) && job.isValid()) {
            JobDetail detail = job.getJobDetail();
            
            JobDetail dupeJ = sched.getJobDetail(detail.getName(), detail.getGroup());

            if ((dupeJ != null) && !localOverWriteExistingJobs) {
                getLog().info("Not overwriting existing job: " + dupeJ.getFullName());
                return;
            }
            
            if (dupeJ != null) {
                getLog().info("Replacing job: " + detail.getFullName());
            }
            else {
                getLog().info("Adding job: " + detail.getFullName());
            }
            
            if (job.getTriggers().size() == 0 && !job.getJobDetail().isDurable()) {
                throw new SchedulerException("A Job defined without any triggers must be durable");
            }
            sched.addJob(detail, true);
            
            for (Iterator iter = job.getTriggers().iterator(); iter.hasNext(); ) {
                Trigger trigger = (Trigger)iter.next();

                Trigger dupeT = sched.getTrigger(trigger.getName(), trigger.getGroup());
    
                trigger.setJobName(detail.getName());
                trigger.setJobGroup(detail.getGroup());
                
                if(trigger.getStartTime() == null)
                    trigger.setStartTime(new Date());
                
                if (dupeT != null) {
                    getLog().debug(
                        "Rescheduling job: " + detail.getFullName() + " with updated trigger: " + trigger.getFullName());
                    if(!dupeT.getJobGroup().equals(trigger.getJobGroup()) || !dupeT.getJobName().equals(trigger.getJobName()))
                        getLog().warn("Possibly duplicately named triggers in jobs xml file!");
                    sched.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
                }
                else {
                    getLog().debug(
                        "Scheduling job: " + detail.getFullName() + " with trigger: " + trigger.getFullName());
                    sched.scheduleJob(trigger);
                }
            }
            
            addScheduledJob(job);
        }
    }

    /**
     * Adds a scheduled job.
     * 
     * @param job
     *          job wrapper.
     */
    protected void addScheduledJob(JobSchedulingBundle job) {
        scheduledJobs.put(job.getFullName(), job);
    }
    
    /**
     * Adds a calendar.
     * 
     * @param calendarBundle calendar bundle.
     * @throws SchedulerException if the Calendar cannot be added to the Scheduler, or
     *              there is an internal Scheduler error.
     */
    public void addCalendar(Scheduler sched, CalendarBundle calendarBundle) throws SchedulerException {
        sched.addCalendar(
            calendarBundle.getCalendarName(),
            calendarBundle.getCalendar(),
            calendarBundle.getReplace(),
            true);
    }

    /**
     * EntityResolver interface.
     * <p/>
     * Allow the application to resolve external entities.
     * <p/>
     * Until <code>quartz.dtd</code> has a public ID, it must resolved as a
     * system ID. Here's the order of resolution (if one fails, continue to the
     * next).
     * <ol>
     * <li>Tries to resolve the <code>systemId</code> with <code>ClassLoader.getResourceAsStream(String)</code>.
     * </li>
     * <li>If the <code>systemId</code> starts with <code>QUARTZ_SYSTEM_ID_PREFIX</code>,
     * then resolve the part after <code>QUARTZ_SYSTEM_ID_PREFIX</code> with
     * <code>ClassLoader.getResourceAsStream(String)</code>.</li>
     * <li>Else try to resolve <code>systemId</code> as a URL.
     * <li>If <code>systemId</code> has a colon in it, create a new <code>URL</code>
     * </li>
     * <li>Else resolve <code>systemId</code> as a <code>File</code> and
     * then call <code>File.toURL()</code>.</li>
     * </li>
     * </ol>
     * <p/>
     * If the <code>publicId</code> does exist, resolve it as a URL.  If the
     * <code>publicId</code> is the Quartz public ID, then resolve it locally.
     * 
     * @param publicId
     *          The public identifier of the external entity being referenced,
     *          or null if none was supplied.
     * @param systemId
     *          The system identifier of the external entity being referenced.
     * @return An InputSource object describing the new input source, or null
     *         to request that the parser open a regular URI connection to the
     *         system identifier.
     * @exception SAXException
     *              Any SAX exception, possibly wrapping another exception.
     * @exception IOException
     *              A Java-specific IO exception, possibly the result of
     *              creating a new InputStream or Reader for the InputSource.
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        InputSource inputSource = null;

        InputStream is = null;

        URL url = null;

        try {
            if (publicId == null) {
                if (systemId != null) {
                    // resolve Quartz Schema locally
                    if (QUARTZ_SCHEMA.equals(systemId)) {
                        is = getClass().getResourceAsStream(QUARTZ_DTD);
                    }
                    else {
                        is = getInputStream(systemId);
    
                        if (is == null) {
                            int start = systemId.indexOf(QUARTZ_SYSTEM_ID_PREFIX);
    
                            if (start > -1) {
                                String fileName = systemId
                                        .substring(QUARTZ_SYSTEM_ID_PREFIX.length());
                                is = getInputStream(fileName);
                            } else {
                                if (systemId.indexOf(':') == -1) {
                                    File file = new java.io.File(systemId);
                                    url = file.toURL();
                                } else {
                                    url = new URL(systemId);
                                }
    
                                is = url.openStream();
                            }
                        }
                    }
                }
            } else {
                // resolve Quartz DTD locally
                if (QUARTZ_PUBLIC_ID.equals(publicId)) {
                    is = getClass().getResourceAsStream(QUARTZ_DTD);
                }
                else {
                    url = new URL(systemId);
                    is = url.openStream();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                inputSource = new InputSource(is);
                inputSource.setPublicId(publicId);
                inputSource.setSystemId(systemId);
            }

        }

        return inputSource;
    }

    /**
     * ErrorHandler interface.
     * 
     * Receive notification of a warning.
     * 
     * @param exception
     *          The error information encapsulated in a SAX parse exception.
     * @exception SAXException
     *              Any SAX exception, possibly wrapping another exception.
     */
    public void warning(SAXParseException e) throws SAXException {
        addValidationException(e);
    }

    /**
     * ErrorHandler interface.
     * 
     * Receive notification of a recoverable error.
     * 
     * @param exception
     *          The error information encapsulated in a SAX parse exception.
     * @exception SAXException
     *              Any SAX exception, possibly wrapping another exception.
     */
    public void error(SAXParseException e) throws SAXException {
        addValidationException(e);
    }

    /**
     * ErrorHandler interface.
     * 
     * Receive notification of a non-recoverable error.
     * 
     * @param exception
     *          The error information encapsulated in a SAX parse exception.
     * @exception SAXException
     *              Any SAX exception, possibly wrapping another exception.
     */
    public void fatalError(SAXParseException e) throws SAXException {
        addValidationException(e);
    }

    /**
     * Adds a detected validation exception.
     * 
     * @param SAXException
     *          SAX exception.
     */
    protected void addValidationException(SAXException e) {
        validationExceptions.add(e);
    }

    /**
     * Resets the the number of detected validation exceptions.
     */
    protected void clearValidationExceptions() {
        validationExceptions.clear();
    }

    /**
     * Throws a ValidationException if the number of validationExceptions
     * detected is greater than zero.
     * 
     * @exception ValidationException
     *              DTD validation exception.
     */
    protected void maybeThrowValidationException() throws ValidationException {
        if (validationExceptions.size() > 0) {
            throw new ValidationException(validationExceptions);
        }
    }
    
    /**
     * RuleSet for common Calendar tags. 
     * 
     * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
     */
    public class CalendarRuleSet extends RuleSetBase {
        protected String prefix;
        protected String setNextMethodName;
        
        public CalendarRuleSet(String prefix, String setNextMethodName) {
            super();
            this.prefix = prefix;
            this.setNextMethodName = setNextMethodName;
        }

        public void addRuleInstances(Digester digester) {
            digester.addObjectCreate(prefix, CalendarBundle.class);
            digester.addSetProperties(prefix, TAG_CLASS_NAME, "className");
            digester.addBeanPropertySetter(prefix + "/" + TAG_NAME, "calendarName");
            digester.addBeanPropertySetter(prefix + "/" + TAG_DESCRIPTION, "description");
            digester.addSetNext(prefix, setNextMethodName);
        }
    }

    /**
     * RuleSet for common Trigger tags. 
     * 
     * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
     */
    public class TriggerRuleSet extends RuleSetBase {
        protected String prefix;
        protected Class clazz;

        public TriggerRuleSet(String prefix, Class clazz) {
            super();
            this.prefix = prefix;
            if (!Trigger.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Class must be an instance of Trigger");
            }
            this.clazz = clazz;
        }

        public void addRuleInstances(Digester digester) {
            digester.addObjectCreate(prefix, clazz);
            digester.addBeanPropertySetter(prefix + "/" + TAG_NAME, "name");
            digester.addBeanPropertySetter(prefix + "/" + TAG_GROUP, "group");
            digester.addBeanPropertySetter(prefix + "/" + TAG_DESCRIPTION, "description");
            digester.addBeanPropertySetter(prefix + "/" + TAG_VOLATILITY, "volatility");
            digester.addRule(prefix + "/" + TAG_MISFIRE_INSTRUCTION, new MisfireInstructionRule("misfireInstruction"));
            digester.addBeanPropertySetter(prefix + "/" + TAG_CALENDAR_NAME, "calendarName");
            digester.addBeanPropertySetter(prefix + "/" + TAG_JOB_NAME, "jobName");
            digester.addBeanPropertySetter(prefix + "/" + TAG_JOB_GROUP, "jobGroup");
            Converter converter = new DateConverter(new String[] { XSD_DATE_FORMAT, DTD_DATE_FORMAT });
            digester.addRule(prefix + "/" + TAG_START_TIME, new SimpleConverterRule("startTime", converter, Date.class));
            digester.addRule(prefix + "/" + TAG_END_TIME, new SimpleConverterRule("endTime", converter, Date.class));
        }
    }
    
    /**
     * This rule is needed to fix <a href="http://jira.opensymphony.com/browse/QUARTZ-153">QUARTZ-153</a>.
     * <p>
     * Since the Jakarta Commons BeanUtils 1.6.x <code>ConvertUtils</code> class uses static utility 
     * methods, the <code>DateConverter</code> and <code>TimeZoneConverter</code> were
     * overriding any previously registered converters for <code>java.util.Date</code> and
     * <code>java.util.TimeZone</code>.
     * <p>
     * Jakarta Commons BeanUtils 1.7.x fixes this issue by internally using per-context-classloader
     * pseudo-singletons (see <a href="http://jakarta.apache.org/commons/beanutils/commons-beanutils-1.7.0/RELEASE-NOTES.txt">
     * http://jakarta.apache.org/commons/beanutils/commons-beanutils-1.7.0/RELEASE-NOTES.txt</a>).
     * This ensures web applications in the same JVM are using independent converters
     * based on their classloaders.  However, the environment for QUARTZ-153 started Quartz
     * using the <code>QuartzInitializationServlet</code> which started <code>JobInitializationPlugin</code>.  
     * In this case, the web classloader instances would be the same.
     * <p>
     * To make sure the converters aren't overridden by the <code>JobSchedulingDataProcessor</code>,
     * it's easier to just override <code>BeanPropertySetterRule.end()</code> to convert the
     * body text to the specified class using the specified converter.
     * 
     * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
     */
    public class SimpleConverterRule extends BeanPropertySetterRule {
        private Converter converter;
        private Class clazz;
        
        /**
         * <p>Construct rule that sets the given property from the body text.</p>
         *
         * @param propertyName name of property to set
         * @param converter    converter to use
         * @param clazz        class to convert to
         */
        public SimpleConverterRule(String propertyName, Converter converter, Class clazz) {
            this.propertyName = propertyName;
            if (converter == null) {
                throw new IllegalArgumentException("Converter must not be null");
            }
            this.converter = converter;
            if (clazz == null) {
                throw new IllegalArgumentException("Class must not be null");
            }
            this.clazz = clazz;
        }

        /**
         * Process the end of this element.
         *
         * @param namespace the namespace URI of the matching element, or an 
         *   empty string if the parser is not namespace aware or the element has
         *   no namespace
         * @param name the local name if the parser is namespace aware, or just 
         *   the element name otherwise
         *
         * @exception NoSuchMethodException if the bean does not
         *  have a writeable property of the specified name
         */
        public void end(String namespace, String name) throws Exception {

            String property = propertyName;

            if (property == null) {
                // If we don't have a specific property name,
                // use the element name.
                property = name;
            }

            // Get a reference to the top object
            Object top = this.digester.peek();

            // log some debugging information
            if (getDigester().getLogger().isDebugEnabled()) {
                getDigester().getLogger().debug("[BeanPropertySetterRule]{" + getDigester().getMatch() +
                        "} Set " + top.getClass().getName() + " property " +
                                   property + " with text " + bodyText);
            }

            // Force an exception if the property does not exist
            // (BeanUtils.setProperty() silently returns in this case)
            if (top instanceof DynaBean) {
                DynaProperty desc =
                    ((DynaBean) top).getDynaClass().getDynaProperty(property);
                if (desc == null) {
                    throw new NoSuchMethodException
                        ("Bean has no property named " + property);
                }
            } else /* this is a standard JavaBean */ {
                PropertyDescriptor desc =
                    PropertyUtils.getPropertyDescriptor(top, property);
                if (desc == null) {
                    throw new NoSuchMethodException
                        ("Bean has no property named " + property);
                }
            }

            // Set the property only using this converter
            Object value = converter.convert(clazz, bodyText);
            PropertyUtils.setProperty(top, property, value);
        }
    }
    
    /**
     * This rule translates the trigger misfire instruction constant name into its
     * corresponding value.
     * 
     * @TODO Consider removing this class and using a
     * <code>org.apache.commons.digester.Substitutor</code> strategy once
     * Jakarta Commons Digester 1.6 is final.  
     * 
     * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
     */
    public class MisfireInstructionRule extends BeanPropertySetterRule {
        /**
         * <p>Construct rule that sets the given property from the body text.</p>
         *
         * @param propertyName name of property to set
         */
        public MisfireInstructionRule(String propertyName) {
            this.propertyName = propertyName;
        }

        /**
         * Process the body text of this element.
         *
         * @param namespace the namespace URI of the matching element, or an 
         *   empty string if the parser is not namespace aware or the element has
         *   no namespace
         * @param name the local name if the parser is namespace aware, or just 
         *   the element name otherwise
         * @param text The text of the body of this element
         */
        public void body(String namespace, String name, String text)
            throws Exception {
            super.body(namespace, name, text);
            this.bodyText = getConstantValue(bodyText);
        }

        /**
         * Returns the value for the constant name.
         * If the constant can't be found or any exceptions occur,
         * return 0.
         * 
         * @param constantName  constant name.
         * @return the value for the constant name.
         */
        private String getConstantValue(String constantName) {
            String value = "0";

            Object top = this.digester.peek();
            if (top != null) {
                Class clazz = top.getClass();
                try {
                    java.lang.reflect.Field field = clazz.getField(constantName);
                    Object fieldValue = field.get(top);
                    if (fieldValue != null) {
                        value = fieldValue.toString();
                    }
                }
                catch (Exception e) {
                    // ignore
                }
            }

            return value;
        }
    }

    /**
     * <p>Standard {@link Converter} implementation that converts an incoming
     * String into a <code>java.util.Date</code> object, optionally using a
     * default value or throwing a {@link ConversionException} if a conversion
     * error occurs.</p>
     */
    public final class DateConverter implements Converter {

        // ----------------------------------------------------------- Constructors

        /**
         * Create a {@link Converter} that will throw a {@link ConversionException}
         * if a conversion error occurs.
         */
        public DateConverter() {
            this.defaultValue = null;
            this.useDefault = false;
        }

        /**
         * Create a {@link Converter} that will return the specified default value
         * if a conversion error occurs.
         *
         * @param defaultValue The default value to be returned
         */
        public DateConverter(Object defaultValue) {
            this.defaultValue = defaultValue;
            this.useDefault = true;
        }

        public DateConverter(String[] formats) {
            this();
            
            int len = formats.length;
            dateFormats = new DateFormat[len];
            for (int i = 0; i < len; i++) {
                dateFormats[i] = new SimpleDateFormat(formats[i]);
            }
        }

        // ----------------------------------------------------- Instance Variables

        /**
         * The default value specified to our Constructor, if any.
         */
        private Object defaultValue = null;

        /**
         * Should we return the default value on conversion errors?
         */
        private boolean useDefault = true;

        private DateFormat[] dateFormats;

        // --------------------------------------------------------- Public Methods

        /**
         * Convert the specified input object into an output object of the
         * specified type.
         *
         * @param type Data type to which this value should be converted
         * @param value The input value to be converted
         *
         * @exception ConversionException if conversion cannot be performed
         *  successfully
         */
        public Object convert(Class type, Object value) {

            if (value == null) {
                if (useDefault) {
                    return (defaultValue);
                }
                else {
                    return (null);
                }
            }

            if (String.class.equals(type)) {
                if ((value instanceof Date) && (dateFormats != null)) {
                    return (dateFormats[0].format((Date) value));
                }
                else {
                    return (value.toString());
                }
            }

            if (value instanceof Date) {
                return (value);
            }

            try {
                if (Date.class.isAssignableFrom(type) && dateFormats != null) {
                    return parseDate(value);
                }
                else {
                    return (value.toString());
                }
            }
            catch (Exception e) {
                if (useDefault) {
                    return (defaultValue);
                }
                else {
                    throw new ConversionException(e);
                }
            }
        }
        
        protected Date parseDate(Object value) throws ParseException {
            Date date = null;

            int len = dateFormats.length;
            for (int i = 0; i < len; i++) {

                try {
                    date = (dateFormats[i].parse(value.toString()));
                    break;
                }
                catch (ParseException e) {
                    // if this is the last format, throw the exception
                    if (i == (len - 1)) {
                        throw e;
                    }
                }
            }

            return date;
        }
    }

    /**
     * <p>Standard {@link Converter} implementation that converts an incoming
     * String into a <code>java.util.TimeZone</code> object throwing a
     * {@link ConversionException} if a conversion error occurs.</p>
     */
    public final class TimeZoneConverter implements Converter {
        //      ----------------------------------------------------------- Constructors

        /**
         * Create a {@link Converter} that will throw a {@link ConversionException}
         * if a conversion error occurs.
         */
        public TimeZoneConverter() {
        }

        //      --------------------------------------------------------- Public Methods

        /**
         * Convert the specified input object into an output object of the
         * specified type.
         *
         * @param type Data type to which this value should be converted
         * @param value The input value to be converted
         *
         * @exception ConversionException if conversion cannot be performed
         *  successfully
         */
        public Object convert(Class type, Object value) {

            if (value == null) {
                return (null);
            }

            if (value instanceof TimeZone) {
                return (value);
            }

            try {
                if (String.class.equals(value.getClass())) {
                    return (TimeZone.getTimeZone((String) value));
                }
                else {
                    return (value.toString());
                }
            }
            catch (Exception e) {
                throw new ConversionException(e);
            }
        }
    }
}
