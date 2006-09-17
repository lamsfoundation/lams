/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.testharness.author;

import java.io.File;

import org.lamsfoundation.testharness.AbstractTest;
import org.lamsfoundation.testharness.Call.CallType;

/**
 * @version
 *
 * <p>
 * <a href="AuthorTest.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class AuthorTest extends AbstractTest {
    
    private String learningDesignUploadURL;
    
    private String learningDesignFile;
    
    private String ldId;

    /**
     * AuthorTest Constructor
     * 
     * @param testName
     * @param callType
     * @param rmiRegistryName
     * @param webServiceAddress
     * @param learningDesignUploadURL
     * @param learningDesignFile
     * @param ldId
     */
    public AuthorTest(String testName, CallType callType, String rmiRegistryName, String webServiceAddress, Integer minDelay, Integer maxDelay, String learningDesignUploadURL, String learningDesignFile, String ldId) {
        super(testName,callType,rmiRegistryName,webServiceAddress, minDelay, maxDelay);
        this.learningDesignUploadURL = learningDesignUploadURL;
        this.learningDesignFile = learningDesignFile;
        this.ldId = ldId;
    }

    @Override
    protected void startWEB(){
    	if(ldId==null){
	        MockAuthor author = (MockAuthor) users[0];
	        author.login();
	        File file = new File(learningDesignFile);
	        ldId = author.importLearningDesign(learningDesignUploadURL,file);
    	}
    }

	@Override
	protected void startWS(){
		//TODO implement me
	}

	@Override
	protected void startRMI(){
		//TODO implement me
	}

	public final String getLearningDesignFile() {
		return learningDesignFile;
	}

	public final void setLearningDesignFile(String learningDesignFile) {
		this.learningDesignFile = learningDesignFile;
	}

	public final String getLearningDesignUploadURL() {
		return learningDesignUploadURL;
	}

	public final void setLearningDesignUploadURL(String learningDesignUploadURL) {
		this.learningDesignUploadURL = learningDesignUploadURL;
	}

	public final String getLdId() {
		return ldId;
	}

	public final void setLdId(String ldId) {
		this.ldId = ldId;
	}

}
