/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.daco.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * Record Form.
 *
 *
 * @author Marcin Cieslak
 */
public class RecordForm {

    private static Logger logger = Logger.getLogger(RecordForm.class.getName());
    private String sessionMapID;

    private List<String> answer;
    private List<MultipartFile> file;
    private int displayedRecordNumber = 1;

    public void setAnswer(int number, String answer) {
	if (this.answer == null) {
	    this.answer = new ArrayList<>();
	}
	while (number >= this.answer.size()) {
	    this.answer.add(null);
	}
	this.answer.set(number, answer);
    }

    public String getAnswer(int number) {
	if (answer == null || number >= answer.size()) {
	    return null;
	}
	return answer.get(number);
    }

    public void setFile(int number, MultipartFile file) {
	if (file.getSize() > 0) {
	    if (this.file == null) {
		this.file = new ArrayList<>();
	    }
	    while (number >= this.file.size()) {
		this.file.add(null);
	    }
	    this.file.set(number, file);
	}
    }

    public MultipartFile getFile(int number) {
	if (file == null || number >= file.size()) {
	    return null;
	}
	return file.get(number);
    }

    public List<MultipartFile> getFile() {
	return file;
    }

    public void setFile(List<MultipartFile> file) {
	this.file = file;
    }

    public void reset(HttpServletRequest request) {
	answer = null;
	file = null;
	displayedRecordNumber = 1;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public int getDisplayedRecordNumber() {
	return displayedRecordNumber;
    }

    public void setDisplayedRecordNumber(int displayedRecordNumber) {
	this.displayedRecordNumber = displayedRecordNumber;
    }

    public int getFileCount() {
	return file.size();
    }

    public List<String> getAnswer() {
	return answer;
    }

    public void setAnswer(List<String> answer) {
	this.answer = answer;
    }
}