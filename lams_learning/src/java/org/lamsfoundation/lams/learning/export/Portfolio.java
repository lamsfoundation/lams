/*
 * Created on 5/12/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
