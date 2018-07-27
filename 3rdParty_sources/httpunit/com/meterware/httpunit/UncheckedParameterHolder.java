package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2003, 2007-2008 Russell Gold
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
import com.meterware.httpunit.protocol.UploadFileSpec;
import com.meterware.httpunit.protocol.ParameterProcessor;

import java.io.IOException;

import java.util.Hashtable;
import java.util.Enumeration;


/**
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
final class UncheckedParameterHolder extends ParameterHolder implements ParameterProcessor {

    private static final String[] NO_VALUES = new String[ 0 ];
    private final String _characterSet;

    private Hashtable _parameters = new Hashtable();
    private boolean   _submitAsMime;


    UncheckedParameterHolder() {
        _characterSet = HttpUnitOptions.getDefaultCharacterSet();
    }


    UncheckedParameterHolder( WebRequestSource source ) {
        _characterSet = source.getCharacterSet();
        _submitAsMime = source.isSubmitAsMime();

        try {
            source.recordPredefinedParameters( this );
            source.recordParameters( this );
        } catch (IOException e) {
            throw new RuntimeException( "This should never happen" );
        }
    }


//----------------------------------- ParameterProcessor methods -------------------------------------------------------


    public void addParameter( String name, String value, String characterSet ) throws IOException {
        Object[] values = (Object[]) _parameters.get( name );
        _parameters.put( name, HttpUnitUtils.withNewValue( values, value ) );
    }


    public void addFile( String parameterName, UploadFileSpec fileSpec ) throws IOException {
        Object[] values = (Object[]) _parameters.get( parameterName );
        _parameters.put( parameterName, HttpUnitUtils.withNewValue( values, fileSpec ) );
    }


//----------------------------------- ParameterHolder methods ----------------------------------------------------------


    /**
     * Specifies the position at which an image button (if any) was clicked.
     **/
    void selectImageButtonPosition( SubmitButton imageButton, int x, int y ) {
    	if (imageButton.isValidImageButton()) {
        setParameter( imageButton.positionParameterName("x"), Integer.toString( x ) );
        setParameter( imageButton.positionParameterName("y"), Integer.toString( y ) );
    	}  
    }


    /**
     * Does nothing, since unchecked requests treat all parameters the same.
     **/
    void recordPredefinedParameters( ParameterProcessor processor ) throws IOException {
    }


    /**
     * Iterates through the parameters in this holder, recording them in the supplied parameter processor.
     **/
    public void recordParameters( ParameterProcessor processor ) throws IOException {
        Enumeration e = _parameters.keys();

        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            Object[] values = (Object[]) _parameters.get( name );
            for (int i = 0; i < values.length; i++) {
           	  if (values[i] instanceof String || values[i] == null) {
                    processor.addParameter( name, (String) values[i], _characterSet );
                } else if (values[i] instanceof UploadFileSpec) {
                    processor.addFile( name, (UploadFileSpec) values[i] );
                }
            }
        }
    }


    String[] getParameterNames() {
        return (String[]) _parameters.keySet().toArray( new String[ _parameters.size() ] );
    }


    String getParameterValue( String name ) {
        String[] values = getParameterValues( name );
        return values.length == 0 ? null : values[0];
    }


    String[] getParameterValues( String name ) {
        Object[] values = (Object[]) _parameters.get( name );
        if (values == null) return NO_VALUES;

        String[] result = new String[ values.length ];
        for (int i = 0; i < result.length; i++) {
            result[i] = values[i] instanceof UploadFileSpec ? ((UploadFileSpec) values[i]).getFileName() : values[i].toString();
        }
        return result;
    }


    void removeParameter( String name ) {
        _parameters.remove( name );
    }


    void setParameter( String name, String value ) {
        _parameters.put( name, new Object[] { value } );
    }


    void setParameter( String name, String[] values ) {
        _parameters.put( name, values );
    }


    void setParameter( String name, UploadFileSpec[] files ) {
        _parameters.put( name, files );
    }


    boolean isFileParameter( String name ) {
        return true;
    }


    String getCharacterSet() {
        return _characterSet;
    }


    boolean isSubmitAsMime() {
        return _submitAsMime;
    }
}
