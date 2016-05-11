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
package org.apache.struts.taglib.nested;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.struts.taglib.TagUtils;

/**
 * NestedPropertyTag.
 *
 * The one of only two additions in this nested suite of tags. This is so that
 * you can specify extra levels of nesting in one elegant tag rather than having
 * to propagate and manage an extra dot notated property in nested child tags.
 *
 * It's simply recognised by the helper class and it's property is added to the
 * nesting list.
 *
 * @since Struts 1.1
 * @version $Rev: 54929 $ $Date$
 */
public class NestedPropertyTag extends BodyTagSupport implements NestedNameSupport {

  public String getName() { return null; }
  public void setName(String newNamed) {}

  /** Getter method for the <i>property</i> property
   * @return String value of the property property
   */
  public String getProperty() {
    return this.property;
  }

  /** Setter method for the <i>property</i> property
   * Also, only setting the original property value to those values not
   * set by the nested logic.
   * @param newProperty new value for the property property
   */
  public void setProperty(String newProperty) {
    property = newProperty;
  }

  /**
   * Overriding method of the heart of the tag. Gets the relative property
   * and tells the JSP engine to evaluate its body content.
   *
   * @return int JSP continuation directive.
   */
  public int doStartTag() throws JspException {
    originalProperty = property;

    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    originalNest = NestedPropertyHelper.getCurrentProperty(request);
    originalName = NestedPropertyHelper.getCurrentName(request, this);

    String nested = NestedPropertyHelper.getAdjustedProperty(request, originalProperty);
    NestedPropertyHelper.setProperty(request, nested);
    NestedPropertyHelper.setName(request, originalName);

    // run the body part
    return (EVAL_BODY_TAG);
  }


  /**
   * Render the resulting content evaluation.
   *
   * @return int JSP continuation directive.
   */
  public int doAfterBody() throws JspException {

    /* Render the output */
    if (bodyContent != null) {
      TagUtils.getInstance().writePrevious(pageContext, bodyContent.getString());
      bodyContent.clearBody();
    }

    return (SKIP_BODY);
  }


  /**
   * Evaluate the rest of the page
   *
   * @return int JSP continuation directive.
   */
  public int doEndTag() throws JspException {
    /* set the reference back */
    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    if (originalNest == null && originalName == null) {
      NestedPropertyHelper.deleteReference(request);
    } else {
      NestedPropertyHelper.setName(request, originalName);
      NestedPropertyHelper.setProperty(request, originalNest);
    }
    property = originalProperty;
    return (EVAL_PAGE);
  }


  /**
   * JSP method to release all resources held by the tag.
   */
  public void release() {
    super.release();
    this.property = null;
    this.originalNest = null;
    this.originalName = null;
    this.originalProperty = null;
  }

  /* the usual private member variable */
  private String property = null;
  private String originalNest = null;
  private String originalName = null;
  private String originalProperty = null;
}
