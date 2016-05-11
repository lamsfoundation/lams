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


package org.apache.struts.taglib.tiles;

import javax.servlet.jsp.JspException;

  /**
   * Add an element to the surrounding list tag.
   * Same syntax as <code>&lt;put&gt;</code>.
   */
public class AddTag extends PutTag {

  /**
   * default constructor
   */
  public AddTag() {
    super();
  }

    /**
     * Call parent tag which must implement AttributeContainer.
     * @throws JspException If we can't find an appropriate enclosing tag.
     */
  protected void callParent() throws JspException
    {
            // Get enclosing parent
    AddTagParent enclosingParent = findEnclosingPutListTagParent();
    enclosingParent.processNestedTag( this );
    }

    /**
     * Find parent tag which must implement AttributeContainer.
     * @throws JspException If we can't find an appropriate enclosing tag.
     */
  protected AddTagParent findEnclosingPutListTagParent() throws JspException {
    try
      {
      AddTagParent parent = (AddTagParent)findAncestorWithClass(this,AddTagParent.class);
      if( parent == null )
        {
        throw new JspException( "Error - tag add : enclosing tag doesn't accept 'add' tag." );
        }
      return parent;
      }
     catch( ClassCastException ex )
      {
      throw new JspException( "Error - tag add : enclosing tag doesn't accept 'add' tag." );
      }
  }
}
