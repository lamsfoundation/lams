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
package org.quartz.jobs;

import java.io.File;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Inspects a file and compares whether it's "last modified date" has changed
 * since the last time it was inspected.  If the file has been updated, the
 * job invokes a "call-back" method on an identified 
 * <code>FileScanListener</code> that can be found in the 
 * <code>SchedulerContext</code>.
 * 
 * @author jhouse
 * @see org.quartz.jobs.FileScanListener
 */
public class FileScanJob implements StatefulJob {

    public static String FILE_NAME = "FILE_NAME";
    public static String FILE_SCAN_LISTENER_NAME = "FILE_SCAN_LISTENER_NAME";
    private static String LAST_MODIFIED_TIME = "LAST_MODIFIED_TIME";
    
    public FileScanJob() {
    }

    /** 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        Log log = LogFactory.getLog(getClass());
        
        JobDataMap data = context.getJobDetail().getJobDataMap();
        SchedulerContext schedCtxt = null;
        try {
            schedCtxt = context.getScheduler().getContext();
        } catch (SchedulerException e) {
            throw new JobExecutionException("Error obtaining scheduler context.", e, false);
        }
        
        String fileName = data.getString(FILE_NAME);
        String listenerName = data.getString(FILE_SCAN_LISTENER_NAME);
        
        if(fileName == null)
            throw new JobExecutionException("Required parameter '" + 
                    FILE_NAME + "' not found in JobDataMap");
        if(listenerName == null)
            throw new JobExecutionException("Required parameter '" + 
                    FILE_SCAN_LISTENER_NAME + "' not found in JobDataMap");

        FileScanListener listener = (FileScanListener)schedCtxt.get(listenerName);
        
        if(listener == null)
            throw new JobExecutionException("FileScanListener named '" + 
                    listenerName + "' not found in SchedulerContext");
        
        long lastDate = -1;
        if(data.containsKey(LAST_MODIFIED_TIME))
            lastDate = data.getLong(LAST_MODIFIED_TIME);
        
        long newDate = getLastModifiedDate(fileName);

        if(newDate < 0) {
            log.warn("File '"+fileName+"' does not exist.");
            return;
        }
        
        if(lastDate > 0 && (newDate != lastDate)) {
            // notify call back...
            log.info("File '"+fileName+"' updated, notifying listener.");
            listener.fileUpdated(fileName); 
        }
        else
            log.debug("File '"+fileName+"' unchanged.");
        
        data.put(LAST_MODIFIED_TIME, newDate);
    }
    
    protected long getLastModifiedDate(String fileName) {
        
        File file = new File(fileName);
        
        if(!file.exists()) {
            return -1;
        }
        else {
            return file.lastModified();
        }
    }
}
