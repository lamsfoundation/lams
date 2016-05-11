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

/**
 * This interface is so managing classes of the nested tag can identify a tag
 * as a parent tag that other tags retrieve nested properties from.
 * 
 * @see org.apache.struts.taglib.nested.NestedPropertyTag
 * @see org.apache.struts.taglib.nested.logic.NestedIterateTag
 *
 * @since Struts 1.1
 * @version $Rev: 54929 $ $Date$
 */
public interface NestedParentSupport extends NestedNameSupport {
  
  /**
   * This is required by all parent tags so that the child tags can get a hold
   * of their nested property.
   *
   * @return String of the qaulified nested property to this implementing tag
   */
  public String getNestedProperty();
  
}

