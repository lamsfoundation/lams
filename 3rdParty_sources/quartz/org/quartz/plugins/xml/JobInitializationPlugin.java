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
package org.quartz.plugins.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.jobs.FileScanJob;
import org.quartz.jobs.FileScanListener;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.xml.JobSchedulingDataProcessor;

/**
* This plugin loads an XML file to add jobs and schedule them with triggers
 * as the scheduler is initialized, and can optionally periodically scan the
 * file for changes.
 * 
 * @author James House
 * @author Pierre Awaragi
 */
public class JobInitializationPlugin implements SchedulerPlugin, FileScanListener {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String name;

    private Scheduler scheduler;

    private boolean overWriteExistingJobs = false;

    private boolean failOnFileNotFound = true;

    private boolean fileFound = false;

    private String fileName = JobSchedulingDataProcessor.QUARTZ_XML_FILE_NAME;
    
    private String filePath = null;
    
    private boolean useContextClassLoader = true;
    
    private boolean validating = false;
    
    private boolean validatingSchema = true;

    private long scanInterval = 0; 
    
    boolean initializing = true;
    
    boolean started = false;
    
    protected ClassLoadHelper classLoadHelper = null;

    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public JobInitializationPlugin() {
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * The file name (and path) to the XML file that should be read.
     * 
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * The file name (and path) to the XML file that should be read.
     * 
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Whether or not jobs defined in the XML file should be overwrite existing
     * jobs with the same name.
     * 
     * @return
     */
    public boolean isOverWriteExistingJobs() {
        return overWriteExistingJobs;
    }

    /**
     * Whether or not jobs defined in the XML file should be overwrite existing
     * jobs with the same name.
     * 
     * @param overWriteExistingJobs
     */
    public void setOverWriteExistingJobs(boolean overWriteExistingJobs) {
        this.overWriteExistingJobs = overWriteExistingJobs;
    }

    /**
     * The interval (in seconds) at which to scan for changes to the file.  
     * If the file has been changed, it is re-loaded and parsed.   The default 
     * value for the interval is 0, which disables scanning.
     * 
     * @return Returns the scanInterval.
     */
    public long getScanInterval() {
        return scanInterval / 1000;
    }

    /**
     * The interval (in seconds) at which to scan for changes to the file.  
     * If the file has been changed, it is re-loaded and parsed.   The default 
     * value for the interval is 0, which disables scanning.
     * 
     * @param scanInterval The scanInterval to set.
     */
    public void setScanInterval(long scanInterval) {
        this.scanInterval = scanInterval * 1000;
    }
    
    /**
     * Whether or not initialization of the plugin should fail (throw an
     * exception) if the file cannot be found. Default is <code>true</code>.
     * 
     * @return
     */
    public boolean isFailOnFileNotFound() {
        return failOnFileNotFound;
    }

    /**
     * Whether or not initialization of the plugin should fail (throw an
     * exception) if the file cannot be found. Default is <code>true</code>.
     * 
     * @param overWriteExistingJobs
     */
    public void setFailOnFileNotFound(boolean failOnFileNotFound) {
        this.failOnFileNotFound = failOnFileNotFound;
    }
    
    /**
     * Whether or not the context class loader should be used. Default is <code>true</code>.
     * 
     * @return
     */
    public boolean isUseContextClassLoader() {
        return useContextClassLoader;
    }

    /**
     * Whether or not context class loader should be used. Default is <code>true</code>.
     * 
     * @param useContextClassLoader
     */
    public void setUseContextClassLoader(boolean useContextClassLoader) {
        this.useContextClassLoader = useContextClassLoader;
    }
    
    /**
     * Whether or not the XML should be validated. Default is <code>false</code>.
     * 
     * @return
     */
    public boolean isValidating() {
        return validating;
    }

    /**
     * Whether or not the XML should be validated. Default is <code>false</code>.
     * 
     * @param validating
     */
    public void setValidating(boolean validating) {
        this.validating = validating;
    }
    
    /**
     * Whether or not the XML schema should be validated. Default is <code>true</code>.
     * 
     * @return
     */
    public boolean isValidatingSchema() {
        return validatingSchema;
    }

    /**
     * Whether or not the XML schema should be validated. Default is <code>true</code>.
     * 
     * @param validatingSchema
     */
    public void setValidatingSchema(boolean validatingSchema) {
        this.validatingSchema = validatingSchema;
    }

    protected static Log getLog() {
        return LogFactory.getLog(JobInitializationPlugin.class);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * SchedulerPlugin Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Called during creation of the <code>Scheduler</code> in order to give
     * the <code>SchedulerPlugin</code> a chance to initialize.
     * </p>
     * 
     * @throws SchedulerConfigException
     *           if there is an error initializing.
     */
    public void initialize(String name, final Scheduler scheduler)
            throws SchedulerException {
        
        initializing = true;
        
        classLoadHelper = new CascadingClassLoadHelper();
        classLoadHelper.initialize();
        
        try {
            this.name = name;
            this.scheduler = scheduler;
    
            getLog().info("Registering Quartz Job Initialization Plug-in.");
            
            findFile();
        }
        finally {
            initializing = false;
        }
    }

    private String getFilePath() throws SchedulerException {
        if(this.filePath == null) {
            findFile();         
        }
        return this.filePath;
    }
    
    /**
     * 
     */
    private void findFile() throws SchedulerException {
        java.io.InputStream f = null;
        String furl = null;
        
        File file = new File(getFileName()); // files in filesystem
        
        if (!file.exists()) {
            URL url = classLoadHelper.getResource(getFileName());
            if(url != null) {
// we need jdk 1.3 compatibility, so we abandon this code...
//                try {
//                    furl = URLDecoder.decode(url.getPath(), "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    furl = url.getPath();
//                }
                furl = URLDecoder.decode(url.getPath()); 
                file = new File(furl); 
                try {
                    f = url.openStream();
                } catch (IOException ignor) {
                    // Swallow the exception
                }
            }        
        }
        else {
            try {              
                f = new java.io.FileInputStream(file);
            }catch (FileNotFoundException e) {
                // ignore
            }
        }
        
        if (f == null && isFailOnFileNotFound()) {
            throw new SchedulerException("File named '" + getFileName()
                    + "' does not exist.");
        } else if (f == null) {
            getLog().warn("File named '" + getFileName() + "' does not exist.");
        } else {
            fileFound = true;
            try {
                if(furl != null)
                    this.filePath = furl;
                else
                    this.filePath = file.getAbsolutePath();
                f.close();
            } catch (IOException ioe) {
                getLog().warn("Error closing jobs file " + getFileName(), ioe);
            }
        }
    }

    public void start() {

        if(scanInterval > 0) {
            try{
                SimpleTrigger trig = new SimpleTrigger(
                        "JobInitializationPlugin_"+name, 
                        "JobInitializationPlugin", 
                        new Date(), null, 
                        SimpleTrigger.REPEAT_INDEFINITELY, scanInterval);
                trig.setVolatility(true);
                JobDetail job = new JobDetail(
                        "JobInitializationPlugin_"+name, 
                        "JobInitializationPlugin",
                        FileScanJob.class);
                job.setVolatility(true);
                job.getJobDataMap().put(FileScanJob.FILE_NAME, getFilePath());
                job.getJobDataMap().put(FileScanJob.FILE_SCAN_LISTENER_NAME, "JobInitializationPlugin_"+name);
                
                scheduler.getContext().put("JobInitializationPlugin_"+name, this);
                scheduler.scheduleJob(job, trig);
            }
            catch(SchedulerException se) {
                getLog().error("Error starting background-task for watching jobs file.", se);
            }
        }
        
        try {
            processFile();
        }
        finally {
            started = true;
        }
    }

    /**
     * <p>
     * Called in order to inform the <code>SchedulerPlugin</code> that it
     * should free up all of it's resources because the scheduler is shutting
     * down.
     * </p>
     */
    public void shutdown() {
        // nothing to do
    }

    
    public void processFile() {
        if (!fileFound) return;

        JobSchedulingDataProcessor processor = 
            new JobSchedulingDataProcessor(isUseContextClassLoader(), isValidating(), isValidatingSchema());

        try {
            processor.processFileAndScheduleJobs(fileName, scheduler, isOverWriteExistingJobs());
        } catch (Exception e) {
            getLog().error("Error scheduling jobs: " + e.getMessage(), e);
        }
    }

    /** 
     * @see org.quartz.jobs.FileScanListener#fileUpdated(java.lang.String)
     */
    public void fileUpdated(String fileName) {
        if(started)
            processFile();
    }
    
}

// EOF
