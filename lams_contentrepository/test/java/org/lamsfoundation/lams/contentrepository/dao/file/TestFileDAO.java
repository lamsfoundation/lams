/*
 * Created on Jan 10, 2005
 */
package org.lamsfoundation.lams.contentrepository.dao.file;

import org.lamsfoundation.lams.contentrepository.BaseTestCase;
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

	public void setUp() {
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
