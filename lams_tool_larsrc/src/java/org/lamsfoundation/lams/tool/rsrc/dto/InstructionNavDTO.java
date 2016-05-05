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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.rsrc.dto;

import java.util.List;

import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;

/**
 *
 * @author Steve.Ni
 * @version $Revision$
 */
public class InstructionNavDTO {

    private String title;
    private int total;
    private int current;
    private ResourceItemInstruction instruction;
    private List allInstructions;
    private short type;

    public int getCurrent() {
	return current;
    }

    public void setCurrent(int current) {
	this.current = current;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public int getTotal() {
	return total;
    }

    public void setTotal(int total) {
	this.total = total;
    }

    public void setType(short type) {
	this.type = type;
    }

    public short getType() {
	return type;
    }

    public ResourceItemInstruction getInstruction() {
	return instruction;
    }

    public void setInstruction(ResourceItemInstruction instruction) {
	this.instruction = instruction;
    }

    public List getAllInstructions() {
	return allInstructions;
    }

    public void setAllInstructions(List allInstructions) {
	this.allInstructions = allInstructions;
    }

}
