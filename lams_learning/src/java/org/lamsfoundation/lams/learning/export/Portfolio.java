/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.export;


/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Portfolio {
    
    private String exportTmpDir;
    private ToolPortfolio[] toolPortfolios;
    
    public Portfolio()
    {
        this.exportTmpDir = null;
        this.toolPortfolios = null;
    }

    /**
     * @return Returns the exportTmpDir.
     */
    public String getExportTmpDir() {
        return exportTmpDir;
    }
    /**
     * @param exportTmpDir The exportTmpDir to set.
     */
    public void setExportTmpDir(String exportTmpDir) {
        this.exportTmpDir = exportTmpDir;
    }
    /**
     * @return Returns the toolPortfolios.
     */
    public ToolPortfolio[] getToolPortfolios() {
        return toolPortfolios;
    }
    /**
     * @param toolPortfolios The toolPortfolios to set.
     */
    public void setToolPortfolios(ToolPortfolio[] toolPortfolios) {
        this.toolPortfolios = toolPortfolios;
    }
}
