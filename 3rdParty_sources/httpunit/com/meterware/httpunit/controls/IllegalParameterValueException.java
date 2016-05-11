package com.meterware.httpunit.controls;
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

import java.util.List;

import com.meterware.httpunit.HttpUnitUtils;
import com.meterware.httpunit.IllegalRequestParameterException;

//============================= exception class IllegalParameterValueException ======================================


/**
 * This exception is thrown on an attempt to set a parameter to a value not permitted to it by the form.
 **/
public class IllegalParameterValueException extends IllegalRequestParameterException {


  /**
   * construct an IllegalParameterValueException
   * @param parameterName - the name of the parameter
   * @param badValue - the bad value that is not allowed
   * @param allowed - the list of allowed values
   */
  public IllegalParameterValueException( String parameterName, String badValue, String[] allowed ) {
      _parameterName = parameterName;        
      _badValue      = badValue;
      _allowedValues = allowed;
  }
  
  /**
   * get the bad value from a list of Values
   * @param values
   * @return
   */
  protected static String getBadValue(List values) {
  	String result="unknown bad value";
  	if (values.size()>0) {
  		Object badValue=values.get(0);
  		result=badValue.toString(); 
  	}	
  	return result;
  }	

  /**
   * 
   * @param parameterName
   * @param values
   * @param allowed
   */
  public IllegalParameterValueException( String parameterName, List values, String[] allowed ) {
  	this(parameterName,getBadValue(values),allowed);
  }	
    

    public String getMessage() {
        StringBuffer sb = new StringBuffer(HttpUnitUtils.DEFAULT_TEXT_BUFFER_SIZE);
        sb.append( "May not set parameter '" ).append( _parameterName ).append( "' to '" );
        sb.append( _badValue ).append( "'. Value must be one of: { " );
        for (int i = 0; i < _allowedValues.length; i++) {
            if (i != 0) sb.append( ", " );
            sb.append( "'"+ _allowedValues[i] +"'" );
        }
        sb.append( " }" );
        return sb.toString();
    }


    private String   _parameterName;
    private String   _badValue;
    private String[] _allowedValues;
}
