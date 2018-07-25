/*
 *
 *
 * Copyright 2000-2005 The Apache Software Foundation.
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

package org.apache.struts.validator;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.UrlValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

/**
 * <p>
 * This class contains the default validations that are used in the
 * validator-rules.xml file.
 * </p>
 * <p>
 * In general passing in a null or blank will return a null Object or a false
 * boolean. However, nulls and blanks do not result in an error being added to the
 * errors.
 * </p>
 *
 * @since Struts 1.1
 */
public class FieldChecks implements Serializable {

    /**
     *  Commons Logging instance.
     */
    private static final Log log = LogFactory.getLog(FieldChecks.class);

    public static final String FIELD_TEST_NULL = "NULL";
    public static final String FIELD_TEST_NOTNULL = "NOTNULL";
    public static final String FIELD_TEST_EQUAL = "EQUAL";

    /**
     * Checks if the field isn't null and length of the field is greater than zero not
     * including whitespace.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>ActionMessages</code> object to add errors to if
     * any validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *                  other field values.
     * @param request Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateRequired(Object bean,
                                           ValidatorAction va, Field field,
                                           ActionMessages errors,
                                           Validator validator,
                                           HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
            return false;
        } else {
            return true;
        }

    }

    /**
     * Checks if the field isn't null based on the values of other fields.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>ActionMessages</code> object to add errors to if
     * any validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param request Current request object.
     * @return true if meets stated requirements, false otherwise.
     */
    public static boolean validateRequiredIf(Object bean,
                                             ValidatorAction va, Field field,
                                             ActionMessages errors,
                                             Validator validator,
                                             HttpServletRequest request) {

        Object form = validator.getParameterValue(org.apache.commons.validator.Validator.BEAN_PARAM);
        String value = null;
        boolean required = false;

        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        int i = 0;
        String fieldJoin = "AND";
        if (!GenericValidator.isBlankOrNull(field.getVarValue("fieldJoin"))) {
            fieldJoin = field.getVarValue("fieldJoin");
        }

        if (fieldJoin.equalsIgnoreCase("AND")) {
            required = true;
        }

        while (!GenericValidator.isBlankOrNull(field.getVarValue("field[" + i + "]"))) {
            String dependProp = field.getVarValue("field[" + i + "]");
            String dependTest = field.getVarValue("fieldTest[" + i + "]");
            String dependTestValue = field.getVarValue("fieldValue[" + i + "]");
            String dependIndexed = field.getVarValue("fieldIndexed[" + i + "]");

            if (dependIndexed == null) {
                dependIndexed = "false";
            }

            String dependVal = null;
            boolean thisRequired = false;
            if (field.isIndexed() && dependIndexed.equalsIgnoreCase("true")) {
                String key = field.getKey();
                if ((key.indexOf("[") > -1) && (key.indexOf("]") > -1)) {
                    String ind = key.substring(0, key.indexOf(".") + 1);
                    dependProp = ind + dependProp;
                }
            }

            dependVal = ValidatorUtils.getValueAsString(form, dependProp);
            if (dependTest.equals(FIELD_TEST_NULL)) {
                if ((dependVal != null) && (dependVal.length() > 0)) {
                    thisRequired = false;
                } else {
                    thisRequired = true;
                }
            }

            if (dependTest.equals(FIELD_TEST_NOTNULL)) {
                if ((dependVal != null) && (dependVal.length() > 0)) {
                    thisRequired = true;
                } else {
                    thisRequired = false;
                }
            }

            if (dependTest.equals(FIELD_TEST_EQUAL)) {
                thisRequired = dependTestValue.equalsIgnoreCase(dependVal);
            }

            if (fieldJoin.equalsIgnoreCase("AND")) {
                required = required && thisRequired;
            } else {
                required = required || thisRequired;
            }

            i++;
        }

		if (required) {
			if (GenericValidator.isBlankOrNull(value)) {
				errors.add(
					field.getKey(),
					Resources.getActionMessage(validator, request, va, field));

                return false;

			} else {
				return true;
			}
		}
        return true;
    }

    /**
     * Checks if the field matches the regular expression in the field's mask attribute.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors   The <code>ActionMessages</code> object to add errors to if
     * any validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param request Current request object.
     * @return true if field matches mask, false otherwise.
     */
    public static boolean validateMask(Object bean,
                                       ValidatorAction va, Field field,
                                       ActionMessages errors,
                                       Validator validator,
                                       HttpServletRequest request) {

        String mask = field.getVarValue("mask");
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        try {
            if (!GenericValidator.isBlankOrNull(value)
                && !GenericValidator.matchRegexp(value, mask)) {

                errors.add(
                    field.getKey(),
                    Resources.getActionMessage(validator, request, va, field));

                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }


    /**
     * Checks if the field can safely be converted to a byte primitive.
     *
     *@param bean The bean validation is being performed on.
     *@param va The <code>ValidatorAction</code> that is currently being performed.
     *@param field The <code>Field</code> object associated with the current
     *field being validated.
     *@param errors The <code>ActionMessages</code> object to add errors to if
     *any validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     *@param request Current request object.
     *@return true if valid, false otherwise.
     */
    public static Object validateByte(Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request) {

        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatByte(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }


    /**
     * Checks if the field can safely be converted to a short primitive.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>ActionMessages</code> object to add errors to if
     * any validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param request Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateShort(Object bean,
                                      ValidatorAction va, Field field,
                                      ActionMessages errors,
                                      Validator validator,
                                      HttpServletRequest request) {
        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatShort(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }


    /**
     * Checks if the field can safely be converted to an int primitive.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateInteger(Object bean,
                                          ValidatorAction va, Field field,
                                          ActionMessages errors,
                                          Validator validator,
                                          HttpServletRequest request) {
        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatInt(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }


    /**
     * Checks if the field can safely be converted to a long primitive.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateLong(Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request) {
        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatLong(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }


    /**
     * Checks if the field can safely be converted to a float primitive.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateFloat(Object bean,
                                      ValidatorAction va, Field field,
                                      ActionMessages errors,
                                      Validator validator,
                                      HttpServletRequest request) {
        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatFloat(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }


    /**
     *  Checks if the field can safely be converted to a double primitive.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateDouble(Object bean,
                                        ValidatorAction va, Field field,
                                        ActionMessages errors,
                                        Validator validator,
                                        HttpServletRequest request) {
        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatDouble(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }


    /**
     *  Checks if the field is a valid date. If the field has a datePattern variable,
     *  that will be used to format <code>java.text.SimpleDateFormat</code>. If the
     *  field has a datePatternStrict variable, that will be used to format <code>java.text.SimpleDateFormat</code>
     *  and the length will be checked so '2/12/1999' will not pass validation with
     *  the format 'MM/dd/yyyy' because the month isn't two digits. If no datePattern
     *  variable is specified, then the field gets the DateFormat.SHORT format for
     *  the locale. The setLenient method is set to <code>false</code> for all variations.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateDate(Object bean,
                                    ValidatorAction va, Field field,
                                    ActionMessages errors,
                                    Validator validator,
                                    HttpServletRequest request) {

        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }
        String datePattern = field.getVarValue("datePattern");
        String datePatternStrict = field.getVarValue("datePatternStrict");
        Locale locale = RequestUtils.getUserLocale(request, null);

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        try {
            if (datePattern != null && datePattern.length() > 0) {
                result = GenericTypeValidator.formatDate(value, datePattern, false);
            } else if (datePatternStrict != null && datePatternStrict.length() > 0) {
                result = GenericTypeValidator.formatDate(value, datePatternStrict, true);
            } else {
                result = GenericTypeValidator.formatDate(value, locale);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;
    }

    /**
     * Checks if a fields value is within a range (min &amp; max specified in the
     * vars attribute).
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return True if in range, false otherwise.
     */
    public static boolean validateIntRange(Object bean,
                                           ValidatorAction va, Field field,
                                           ActionMessages errors,
                                           Validator validator,
                                           HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                int intValue = Integer.parseInt(value);
                int min = Integer.parseInt(field.getVarValue("min"));
                int max = Integer.parseInt(field.getVarValue("max"));

                if (!GenericValidator.isInRange(intValue, min, max)) {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));

                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }

        return true;
    }

    /**
     *  Checks if a fields value is within a range (min &amp; max specified in the
     *  vars attribute).
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     * other field values.
     * @param  request  Current request object.
     * @return          True if in range, false otherwise.
     */
    public static boolean validateDoubleRange(Object bean,
                                              ValidatorAction va, Field field,
                                              ActionMessages errors,
                                              Validator validator,
                                              HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                double doubleValue = Double.parseDouble(value);
                double min = Double.parseDouble(field.getVarValue("min"));
                double max = Double.parseDouble(field.getVarValue("max"));

                if (!GenericValidator.isInRange(doubleValue, min, max)) {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));

                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }

        return true;
    }

    /**
     *  Checks if a fields value is within a range (min &amp; max specified in the
     *  vars attribute).
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *      other field values.
     * @param  request  Current request object.
     * @return True if in range, false otherwise.
     */
    public static boolean validateFloatRange(Object bean,
                                             ValidatorAction va, Field field,
                                             ActionMessages errors,
                                             Validator validator,
                                             HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                float floatValue = Float.parseFloat(value);
                float min = Float.parseFloat(field.getVarValue("min"));
                float max = Float.parseFloat(field.getVarValue("max"));

                if (!GenericValidator.isInRange(floatValue, min, max)) {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));

                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }

        return true;
    }


    /**
     *  Checks if the field is a valid credit card number.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *      other field values.
     * @param  request  Current request object.
     * @return true if valid, false otherwise.
     */
    public static Object validateCreditCard(Object bean,
                                          ValidatorAction va, Field field,
                                          ActionMessages errors,
                                          Validator validator,
                                          HttpServletRequest request) {

        Object result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return Boolean.TRUE;
        }

        result = GenericTypeValidator.formatCreditCard(value);

        if (result == null) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
        }

        return result == null ? Boolean.FALSE : result;

    }


    /**
     *  Checks if a field has a valid e-mail address.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *      other field values.
     * @param  request  Current request object.
     * @return True if valid, false otherwise.
     */
    public static boolean validateEmail(Object bean,
                                        ValidatorAction va, Field field,
                                        ActionMessages errors,
                                        Validator validator,
                                        HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value) && !GenericValidator.isEmail(value)) {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
            return false;
        } else {
            return true;
        }
    }


    /**
     *  Checks if the field's length is less than or equal to the maximum value.
     *  A <code>Null</code> will be considered an error.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *      other field values.
     * @param  request  Current request object.
     * @return True if stated conditions met.
     */
    public static boolean validateMaxLength(Object bean,
                                            ValidatorAction va, Field field,
                                            ActionMessages errors,
                                            Validator validator,
                                            HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (value != null) {
            try {
                int max = Integer.parseInt(field.getVarValue("maxlength"));

                if (!GenericValidator.maxLength(value, max)) {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));

                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }

        return true;
    }


    /**
     * Checks if the field's length is greater than or equal to the minimum value.
     * A <code>Null</code> will be considered an error.
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *      other field values.
     * @param  request  Current request object.
     * @return True if stated conditions met.
     */
    public static boolean validateMinLength(Object bean,
                                            ValidatorAction va, Field field,
                                            ActionMessages errors,
                                            Validator validator,
                                            HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                int min = Integer.parseInt(field.getVarValue("minlength"));

                if (!GenericValidator.minLength(value, min)) {
                    errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));

                    return false;
                }
            } catch (Exception e) {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a field has a valid url. Four optional variables can be
     * specified to configure url validation.
     * <ul>
     *    <li>Variable <code>allow2slashes</code> can be set to <code>true</code> or
     *        <code>false</code> to control whether two slashes are allowed -
     *        default is <code>false</code> (i.e. two slashes are NOT allowed).</li>
     *    <li>Variable <code>nofragments</code> can be set to <code>true</code> or
     *        <code>false</code> to control whether fragments are allowed -
     *        default is <code>false</code> (i.e. fragments ARE allowed).</li>
     *    <li>Variable <code>allowallschemes</code> can be set to <code>true</code> or
     *        <code>false</code> to control if all schemes are allowed - default
     *        is <code>false</code> (i.e. all schemes are NOT allowed).</li>
     *    <li>Variable <code>schemes</code> can be set to a comma delimited list of
     *        valid schemes. This value is ignored if <code>allowallschemes</code>
     *        is set to <code>true</code>. Default schemes allowed are "http",
     *        "https" and "ftp" if this variable is not specified.</li>
     * </ul>
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *      field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *      validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *      other field values.
     * @param  request  Current request object.
     * @return True if valid, false otherwise.
     */
    public static boolean validateUrl(Object bean,
                                        ValidatorAction va, Field field,
                                        ActionMessages errors,
                                        Validator validator,
                                        HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return true;
        }

        // Get the options and schemes Vars
        boolean allowallschemes = "true".equalsIgnoreCase(field.getVarValue("allowallschemes"));
        int options = allowallschemes ? UrlValidator.ALLOW_ALL_SCHEMES : 0;

        if ("true".equalsIgnoreCase(field.getVarValue("allow2slashes"))) {
          options += UrlValidator.ALLOW_2_SLASHES;
        }

        if ("true".equalsIgnoreCase(field.getVarValue("nofragments"))) {
          options += UrlValidator.NO_FRAGMENTS;
        }

        String schemesVar = allowallschemes ? null : field.getVarValue("schemes");

        // No options or schemes - use GenericValidator as default
        if (options == 0 && schemesVar == null) {
            if (GenericValidator.isUrl(value)) {
                return true;
            } else {
                errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
                return false;
            }
        }

        // Parse comma delimited list of schemes into a String[]
        String[] schemes = null;
        if (schemesVar != null) {

          StringTokenizer st = new StringTokenizer(schemesVar, ",");
          schemes = new String[st.countTokens()];

          int i = 0;
          while (st.hasMoreTokens()) {
              schemes[i++] = st.nextToken().trim();
          }

        }

        // Create UrlValidator and validate with options/schemes
        UrlValidator urlValidator = new UrlValidator(schemes, options);
        if (urlValidator.isValid(value)) {
            return true;
        } else {
            errors.add(field.getKey(), Resources.getActionMessage(validator, request, va, field));
            return false;
        }
    }

    /**
     *  Return <code>true</code> if the specified object is a String or a <code>null</code>
     *  value.
     *
     * @param o Object to be tested
     * @return The string value
     */
    protected static boolean isString(Object o) {
        return (o == null) ? true : String.class.isInstance(o);
    }

}

