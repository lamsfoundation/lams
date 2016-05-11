package com.meterware.httpunit.protocol;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2001-2008, 2007, Russell Gold
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 *******************************************************************************************************************/
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;


/**
 * A description of a file to be uploaded as part of a form submission.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class UploadFileSpec {

    /**
     * Creates a specification based on a File object. The content type will be guessed from the file extension.
     */
    public UploadFileSpec( File file ) {
        _file = file;
        guessContentType();
    }


    /**
     * Creates a specification based on a File object and with a specified content type.
     */
    public UploadFileSpec( File file, String contentType ) {
        _file = file;
        _contentType = contentType;
    }


    /**
     * Creates a specification for an upload from an input stream. The file name and content type must be specified.
     */
    public UploadFileSpec( String fileName, InputStream inputStream, String contentType ) {
        _fileName = fileName;
        _inputStream = inputStream;
        _contentType = contentType;
    }


    public InputStream getInputStream() throws IOException {
        if (_inputStream == null) {
            _inputStream = new FileInputStream( _file );
        }
        return _inputStream;
    }


    public String getFileName() {
        if (_fileName == null) {
            _fileName = _file.getAbsolutePath();
        }
        return _fileName;
    }


    /**
     * Returns the content type associated with this file upload specification.
     */
    public String getContentType() {
        return _contentType;
    }


    private File _file;

    private InputStream _inputStream;

    private String _fileName;

    private String _contentType = "text/plain";

    /**
     * the default content extensions
     */
    private static String[][] CONTENT_EXTENSIONS = {
        { "text/plain",               "txt", "text" },
        { "text/html",                "htm", "html" },
        { "image/gif",                "gif" },
        { "image/jpeg",               "jpg", "jpeg" },
        { "image/png",                "png" },
        { "image/tiff",               "tif", "tiff" },
        { "application/pdf",          "pdf" },        
        { "application/octet-stream", "zip" }
    };


    private void guessContentType() {
        String extension = getExtension( _file.getName() );
        for (int i = 0; i < CONTENT_EXTENSIONS.length; i++) {
            for (int j=1; j < CONTENT_EXTENSIONS[i].length; j++) {
                if (extension.equalsIgnoreCase( CONTENT_EXTENSIONS[i][j] )) {
                    _contentType = CONTENT_EXTENSIONS[i][0];
                    return;
                }
            }
        }
    }

    private String getExtension( String fileName ) {
        return fileName.substring( fileName.lastIndexOf( '.' ) + 1 );
    }
}
