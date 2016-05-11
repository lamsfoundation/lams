/*
 *
 *
 * Copyright 2003-2005 The Apache Software Foundation.
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

package org.apache.struts.validator.validwhen;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.Resources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class contains the validwhen validation that is used in the 
 * validator-rules.xml file.
 *
 * @since Struts 1.2
 */
public class ValidWhen {

    /**
     *  Commons Logging instance.
     */
    private static final Log log = LogFactory.getLog(ValidWhen.class);

    /**
     * Returns true if <code>obj</code> is null or a String.
     */
    private static boolean isString(Object obj) {
        return (obj == null) ? true : String.class.isInstance(obj);
    }

    /**
     * Checks if the field matches the boolean expression specified in 
     * <code>test</code> parameter.
     *
     * @param bean The bean validation is being performed on.
     * 
     * @param va The <code>ValidatorAction</code> that is currently being 
     *      performed.
     * 
     * @param field The <code>Field</code> object associated with the current
     *      field being validated.
     * 
     * @param errors The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * 
     * @param request Current request object.
     * 
     * @return <code>true</code> if meets stated requirements, 
     *      <code>false</code> otherwise.
     */
    public static boolean validateValidWhen(
        Object bean,
        ValidatorAction va,
        Field field,
        ActionMessages errors,
        Validator validator,
        HttpServletRequest request) {
            
        Object form = validator.getParameterValue(Validator.BEAN_PARAM);
        String value = null;
        boolean valid = false;
        int index = -1;
        
        if (field.isIndexed()) {
            String key = field.getKey();

            final int leftBracket = key.indexOf("[");
            final int rightBracket = key.indexOf("]");

            if ((leftBracket > -1) && (rightBracket > -1)) {
                index =
                    Integer.parseInt(key.substring(leftBracket + 1, rightBracket));
            }
        }
        
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }
        
        String test = field.getVarValue("test");
        if (test == null) {
            String msg = "ValidWhen Error 'test' parameter is missing for field ' " + field.getKey() + "'";
            errors.add(field.getKey(), new ActionMessage(msg, false));
            log.error(msg);
            return false;
        }
        
        // Create the Lexer
        ValidWhenLexer lexer= null;
        try {
            lexer = new ValidWhenLexer(new StringReader(test));
        } catch (Exception ex) {
            String msg = "ValidWhenLexer Error for field ' " + field.getKey() + "' - " + ex;
            errors.add(field.getKey(), new ActionMessage(msg + " - " + ex, false));
            log.error(msg);
            log.debug(msg, ex);
            return false;
        }

        // Create the Parser
        ValidWhenParser parser = null;
        try {
            parser = new ValidWhenParser(lexer);
        } catch (Exception ex) {
            String msg = "ValidWhenParser Error for field ' " + field.getKey() + "' - " + ex;
            errors.add(field.getKey(), new ActionMessage(msg, false));
            log.error(msg);
            log.debug(msg, ex);
            return false;
        }


        parser.setForm(form);
        parser.setIndex(index);
        parser.setValue(value);

        try {
            parser.expression();
            valid = parser.getResult();
            
        } catch (Exception ex) {

            // errors.add(
            //    field.getKey(),
            //    Resources.getActionMessage(validator, request, va, field));

            String msg = "ValidWhen Error for field ' " + field.getKey() + "' - " + ex;
            errors.add(field.getKey(), new ActionMessage(msg, false));
            log.error(msg);
            log.debug(msg, ex);
                
            return false;
        }
        
        if (!valid) {
            errors.add(
                field.getKey(),
                Resources.getActionMessage(validator, request, va, field));
                
            return false;
        }
        
        return true;
    }
    
}
