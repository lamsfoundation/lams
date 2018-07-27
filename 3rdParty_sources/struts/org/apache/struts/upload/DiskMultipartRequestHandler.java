/*
 *
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.struts.upload;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;

/**
 * This is a MultipartRequestHandler that writes file data directly to
 * to temporary files on disk.
 *
 * @deprecated Use the Commons FileUpload based multipart handler instead. This
 *             class will be removed after Struts 1.2.
 */
public class DiskMultipartRequestHandler implements MultipartRequestHandler {

    /**
     * Commons Logging instance.
     */
    protected static Log log =
            LogFactory.getLog(DiskMultipartRequestHandler.class);

    /**
     * The ActionServlet instance used for this class.
     */
    protected ActionServlet servlet;

    /**
     * The ActionMapping instance used for this class.
     */
    protected ActionMapping mapping;

    /**
     * A Hashtable representing the form files uploaded.
     */
    protected Hashtable fileElements;

    /**
     * A Hashtable representing the form text input names and values.
     */
    protected Hashtable textElements;

    /**
     * A Hashtable representing all elemnents.
     */
    protected Hashtable allElements;

    /**
     * The temporary directory.
     */
    protected String tempDir;


    /**
     * This method populates the internal hashtables with multipart request data.
     * If the request argument is an instance of MultipartRequestWrapper,
     * the request wrapper will be populated as well.
     */
    public void handleRequest(HttpServletRequest request) throws ServletException {
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(request);
        this.retrieveTempDir(moduleConfig);
        
        try {
            MultipartIterator iterator =
                new MultipartIterator(
                    request,
                    moduleConfig.getControllerConfig().getBufferSize(),
                    getMaxSize(moduleConfig.getControllerConfig().getMaxFileSize()),
                    tempDir);
                    
            MultipartElement element;

            textElements = new Hashtable();
            fileElements = new Hashtable();
            allElements = new Hashtable();

            while ((element = iterator.getNextElement()) != null) {
                if (!element.isFile()) {
                    createTextElement(request, element);
                } else {
                    createDiskFile(element);
                }
            }
            
            //take care of maximum length being exceeded
            if (iterator.isMaxLengthExceeded()) {
                request.setAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED, Boolean.TRUE);
            }
            
        } catch(IOException ioe) {
            throw new ServletException(ioe);
        }
    }

    protected void createTextElement(HttpServletRequest request, MultipartElement element) {
        if (request instanceof MultipartRequestWrapper) {
            ((MultipartRequestWrapper) request).setParameter(element.getName(), element.getValue());
        }
        String[] textValues = (String[]) textElements.get(element.getName());

        if (textValues != null) {
            String[] textValues2 = new String[textValues.length + 1];
            System.arraycopy(textValues, 0, textValues2, 0, textValues.length);
            textValues2[textValues.length] = element.getValue();
            textValues = textValues2;
        } else {
            textValues = new String[1];
            textValues[0] = element.getValue();
        }
        textElements.put(element.getName(), textValues);
        allElements.put(element.getName(), textValues);
    }

    protected void createDiskFile(MultipartElement element) {
        File tempFile = element.getFile();
        if (tempFile.exists()) {
            DiskFile theFile = new DiskFile(tempFile.getAbsolutePath());
            theFile.setContentType(element.getContentType());
            theFile.setFileName(element.getFileName());
            theFile.setFileSize((int) tempFile.length());
            fileElements.put(element.getName(), theFile);
            allElements.put(element.getName(), theFile);
        }
    }

    public Hashtable getAllElements() {
        return allElements;
    }

    public Hashtable getTextElements() {
        return textElements;
    }

    public Hashtable getFileElements() {
        return fileElements;
    }

    /**
     * Delete all the files uploaded.
     */
    public void rollback() {
        Enumeration names = fileElements.keys();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            DiskFile theFile = (DiskFile) fileElements.get(name);
            theFile.destroy();
        }
    }

    /**
     * Calls on {@link #rollback() rollback()} to delete
     * temporary files.
     */
    public void finish() {
        rollback();
    }

    public void setServlet(ActionServlet servlet) {
        this.servlet = servlet;
    }

    public void setMapping(ActionMapping mapping) {
        this.mapping = mapping;
    }

    public ActionServlet getServlet() {
        return servlet;
    }

    public ActionMapping getMapping() {
        return mapping;
    }

    /**
     * Gets the maximum post data size in bytes from the string
     * representation in the configuration file.
     */
    protected long getMaxSize(String stringSize) throws ServletException {
        long size = -1;
        int multiplier = 1;

        if (stringSize.endsWith("K")) {
            multiplier = 1024;
            stringSize = stringSize.substring(0, stringSize.length() - 1);
        }
        if (stringSize.endsWith("M")) {
            multiplier = 1024 * 1024;
            stringSize = stringSize.substring(0, stringSize.length() - 1);
        } else if (stringSize.endsWith("G")) {
            multiplier = 1024 * 1024 * 1024;
            stringSize = stringSize.substring(0, stringSize.length() - 1);
        }

        try {
            size = Long.parseLong(stringSize);
        } catch(NumberFormatException nfe) {
            throw new ServletException("Invalid format for maximum file size");
        }

        return (size * multiplier);
    }

    /**
     * Retrieves the temporary directory from either ActionServlet, a context
     * property, or a system property, in that order.
     */
    protected void retrieveTempDir(ModuleConfig moduleConfig) {

        //attempt to retrieve the servlet container's temporary directory
        ActionServlet servlet = getServlet();
        if (servlet != null) {
            //attempt to retrieve the servlet container's temporary directory
            ServletContext context = servlet.getServletContext();

            try {
                tempDir =
                        (String) context.getAttribute("javax.servlet.context.tempdir");
            } catch(ClassCastException cce) {
                tempDir = ((File) context.getAttribute("javax.servlet.context.tempdir")).getAbsolutePath();
            }
        }

        if (tempDir == null) {
            //attempt to retrieve the temporary directory from the controller
            tempDir = moduleConfig.getControllerConfig().getTempDir();

            if (tempDir == null) {
                //default to system-wide tempdir
                tempDir = System.getProperty("java.io.tmpdir");

                log.debug(
                    "DiskMultipartRequestHandler.handleRequest(): "
                        + "defaulting to java.io.tmpdir directory \""
                        + tempDir);
            }
        }
    }
}
