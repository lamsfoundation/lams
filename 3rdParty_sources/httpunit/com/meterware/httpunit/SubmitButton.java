package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2000-2004, 2007, 2008 Russell Gold
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
import java.io.IOException;

import org.xml.sax.SAXException;
import com.meterware.httpunit.dom.HTMLControl;
import com.meterware.httpunit.protocol.ParameterProcessor;

/**
 * This class represents a submit button in an HTML form.
 **/
public class SubmitButton extends Button {

    private boolean _fake;
    

    public String getType() {
        return (isImageButton()?IMAGE_BUTTON_TYPE:SUBMIT_BUTTON_TYPE);
    }

    /**
     * Returns true if this submit button is an image map.
     **/
    public boolean isImageButton() {
        return _isImageButton;
    }


		/**
     * Performs the action associated with clicking this button after running any 'onClick' script.
     * For a submit button this typically submits the form.
     *
     * @since 1.6
     */
    public void click( int x, int y ) throws IOException, SAXException {
        if (!isImageButton()) 
        	throw new IllegalStateException( "May only specify positions for an image button" );
        doOnClickSequence(x,y);
    }


//--------------------------------- Button methods ----------------------------------------------

    /**
     * do the button Action 
     * @param x - x coordinate
     * @param y - y coordinate
     */
    protected void doButtonAction(int x,int y) throws IOException, SAXException {
      getForm().doFormSubmit( this ,x,y);
    }  


//------------------------------------ Object methods ----------------------------------------


    public String toString() {
        return "Submit with " + getName() + "=" + getValue();
    }


    public int hashCode() {
        return getName().hashCode() + getValue().hashCode();
    }


    public boolean equals( Object o ) {
        return getClass().equals( o.getClass() ) && equals( (SubmitButton) o );
    }


//------------------------------------------ package members ----------------------------------


    SubmitButton( WebForm form, HTMLControl control ) {
        super( form, control );
        _isImageButton = control.getType().equalsIgnoreCase( IMAGE_BUTTON_TYPE );
    }


    SubmitButton( WebForm form ) {
        super( form );
        _isImageButton = false;
    }


    static SubmitButton createFakeSubmitButton( WebForm form ) {
        return new SubmitButton( form, /* fake */ true );
    }


    private SubmitButton( WebForm form, boolean fake ) {
        this( form );
        _fake = fake;
    }


    /**
     * getter for the fake flag
     * Returns true for synthetic submit buttons, created by HttpUnit in forms that contain no
     * submit buttons, or used during {@link WebForm#submitNoButton()} call.
     * @return - whether this button is a faked button inserted by httpunit
     */
    public boolean isFake() {
        return _fake;
    }


    /**
     * flag that the button was pressed
     * @param pressed
     */
    void setPressed( boolean pressed ) {
        _pressed = pressed;
    }


    void setLocation( int x, int y ) {
        _x = x;
        _y = y;
    }


//--------------------------------- FormControl methods ----------------------------------------------------------------


    /**
     * Returns the current value(s) associated with this control. These values will be transmitted to the server
     * if the control is 'successful'.
     **/
    protected String[] getValues() {
        return (isDisabled() || !_pressed) ? NO_VALUE : toArray( getValue() );
    }

    /**
     * should we allow unnamed Image Buttons?
     */
    private static boolean allowUnnamedImageButton=false;
    
    /**
		 * @return the allowUnnamedImageButton
		 */
		public static boolean isAllowUnnamedImageButton() {
			return allowUnnamedImageButton;
		}

		/**
		 * @param allowUnnamedImageButton the allowUnnamedImageButton to set
		 */
		public static void setAllowUnnamedImageButton(boolean allowUnnamedImageButton) {
			SubmitButton.allowUnnamedImageButton = allowUnnamedImageButton;
		}

    
    /**
     * return whether this is a validImageButton
     * @return true if it is an image Button
     */
    public boolean isValidImageButton() {
      String buttonName = getName();
      boolean valid=this.isImageButton();
      if (!allowUnnamedImageButton)
      	valid=valid && buttonName != null && buttonName.length() > 0;
    	return valid;
    }
    
    /**
     * return the name of the positionParameter for this button (if this is an image Button)
     * @param direction e.g. "x" or "y"
     * @return the name e.g. "image.x" or just "x"
     */
    public String positionParameterName(String direction) {
    	// [ 1443333 ] Allow unnamed Image input elments to submit x,y values
      String buttonName = getName();
    	String buttonPrefix="";        	
     	if (buttonName != null && buttonName.length() > 0) {
     		buttonPrefix=buttonName+".";
     	}	
    	return buttonPrefix+direction;
    }
    
    /**
     * addValues if not disabled and pressed
     * @param processor - the ParameterProcessor used
     * @param characterSet - the active character set
     * @throws IOException if addValues fails
     */
    protected void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
      if (_pressed && !isDisabled()) {
        String buttonName = getName();
        if (buttonName != null && buttonName.length() > 0 && getValue().length() > 0) {
        	processor.addParameter( getName(), getValue(), characterSet );
       	}
        if (isValidImageButton()) {
          processor.addParameter( positionParameterName("x"), Integer.toString( _x ), characterSet );
          processor.addParameter( positionParameterName("y"), Integer.toString( _y ), characterSet );
        }
      } // if  
    }    


//------------------------------------------ private members ----------------------------------


    private       String[] _value = new String[1];
    private final boolean  _isImageButton;
    private       boolean  _pressed;
    private       int      _x;
    private       int      _y;


    private String[] toArray( String value ) {
        _value[0] = value;
        return _value;
    }


    private boolean equals( SubmitButton button ) {
        return getName().equals( button.getName() ) &&
                  (getName().length() == 0 || getValue().equals( button.getValue() ));
    }
    
    public void throwDisabledException() {
    	throw new DisabledSubmitButtonException( this );
    }	
    
    /**
     * This exception is thrown on an attempt to define a form request with a button not defined on that form.
     **/
    class DisabledSubmitButtonException extends DisabledButtonException {


        DisabledSubmitButtonException( SubmitButton button ) {
        		super(button);
        }


        public String getMessage() {
            return "The specified button (name='" + _name + "' value='" + _value
                   + "' is disabled and may not be used to submit this form.";
        }

    }

}

