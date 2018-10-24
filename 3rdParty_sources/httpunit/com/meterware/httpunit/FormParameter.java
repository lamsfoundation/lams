package com.meterware.httpunit;
/********************************************************************************************************************
 *
 *
 * Copyright (c) 2002-2003,2006-2007 Russell Gold
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

import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.protocol.UploadFileSpec;

import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the aggregate of all form controls with a particular name.  This permits us to abstract setting
 * values so that changing a control type does not break a test.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class FormParameter {


    private FormControl[] _controls;
    private ArrayList _controlList = new ArrayList();
    private RadioGroupFormControl _group;
    private String _name;


    void addControl( FormControl control ) {
        _controls = null;
        if (_name == null) _name = control.getName();
        if (!_name.equalsIgnoreCase( control.getName() )) throw new RuntimeException( "all controls should have the same name" );
        if (control.isExclusive()) {
            getRadioGroup( control.getForm() ).addRadioButton( (RadioButtonFormControl) control );
        } else {
            _controlList.add( control );
        }
    }


    /**
     * get the controls for this form Parameter
     * @return the controls
     */
    public FormControl[] getControls() {
        if (_controls == null) _controls = (FormControl[]) _controlList.toArray( new FormControl[ _controlList.size() ] );
        return _controls;
    }
    
    /**
     * get the control for this form Parameter (assuming it 
     * has only one as for a text control
     * @return the controls
     */
    public FormControl getControl()  {
    	FormControl[] controls=getControls();
    	if (controls.length!=1) {
    		throw new RuntimeException("getControl can only be called if the number of controls is 1 but it is "+controls.length+" you might want to use getControls instead");
    	}
    	return controls[0];
    }


    Object getScriptableObject() {
        if (getControls().length == 1) {
            return getControls()[0].getDelegate();
        } else {
            ArrayList list = new ArrayList();
            for (int i = 0; i < _controls.length; i++) {
                FormControl control = _controls[i];
                list.add( control.getScriptingHandler() );
            }
            return list.toArray( new ScriptableDelegate[ list.size() ] );
        }
    }


    String[] getValues() {
        ArrayList valueList = new ArrayList();
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            valueList.addAll( Arrays.asList( controls[i].getValues() ) );
        }
        return (String[]) valueList.toArray( new String[ valueList.size() ] );
    }


    /**
     * set values to the given values
     * @param values
     */
    void setValues( String[] values ) {
        ArrayList list = new ArrayList( values.length );
        list.addAll( Arrays.asList( values ) );
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) 
        	controls[i].claimRequiredValues( list );
        for (int i = 0; i < controls.length; i++) 
        	controls[i].claimUniqueValue( list );
        for (int i = 0; i < controls.length; i++) 
        	controls[i].claimValue( list );
        if (!list.isEmpty()) throw 	
        	new UnusedParameterValueException( _name, (String) list.get(0) );
    }


    public void toggleCheckbox() {
        FormControl[] controls = getControls();
        if (controls.length != 1) throw new IllegalCheckboxParameterException( _name, "toggleCheckbox" );
        controls[0].toggle();
    }


    public void toggleCheckbox( String value ) {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            FormControl control = controls[i];
            if (value.equals( control.getValueAttribute())) {
                control.toggle();
                return;
            }
        }
        throw new IllegalCheckboxParameterException( _name + "/" + value , "toggleCheckbox" );
    }


    public void setValue( boolean state ) {
        FormControl[] controls = getControls();
        if (controls.length != 1) throw new IllegalCheckboxParameterException( _name, "setCheckbox" );
        controls[0].setState( state );
    }


    public void setValue( String value, boolean state ) {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            FormControl control = controls[i];
            if (value.equals( control.getValueAttribute())) {
                control.setState( state );
                return;
            }
        }
        throw new IllegalCheckboxParameterException( _name + "/" + value , "setCheckbox" );
    }


    void setFiles( UploadFileSpec[] fileArray ) {
        ArrayList list = new ArrayList( fileArray.length );
        list.addAll( Arrays.asList( fileArray ) );
        for (int i = 0; i < getControls().length; i++) getControls()[i].claimUploadSpecification( list );
        if (!list.isEmpty()) throw new UnusedUploadFileException( _name, fileArray.length - list.size(), fileArray.length );
    }


    String[] getOptions() {
        ArrayList optionList = new ArrayList();
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            optionList.addAll( Arrays.asList( controls[i].getDisplayedOptions() ) );
        }
        return (String[]) optionList.toArray( new String[ optionList.size() ] );
    }


    String[] getOptionValues() {
        ArrayList valueList = new ArrayList();
        for (int i = 0; i < getControls().length; i++) {
            valueList.addAll( Arrays.asList( getControls()[i].getOptionValues() ) );
        }
        return (String[]) valueList.toArray( new String[ valueList.size() ] );
    }


    boolean isMultiValuedParameter() {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].isMultiValued()) return true;
            if (!controls[i].isExclusive() && controls.length > 1) return true;
        }
        return false;
    }


    int getNumTextParameters() {
        int result = 0;
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].isTextControl()) result++;
        }
        return result;
    }


    boolean isTextParameter() {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].isTextControl()) return true;
        }
        return false;
    }


    boolean isFileParameter() {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].isFileParameter()) return true;
        }
        return false;
    }


    boolean isDisabledParameter() {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (!controls[i].isDisabled()) return false;
        }
        return true;
    }


    boolean isReadOnlyParameter() {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (!controls[i].isReadOnly()) return false;
        }
        return true;
    }

    
    public boolean isHiddenParameter() {
        FormControl[] controls = getControls();
        for (int i = 0; i < controls.length; i++) {
            if (!controls[i].isHidden()) return false;
        }
        return true;
    }


    private RadioGroupFormControl getRadioGroup( WebForm form ) {
        if (_group == null) {
            _group = new RadioGroupFormControl( form );
            _controlList.add( _group );
        }
        return _group;
    }


//============================= exception class UnusedParameterValueException ======================================


    /**
     * This exception is thrown on an attempt to set a parameter to a value not permitted to it by the form.
     **/
    public class UnusedParameterValueException extends IllegalRequestParameterException {

    	
    	/**
    	 * construct an exception for an unused parameter with the given name 
    	 * and the value that is bad
    	 * @param parameterName
    	 * @param badValue
    	 */
        UnusedParameterValueException( String parameterName, String badValue ) {
            _parameterName = parameterName;
            _badValue      = badValue;
        }

        
        /**
         * get the message for this exception
         * @return the message
         */
        public String getMessage() {
            StringBuffer sb = new StringBuffer(HttpUnitUtils.DEFAULT_TEXT_BUFFER_SIZE);
            sb.append( "Attempted to assign to parameter '" ).append( _parameterName );
            sb.append( "' the extraneous value '" ).append( _badValue ).append( "'." );
            return sb.toString();
        }


        private String   _parameterName;
        private String   _badValue;
    }


//============================= exception class UnusedUploadFileException ======================================


    /**
     * This exception is thrown on an attempt to upload more files than permitted by the form.
     **/
    class UnusedUploadFileException extends IllegalRequestParameterException {


    	/**
    	 * construct a new UnusedUploadFileException exception base on the parameter Name the number of files expected and supplied
    	 * @param parameterName
    	 * @param numFilesExpected
    	 * @param numFilesSupplied
    	 */
        UnusedUploadFileException( String parameterName, int numFilesExpected, int numFilesSupplied ) {
            _parameterName = parameterName;
            _numExpected   = numFilesExpected;
            _numSupplied   = numFilesSupplied;
        }


        /**
         * get the message for this exception
         */
        public String getMessage() {
            StringBuffer sb = new StringBuffer( HttpUnitUtils.DEFAULT_TEXT_BUFFER_SIZE );
            sb.append( "Attempted to upload " ).append( _numSupplied ).append( " files using parameter '" ).append( _parameterName );
            if (_numExpected == 0) {
                sb.append( "' which is not a file parameter." );
            } else {
                sb.append( "' which only has room for " ).append( _numExpected ).append( '.' );
            }
            return sb.toString();
        }


        private String _parameterName;
        private int    _numExpected;
        private int    _numSupplied;
    }


//============================= exception class IllegalCheckboxParameterException ======================================


    /**
     * This exception is thrown on an attempt to set a parameter to a value not permitted to it by the form.
     **/
    static class IllegalCheckboxParameterException extends IllegalRequestParameterException {


        IllegalCheckboxParameterException( String parameterName, String methodName ) {
            _parameterName = parameterName;
            _methodName      = methodName;
        }


        public String getMessage() {
            StringBuffer sb = new StringBuffer(HttpUnitUtils.DEFAULT_TEXT_BUFFER_SIZE);
            sb.append( "Attempted to invoke method '" ).append( _methodName );
            sb.append( "' for parameter '" ).append( _parameterName ).append( "', which is not a unique checkbox control." );
            return sb.toString();
        }


        private String   _parameterName;
        private String   _methodName;
    }



}

