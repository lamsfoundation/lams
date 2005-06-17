package org.lamsfoundation.lams.tool.forum.persistence;

import org.lamsfoundation.lams.tool.forum.core.FactoryException;
import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;

import java.util.List;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 6/06/2005
 * Time: 13:46:53
 * To change this template use File | Settings | File Templates.
 */
public class GenericEntityTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAndDeleteGenericEntity() throws FactoryException {
        GenericEntity entity = new GenericEntity();
        entity.setCreatedBy(new Long("1002"));
        entity.setModifiedBy(new Long("1004"));

        //save
        GenericEntityDao dao = (GenericEntityDao) GenericObjectFactoryImpl.getInstance().lookup(GenericEntityDao.class);
        dao.saveOrUpdate(entity);
        assertNotNull(entity.getId());
        assertNotNull("date created is null", entity.getCreated());
        assertNotNull("date updated is null", entity.getUpdated());
        assertEquals("date created and updated are different for first save", entity.getCreated(), entity.getUpdated());

        //load
        GenericEntity reloaded = (GenericEntity) dao.getById(entity.getId());
        assertEquals("reloaded object not equal", entity, reloaded);
        assertEquals("date difference in database and memory", entity.getCreated().getTime()/1000, reloaded.getCreated().getTime()/1000);
        assertEquals("date difference in database and memory", entity.getUpdated().getTime()/1000, reloaded.getUpdated().getTime()/1000);
        assertEquals("createdBy difference in database and memory", entity.getCreatedBy(), reloaded.getCreatedBy());
        assertEquals("createdBy difference in database and memory", entity.getCreatedBy(), reloaded.getCreatedBy());

        //find
        List values = dao.findByNamedQuery("all");
        assertTrue("find all result not containing object", values.contains(entity));

        //delete
        dao.delete(reloaded);
        assertNull("object not deleted", dao.getById(entity.getId()) );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
