package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2008, Russell Gold
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
import com.meterware.httpunit.scripting.NamedDelegate;
import com.meterware.httpunit.scripting.ScriptableDelegate;
import com.meterware.httpunit.scripting.FormScriptable;
import com.meterware.httpunit.protocol.UploadFileSpec;
import com.meterware.httpunit.protocol.ParameterProcessor;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLCollection;
import org.xml.sax.SAXException;


/**
 * This class represents a form in an HTML page. Users of this class may examine the parameters
 * defined for the form, the structure of the form (as a DOM), or the text of the form. They
 * may also create a {@link WebRequest} to simulate the submission of the form.
 *
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public class WebForm extends WebRequestSource {
    private static final FormParameter UNKNOWN_PARAMETER = new FormParameter();

    private final static String[] NO_VALUES = new String[0];

    private Button[] _buttons;

    /** The submit buttons in this form. **/
    private SubmitButton[] _submitButtons;

    /** The character set in which the form will be submitted. **/
    private String         _characterSet;

    private Vector _buttonVector;

    private FormControl[] _presetParameters;
    private ArrayList     _presets;

    private ElementRegistry _registry;



    /** Predicate to match a link's name. **/
    public final static HTMLElementPredicate MATCH_NAME;
    private HTMLFormElement _domElement;


    /**
     * Submits this form using the web client from which it was originally obtained.
     **/
    public WebResponse submit() throws IOException, SAXException {
        return submit( getDefaultButton() );
    }


    /**
     * Submits this form using the web client from which it was originally obtained.
     * Will usually return the result of that submission; however, if the submit button's 'onclick'
     * or the form's 'onsubmit' event is triggered and
     * inhibits the submission, will return the updated contents of the frame containing this form.
     **/
    public WebResponse submit( SubmitButton button ) throws IOException, SAXException {
    	WebResponse result=submit(button,0,0);
      return result;
    }


    /**
     * Submits this form using the web client from which it was originally obtained.
     * Will usually return the result of that submission; however, if the submit button's 'onclick'
     * or the form's 'onsubmit' event is triggered and
     * inhibits the submission, will return the updated contents of the frame containing this form.
     * @since 1.6
     **/
    public WebResponse submit( SubmitButton button, int x, int y ) throws IOException, SAXException {
    	WebResponse result=null;
      if (button==null || button.doOnClickSequence(x, y)) {
      	result= doFormSubmit( button, x, y );
      } else {
      	result=getCurrentFrameContents();
      }
      return result;
    }


    /**
     * Submits this form using the web client from which it was originally obtained, ignoring any buttons defined for the form.
     * @since 1.6
     **/
     public WebResponse submitNoButton() throws SAXException, IOException {
        return submit( SubmitButton.createFakeSubmitButton( this /* fake */ ) );
    }


    protected WebResponse submitRequest( String event, WebRequest request ) throws IOException, SAXException {
        try {
            return super.submitRequest( event, request );
        } catch (UnknownServiceException e) {
            throw new UnsupportedActionException( "HttpUnit does not support " + request.getURL().getProtocol() + " URLs in form submissions" );
        }
    }


    /**
     * Submits the form without also invoking the button's "onclick" event.
     */
    WebResponse doFormSubmit( SubmitButton button ) throws IOException, SAXException {
        return submitRequest( getAttribute( "onsubmit" ), getRequest( button ) );
    }


    WebResponse doFormSubmit( SubmitButton button, int x, int y ) throws IOException, SAXException {
        return submitRequest( getAttribute( "onsubmit" ), getRequest( button, x, y ) );
    }


    /**
     * Returns the method defined for this form.
     **/
    public String getMethod() {
        return getAttribute( "method", "GET" );
    }


    /**
     * Returns the action defined for this form.
     **/
    public String getAction() {
        return getDestination();
     }


    /**
     * Returns true if a parameter with given name exists in this form.
     **/
    public boolean hasParameterNamed( String soughtName ) {
        return getFormParameters().containsKey( soughtName );
    }


    /**
     * Returns true if a parameter starting with a given name exists,
     **/
    public boolean hasParameterStartingWithPrefix( String prefix ) {
        String[] names = getParameterNames();
        for (int i = 0; i < names.length; i++) {
            if (names[i].startsWith( prefix )) return true;
        }
        return false;
    }


    /**
     * Returns an array containing all of the buttons defined for this form.
     **/
    public Button[] getButtons() {
        if (_buttons == null) {
            FormControl[] controls = getFormControls();
            ArrayList buttonList = new ArrayList();
            for (int i = 0; i < controls.length; i++) {
                FormControl control = controls[ i ];
                if (control instanceof Button) buttonList.add( control );
            }
            _buttons = (Button[]) buttonList.toArray( new Button[ buttonList.size() ] );
        }
        return _buttons;
    }


    public Button getButton( HTMLElementPredicate predicate, Object criteria ) {
        Button[] buttons = getButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (predicate.matchesCriteria( buttons[i], criteria )) return buttons[i];
        }
        return null;
    }


    /**
     * Convenience method which returns the button with the specified ID.
     */
    public Button getButtonWithID( String buttonID ) {
        return getButton( Button.WITH_ID, buttonID );
    }


    /**
     * Returns an array containing the submit buttons defined for this form.
     **/
    public SubmitButton[] getSubmitButtons() {
        if (_submitButtons == null) {
            Vector buttons = getSubmitButtonVector();
            _submitButtons = new SubmitButton[ buttons.size() ];
            buttons.copyInto( _submitButtons );
        }
        return _submitButtons;
    }


    /**
     * Returns the submit button defined in this form with the specified name.
     * If more than one such button exists, will return the first found.
     * If no such button is found, will return null.
     **/
    public SubmitButton getSubmitButton( String name ) {
        SubmitButton[] buttons = getSubmitButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getName().equals( name )) {
                return buttons[i];
            }
        }
        return null;
    }


    /**
     * Returns the submit button defined in this form with the specified name and value.
     * If more than one such button exists, will return the first found.
     * If no such button is found, will return null.
     **/
    public SubmitButton getSubmitButton( String name, String value ) {
        SubmitButton[] buttons = getSubmitButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getName().equals( name ) && buttons[i].getValue().equals( value )) {
                return buttons[i];
            }
        }
        return null;
    }


    /**
     * Returns the submit button defined in this form with the specified ID.
     * If more than one such button exists, will return the first found.
     * If no such button is found, will return null.
     **/
    public SubmitButton getSubmitButtonWithID( String ID ) {
        SubmitButton[] buttons = getSubmitButtons();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getID().equals( ID )) {
                return buttons[i];
            }
        }
        return null;
    }


    /**
     * Creates and returns a web request which will simulate the submission of this form with a button with the specified name and value.
     **/
    public WebRequest getRequest( String submitButtonName, String submitButtonValue ) {
        SubmitButton sb = getSubmitButton( submitButtonName, submitButtonValue );
        if (sb == null) throw new IllegalSubmitButtonException( submitButtonName, submitButtonValue );
        return getRequest( sb );
    }


    /**
     * Creates and returns a web request which will simulate the submission of this form with a button with the specified name.
     **/
    public WebRequest getRequest( String submitButtonName ) {
        SubmitButton sb = getSubmitButton( submitButtonName );
        if (sb == null) throw new IllegalSubmitButtonException( submitButtonName, "" );
        return getRequest( sb );
    }


    /**
     * Creates and returns a web request which will simulate the submission of this form by pressing the specified button.
     * If the button is null, simulates the pressing of the default button.
     **/
    public WebRequest getRequest( SubmitButton button ) {
        return getRequest( button, 0, 0 );
    }


    /**
     * Creates and returns a web request which will simulate the submission of this form by pressing the specified button.
     * If the button is null, simulates the pressing of the default button.
     * @param button - the submitbutton to be pressed - may be null
     * @param x - the x position
     * @param y - the y position
     **/
    public WebRequest getRequest( SubmitButton button, int x, int y ) {
        if (button == null) 
        	button = getDefaultButton();

        if (HttpUnitOptions.getParameterValuesValidated()) {
            if (button == null) {
                throw new IllegalUnnamedSubmitButtonException();
            } else if (button.isFake()) {
                // bypass checks
            } else if (!getSubmitButtonVector().contains( button )) {
                throw new IllegalSubmitButtonException( button );
            } else if (!button.wasEnabled()) {            	
            	// this is too late for the check of isDisabled()
            	// onclick has already been done ...
            	// [ 1289151 ] Order of events in button.click() is wrong
              button.throwDisabledException();
            }
        }

        SubmitButton[] buttons = getSubmitButtons();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPressed( false );
        }
        button.setPressed( true );

        if (getMethod().equalsIgnoreCase( "post" )) {
            return new PostMethodWebRequest( this, button, x, y );
        } else {
            return new GetMethodWebRequest( this, WebRequest.newParameterHolder( this ), button, x, y );
        }
    }


    /**
     * Creates and returns a web request which includes the specified button. If no button is specified, will include
     * the default button, if any. No parameter validation will be done on the returned request and no scripts
     * will be run when it is submitted.
     **/
    public WebRequest newUnvalidatedRequest( SubmitButton button ) {
        return newUnvalidatedRequest( button, 0, 0 );
    }


    /**
     * Creates and returns a web request which includes the specified button and position. If no button is specified,
     * will include the default button, if any. No parameter validation will be done on the returned request
     * and no scripts will be run when it is submitted.
     **/
    public WebRequest newUnvalidatedRequest( SubmitButton button, int x, int y ) {
        if (button == null) button = getDefaultButton();

        SubmitButton[] buttons = getSubmitButtons();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPressed( false );
        }
        button.setPressed( true );

        if (getMethod().equalsIgnoreCase( "post" )) {
            return new PostMethodWebRequest( this, new UncheckedParameterHolder( this ), button, x, y );
        } else {
            return new GetMethodWebRequest( this, new UncheckedParameterHolder( this ), button, x, y );
        }
    }


    private WebRequest getScriptedSubmitRequest() {
        SubmitButton[] buttons = getSubmitButtons();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setPressed( false );
        }

        if (getMethod().equalsIgnoreCase( "post" )) {
            return new PostMethodWebRequest( this );
        } else {
            return new GetMethodWebRequest( this );
        }

    }


    /**
     * Returns the default value of the named parameter.  If the parameter does not exist returns null.
     **/
    public String getParameterValue( String name ) {
        String[] values = getParameterValues( name );
        return values.length == 0 ? null : values[0];
    }


    /**
     * Returns the displayed options defined for the specified parameter name.
     **/
    public String[] getOptions( String name ) {
        return getParameter( name ).getOptions();
    }


    /**
     * Returns the option values defined for the specified parameter name.
     **/
    public String[] getOptionValues( String name ) {
        return getParameter( name ).getOptionValues();
    }


    /**
     * Returns true if the named parameter accepts multiple values.
     **/
    public boolean isMultiValuedParameter( String name ) {
        return getParameter( name ).isMultiValuedParameter();
    }


    /**
     * Returns the number of text parameters in this form with the specified name.
     **/
    public int getNumTextParameters( String name ) {
        return getParameter( name ).getNumTextParameters();
    }


    /**
     * Returns true if the named parameter accepts free-form text.
     **/
    public boolean isTextParameter( String name ) {
        return getParameter( name ).isTextParameter();
    }


    /**
     * Returns true if this form is to be submitted using mime encoding (the default is URL encoding).
     **/
    public boolean isSubmitAsMime() {
        return "multipart/form-data".equalsIgnoreCase( getAttribute( "enctype" ) );
    }


    public FormScriptable getScriptableObject() {
        return (FormScriptable) getScriptingHandler();
    }

    /**
     * Resets all parameters to their initial values.
     */
    public void reset() {
    	if (handleEvent("onreset"))
    		resetControls();
    }


    private void resetControls() {
        FormControl[] controls = getFormControls();
        for (int i = 0; i < controls.length; i++) {
            controls[i].reset();
        }
    }


    public ScriptableDelegate newScriptable() {
        return new Scriptable();
    }

//---------------------------------- WebRequestSource methods --------------------------------

    /**
     * Returns the character set encoding for this form.
     **/
    public String getCharacterSet() {
        return _characterSet;
    }


    /**
     * Returns true if the named parameter accepts files for upload.
     **/
    public boolean isFileParameter( String name ) {
        return getParameter( name ).isFileParameter();
    }


    /**
     * Returns an array containing the names of the parameters defined for this form.
     **/
    public String[] getParameterNames() {
        ArrayList parameterNames = new ArrayList( getFormParameters().keySet() );
        return (String[]) parameterNames.toArray( new String[ parameterNames.size() ] );
    }


    /**
     * Returns the multiple default values of the named parameter.
     **/
    public String[] getParameterValues( String name ) {
        final FormParameter parameter = getParameter( name );
        return parameter.getValues();
    }


    /**
     * Returns true if the named parameter is read-only. If more than one control exists with the same name,
     * will return true only if all such controls are read-only.
     **/
    public boolean isReadOnlyParameter( String name ) {
        return getParameter( name ).isReadOnlyParameter();
    }


    /**
     * Returns true if the named parameter is disabled. If more than one control exists with the same name,
     * will return true only if all such controls are read-only.
     **/
    public boolean isDisabledParameter( String name ) {
        return getParameter( name ).isDisabledParameter();
    }


    /**
     * Returns true if the named parameter is hidden. If more than one control exists with the same name,
     * will return true only if all such controls are hidden.
     **/
    public boolean isHiddenParameter( String name ) {
        return getParameter( name ).isHiddenParameter();
    }


    /**
     * Creates and returns a web request which will simulate the submission of this form with an unnamed submit button.
     **/
    public WebRequest getRequest() {
        return getRequest( (SubmitButton) null );
    }


    /**
     * Creates and returns a web request based on the current state of this form. No parameter validation will be done
     * and there is no guarantee over the order of parameters transmitted.
     */
    public WebRequest newUnvalidatedRequest() {
        return newUnvalidatedRequest( null );
    }


    /**
     * Records a parameter defined by including it in the destination URL. Ignores any parameters whose name matches
     * a form control.
     **/
    protected void addPresetParameter( String name, String value ) {
        FormControl[] formControls = getFormControls();
        for (int i = 0; i < formControls.length; i++) {
            if (formControls[i].getName().equals( name)) return;
        }
        _presets.add( new PresetFormParameter( this, name, value ) );
    }


    protected String getEmptyParameterValue() {
        return null;
    }


//---------------------------------- ParameterHolder methods --------------------------------


    /**
     * Specifies the position at which an image button (if any) was clicked.
     **/
    void selectImageButtonPosition( SubmitButton imageButton, int x, int y ) {
        imageButton.setLocation( x, y );
    }


    /**
     * Iterates through the fixed, predefined parameters in this holder, recording them in the supplied parameter processor.\
     * These parameters always go on the URL, no matter what encoding method is used.
     **/

    void recordPredefinedParameters( ParameterProcessor processor ) throws IOException {
        FormControl[] controls = getPresetParameters();
        for (int i = 0; i < controls.length; i++) {
            controls[i].addValues( processor, getCharacterSet() );
        }
    }


    /**
     * Iterates through the parameters in this holder, recording them in the supplied parameter processor.
     **/
    public void recordParameters( ParameterProcessor processor ) throws IOException {
        FormControl[] controls = getFormControls();
        for (int i = 0; i < controls.length; i++) {
            controls[i].addValues( processor, getCharacterSet() );
        }
    }


    /**
     * Removes a parameter name from this collection.
     **/
    public void removeParameter( String name ) {
    	setParameter( name, NO_VALUES );
    }


    /**
     * Sets the value of a parameter in this form.
     * @param name - the name of the parameter
     * @param value - the value of the parameter
     **/
    public void setParameter( String name, String value ) {
    	setParameter( name, new String[] { value } );
    }


    /**
     * Sets the multiple values of a parameter in this form. This is generally used when there are multiple
     * controls with the same name in the form.
     */
    public void setParameter( String name, final String[] values ) {
        FormParameter parameter = getParameter( name );        
        if (parameter == UNKNOWN_PARAMETER) throw new NoSuchParameterException( name );
        if (parameter.isFileParameter()) {
        	throw new InvalidFileParameterException(name,values);
        }        	
        parameter.setValues( values );
    }


    /**
     * Sets the multiple values of a file upload parameter in a web request.
     **/
    public void setParameter( String name, UploadFileSpec[] files ) {
        FormParameter parameter = getParameter( name );
        if ((parameter == null) || (!parameter.isFileParameter()))
        	throw new NoSuchParameterException( name );
       	parameter.setFiles( files );
    }


    /**
     * Sets the single value of a file upload parameter in this form.
     * A more convenient way to do this than using {@link #setParameter(String,com.meterware.httpunit.protocol.UploadFileSpec[])}
     * @since 1.6
     */
    public void setParameter( String name, File file ) {
        setParameter( name, new UploadFileSpec[] { new UploadFileSpec( file ) } );
    }


    /**
     * Toggles the value of the specified checkbox parameter.
     * @param name the name of the checkbox parameter
     * @throws IllegalArgumentException if the specified parameter is not a checkbox or there is more than one
     *         control with that name.
     * @since 1.5.4
     */
    public void toggleCheckbox( String name ) {
        FormParameter parameter = getParameter( name );
        if (parameter == null) throw new NoSuchParameterException( name );
        parameter.toggleCheckbox();
    }


    /**
     * Toggles the value of the specified checkbox parameter.
     * @param name the name of the checkbox parameter
     * @param value of the checkbox parameter
     * @throws IllegalArgumentException if the specified parameter is not a checkbox or if there is no checkbox
     *         with the specified name and value.
     * @since 1.6
     */
    public void toggleCheckbox( String name, String value ) {
        FormParameter parameter = getParameter( name );
        if (parameter == null) throw new NoSuchParameterException( name );
        parameter.toggleCheckbox( value );
    }


    /**
     * Sets the value of the specified checkbox parameter.
     * @param name the name of the checkbox parameter
     * @param state the new state of the checkbox
     * @throws IllegalArgumentException if the specified parameter is not a checkbox or there is more than one
     *         control with that name.
     * @since 1.5.4
     */
    public void setCheckbox( String name, boolean state ) {
        FormParameter parameter = getParameter( name );
        if (parameter == null) throw new NoSuchParameterException( name );
        parameter.setValue( state );
    }


    /**
     * Sets the value of the specified checkbox parameter.
     * @param name the name of the checkbox parameter
     * @param value of the checkbox parameter
     * @param state the new state of the checkbox
     * @throws IllegalArgumentException if the specified parameter is not a checkbox or if there is no checkbox
     *         with the specified name and value.
     * @since 1.6
     */
    public void setCheckbox( String name, String value, boolean state ) {
        FormParameter parameter = getParameter( name );
        if (parameter == null) throw new NoSuchParameterException( name );
        parameter.setValue( value, state );
    }


    public class Scriptable extends HTMLElementScriptable implements NamedDelegate, FormScriptable {
        public String getAction() { return WebForm.this.getAction(); }
        public void setAction( String newAction ) { setDestination( newAction ); _presetParameters = null; }


        public void submit() throws IOException, SAXException {
            submitRequest( getScriptedSubmitRequest() );
        }


        public void reset() throws IOException, SAXException {
            resetControls();
        }


        public String getName() {
            return WebForm.this.getID().length() != 0 ? WebForm.this.getID() : WebForm.this.getName();
        }


        /**
         * get the Object for the given propertyName
         * @param propertyName - the name of the property to get
         * @return the Object for the property
         */
        public Object get( String propertyName ) {
            if (propertyName.equals( "target" )) {
                return getTarget();
            } else if (propertyName.equals( "action" )) {
                return getAction();                
            } else if (propertyName.equals( "length" )) {
                return new Integer(getFormControls().length);
            } else {
                final FormParameter parameter = getParameter( propertyName );
                if (parameter != UNKNOWN_PARAMETER) return parameter.getScriptableObject();
                FormControl control = getControlWithID( propertyName );
                return control == null ? super.get( propertyName ) : control.getScriptingHandler();
            }
        }


        /**
         * Sets the value of the named property. Will throw a runtime exception if the property does not exist or
         * cannot accept the specified value.
         * @param propertyName - the name of the property
         * @param value - the new value
         **/
        public void set( String propertyName, Object value ) {
            if (propertyName.equals( "target" )) {
              setTargetAttribute( value.toString() );
            } else if (propertyName.equals( "action" )) {
              setAction( value.toString() );
            } else if (value instanceof String) {
                setParameterValue( propertyName, (String) value );
            } else if (value instanceof Number) {
                setParameterValue( propertyName, HttpUnitUtils.trimmedValue( (Number) value ) );
            } else {
                super.set( propertyName, value );
            }
        }


        public void setParameterValue( String name, String value ) {
            final Object scriptableObject = getParameter( name ).getScriptableObject();
            if (scriptableObject instanceof ScriptableDelegate) {
                ((ScriptableDelegate) scriptableObject).set( "value", value );
            } else if (scriptableObject instanceof ScriptableDelegate[]) {
                ((ScriptableDelegate[]) scriptableObject)[0].set( "value", value );
            }
        }


        public ScriptableDelegate[] getElementDelegates() {
            FormControl[] controls = getFormControls();
            ScriptableDelegate[] result = new ScriptableDelegate[ controls.length ];
            for (int i = 0; i < result.length; i++) {
                result[i] = (ScriptableDelegate) controls[i].getScriptingHandler();
            }
            return result;
        }


        public ScriptableDelegate[] getElementsByTagName( String name ) throws SAXException {
            return getDelegates( getHTMLPage().getElementsByTagName( getElement(), name ) );
        }


        Scriptable() {
            super( WebForm.this );
        }
    }


//---------------------------------- package members --------------------------------

    /**
     * Contructs a web form given the URL of its source page and the DOM extracted
     * from that page.
     **/
    WebForm( WebResponse response, URL baseURL, Node node, FrameSelector frame, String defaultTarget, String characterSet, ElementRegistry registry ) {
        super( response, node, baseURL, "action", frame, defaultTarget );
        _characterSet = characterSet;
        _registry = registry;
        _domElement = (HTMLFormElement) node;
    }


    /**
     * Returns the form control which is part of this form with the specified ID.
     */
    public FormControl getControlWithID( String id ) {
        FormControl[] controls = getFormControls();
        for (int i = 0; i < controls.length; i++) {
            FormControl control = controls[i];
            if (control.getID().equals(id)) return control;
        }
        return null;
    }


//---------------------------------- private members --------------------------------

    private SubmitButton getDefaultButton() {
        if (getSubmitButtons().length == 1) {
            return getSubmitButtons()[0];
        } else {
            return getSubmitButton( "" );
        }
    }


    /**
     * get the Vector of submit buttons - will always contain at least
     * one button - if the original vector has none a faked submit button will be added
     * @return a Vector with the submit buttons
     */
    private Vector getSubmitButtonVector() {
        if (_buttonVector == null) {
            _buttonVector = new Vector();
            FormControl[] controls = getFormControls();
            for (int i = 0; i < controls.length; i++) {
                FormControl control = controls[ i ];
                if (control instanceof SubmitButton) {
                	SubmitButton sb=(SubmitButton)control;
                	sb.rememberEnableState();
                	_buttonVector.add( sb );
                }
            }

            /**
             * make sure that there is always at least one submit button
             * if none is in the Vector add a faked one
             */
            if (_buttonVector.isEmpty()) 
            	_buttonVector.addElement( SubmitButton.createFakeSubmitButton( this ) );
        }
        return _buttonVector;
    }


    private FormControl[] getPresetParameters() {
        if (_presetParameters == null) {
            _presets = new ArrayList();
            loadDestinationParameters();
            _presetParameters = (FormControl[]) _presets.toArray( new FormControl[ _presets.size() ] );
        }
        return _presetParameters;
    }


    FormControl newFormControl( Node child ) {
        return FormControl.newFormParameter( this, child );
    }


    /**
     * Returns an array of form parameter attributes for this form.
     **/
    private FormControl[] getFormControls() {
        HTMLCollection controlElements = _domElement.getElements();
        FormControl[] controls = new FormControl[ controlElements.getLength() ];
        for (int i = 0; i < controls.length; i++) {
            controls[i] = getControlForNode( controlElements.item( i ) );
        }
        return controls;
    }


    private FormControl getControlForNode( Node node ) {
        if (_registry.hasNode( node )) {
            return (FormControl) _registry.getRegisteredElement( node );
        } else {
            return (FormControl) _registry.registerElement( node, newFormControl( node ) );
        }
    }


    /**
     * get the form parameter with the given name
     * @param name
     * @return the form parameter with this name
     */
    public FormParameter getParameter( String name ) {
        final FormParameter parameter = ((FormParameter) getFormParameters().get( name ));
        return parameter != null ? parameter : UNKNOWN_PARAMETER;
    }


    /**
     * Returns a map of parameter name to form parameter objects. Each form parameter object represents the set of form
     * controls with a particular name. Unnamed parameters are ignored.
     */
    private Map getFormParameters() {
        Map formParameters = new HashMap();
        loadFormParameters( formParameters, getPresetParameters() );
        loadFormParameters( formParameters, getFormControls() );
        return formParameters;
    }


    private void loadFormParameters( Map formParameters, FormControl[] controls ) {
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].getName().length() == 0) continue;
            FormParameter parameter = (FormParameter) formParameters.get( controls[i].getName() );
            if (parameter == null) {
                parameter = new FormParameter();
                formParameters.put( controls[i].getName(), parameter );
            }
            parameter.addControl( controls[i] );
        }
    }


    static {
        MATCH_NAME = new HTMLElementPredicate() {
            public boolean matchesCriteria( Object htmlElement, Object criteria ) {
                return HttpUnitUtils.matches( ((WebForm) htmlElement).getName(), (String) criteria );
            }
        };

    }


//===========================---===== exception class NoSuchParameterException =========================================

    /**
     * This exception is thrown on an attempt to set a file parameter to a non file type
     **/
    class InvalidFileParameterException extends IllegalRequestParameterException {


    		/**
    		 * construct a new InvalidFileParameterException for the given parameter name and value list
    		 * @param parameterName
    		 * @param values
    		 */
        InvalidFileParameterException( String parameterName, String[] values ) {
            _parameterName = parameterName;
            _values=values;
        }


        /**
         * get the message for this exception
         */
        public String getMessage() {
        	String valueList="";
        	String delim="";
        	for (int i=0;i<_values.length;i++) {
        		valueList+=delim+"'"+_values[i]+"'";
        		delim=", ";
        	}
        	String msg="The file parameter with the name '"+_parameterName+"' must have type File but the string values "+valueList+" where supplied";
        	return msg;
        }


        private String _parameterName;
        private String[] _values;
    }

    /**
     * This exception is thrown on an attempt to set a parameter to a value not permitted to it by the form.
     **/
    class NoSuchParameterException extends IllegalRequestParameterException {


        NoSuchParameterException( String parameterName ) {
            _parameterName = parameterName;
        }


        public String getMessage() {
            return "No parameter named '" + _parameterName + "' is defined in the form";
        }


        private String _parameterName;

    }


//============================= exception class IllegalUnnamedSubmitButtonException ======================================


    /**
     * This exception is thrown on an attempt to define a form request with a button not defined on that form.
     **/
    class IllegalUnnamedSubmitButtonException extends IllegalRequestParameterException {


        IllegalUnnamedSubmitButtonException() {
        }


        public String getMessage() {
            return "This form has more than one submit button, none unnamed. You must specify the button to be used.";
        }

    }


//============================= exception class IllegalSubmitButtonException ======================================


    /**
     * This exception is thrown on an attempt to define a form request with a button not defined on that form.
     **/
    class IllegalSubmitButtonException extends IllegalRequestParameterException {


        IllegalSubmitButtonException( SubmitButton button ) {
            _name  = button.getName();
            _value = button.getValue();
        }


        IllegalSubmitButtonException( String name, String value ) {
            _name = name;
            _value = value;
        }


        public String getMessage() {
            return "Specified submit button (name=\"" + _name + "\" value=\"" + _value + "\") not part of this form.";
        }


        private String _name;
        private String _value;

    }

//============================= exception class IllegalUnnamedSubmitButtonException ======================================



}



//========================================== class PresetFormParameter =================================================


    class PresetFormParameter extends FormControl {

        PresetFormParameter( WebForm form, String name, String value ) {
            super( form );
            _name   = name;
            _value  = value;
        }


        /**
         * Returns the name of this control..
         **/
        public String getName() {
            return _name;
        }


        /**
         * Returns true if this control is read-only.
         **/
        public boolean isReadOnly() {
            return true;
        }


        /**
         * Returns true if this control accepts free-form text.
         **/
        public boolean isTextControl() {
            return true;
        }


        /**
         * Remove any required values for this control from the list, throwing an exception if they are missing.
         **/
        void claimRequiredValues( List values ) {
            if (_value != null) claimValueIsRequired( values, _value );
        }


        public String getType() {
            return UNDEFINED_TYPE;
        }

        /**
         * Returns the current value(s) associated with this control. These values will be transmitted to the server
         * if the control is 'successful'.
         **/
        public String[] getValues() {
            if (_values == null) _values = new String[] { _value };
            return _values;
        }


        protected void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
            processor.addParameter( _name, _value, characterSet );
        }


        private String   _name;
        private String   _value;
        private String[] _values;
    }





