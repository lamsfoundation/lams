/*
 * Created on Jan 10, 2005
 */
package org.lamsfoundation.lams.contentrepository;

import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import org.lamsfoundation.lams.contentrepository.IVersionDetail;
import org.lamsfoundation.lams.contentrepository.SimpleVersionDetail;

import junit.framework.TestCase;

/**
 * Checks creation and ordering of SimpleVersionDetail objects,
 * using the IVersionDetail interface.
 * 
 * Needs to be in the same package as SimpleVersionDetail
 * to access protected constructor.
 * 
 * @author Fiona Malikoff
 */
public class TestSimpleVersionDetail extends TestCase {

	private IVersionDetail detail1a = null;
	private IVersionDetail detail1aSame = null;
	private IVersionDetail detail1b = null;
	private IVersionDetail detail1c = null;
	private IVersionDetail detail2 = null;
	private IVersionDetail detailnull = null;
	
	private static Date KNOWNDATE = new Date(0);
	private static Long V1 = new Long(1);
	private static Long V2 = new Long(1);
	
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		Date dt = new Date(System.currentTimeMillis());
		detail1a = new SimpleVersionDetail(new Long(1), KNOWNDATE, "1");
		detail1aSame = new SimpleVersionDetail(new Long(1), KNOWNDATE, "1");
		detail1b = new SimpleVersionDetail(new Long(1), dt, "2");
		detail1c = new SimpleVersionDetail(new Long(1), dt, "3");
		detail2 = new SimpleVersionDetail(new Long(2), dt, "4");
	}

	public void testGetCreatedDateTime() {
		Date dt = detail1a.getCreatedDateTime();
		assertTrue("Date is as expected. Expected "+KNOWNDATE+" got "+dt, KNOWNDATE.equals(dt));
	}

	public void testGetDescription() {
		String desc = detail1b.getDescription();
		assertTrue("Description is as expected. Expected 2 got "+desc, "2".equals(desc));
	}

	public void testGetVersionId() {
		Long id = detail1c.getVersionId();
		assertTrue("Description is as expected. Expected "+V1+" got "+id, V1.equals(id));
	}

	/*
	 * Class under test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		assertTrue("Detail 1a and detail 1a same are equal as expected ",detail1a.equals(detail1aSame));
		assertTrue("Detail 1a and detail 1b are different as expected ",!detail1a.equals(detail1b));
		assertTrue("Detail 1b and detail 1c are different as expected ",!detail1b.equals(detail1c));
		assertTrue("Detail 1a and detail 2 are different as expected ",!detail1b.equals(detail2));
		assertTrue("Detail 1a and detailnull are different as expected ",!detail1a.equals(null));
	}

	public void testCompareTo() {
		assertTrue("Null object comes at end of sort order (i.e. null is high)", detail2.compareTo(detailnull) < 0);
		assertTrue("Detail 1a earlier in sort order than 1c (i.e. lower)", detail1a.compareTo(detail1c) < 0);
		assertTrue("Detail 1a earlier in sort order than 1c (i.e. lower)", detail1a.compareTo(detail1c) < 0);
		TreeSet sortedSet = new TreeSet();
		sortedSet.add(detail1c);
		sortedSet.add(detail2);
		sortedSet.add(detail1a);
		sortedSet.add(detail1b);
		Iterator iter = sortedSet.iterator();
		int i = 1;
		while ( iter.hasNext() ) {
			IVersionDetail detail = (IVersionDetail) iter.next();
			switch ( i ) {
				case 1: assertTrue("First object is detail1a", detail.equals(detail1a));
						break;
				case 2: assertTrue("First object is detail1b", detail.equals(detail1b));
						break;
				case 3: assertTrue("First object is detail1c", detail.equals(detail1c));
						break;
				case 4: assertTrue("First object is detail2", detail.equals(detail2));
						break;
				default: fail("More objects in set than expected. Object was "+detail);
			}
			i++;
		}
	}

}
