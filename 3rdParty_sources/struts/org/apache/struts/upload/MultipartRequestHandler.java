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

import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionMapping;

/**
  * MultipartRequestHandler provides an standard interface for struts to
  * deal with file uploads from forms with enctypes of "multipart/form-data".
  * Providers must provide a no-argument constructor for initialization.
  *
  */
public interface MultipartRequestHandler
{
    /**
     * This is the ServletRequest attribute that should be set when a multipart request is being read
     * and the maximum length is exceeded. The value is a Boolean. If the maximum length isn't exceeded,
     * this attribute shouldn't be put in the ServletRequest. It's the job of the implementation to put this
     * attribute in the request if the maximum length is exceeded; in the handleRequest(HttpServletRequest) method.
     */
    public static final String ATTRIBUTE_MAX_LENGTH_EXCEEDED = "org.apache.struts.upload.MaxLengthExceeded";

    /**
     * Convienience method to set a reference to a working
     * ActionServlet instance.
     */
    public void setServlet(ActionServlet servlet);

    /**
     * Convienience method to set a reference to a working
     * ActionMapping instance.
     */
    public void setMapping(ActionMapping mapping);

    /**
     * Get the ActionServlet instance
     */
    public ActionServlet getServlet();

    /**
     * Get the ActionMapping instance for this request
     */
    public ActionMapping getMapping();

    /**
      * After constructed, this is the first method called on
      * by ActionServlet.  Use this method for all your
      * data-parsing of the ServletInputStream in the request
      *
      * @exception ServletException thrown if something goes wrong
      */
    public void handleRequest(HttpServletRequest request)
        throws ServletException;

    /**
     * This method is called on to retrieve all the text
     * input elements of the request.
     *
     * @return A Hashtable where the keys and values are the names and
     *  values of the request input parameters
     */
    public Hashtable getTextElements();
    
    /**
     * This method is called on to retrieve all the FormFile
     * input elements of the request.
     * @see org.apache.struts.upload.FormFile
     * @return A Hashtable where the keys are the input names of the
     *  files and the values are FormFile objects
     */
    public Hashtable getFileElements();

    /**
     * This method returns all elements of a multipart request.
     * @return A Hashtable where the keys are input names and values
     *   are either Strings or FormFiles
     */
    public Hashtable getAllElements();

    /**
     * This method is called on when there's some sort of problem
     * and the form post needs to be rolled back.  Providers
     * should remove any FormFiles used to hold information
     * by setting them to null and also physically delete
     * them if the implementation calls for writing directly
     * to disk.
     * NOTE: Currently implemented but not automatically
     * supported, ActionForm implementors must call rollback()
     * manually for rolling back file uploads.
     */
    public void rollback();

    /**
     * This method is called on when a successful form post
     * has been made.  Some implementations will use this
     * to destroy temporary files or write to a database
     * or something of that nature.
     */
    public void finish();

}

