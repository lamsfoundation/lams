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
package org.lamsfoundation.lams.contentrepository.dao.file;

import org.lamsfoundation.lams.contentrepository.service.BaseTestCase;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.dao.file.FileDAO;


/**
 * @author Fiona Malikoff
 * 
 * Tests just the generation of file names. The actual writing is
 * tested via the save/get file functions on SimpleVersionedNode
 */
public class TestFileDAO extends BaseTestCase {

	Long idEven = null;
	Long idUneven = null;
	Long v1 = null;
	Long v101 = null;
	static FileDAO fileDAO = null;

	public TestFileDAO(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();
		if ( fileDAO == null ) {
			fileDAO = (FileDAO)context.getBean("fileDAO", FileDAO.class);
		}
	}
	
	public void testGenerateFilePathEven1() {
		String paths[] = null;
		try {
			paths = fileDAO.generateFilePath(new Long(140412),new Long(1));
			assertTrue("Directory path ends with \\14\\04\\12 and file path ends with /14/04/12/1",
				paths[0].endsWith("\\14\\04\\12") && paths[1].endsWith("\\14\\04\\12\\1"));
			System.out.println("Directory path is "+paths[0]);
		} catch (FileException e) {
			e.printStackTrace();
			fail("Unexpected exception thrown "+e.getMessage()+" paths "+paths);
		}
	}

	public void testGenerateFilePathEven101() {
		String paths[] = null;
		try {
			paths = fileDAO.generateFilePath(new Long(140412),new Long(101));
			assertTrue("Directory path ends with \\14\\04\\12 and file path ends with \\14\\04\\12\\101",
					paths[0].endsWith("\\14\\04\\12") && paths[1].endsWith("\\14\\04\\12\\101"));
			System.out.println("Directory path is "+paths[0]);
		} catch (FileException e) {
			e.printStackTrace();
			fail("Unexpected exception thrown "+e.getMessage()+" paths "+paths);
		}
	}
	
	public void testGenerateFilePathOdd1() {
		String paths[] = null;
		try {
			paths = fileDAO.generateFilePath(new Long(12345),new Long(1));
			assertTrue("Directory path ends with \\01\\23\\45 and file path ends with \\01\\23\\45\\1",
					paths[0].endsWith("\\01\\23\\45") && paths[1].endsWith("\\01\\23\\45\\1"));
			System.out.println("Directory path is "+paths[0]);
		} catch (FileException e) {
			e.printStackTrace();
			fail("Unexpected exception thrown "+e.getMessage()+" paths "+paths);
		}
	}

	public void testGenerateFilePathOdd101() {
		String paths[] = null;
		try {
			paths = fileDAO.generateFilePath(new Long(12345),new Long(101));
			assertTrue("Directory path ends with \\01\\23\\45 and file path ends with \\01\\23\\45\\101",
					paths[0].endsWith("\\01\\23\\45") && paths[1].endsWith("\\01\\23\\45\\101"));
			System.out.println("Directory path is "+paths[0]);
		} catch (FileException e) {
			e.printStackTrace();
			fail("Unexpected exception thrown "+e.getMessage()+" paths "+paths);
		}

	}
}
