/*
 *
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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

package org.apache.struts.taglib.html;

/**
 * Renders an HTML BUTTON tag within the Struts framework.
 *
 * @version $Rev: 54954 $ $Date$
 */
public class ButtonTag extends SubmitTag {

    /**
     * Render the opening element.
     *
     * @return The opening part of the element.
     */
    protected String getElementOpen() {
        return "<input type=\"button\"";
    }

    /**
     * Return the default value.
     *
     * @return The default value if none supplied.
     */
    protected String getDefaultValue() {
        return "Click";
    }


}
