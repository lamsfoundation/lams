/*
 * Task.java
 *
 * Created on 29 March 2005, 17:07
 */

package org.lamsfoundation.lams.tool.deploy;

/**
 *
 * @author chris
 */
public interface Task
{
    public void execute() throws DeployException;
}
