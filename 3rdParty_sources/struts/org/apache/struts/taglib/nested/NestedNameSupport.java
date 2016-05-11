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
 * This is so that managing classes can tell if a nested tag needs to have its
 * <i>name</i> property set. From what I know, these tags use the property 
 * property, and the name is an addition.
 *
 * @since Struts 1.1
 * @version $Rev: 54929 $ $Date$
 */
public interface NestedNameSupport extends NestedPropertySupport {
  
  /**
   * The getters and setters required to set a tags <i>name</i> property.
   * @return String value of the tags' name property
   */
  public String getName();
  
  /**
   * The setter for the <i>name</i> property
   * @param newNamed new String value to set the name property to
   */
  public void setName(String newNamed);
  
}
