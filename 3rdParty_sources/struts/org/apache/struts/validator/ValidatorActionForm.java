/*
 *
 *
 * Copyright 2000-2004 The Apache Software Foundation.
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
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;


/**
 * <p>This class extends <strong>ValidatorForm</strong> and provides
 * basic field validation based on an XML file.  The key passed into the
 * validator is the action element's 'path' attribute from the
 * struts-config.xml which should match the form element's name attribute
 * in the validation.xml.</p>
 *
 * <ul><li>See <code>ValidatorPlugin</code> definition in struts-config.xml
 * for validation rules.</li></ul>
 *
 * @version $Rev: 54929 $ $Date$
 * @since Struts 1.1
 */

public class ValidatorActionForm extends ValidatorForm implements Serializable {

    /**
     * Returns the Validation key.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return validation key - the action element's 'path' attribute in this case
     */
    public String getValidationKey(ActionMapping mapping,
                                   HttpServletRequest request) {

        return mapping.getPath();
    }

}
