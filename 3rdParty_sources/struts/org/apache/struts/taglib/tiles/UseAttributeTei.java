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


import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;


/**
 * Implementation of <code>TagExtraInfo</code> for the <b>UseAttribute</b>
 * tag, identifying the scripting object(s) to be made visible.
 *
 */

public final class UseAttributeTei extends TagExtraInfo {


    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {

      String classname = data.getAttributeString("classname");
      if( classname == null )
        classname = "java.lang.Object";
      String id = data.getAttributeString("id");
      if( id == null )
        id = data.getAttributeString("name");

      return new VariableInfo[] {
      new VariableInfo(id,
                       classname,
                       true,
                       VariableInfo.AT_END)
  };

    }


}
