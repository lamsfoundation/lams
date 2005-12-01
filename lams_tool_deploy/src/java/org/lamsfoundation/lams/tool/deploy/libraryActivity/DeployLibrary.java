/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/

/*
 * Created on 17/11/2005
 */
package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
/**
 * @author mtruong
 *
 * Deployer for library activities that contain more than one tool activity.
 * Currently, is only supported for the complex activity types: parallel,
 * options, sequence
 * 
 * The main logic is adopted from Deploy.java
 * See org.lamsfoundation.lams.tool.deploy.Deploy
 */
public class DeployLibrary {

    public DeployLibrary() {
        
    }
 
    public static void main (String[] args)
    {
        if ((args.length < 1) || (args[0] == null))
        {
            throw new IllegalArgumentException("Usage: Deployer <properties_file_path>." );
        }
        
        System.out.println("Starting Library Deploy");
        
       try
       {
            DeployLibraryConfig config= new DeployLibraryConfig(args[0]);
            LibraryDBDeployTask deployTask = new LibraryDBDeployTask(config);
            deployTask.execute();   
            System.out.println("Library Deployed");
            
       }
       catch (Exception ex)
       {
           System.out.println("TOOL DEPLOY FAILED");
           ex.printStackTrace();
       }
       
        
        
    }
    
}
