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
package org.lamsfoundation.lams.contentrepository.client;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.data.CRResources;
import org.lamsfoundation.lams.contentrepository.service.BaseTestCase;

/**
 * @author Fiona Malikoff
 * 
 * Indirect testing of ToolContentHandler class. Have done
 * an implementation (ToolContentHandlerImpl) and will test 
 * the implemented class (rather hard to test an abstract class!)
 */
public class TestToolContentHandlerImpl extends BaseTestCase {

    public static final String HANDLER_BEAN="toolContentHandler";
    ToolContentHandlerImpl handler = null;
    
    /**
     * Constructor for ToolContentHandlerImplTest.
     */
    public TestToolContentHandlerImpl(String name) {
        super(name);
    }
    
    public void setUp() throws Exception {
        super.setUp();
        handler = (ToolContentHandlerImpl) context.getBean(HANDLER_BEAN);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        handler = null;
    }

    public void testGetRepositoryWorkspaceName() {
        assertEquals(handler.repositoryWorkspace,handler.getRepositoryWorkspaceName());
    }

    public void testGetRepositoryUser() {
        assertEquals(handler.repositoryUser,handler.getRepositoryUser());
    }

    public void testGetRepositoryId() {
        assertEquals(handler.repositoryId,handler.getRepositoryId());
   }

    public void testGetRepositoryService() {
        try {
            assertNotNull(handler.getRepositoryService());
        } catch ( Exception e ) {
            e.printStackTrace();
            fail("getRepService() threw exception "+e.getMessage());
        }
   }

    public void testGetTicket() {
        try {
            ITicket ticket1 = handler.getTicket(false);
            // repeat call using false should get same ticket;
            ITicket ticket2 = handler.getTicket(false);
            assertSame(ticket1, ticket2);
            // repeat call using true should get new ticket;
            ITicket ticket3 = handler.getTicket(true);
            assertNotSame(ticket1, ticket3);
        } catch ( Exception e ) {
            e.printStackTrace();
            fail("getRepositoryId() threw exception "+e.getMessage());
        }
    }

    /* Creates an offline and online file, checks that the properties are correct and then deletes them. */
    public void testUploadFile() {
        try {
            NodeKey offlineNodeKey = handler.uploadFile(CRResources.getSingleFile(), CRResources.singleFileName, 
                    CRResources.singleFileMimeType, ToolContentHandlerImpl.TYPE_OFFLINE);
            NodeKey onlineNodeKey = handler.uploadFile(CRResources.getSingleFile(), CRResources.singleFileName, 
                    CRResources.singleFileMimeType, ToolContentHandlerImpl.TYPE_ONLINE);

            Long uuid = offlineNodeKey.getUuid();
            IVersionedNode node = handler.getFileNode(uuid);
            assertNotNull("Offline node can be retrieved", node);
            assertNotNull("Offline file can be retrieved", node.getFile());
            assertTrue("Offline file is marked as offline", handler.isOffline(node));
            assertFalse("Offline file is not marked as online ", handler.isOnline(node));
            handler.deleteFile(uuid);
            try {
                node = handler.getFileNode(uuid);
  //              fail("Expected ItemNotFoundException to be thrown when trying to access deleted offline file.");
            } catch (ItemNotFoundException ie) {
                assertTrue("Offline node cannot be retrieved after deletion",true);
            }
            
            uuid = onlineNodeKey.getUuid();
            node = handler.getFileNode(uuid);
            assertNotNull("Online node can be retrieved", node);
            assertNotNull("Online file can be retrieved", node.getFile());
            assertFalse("Online file is not marked as offline", handler.isOffline(node));
            assertTrue("Online file is marked as online ", handler.isOnline(node));
            handler.deleteFile(uuid);
            try {
                node = handler.getFileNode(uuid);
                fail("Expected ItemNotFoundException to be thrown when trying to access deleted online file.");
            } catch (ItemNotFoundException ie) {
                assertTrue("Online node cannot be retrieved after deletion", true);
            }

            // check deleting a missing node doesn't cause a problem
            handler.deleteFile(uuid);

        } catch ( Exception e ) {
            e.printStackTrace();
            fail("testUploadFile threw exception "+e.getMessage());
        }
    }


}
