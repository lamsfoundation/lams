/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.xslf.draw.geom;

import org.apache.poi.sl.draw.geom.AdjustPointIf;
import org.apache.poi.sl.draw.geom.LineToCommandIf;
import org.apache.poi.util.Beta;
import org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DLineTo;

/**
 * Wrapper / delegate for XmlBeans custom geometry
 */
@Beta
public class XSLFLineTo implements LineToCommandIf {

    private final CTPath2DLineTo lineTo;

    public XSLFLineTo(CTPath2DLineTo lineTo) {
        this.lineTo = lineTo;
    }

    @Override
    public AdjustPointIf getPt() {
        return new XSLFAdjustPoint(lineTo.getPt());
    }

    @Override
    public void setPt(AdjustPointIf pt) {
        CTAdjPoint2D xpt = lineTo.getPt();
        if (xpt == null) {
            xpt = lineTo.addNewPt();
        }
        xpt.setX(pt.getX());
        xpt.setY(pt.getY());
    }
}
