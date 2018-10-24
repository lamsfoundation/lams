package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2007, Russell Gold
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
import java.util.*;
import java.net.URL;
import java.io.IOException;

import org.w3c.dom.Node;
import com.meterware.httpunit.protocol.UploadFileSpec;
import com.meterware.httpunit.protocol.ParameterProcessor;


/**
 * An implementation of web request source whose URL does not change under user action.
 *
 * @author <a href="mailto:russgold@acm.org">Russell Gold</a>
 **/
abstract class FixedURLWebRequestSource extends WebRequestSource {

    private static final String[] NO_VALUES = new String[0];
    private Map       _presetParameterMap;
    private ArrayList _presetParameterList;
    private String    _characterSet;


    public FixedURLWebRequestSource( WebResponse response, Node node, URL baseURL, String attribute, FrameSelector frame, String defaultTarget, String characterSet ) {
        super( response, node, baseURL, attribute, frame, defaultTarget );
        _characterSet = characterSet;
    }


//------------------------------------------- WebRequestSource methods -------------------------------------------------


    /**
     * Creates and returns a web request which will simulate clicking on this link.
     **/
    public WebRequest getRequest() {
        return new GetMethodWebRequest( this );
    }


    /**
     * Returns an array containing the names of any parameters defined as part of this link's URL.
     **/
    public String[] getParameterNames() {
        ArrayList parameterNames = new ArrayList( getPresetParameterMap().keySet() );
        return (String[]) parameterNames.toArray( new String[ parameterNames.size() ] );
    }


    /**
     * Returns the multiple default values of the named parameter.
     **/
    public String[] getParameterValues( String name ) {
        final String[] values = (String[]) getPresetParameterMap().get( name );
        return values == null ? NO_VALUES : values;
    }


    protected void addPresetParameter( String name, String value ) {
        _presetParameterMap.put( name, HttpUnitUtils.withNewValue( (String[]) _presetParameterMap.get( name ), value ) );
        _presetParameterList.add( new PresetParameter( name, value ) );
    }


    protected String getEmptyParameterValue() {
        return "";
    }


    protected void setDestination( String destination ) {
        super.setDestination( destination );
        _presetParameterList = null;
        _presetParameterMap = null;
    }


//------------------------------------------- ParameterHolder methods --------------------------------------------------


    /**
     * Specifies the position at which an image button (if any) was clicked.
     **/
    void selectImageButtonPosition( SubmitButton imageButton, int x, int y ) {
        throw new IllegalNonFormParametersRequest();
    }


    /**
     * Iterates through the fixed, predefined parameters in this holder, recording them in the supplied parameter processor.\
     * These parameters always go on the URL, no matter what encoding method is used.
     **/

    void recordPredefinedParameters( ParameterProcessor processor ) throws IOException {
    }


    /**
     * Iterates through the parameters in this holder, recording them in the supplied parameter processor.
     **/
    public void recordParameters( ParameterProcessor processor ) throws IOException {
        Iterator i = getPresetParameterList().iterator();
        while (i.hasNext()) {
            PresetParameter o = (PresetParameter) i.next();
            processor.addParameter( o.getName(), o.getValue(), getCharacterSet() );
         }
    }


    /**
     * Removes a parameter name from this collection.
     **/
    void removeParameter( String name ) {
        throw new IllegalNonFormParametersRequest();
    }


    /**
     * Sets the value of a parameter in a web request.
     **/
    void setParameter( String name, String value ) {
        setParameter( name, new String[] { value } );
    }


    /**
     * Sets the multiple values of a parameter in a web request.
     **/
    void setParameter( String name, String[] values ) {
        if (values == null) {
            throw new IllegalArgumentException( "May not supply a null argument array to setParameter()" );
        } else if (!getPresetParameterMap().containsKey( name )) {
            throw new IllegalNonFormParametersRequest();
        } else if (!equals( getParameterValues( name ), values )) {
            throw new IllegalNonFormParametersRequest();
        }
    }


    String getCharacterSet() {
        return _characterSet;
    }


    private boolean equals( String[] left, String[] right ) {
        if (left.length != right.length) return false;
        List rightValues = Arrays.asList( right );
        for (int i = 0; i < left.length; i++) {
            if (!rightValues.contains( left[i] )) return false;
        }
        return true;
    }


    /**
     * Sets the multiple values of a file upload parameter in a web request.
     **/
    void setParameter( String name, UploadFileSpec[] files ) {
        throw new IllegalNonFormParametersRequest();
    }


    /**
     * Returns true if the specified parameter is a file field.
     **/
    boolean isFileParameter( String name ) {
        return false;
    }


    boolean isSubmitAsMime() {
        return false;
    }


    private Map getPresetParameterMap() {
        if (_presetParameterMap == null) loadPresetParameters();
        return _presetParameterMap;
    }


    private ArrayList getPresetParameterList() {
        if (_presetParameterList == null) loadPresetParameters();
        return _presetParameterList;
    }


    private void loadPresetParameters() {
        _presetParameterMap = new HashMap();
        _presetParameterList = new ArrayList();
        loadDestinationParameters();
    }


}


class PresetParameter {
    private String _name;
    private String _value;


    public PresetParameter( String name, String value ) {
        _name = name;
        _value = value;
    }


    public String getName() {
        return _name;
    }


    public String getValue() {
        return _value;
    }
}


class IllegalNonFormParametersRequest extends IllegalRequestParameterException {

    public IllegalNonFormParametersRequest() {
    }

    public String getMessage() {
        return "May not modify parameters for a request not derived from a form with parameter checking enabled.";
     }


}
