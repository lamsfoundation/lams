package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2001-2008, Russell Gold
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
import com.meterware.httpunit.scripting.*;
import com.meterware.httpunit.controls.SelectionFormControl;
import com.meterware.httpunit.dom.*;
import com.meterware.httpunit.protocol.ParameterProcessor;
import com.meterware.httpunit.protocol.UploadFileSpec;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.io.IOException;


/**
 * Represents a control in an HTML form.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public abstract class FormControl extends HTMLElementBase {

    final static String[] NO_VALUE = new String[0];

    private final WebForm _form;
    private HTMLControl _control;

    public static final String UNDEFINED_TYPE     = "undefined";
    public static final String BUTTON_TYPE        = "button";
    public static final String RESET_BUTTON_TYPE  = "reset";
    public static final String SUBMIT_BUTTON_TYPE = "submit";
    public static final String IMAGE_BUTTON_TYPE  = "image";
    public static final String RADIO_BUTTON_TYPE  = "radio";
    public static final String CHECKBOX_TYPE      = "checkbox";
    public static final String TEXT_TYPE          = "text";
    public static final String PASSWORD_TYPE      = "password";
    public static final String HIDDEN_TYPE        = "hidden";
    public static final String TEXTAREA_TYPE      = "textarea";
    public static final String FILE_TYPE          = "file";
    public static final String SINGLE_TYPE        = "select-one";
    public static final String MULTIPLE_TYPE      = "select-multiple";


    /**
     * Return the type of the control, as seen from JavaScript.
     */
    abstract public String getType();

    static ScriptableDelegate newSelectionOption() {
        return new SelectionFormControl.Option();
    }


    FormControl( WebForm form ) {
        this( form, newEmptyControlElement( form ) );
    }


    private static HTMLControl newEmptyControlElement( WebForm form ) {
        return (HTMLControl) form.getElement().getOwnerDocument().createElement( "input" );
    }


    /**
     * initialize the given form control from a Webform and a HTMLControl
     * @param form
     * @param control
     */
    protected FormControl( WebForm form, HTMLControl control ) {
        super( control );
        _control = control;
        _form               = form;
        supportAttribute( "tabindex" );
        supportAttribute( "disabled" );
        //      Add all custom attributes
        Set customAttributes = HttpUnitOptions.getCustomAttributes();
        if(customAttributes != null) {
            for(Iterator iter = customAttributes.iterator(); iter.hasNext(); ) {
                supportAttribute((String)iter.next());
            }
        }        
    }


    /**
     * Returns the current value(s) associated with this control. These values will be transmitted to the server
     * if the control is 'successful'.
     **/
    protected abstract String[] getValues();


    /**
     * Returns either a single delegate object or potentially an array of delegates as needed, given the form control.
     * This default implementation returns the scriptable delegate for the control.
     */
    Object getDelegate() {
        return getScriptingHandler();
    }


    final protected WebForm getForm() {
        return _form;
    }


    public ScriptableDelegate getParentDelegate() {
        return (ScriptableDelegate) getForm().getScriptingHandler();
    }


    /**
     * Returns the values permitted in this control. Does not apply to text or file controls.
     **/
    public String[] getOptionValues() {
        return NO_VALUE;
    }


    /**
     * Returns the list of values displayed by this control, if any.
     **/
    protected String[] getDisplayedOptions() {
        return NO_VALUE;
    }


    /**
     * Returns true if this control is read-only.
     **/
    protected boolean isReadOnly() {
        return isDisabled() || _control.getReadOnly();
    }


    /**
     * Returns true if this control is hidden.
     **/
    public boolean isHidden() {
        return false;
    }


    void setDisabled( boolean disabled ) {
        _control.setDisabled( disabled );
    }


    /**
     * Returns true if this control is disabled, meaning that it will not send a value to the server as part of a request.
     **/
    public boolean isDisabled() {
        return _control.getDisabled();
    }


    /**
     * Returns true if this control accepts free-form text.
     **/
    boolean isTextControl() {
        return false;
    }


    /**
     * Returns true if only one control of this kind with this name can have a value. This is true for radio buttons.
     **/
    boolean isExclusive() {
        return false;
    }


    /**
     * Returns true if a single control can have multiple values.
     **/
    protected boolean isMultiValued() {
        return false;
    }


    /**
     * Returns true if this control accepts a file for upload.
     **/
    boolean isFileParameter() {
        return false;
    }


    protected abstract void addValues( ParameterProcessor processor, String characterSet ) throws IOException;


    /**
     * Remove any required values for this control from the list, throwing an exception if they are missing.
     **/
    void claimRequiredValues( List values ) {
    }


    /**
     * Sets this control to the next compatible value from the list, removing it from the list.
     **/
    void claimValue( List values ) {
    }


    /**
     * Sets this control to the next compatible value from the list, removing it from the list.
     **/
    protected void claimUniqueValue( List values ) {
    }


    /**
     * Specifies a file to be uploaded via this control.
     **/
    void claimUploadSpecification( List files ) {
    }


    /**
     * Resets this control to its initial value.
     **/
    protected void reset() {
        _control.reset();
    }


    /**
     * Toggles the value of this control.
     */
    public void toggle() {
        throw new FormParameter.IllegalCheckboxParameterException( getName(), "toggleCheckbox" );
    }


    /**
     * Sets the state of this boolean control.
     */
    public void setState( boolean state ) {
        throw new FormParameter.IllegalCheckboxParameterException( getName(), "setCheckbox" );
    }


    /**
     * Performs the 'onChange' event defined for this control.
     * @deprecated since 1.7 use doOnChangeEvent instead
     */
    protected void sendOnChangeEvent() {
    	doOnChangeEvent();
    }
    
    /**
     * Performs the 'onchange' event defined for this control.
     */
    protected boolean doOnChangeEvent() {
    	return handleEvent("onchange");
    }


    /**
     * Performs the 'onClick' event defined for this control.
     * @deprecated since 1.7 use doOnClickEvent instead
     */
    protected void sendOnClickEvent() {
      doOnClickEvent();
    }
    
   
    /**
     * Performs the 'onClick' event defined for this control.
     */
    protected  boolean doOnClickEvent() {
    	return handleEvent("onclick");
    }
    
    /**
     * Performs the 'onMouseUp' event defined for this control.
    * @deprecated since 1.7 use doOnMouseUpEvent instead
     */
    protected void sendOnMouseUpEvent() {
    	doOnMouseUpEvent();
    }

    /**
     * Performs the 'onMouseUp' event defined for this control.
     */
    protected boolean doOnMouseUpEvent() {
    	return handleEvent("onmouseup");
    }

    /**
     * Performs the 'onMouseDown' event defined for this control.
     * @deprecated since 1.7 use doOnMouseDownEvent instead
     */
    protected void sendOnMouseDownEvent() {
    	doOnMouseDownEvent();
    }
    
    /**
     * Performs the 'onMouseDown' event defined for this control.
     */
    protected boolean doOnMouseDownEvent() {
    	return handleEvent("onmousedown");
    }

    /**
     * Creates and returns a scriptable object for this control. Subclasses should override this if they use a different
     * implementation of Scriptable.
     */
    public ScriptableDelegate newScriptable() {
        return new Scriptable();
    }


    /**
     * Returns the value of this control in the form. If no value is specified, defaults to the empty string.
     **/
    protected String getValueAttribute() {
        return "";
    }

    /**
     * Sets the value of this control in the form.
     */
    protected void setValueAttribute( String value ) {}


    /**
     * Removes the specified required value from the list of values, throwing an exception if it is missing.
     **/
    final protected void claimValueIsRequired( List values, final String value ) {
        if (!values.contains( value )) throw new MissingParameterValueException( getName(), value, (String[]) values.toArray( new String[ values.size() ]) );
        values.remove( value );
    }


    static String[] getControlElementTags() {
        return new String[] { "textarea", "select", "button", "input" };
    }


    /**
     * return the FormControl for the given parameter node in a form
     * @param form - the form in which the parameter is defined
     * @param node - the node in which the parameter is defined
     * @return the form control
     */
    static FormControl newFormParameter( WebForm form, Node node ) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        } else if (node.getNodeName().equalsIgnoreCase( "textarea" )) {
            return new TextAreaFormControl( form, (HTMLTextAreaElementImpl) node );
        } else if (node.getNodeName().equalsIgnoreCase( "select" )) {
            return new SelectionFormControl( form, (HTMLSelectElementImpl) node );
        } else if (node.getNodeName().equalsIgnoreCase( "button" )) {
            HTMLControl control = (HTMLControl) node;
            final String type = control.getType();
            if (type.equalsIgnoreCase( SUBMIT_BUTTON_TYPE )) {
                return new SubmitButton( form, control );
            } else if (type.equalsIgnoreCase( RESET_BUTTON_TYPE )) {
                return new ResetButton( form, control );
            } else {
                return new Button( form, control );
            }
        } else if (!node.getNodeName().equalsIgnoreCase( "input" )) {
            return null;
        } else {
            HTMLInputElementImpl element = (HTMLInputElementImpl) node;
            final String type = element.getType();
            if (type.equalsIgnoreCase( TEXT_TYPE )) {
                return new TextFieldFormControl( form, element );
            } else if (type.equalsIgnoreCase( PASSWORD_TYPE )) {
                return new PasswordFieldFormControl( form, element );
            } else if (type.equalsIgnoreCase( HIDDEN_TYPE )) {
                return new HiddenFieldFormControl( form, element );
            } else if (type.equalsIgnoreCase( RADIO_BUTTON_TYPE )) {
                return new RadioButtonFormControl( form, element );
            } else if (type.equalsIgnoreCase( CHECKBOX_TYPE )) {
                return new CheckboxFormControl( form, element );
            } else if (type.equalsIgnoreCase( SUBMIT_BUTTON_TYPE ) || type.equalsIgnoreCase( IMAGE_BUTTON_TYPE )) {
                return new SubmitButton( form, element );
            } else if (type.equalsIgnoreCase( BUTTON_TYPE )) {
                return new Button( form, (HTMLControl) node );
            } else if (type.equalsIgnoreCase( RESET_BUTTON_TYPE )) {
                return new ResetButton( form, (HTMLControl) node );
            } else if (type.equalsIgnoreCase( FILE_TYPE )) {
                return new FileSubmitFormControl( form, element );
            } else {
                return new TextFieldFormControl( form, element );
            }
        }
    }


    protected String emptyIfNull( String value ) {
        return (value == null) ? "" : value;
    }

    /**
     * implementation of Scriptable input elements
     */
    public class Scriptable extends HTMLElementScriptable implements Input {

    	  /**
    	   * get my Name
    	   * @return the name of this scriptable
    	   */
        public String getName() {
            return FormControl.this.getName();
        }


        /**
         * get my ID
         * @return the id of this scriptable
         */
        public String getID() {
            return FormControl.this.getID();
        }


        /**
         * construct a Scriptable
         */
        public Scriptable() {
            super( FormControl.this );
        }


        /**
         * get the given property
         * @param propertyName - the name of the property to get
         */
        public Object get( String propertyName ) {
            if (propertyName.equalsIgnoreCase( "name" )) {
                return FormControl.this.getName();
            } else if (propertyName.equalsIgnoreCase( "type" )) {
                return FormControl.this.getType();
            } else {
                return super.get( propertyName );
            }
        }


        /**
         * set the given property to the given value
         * @param propertyName - the property to set
         * @param value - the value to use
         */
        public void set( String propertyName, Object value ) {
            if (propertyName.equalsIgnoreCase( "value" )) {
                setValueAttribute( value.toString() );
            } else if (propertyName.equalsIgnoreCase( "disabled" )) {
                setDisabled( value instanceof Boolean && ((Boolean) value).booleanValue() );
            } else {
                super.set( propertyName, value );
            }
        }

        /**
         * set the given attribute to  the given value
         * @param attributeName - the name of the attribute to set
         * @param value - the value to use
         */
        public void setAttribute(String attributeName, Object value) {
           // Value set by JavaScript, make sure attribute is supported
           supportAttribute(attributeName);
           super.setAttribute( attributeName, value );          
        }
        
        /**
         * allow calling click for this control
         */
        public void click() throws IOException, SAXException {
        	// TODO check whether the empty body of this method was correct
        	// call onclick event handler
        	HTMLElement element=this.get_element();
        	if (element instanceof FormControl) {
        		FormControl control=(FormControl)element;
        		control.sendOnClickEvent();
        	}	
        }
        
        /**
         * simulate blur
         */
        public void blur() {
        	handleEvent("onblur");
        }


        /**
         * simulate focus;
         */
        public void focus() {
        	handleEvent("onfocus");
        }
        
        /**
         * allow firing a sendOnChangeEvent
         *
         */
        public void sendOnChangeEvent() {
        	// TODO check why the test for this does not work although
        	// the javascript function call is done in the corresponding testcase
        	// testCallOnChange()
        	HTMLElement element=this.get_element();
        	if (element instanceof FormControl) {
        		FormControl control=(FormControl)element;
        		control.sendOnChangeEvent();
        	}
        }

    }

}


abstract class BooleanFormControl extends FormControl {

    private String[] _displayedValue;

    private HTMLInputElementImpl _element;


    public ScriptableDelegate newScriptable() {
        return new Scriptable();
    }


    class Scriptable extends FormControl.Scriptable {

        public Object get( String propertyName ) {
            if (propertyName.equalsIgnoreCase( "value" )) {
                return getQueryValue();
            } else if (propertyName.equalsIgnoreCase( "checked" )) {
                return isChecked() ? Boolean.TRUE : Boolean.FALSE;
            } else if (propertyName.equalsIgnoreCase( "defaultchecked" )) {
                return _element.getDefaultChecked() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return super.get( propertyName );
            }
        }


        public void set( String propertyName, Object value ) {
            if (propertyName.equalsIgnoreCase( "checked" )) {
                setChecked( value instanceof Boolean && ((Boolean) value).booleanValue() );
            } else {
                super.set( propertyName, value );
            }
        }
    }

    public BooleanFormControl( WebForm form, HTMLInputElementImpl element ) {
        super( form, element );
        _element = element;
        _displayedValue = new String[] { readDisplayedValue( element ) };
    }


    private String readDisplayedValue( Node node ) {
        Node nextSibling = node.getNextSibling();
        while (nextSibling != null && nextSibling.getNodeType() != Node.TEXT_NODE && nextSibling.getNodeType() != Node.ELEMENT_NODE) nextSibling = nextSibling.getNextSibling();
        if (nextSibling == null || nextSibling.getNodeType() != Node.TEXT_NODE) return "";
        return nextSibling.getNodeValue();
    }


    boolean isChecked() {
        return _element.getChecked();
    }


    protected String getValueAttribute() {
        return emptyIfNull( _element.getValue() );
    }


    protected void setValueAttribute( String value ) {
        _element.setValue( value );
    }


    public void setChecked( boolean checked ) {
        _element.setChecked( checked );
    }


    /**
     * Returns the current value(s) associated with this control. These values will be transmitted to the server
     * if the control is 'successful'.
     **/
    public String[] getValues() {
        return isChecked() ? toArray( getQueryValue() ) : NO_VALUE;
    }


    /**
     * Returns the values permitted in this control.
     **/
    public String[] getOptionValues() {
        return (isReadOnly() && !isChecked()) ? NO_VALUE : toArray( getQueryValue() );
    }


    protected String[] getDisplayedOptions() {
        return _displayedValue;
    }


    protected void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
        if (isChecked() && !isDisabled()) processor.addParameter( getName(), getQueryValue(), characterSet );
    }


    /**
     * Remove any required values for this control from the list, throwing an exception if they are missing.
     **/
    void claimRequiredValues( List values ) {
        if (isValueRequired()) claimValueIsRequired( values, getQueryValue() );
    }


    protected boolean isValueRequired() {
        return isReadOnly() && isChecked();
    }


    abstract String getQueryValue();


    private String[] toArray( String value ) {
        return new String[] { value };
    }
}


class CheckboxFormControl extends BooleanFormControl {

    public String getType() {
        return CHECKBOX_TYPE;
    }

    public CheckboxFormControl( WebForm form, HTMLInputElementImpl element ) {
        super( form, element );
    }


    protected void claimUniqueValue( List values ) {
        if (isValueRequired()) return;
        setState( values.contains( getQueryValue() ) );
        if (isChecked()) values.remove( getQueryValue() );
    }


    String getQueryValue() {
        final String value = getValueAttribute();
        return value.length() == 0 ? "on" : value;
    }


    /**
     * Toggles the value of this control.
     */
    public void toggle() {
        setState( !isChecked() );
    }


    /**
     * Sets the state of this boolean control. Triggers the 'onclick' event if the state has changed.
     */
    public void setState( boolean state ) {
        boolean wasChecked = isChecked();
        setChecked( state );
        if (isChecked() != wasChecked) sendOnClickEvent();
    }
 }


abstract class TextFormControl extends FormControl {

    public TextFormControl( WebForm form, HTMLControl control ) {
        super( form, control );
    }


    /**
     * Returns the current value(s) associated with this control. These values will be transmitted to the server
     * if the control is 'successful'.
     **/
    public String[] getValues() {
        return new String[] { getValue() };
   }


    abstract protected String getDefaultValue();

    abstract protected String getValue();

    abstract protected void setValue( String value );


    /**
     * Returns true to indicate that this control accepts free-form text.
     **/
    public boolean isTextControl() {
        return true;
    }


    public ScriptableDelegate newScriptable() {
        return new Scriptable();
    }


    protected void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
        if (!isDisabled() && getName().length() > 0) processor.addParameter( getName(), getValues()[0], characterSet );
    }


    /**
     * claim values and fire onChange Event if a change occured 
     * @param values - the list of values
     */
    void claimValue( List values ) {
        if (isReadOnly()) return;

        String oldValue = getValue();
        if (values.isEmpty()) {
            setValue( "" );
        } else {
            setValue( (String) values.get(0) );
            values.remove(0);
        }
        boolean same=oldValue==null && getValue()==null;
        if (oldValue!=null)
        	 same=oldValue.equals( getValue());
        if (!same) sendOnChangeEvent();
    }


    void claimRequiredValues( List values ) {
        if (isReadOnly()) claimValueIsRequired( values );
    }


    protected void claimValueIsRequired( List values ) {
        claimValueIsRequired( values, getDefaultValue() );
    }

    class Scriptable extends FormControl.Scriptable {

        public Object get( String propertyName ) {
            if (propertyName.equalsIgnoreCase( "value" )) {
                return getValue();
            } else if (propertyName.equalsIgnoreCase( "defaultValue" )) {
                return getDefaultValue();
            } else {
                return super.get( propertyName );
            }
        }


        public void set( String propertyName, Object value ) {
            if (!propertyName.equalsIgnoreCase( "value" )) {
                super.set( propertyName, value );
            } else if (value instanceof Number) {
                setValue( HttpUnitUtils.trimmedValue( (Number) value ) );
            } else {
                setValue( (value == null) ? null : value.toString() );
            }
        }
    }
}


class TextFieldFormControl extends TextFormControl {

    private HTMLInputElementImpl _element;

    public String getType() {
        return TEXT_TYPE;
    }

    public TextFieldFormControl( WebForm form, HTMLInputElementImpl element ) {
        super( form, element );
        _element = element;
        supportAttribute( "maxlength" );
    }


    protected String getDefaultValue() {
        return _element.getDefaultValue();
    }


    protected String getValue() {
        return emptyIfNull( _element.getValue() );
    }


    protected void setValue( String value ) {
        _element.setValue( value );
    }
}

class PasswordFieldFormControl extends TextFieldFormControl {

    public String getType() {
        return PASSWORD_TYPE;
    }

    public PasswordFieldFormControl(WebForm form, HTMLInputElementImpl element) {
        super(form, element);
    }
}

/**
 * a hidden text field
 */
class HiddenFieldFormControl extends TextFieldFormControl {

    public String getType() {
        return HIDDEN_TYPE;
    }

    public HiddenFieldFormControl( WebForm form, HTMLInputElementImpl element ) {
        super( form, element );
    }


    void claimRequiredValues( List values ) {
        claimValueIsRequired( values );
    }


    void claimValue( List values ) {
    }


    public boolean isHidden() {
        return true;
    }
}


class TextAreaFormControl extends TextFormControl {

    private HTMLTextAreaElementImpl _element;

    public TextAreaFormControl( WebForm form, HTMLTextAreaElementImpl element ) {
        super( form, element );
        _element = element;
    }

    public String getType() {
        return TEXTAREA_TYPE;
    }


    protected String getDefaultValue() {
        return _element.getDefaultValue();
    }


    protected String getValue() {
        return _element.getValue();
    }


    protected void setValue( String value ) {
        _element.setValue( value );
    }

}


/**
 * a control for File submit
 */
class FileSubmitFormControl extends FormControl {

	  /**
	   * accessor for the type
	   * @return the constant FILE_TYPE
	   */
    public String getType() {
        return FILE_TYPE;
    }

    private UploadFileSpec _fileToUpload;


    public ScriptableDelegate newScriptable() {
        return new Scriptable();
    }


    class Scriptable extends FormControl.Scriptable {

        public Object get( String propertyName ) {
            if (propertyName.equalsIgnoreCase( "value" )) {
                return getSelectedName();
           } else {
                return super.get( propertyName );
            }
        }


    }

    public FileSubmitFormControl( WebForm form, HTMLInputElementImpl node ) {
        super( form, node );
    }


    /**
     * Returns true if this control accepts a file for upload.
     **/
    public boolean isFileParameter() {
        return true;
    }


    /**
     * Returns the name of the selected file, if any.
     */
    public String[] getValues() {
        return new String[] { getSelectedName() };
    }


    private String getSelectedName() {
        return _fileToUpload == null ? "" : _fileToUpload.getFileName();
    }


    /**
     * Specifies a number of file upload specifications for this control.
     **/
    void claimUploadSpecification( List files ) {
        if (files.isEmpty()) {
            _fileToUpload = null;
        } else {
            _fileToUpload = (UploadFileSpec) files.get(0);
            files.remove(0);
        }
    }


    protected void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
        if (!isDisabled() && _fileToUpload != null) {
            processor.addFile( getName(), _fileToUpload );
        }
    }
}




//============================= exception class MissingParameterValueException ======================================


/**
 * This exception is thrown on an attempt to remove a required value from a form parameter.
 **/
class MissingParameterValueException extends IllegalRequestParameterException {


    MissingParameterValueException( String parameterName, String missingValue, String[] proposed ) {
        _parameterName  = parameterName;
        _missingValue   = missingValue;
        _proposedValues = proposed;
    }


    public String getMessage() {
        StringBuffer sb = new StringBuffer(HttpUnitUtils.DEFAULT_TEXT_BUFFER_SIZE);
        sb.append( "Parameter '" ).append( _parameterName ).append( "' must have the value '" );
        sb.append( _missingValue ).append( "'. Attempted to set it to: { " );
        for (int i = 0; i < _proposedValues.length; i++) {
            if (i != 0) sb.append( ", " );
            sb.append( _proposedValues[i] );
        }
        sb.append( " }" );
        return sb.toString();
    }


    private String   _parameterName;
    private String   _missingValue;
    private String[] _proposedValues;
}


