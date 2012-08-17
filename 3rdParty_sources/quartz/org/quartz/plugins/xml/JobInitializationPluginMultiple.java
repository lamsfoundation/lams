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
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.jobs.FileScanJob;
import org.quartz.jobs.FileScanListener;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.xml.JobSchedulingDataProcessor;

/**
* This plugin loads XML files to add jobs and schedule them with triggers
 * as the scheduler is initialized, and can optionally periodically scan the
 * file for changes.
 * 
 * @author Brooke Hedrick
 */
public class JobInitializationPluginMultiple implements SchedulerPlugin, FileScanListener {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private String name;

    private Scheduler scheduler;

    private boolean overWriteExistingJobs = true;

    private boolean failOnFileNotFound = true;
    
    private String fileName = JobSchedulingDataProcessor.QUARTZ_XML_FILE_NAME;

    private Vector files = new Vector();
    
    private boolean useContextClassLoader = true;
    
    private boolean validating = true;
    
    private boolean validatingSchema = true;

    private long scanInterval = 0; 
    
    boolean initializing = true;
    
    boolean started = false;
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public JobInitializationPluginMultiple() {
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

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
     * Whether or not the XML should be validated. Default is <code>true</code>.
     * 
     * @return
     */
    public boolean isValidating() {
        return validating;
    }

    /**
     * Whether or not the XML should be validated. Default is <code>true</code>.
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
        return LogFactory.getLog(JobInitializationPluginMultiple.class);
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
        try {
            this.name = name;
            this.scheduler = scheduler;
    
            getLog().info("Registering Quartz Job Initialization Plug-in.");
            
            updateJobFileList();
        }
        finally {
            initializing = false;
        }
    }
    
    private void updateJobFileList() {
        StringTokenizer stok = new StringTokenizer(fileName, ",");

    	while(stok.hasMoreTokens()) {
    		JobFile jobFile = new JobFile(stok.nextToken());
    		files.add(jobFile);
    	}
    }
    
    public void start() {
    	//TODO:bth 6.3.2005 The way this works, I believe we only need one job scanning for changes per directory

        if(scanInterval > 0) {
            try{
            	Iterator iterator = files.iterator();
            	while (iterator.hasNext()) {
            		JobFile jobFile = (JobFile)iterator.next();
            		
            		SimpleTrigger trig = new SimpleTrigger(
            				"JobInitializationPluginMultiple_"+name, 
            				"JobInitializationPluginMultiple", 
            				new Date(), null, 
            				SimpleTrigger.REPEAT_INDEFINITELY, scanInterval);
            		trig.setVolatility(true);
            		JobDetail job = new JobDetail(
            				"JobInitializationPluginMultiple_"+name, 
            				"JobInitializationPluginMultiple",
            				FileScanJob.class);
            		job.setVolatility(true);
            		job.getJobDataMap().put(FileScanJob.FILE_NAME, jobFile.getFilePath());
            		job.getJobDataMap().put(FileScanJob.FILE_SCAN_LISTENER_NAME, "JobInitializationPluginMultiple_"+name);
            		
            		scheduler.getContext().put("JobInitializationPluginMultiple_"+name, this);
            		scheduler.scheduleJob(job, trig);
            	}
            }
            catch(SchedulerException se) {
                getLog().error("Error starting background-task for watching jobs file.", se);
            }
        }
        
        try {
            processFiles();
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

    
    public void processFiles() {
        JobSchedulingDataProcessor processor = 
            new JobSchedulingDataProcessor(isUseContextClassLoader(), isValidating(), isValidatingSchema());

        Iterator iterator = files.iterator();
        while (iterator.hasNext()) {
        	JobFile jobFile = (JobFile)iterator.next();

        	try {
                if (jobFile.getFileFound()) {
                	processor.processFileAndScheduleJobs(jobFile.getFileName(), scheduler, isOverWriteExistingJobs());
               	}
       		} catch (Exception e) {
       			getLog().error("Error scheduling jobs: " + e.getMessage(), e);
       		}
        }
    }

    /** 
     * @see org.quartz.jobs.FileScanListener#fileUpdated(java.lang.String)
     */
    public void fileUpdated(String fileName) {
        if(started)
            processFiles();
    }
    
    class JobFile {
        private String fileName = null;
        
        private String filePath = null;
        
        private boolean fileFound = false;

        protected JobFile(String fileName) {
        	this.fileName = fileName;
        }
        
        protected String getFileName() {
        	return fileName;
        }
        
        protected boolean getFileFound() throws SchedulerException {
            if(this.filePath == null) {
                findFile();         
            }

            return fileFound;
        }

        protected String getFilePath() throws SchedulerException {
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
            
            File file = new File(fileName); // files in filesystem
            if (file == null || !file.exists()) {
                // files in classpath
                URL url = Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(fileName);
                if(url != null) {
                    file = new File(url.getPath()); 
                }
            }        
            try {              
                f = new java.io.FileInputStream(file);
            }catch (FileNotFoundException e) {
                // ignore
            }
            
            if (f == null && isFailOnFileNotFound()) {
                throw new SchedulerException("File named '" + fileName
                        + "' does not exist.");
            } else if (f == null) {
                getLog().warn("File named '" + fileName + "' does not exist.");
            } else {
                fileFound = true;
                try {
                    this.filePath = file.getPath();
                    f.close();
                } catch (IOException ioe) {
                    getLog()
                            .warn("Error closing file named '" + fileName, ioe);
                }
            }
        }
    }
}

// EOF
