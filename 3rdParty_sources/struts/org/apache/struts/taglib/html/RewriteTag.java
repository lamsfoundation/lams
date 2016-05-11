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

import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;

/**
 * Generate a URL-encoded URI as a string.
 *
 * @version $Rev: 54929 $ $Date$
 */
public class RewriteTag extends LinkTag {


    // --------------------------------------------------------- Public Methods


    /**
     * Render the appropriately encoded URI.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Generate the hyperlink URL
        Map params = TagUtils.getInstance().computeParameters
            (pageContext, paramId, paramName, paramProperty, paramScope,
             name, property, scope, transaction);
             
        String url = null;
        try {
            // Note that we're encoding the & character to &amp; in XHTML mode only, 
            // otherwise the & is written as is to work in javascripts. 
			url =
				TagUtils.getInstance().computeURLWithCharEncoding(
					pageContext,
					forward,
					href,
					page,
					action,
					module,
					params,
					anchor,
					false,
                    this.isXhtml(),
                    useLocalEncoding);
                    
        } catch (MalformedURLException e) {
            TagUtils.getInstance().saveException(pageContext, e);
            throw new JspException
                (messages.getMessage("rewrite.url", e.toString()));
        }

        TagUtils.getInstance().write(pageContext, url);

        return (SKIP_BODY);

    }



    /**
     * Ignore the end of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        return (EVAL_PAGE);

    }


}
