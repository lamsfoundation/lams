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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
 * <p> Built in job for executing native executables in a separate process.</p> 
 * 
 * 
 * @see #PROP_COMMAND
 * @see #PROP_PARAMETERS
 * @see #PROP_WAIT_FOR_PROCESS
 * @see #PROP_CONSUME_STREAMS
 * 
 * @author Matthew Payne
 * @author James House
 * @author Steinar Overbeck Cook
 * @date Sep 17, 2003 @Time: 11:27:13 AM
 */
public class NativeJob implements Job {

    /*
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants.
     *  
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
        
    /**
     * Required parameter that specifies the name of the command (executable) 
     * to be ran.
     */
    public static final String PROP_COMMAND = "command";
    
    /**
     * Optional parameter that specifies the parameters to be passed to the
     * executed command.
     */
    public static final String PROP_PARAMETERS = "parameters";
    
    
    /**
     * Optional parameter (value should be 'true' or 'false') that specifies 
     * whether the job should wait for the execution of the native process to 
     * complete before it completes.
     * 
     * <p>Defaults to <code>true</code>.</p>  
     */
    public static final String PROP_WAIT_FOR_PROCESS = "waitForProcess";
    
    /**
     * Optional parameter (value should be 'true' or 'false') that specifies 
     * whether the spawned process's stdout and stderr streams should be 
     * consumed.  If the process creates output, it is possible that it might
     * 'hang' if the streams are not consumed.
     * 
     * <p>Defaults to <code>false</code>.</p>  
     */
    public static final String PROP_CONSUME_STREAMS = "consumeStreams";
    
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public void execute(JobExecutionContext context)
    throws JobExecutionException {

        JobDataMap data = context.getJobDetail().getJobDataMap();
        
        String command = data.getString(PROP_COMMAND);

        String parameters = data.getString(PROP_PARAMETERS);

        if (parameters == null) {
            parameters = "";
        }

        boolean wait = true;
        if(data.containsKey(PROP_WAIT_FOR_PROCESS)) {
            wait = data.getBooleanValue(PROP_WAIT_FOR_PROCESS);
        }
        boolean consumeStreams = false;
        if(data.containsKey(PROP_CONSUME_STREAMS)) {
            consumeStreams = data.getBooleanValue(PROP_CONSUME_STREAMS);
        }
            
        this.runNativeCommand(command, parameters, wait, consumeStreams);
    }

    private static Log getLog()
    {
        return LogFactory.getLog(NativeJob.class);
    }
    
    private void runNativeCommand(String command, String parameters, boolean wait, boolean consumeStreams) throws JobExecutionException {

        String[] cmd = null;
        String[] args = new String[2];
        args[0] = command;
        args[1] = parameters;

        try {
            //with this variable will be done the swithcing
            String osName = System.getProperty("os.name");

            //only will work with Windows NT
            if (osName.equals("Windows NT")) {
                if (cmd == null) cmd = new String[args.length + 2];
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";
                for (int i = 0; i < args.length; i++)
                    cmd[i + 2] = args[i];
            }
            //only will work with Windows 95
            else if (osName.equals("Windows 95")) {
                if (cmd == null) cmd = new String[args.length + 2];
                cmd[0] = "command.com";
                cmd[1] = "/C";
                for (int i = 0; i < args.length; i++)
                    cmd[i + 2] = args[i];
            }
            //only will work with Windows 2003
            else if (osName.equals("Windows 2003")) {
                if (cmd == null) cmd = new String[args.length + 2];
                    cmd[0] = "cmd.exe";
                    cmd[1] = "/C";

                for (int i = 0; i < args.length; i++)
                    cmd[i + 2] = args[i];
            }
            //only will work with Windows 2000
            else if (osName.equals("Windows 2000")) {
                if (cmd == null) cmd = new String[args.length + 2];
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";

                for (int i = 0; i < args.length; i++)
                    cmd[i + 2] = args[i];
            }
            //only will work with Windows XP
            else if (osName.equals("Windows XP")) {
                if (cmd == null) cmd = new String[args.length + 2];
                cmd[0] = "cmd.exe";
                cmd[1] = "/C";

                for (int i = 0; i < args.length; i++)
                    cmd[i + 2] = args[i];
            }
            //only will work with Linux
            else if (osName.equals("Linux")) {
                if (cmd == null) cmd = new String[args.length];
                cmd = args;
            }
            //will work with the rest
            else {
                if (cmd == null) cmd = new String[args.length];
                cmd = args;
            }

            Runtime rt = Runtime.getRuntime();
            // Executes the command
            getLog().info("About to run" + cmd[0] + cmd[1]);
            Process proc = rt.exec(cmd);
            // Consumes the stdout from the process
            StreamConsumer stdoutConsumer = new StreamConsumer(proc.getInputStream(), "stdout");

            // Consumes the stderr from the process
            if(consumeStreams) {
                StreamConsumer stderrConsumer = new StreamConsumer(proc.getErrorStream(), "stderr");
                stdoutConsumer.start();
                stderrConsumer.start();
            }
            
            if(wait)
                proc.waitFor(); 
            // any error message?
            
        } catch (Exception x) {
            throw new JobExecutionException("Error launching native command: ", x, false);
        }
    }

    /**
     * Consumes data from the given input stream until EOF and prints the data to stdout
     *
     * @author cooste
     * @author jhouse
     */
    class StreamConsumer extends Thread {
        InputStream is;
        String type;

        /**
         *
         */
        public StreamConsumer(InputStream inputStream, String type) {
            this.is = inputStream;
            this.type = type;
        }

        /**
         * Runs this object as a separate thread, printing the contents of the InputStream
         * supplied during instantiation, to either stdout or stderr
         */
        public void run() {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(is));
                String line = null;

                while ((line = br.readLine()) != null) {
                    if(type.equalsIgnoreCase("stderr"))
                        getLog().warn(type + ">" + line);
                    else
                        getLog().info(type + ">" + line);
                }
            } catch (IOException ioe) {
                getLog().error("Error consuming " + type + " stream of spawned process.", ioe);
            }
            finally {
                if(br != null)
                    try { br.close(); } catch(Exception ignore) {}
            }
        }
    }
    
}
