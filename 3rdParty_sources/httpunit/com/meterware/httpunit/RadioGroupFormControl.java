package com.meterware.httpunit;
/********************************************************************************************************************

*
* Copyright (c) 2001-2007,2008 Russell Gold
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.meterware.httpunit.controls.IllegalParameterValueException;
import com.meterware.httpunit.protocol.ParameterProcessor;
import com.meterware.httpunit.scripting.ScriptableDelegate;

/**
 * Radio button control
 */
public class RadioGroupFormControl extends FormControl {

  private List _buttonList = new ArrayList();
  private RadioButtonFormControl[] _buttons;
  private String[] _allowedValues;


  public String getType() {
      return UNDEFINED_TYPE;
  }

  /**
   * construct Radiobuttons for a form
   * @param form
   */
  public RadioGroupFormControl( WebForm form ) {
      super( form );
  }


  /**
   * add a radio button 
   * @param control
   */
  void addRadioButton( RadioButtonFormControl control ) {
      _buttonList.add( control );
      _buttons = null;
      _allowedValues = null;
  }


  /**
   * get the values for the buttons
   * @return an array of String values
   */
  public String[] getValues() {
      for (int i = 0; i < getButtons().length; i++) {
          if (getButtons()[i].isChecked()) return getButtons()[i].getValues();
      }
      return NO_VALUE;
  }


  /**
   * Returns the option values defined for this radio button group.
   **/
  public String[] getOptionValues() {
      ArrayList valueList = new ArrayList();
      FormControl[] buttons = getButtons();
      for (int i = 0; i < buttons.length; i++) {
          valueList.addAll( Arrays.asList( buttons[i].getOptionValues() ) );
      }
      return (String[]) valueList.toArray( new String[ valueList.size() ] );
  }


  /**
   * Returns the options displayed for this radio button group.
   */
  protected String[] getDisplayedOptions() {
      ArrayList valueList = new ArrayList();
      FormControl[] buttons = getButtons();
      for (int i = 0; i < buttons.length; i++) {
          valueList.addAll( Arrays.asList( buttons[i].getDisplayedOptions() ) );
      }
      return (String[]) valueList.toArray( new String[ valueList.size() ] );
  }


  Object getDelegate() {
      ScriptableDelegate[] delegates = new ScriptableDelegate[ getButtons().length ];
      for (int i = 0; i < delegates.length; i++) {
          delegates[i] = (ScriptableDelegate) getButtons()[i].getScriptingHandler();
      }
      return delegates;
  }


  protected void addValues( ParameterProcessor processor, String characterSet ) throws IOException {
      for (int i = 0; i < getButtons().length; i++) getButtons()[i].addValues( processor, characterSet );
  }


  /**
   * Remove any required values for this control from the list, throwing an exception if they are missing.
   **/
  void claimRequiredValues( List values ) {
      for (int i = 0; i < getButtons().length; i++) {
          getButtons()[i].claimRequiredValues( values );
      }
  }


  protected void claimUniqueValue( List values ) {
      int matchingButtonIndex = -1;
      for (int i = 0; i < getButtons().length && matchingButtonIndex < 0; i++) {
          if (!getButtons()[i].isReadOnly() && values.contains( getButtons()[i].getQueryValue() )) matchingButtonIndex = i;
      }
      if (matchingButtonIndex <0) throw new IllegalParameterValueException( getButtons()[0].getName(), values, getAllowedValues() );

      boolean wasChecked = getButtons()[ matchingButtonIndex ].isChecked();
      for (int i = 0; i < getButtons().length; i++) {
          if (!getButtons()[i].isReadOnly()) getButtons()[i].setChecked( i == matchingButtonIndex );
      }
      values.remove( getButtons()[ matchingButtonIndex ].getQueryValue() );
      if (!wasChecked) getButtons()[ matchingButtonIndex ].sendOnClickEvent();
  }


  protected void reset() {
      for (int i = 0; i < getButtons().length; i++) getButtons()[i].reset();
  }


  private String[] getAllowedValues() {
      if (_allowedValues == null) {
          _allowedValues = new String[ getButtons().length ];
          for (int i = 0; i < _allowedValues.length; i++) {
              _allowedValues[i] = getButtons()[i].getQueryValue();
          }
      }
      return _allowedValues;
  }


  private RadioButtonFormControl[] getButtons() {
      if (_buttons == null) _buttons = (RadioButtonFormControl[]) _buttonList.toArray( new RadioButtonFormControl[ _buttonList.size() ] );
      return _buttons;
  }
}
