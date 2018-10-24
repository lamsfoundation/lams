package org.jboss.security.acl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.authorization.Resource;

/**
 * <p>
 * Implementation of {@code ACLPersistenceStrategy} that uses the Java Persistence API (JPA) to
 * persist the {@code ACL}s.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class JPAPersistenceStrategy implements ACLPersistenceStrategy
{

   // in memory cache of the created ACLs.
   private final Map<Resource, ACL> aclMap;

   private final EntityManagerFactory managerFactory;

   private final ACLResourceFactory resourceFactory;

   public JPAPersistenceStrategy()
   {
      this(null);
   }

   public JPAPersistenceStrategy(ACLResourceFactory resourceFactory)
   {
      this.aclMap = new HashMap<Resource, ACL>();
      this.managerFactory = Persistence.createEntityManagerFactory("ACL");
      this.resourceFactory = resourceFactory;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLProvider#createACL(org.jboss.security.authorization.Resource)
    */
   public ACL createACL(Resource resource)
   {
      return this.createACL(resource, new ArrayList<ACLEntry>());
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLProvider#createACL(org.jboss.security.authorization.Resource, java.util.Collection)
    */
   public ACL createACL(Resource resource, Collection<ACLEntry> entries)
   {
      if (resource == null)
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("resource");

      // check the cache first.
      ACL acl = this.aclMap.get(resource);
      if (acl == null)
      {
         EntityManager entityManager = this.managerFactory.createEntityManager();
         EntityTransaction transaction = entityManager.getTransaction();
         transaction.begin();
         try
         {
            // create a new ACL and persist it to the database.
            acl = new ACLImpl(resource, entries);
            entityManager.persist(acl);
            // add the newly-created ACL to the cache.
            this.aclMap.put(resource, acl);
            transaction.commit();
         }
         catch (RuntimeException re)
         {
            re.printStackTrace();
            transaction.rollback();
         }
         finally
         {
            entityManager.close();
         }
      }
      return acl;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLProvider#removeACL(org.jboss.security.acl.ACL)
    */
   public boolean removeACL(ACL acl)
   {
      return this.removeACL(acl.getResource());
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLProvider#removeACL(org.jboss.security.authorization.Resource)
    */
   public boolean removeACL(Resource resource)
   {
      boolean result = false;

      EntityManager entityManager = this.managerFactory.createEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();
      try
      {
         // find the ACL associated with the specified resource and remove it from the database.
         ACL acl = this.findACLByResource(resource, entityManager);
         if (acl != null)
         {
            entityManager.remove(acl);
            // remove the ACL from the cache.
            result = this.aclMap.remove(resource) != null;
         }
         transaction.commit();
      }
      catch (RuntimeException re)
      {
         re.printStackTrace();
         transaction.rollback();
      }
      finally
      {
         entityManager.close();
      }
      return result;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLProvider#getACL(org.jboss.security.authorization.Resource)
    */
   public ACL getACL(Resource resource)
   {
      // check the cache first.
      ACL acl = this.aclMap.get(resource);
      if (acl == null)
      {
         EntityManager entityManager = this.managerFactory.createEntityManager();
         try
         {
            acl = this.findACLByResource(resource, entityManager);
            if (acl != null)
               this.aclMap.put(resource, acl);
         }
         finally
         {
            entityManager.close();
         }
      }
      return acl;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLPersistenceStrategy#getACLs()
    */
   @SuppressWarnings("unchecked")
   public Collection<ACL> getACLs()
   {
      Collection<ACL> acls = null;
      EntityManager entityManager = this.managerFactory.createEntityManager();
      try
      {
         acls = entityManager.createQuery("SELECT a FROM ACLImpl a").getResultList();
         if (acls != null && this.resourceFactory != null)
         {
            for (ACL acl : acls)
            {
               ACLImpl impl = (ACLImpl) acl;
               String[] resourceName = impl.getResourceAsString().split(":");
               impl.setResource(this.resourceFactory.instantiateResource(resourceName[0], resourceName[1]));
            }
         }
      }
      finally
      {
         entityManager.close();
      }
      return acls;
   }

   /*
    * (non-Javadoc)
    * @see org.jboss.security.acl.ACLProvider#updateACL(org.jboss.security.acl.ACL)
    */
   public boolean updateACL(ACL acl)
   {
      if (((ACLImpl) acl).getACLId() == 0)
         return false;

      EntityManager entityManager = this.managerFactory.createEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();
      try
      {
         for (ACLEntry entry : acl.getEntries())
         {
            // persist the new entries that might have been added to the ACL.
            ACLEntryImpl entryImpl = (ACLEntryImpl) entry;
            if (entryImpl.getACLEntryId() == 0)
               entityManager.persist(entryImpl);
         }
         // merge will take care of the entries that might have been removed.
         entityManager.merge(acl);
         // update the cache.
         this.aclMap.put(acl.getResource(), acl);
         transaction.commit();
         return true;
      }
      catch (RuntimeException re)
      {
         re.printStackTrace();
         transaction.rollback();
      }
      finally
      {
         entityManager.close();
      }
      return false;
   }

   /**
    * <p>
    * Searches the database for the {@code ACL} associated with the specified resource.
    * </p>
    * 
    * @param resource   the {@code Resource} that is associated with the {@code ACL} being searched.
    * @param entityManager  the {@code EntityManager} used to search the database.
    * @return   the {@code ACL} retrieved from the database, or {@code null} if no {@code ACL} could be found.
    */
   private ACLImpl findACLByResource(Resource resource, EntityManager entityManager)
   {
      ACLImpl acl = null;
      try
      {
         acl = (ACLImpl) entityManager.createQuery(
               "SELECT a FROM ACLImpl a WHERE a.resourceAsString LIKE '" + Util.getResourceAsString(resource) + "'")
               .getSingleResult();
         acl.setResource(resource);
      }
      catch (NoResultException nre)
      {
         // ignore the exception when no ACL could be found for the given resource.
      }
      return acl;
   }
}
