/*
 * DeployWarTask.java
 *
 * Created on 29 March 2005, 16:57
 */

package org.lamsfoundation.lams.tool.deploy;

/**
 *
 * @author chris
 */
public class DeployWarTask
{

    /**
     * Holds value of property jbossServerLocation.
     */
    private String jbossServerLocation;

    /**
     * Holds value of property lamsEarFile.
     */
    private String lamsEarFile;
    
    /** Creates a new instance of DeployWarTask */
    public DeployWarTask()
    {
    }

    /**
     * Setter for property jbossServerLocation.
     * @param jbossServerLocation New value of property jbossServerLocation.
     */
    public void setJbossServerLocation(String jbossServerLocation)
    {

        this.jbossServerLocation = jbossServerLocation;
    }

    /**
     * Setter for property lamsEarFile.
     * @param lamsEarFile New value of property lamsEarFile.
     */
    public void setLamsEarFile(String lamsEarFile)
    {

        this.lamsEarFile = lamsEarFile;
    }
    
}
