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

/**
 * This class represents an element in a multipart request.
 * It has a few methods for determining * whether or not the element is a
 * String or a file, and methods to retrieve the data of the aforementioned
 * element.  Text input elements have a <code>null</code> content type,
 * files have a non-null content type.
 *
 *
 * @deprecated Use the Commons FileUpload based multipart handler instead. This
 *             class will be removed after Struts 1.2.
 */
public class MultipartElement
{
    /**
     * The content type of this element.
     */
    protected String contentType;

    /**
     * The element data.
     * @deprecated This should never be used.
     */
    protected byte[] data;

    /**
     * The element's data represented in a (possibly temporary) file.
     */
    protected File file;

    /**
     * The element name.
     */
    protected String name;

    /**
     * The element's filename, null for text elements.
     */
    protected String fileName;


    /**
     * The element's text value, null for file elements
     */
    protected String value;


    /**
     * Whether or not this element is a file.
     */
    protected boolean isFile = false;

    /**
     * Constructor for a file element.
     * @param name The form name of the element
     * @param fileName The file name of the element if this element is a file
     * @param contentType The content type of the element if a file
     * @param file The (possibly temporary) file representing this element if
     *             it's a file
     */
    public MultipartElement(String name, String fileName,
                            String contentType, File file)
    {
        this.name = name;
        this.fileName = fileName;
        this.contentType = contentType;
        this.file = file;
        this.isFile = true;
    }

    /**
     * Constructor for a text element.
     * @param name The name of the element
     * @param value The value of the element
     */
    public MultipartElement(String name, String value)
    {
         this.name = name;
         this.value = value;
         this.isFile = false;
    }

    /**
      * Retrieve the content type.
      */
    public String getContentType()
    {
         return contentType;
    }

    /**
     * Get the File that holds the data for this element.
     */
    public File getFile()
    {
       return file;
    }


    /**
     * Retrieve the name.
     */
    public String getName()
    {
       return name;
    }

    /**
     * Retrieve the filename, can return <code>null</code>
     * for text elements.
     */
    public String getFileName()
    {
      return fileName;
    }


    /**
     * Returns the value of this multipart element.
     * @return A String if the element is a text element, <code>null</code>
     *         otherwise
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Set the file that represents this element.
     */
    public void setFile(File file)
    {
        this.file = file;
    }


    /**
     * Set the file name for this element.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    /**
     * Set the name for this element.
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Set the content type.
     */
    public void setContentType(String contentType)
    {
         this.contentType = contentType;
    }


    /**
     * Is this element a file.
     */
    public boolean isFile()
    {
        if (file == null)
        {
            return false;
        }
        return true;
    }


    public void setValue(String value)
    {
        this.value = value;
    }

}
