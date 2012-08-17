/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2000 - 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cache.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.cache.CacheException;
import org.jboss.cache.CacheSPI;
import org.jboss.cache.CacheStatus;
import org.jboss.cache.Lifecycle;
import org.jboss.cache.Version;
import org.jboss.cache.config.Configuration;
import org.jboss.cache.config.ConfigurationException;
import org.jboss.cache.config.RuntimeConfig;
import org.jboss.cache.factories.annotations.DefaultFactoryFor;
import org.jboss.cache.factories.annotations.Destroy;
import org.jboss.cache.factories.annotations.Inject;
import org.jboss.cache.factories.annotations.NonVolatile;
import org.jboss.cache.factories.annotations.Start;
import org.jboss.cache.factories.annotations.Stop;
import org.jboss.cache.util.BeanUtils;
import org.jboss.cache.util.reflect.ReflectionUtil;

import javax.management.MBeanServerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A registry where components which have been created are stored.  Components are stored as singletons, registered under
 * a specific name.
 * <p/>
 * Components can be retrieved from the registry using {@link #getComponent(Class)}.
 * <p/>
 * Components can be registered using {@link #registerComponent(Object, Class)}, which will cause any dependencies to be
 * wired in as well.  Components that need to be created as a result of wiring will be done using {@link #getOrCreateComponent(Class)},
 * which will look up the default factory for the component type (factories annotated with the appropriate {@link DefaultFactoryFor} annotation.
 * <p/>
 * Default factories are treated as components too and will need to be wired before being used.
 * <p/>
 * The registry can exist in one of several states, as defined by the {@link CacheStatus} enumeration.  In terms of the cache,
 * state changes in the following manner:
 * <ul>
 * <li>INSTANTIATED - when first constructed</li>
 * <li>CONSTRUCTED - when created using the DefaultCacheFactory</li>
 * <li>When {@link org.jboss.cache.Cache#create()} is called, the components are rewired.</li>
 * <li>STARTED - when {@link org.jboss.cache.Cache#start()} is called</li>
 * <li>STOPPED - when {@link org.jboss.cache.Cache#stop()} is called</li>
 * <li>DESTROYED - when {@link org.jboss.cache.Cache#destroy()} is called.</li>
 * </ul>
 * <p/>
 * Cache configuration can only be changed and will only be reinjected if the cache is not in the {@link org.jboss.cache.CacheStatus#STARTED} state.
 *
 * @author Manik Surtani (<a href="mailto:manik AT jboss DOT org">manik AT jboss DOT org</a>)
 * @since 2.1.0
 */
@NonVolatile
public class ComponentRegistry implements Lifecycle
{
   /**
    * Contains class definitions of component factories that can be used to construct certain components
    */
   Map<Class, Class<? extends ComponentFactory>> defaultFactories = null;

   private static final Log log = LogFactory.getLog(ComponentRegistry.class);
   private static final boolean trace = log.isTraceEnabled();
   protected static final Object NULL_COMPONENT = new Object();

   // component and method containers
   final Map<String, Component> componentLookup = new HashMap<String, Component>();

   volatile CacheStatus state = CacheStatus.INSTANTIATED;

   /**
    * Hook to shut down the cache when the JVM exits.
    */
   private Thread shutdownHook;
   /**
    * A flag that the shutdown hook sets before calling cache.stop().  Allows stop() to identify if it has been called
    * from a shutdown hook.
    */
   private boolean invokedFromShutdownHook;

   private volatile boolean statusCheckNecessary = true;

   /**
    * Creates an instance of the component registry.  The configuration passed in is automatically registered.
    *
    * @param configuration configuration with which this is created
    */
   public ComponentRegistry(Configuration configuration, CacheSPI cache)
   {
      try
      {
         // bootstrap.
         registerDefaultClassLoader(null);
         registerComponent(this, ComponentRegistry.class);
         registerComponent(configuration, Configuration.class);
         registerComponent(new BootstrapFactory(cache, configuration, this), BootstrapFactory.class);
      }
      catch (Exception e)
      {
         throw new CacheException("Unable to construct ComponentRegistry", e);
      }
   }

   /**
    * Retrieves the state of the registry
    *
    * @return state of the registry
    */
   public CacheStatus getState()
   {
      return state;
   }

   /**
    * Wires an object instance with dependencies annotated with the {@link org.jboss.cache.factories.annotations.Inject} annotation, creating more components
    * as needed based on the Configuration passed in if these additional components don't exist in the
    * {@link ComponentRegistry}.  Strictly for components that don't otherwise live in the registry and have a lifecycle, such as Nodes.
    *
    * @param target object to wire
    * @throws ConfigurationException if there is a problem wiring the instance
    */
   public void wireDependencies(Object target) throws ConfigurationException
   {
      try
      {
         // don't use the reflection cache for wireDependencies calls since these are not managed by the ComponentRegistry
         // and may be invoked at any time, even after the cache starts.
         List<Method> methods = ReflectionUtil.getAllMethods(target.getClass(), Inject.class);

         // search for anything we need to inject
         for (Method method : methods) invokeInjectionMethod(target, method);
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unable to configure component (type: " + target.getClass() + ", instance " + target + ")", e);
      }
   }

   /**
    * Registers the default class loader.  This method *must* be called before any other components are registered,
    * typically called by bootstrap code.  Defensively, it is called in the constructor of ComponentRegistry with a null
    * parameter.
    *
    * @param loader a class loader to use by default.  If this is null, the class loader used to load this instance of ComponentRegistry is used.
    */
   public void registerDefaultClassLoader(ClassLoader loader)
   {
      registerComponent(loader == null ? getClass().getClassLoader() : loader, ClassLoader.class);
      // make sure the class loader is non-volatile, so it survives restarts.
      componentLookup.get(ClassLoader.class.getName()).nonVolatile = true;
   }

   /**
    * This is hard coded for now, since scanning the classpath for factories annotated with {@link org.jboss.cache.factories.annotations.DefaultFactoryFor}
    * does not work with all class loaders.  This is a temporary solution until a more elegant one can be designed.
    * <p/>
    * BE SURE TO ADD ANY NEW FACTORY TYPES ANNOTATED WITH DefaultFactoryFor TO THIS SET!!
    * <p/>
    *
    * @return set of known factory types.
    */
   private Set<Class<? extends ComponentFactory>> getHardcodedFactories()
   {
      Set<Class<? extends ComponentFactory>> s = new HashSet<Class<? extends ComponentFactory>>();
      s.add(BootstrapFactory.class);
      s.add(BuddyManagerFactory.class);
      s.add(EmptyConstructorFactory.class);
      s.add(InterceptorChainFactory.class);
      s.add(RuntimeConfigAwareFactory.class);
      s.add(TransactionManagerFactory.class);
      s.add(ReplicationQueueFactory.class);
      s.add(LockManagerFactory.class);
      s.add(ContextMetaFactory.class);
      s.add(NodeMetaFactory.class);
      s.add(StateTransferManagerFactory.class);
      s.add(StateTransferFactory.class);
      s.add(RegionManagerFactory.class);
      s.add(NodeMetaFactory.class);
      s.add(CommandsMetaFactory.class);
      s.add(TransactionLogFactory.class);
      return s;
   }

   /**
    * Registers a component in the registry under the given type, and injects any dependencies needed.  If a component
    * of this type already exists, it is overwritten.
    *
    * @param component component to register
    * @param type      type of component
    */
   public void registerComponent(Object component, Class type)
   {

      String name = type.getName();
      Component old = componentLookup.get(name);

      if (old != null && old.instance.equals(component))
      {
         if (trace)
         {
            log.trace("Attempting to register a component equal to one that already exists under the same name (" + name + ").  Not doing anything.");
         }
         return;
      }


      Component c;
      if (old != null)
      {
         if (trace) log.trace("Replacing old component " + old + " with new instance " + component);
         old.instance = component;
         old.methodsScanned = false;
         c = old;

         if (state == CacheStatus.STARTED) populateLifecycleMethods();
      }
      else
      {
         c = new Component();
         c.name = name;
         c.instance = component;
         if (trace) log.trace("Registering component " + c + " under name " + name);
         componentLookup.put(name, c);
      }
      c.nonVolatile = component.getClass().isAnnotationPresent(NonVolatile.class);
      addComponentDependencies(c);
      // inject dependencies for this component
      c.injectDependencies();
   }

   /**
    * Adds component dependencies for a given component, by populating {@link Component#injectionMethods}.
    *
    * @param c component to add dependencies to
    */
   protected void addComponentDependencies(Component c)
   {
      Class type = c.instance.getClass();
      List<Method> methods = ReflectionUtil.getAllMethods(type, Inject.class);
      c.injectionMethods.clear();
      c.injectionMethods.addAll(methods);
   }

   @SuppressWarnings("unchecked")
   protected void invokeInjectionMethod(Object o, Method m)
   {
      Class[] dependencies = m.getParameterTypes();
      Object[] params = new Object[dependencies.length];

      for (int i = 0; i < dependencies.length; i++)
      {
         params[i] = getOrCreateComponent(dependencies[i]);
      }

      ReflectionUtil.invokeAccessibly(o, m, params);
   }

   /**
    * Retrieves a component if one exists, and if not, attempts to find a factory capable of constructing the component
    * (factories annotated with the {@link DefaultFactoryFor} annotation that is capable of creating the component class).
    * <p/>
    * If an instance needs to be constructed, dependencies are then automatically wired into the instance, based on methods
    * on the component type annotated with {@link Inject}.
    * <p/>
    * Summing it up, component retrieval happens in the following order:<br />
    * 1.  Look for a component that has already been created and registered.
    * 2.  Look for an appropriate component that exists in the {@link Configuration} that may be injected from an external system.
    * 3.  Look for a class definition passed in to the {@link org.jboss.cache.config.Configuration} - such as an EvictionPolicy implementation
    * 4.  Attempt to create it by looking for an appropriate factory (annotated with {@link DefaultFactoryFor})
    * <p/>
    *
    * @param componentClass type of component to be retrieved.  Should not be null.
    * @return a fully wired component instance, or null if one cannot be found or constructed.
    * @throws ConfigurationException if there is a problem with consructing or wiring the instance.
    */
   protected <T> T getOrCreateComponent(Class<T> componentClass)
   {

      T component = getComponent(componentClass);

      if (component == null)
      {
         // first see if this has been injected externally.
         component = getFromConfiguration(componentClass);
         boolean attemptedFactoryConstruction = false;

         if (component == null)
         {
            // create this component and add it to the registry
            ComponentFactory factory = getFactory(componentClass);
            component = factory.construct(componentClass);
            attemptedFactoryConstruction = true;

         }

         if (component != null)
         {
            registerComponent(component, componentClass);
         }
         else if (attemptedFactoryConstruction)
         {
            if (trace) log.trace("Registering a null for component " + componentClass.getSimpleName());
            registerNullComponent(componentClass);
         }
      }

      return component;
   }

   /**
    * Retrieves a component factory instance capable of constructing components of a specified type.  If the factory doesn't
    * exist in the registry, one is created.
    *
    * @param componentClass type of component to construct
    * @return component factory capable of constructing such components
    */
   protected ComponentFactory getFactory(Class componentClass)
   {
      if (defaultFactories == null) scanDefaultFactories();
      Class<? extends ComponentFactory> cfClass = defaultFactories.get(componentClass);
      if (cfClass == null)
      {
         throw new ConfigurationException("No registered default factory for component " + componentClass + " found!");
      }
      // a component factory is a component too!  See if one has been created and exists in the registry
      ComponentFactory cf = getComponent(cfClass);
      if (cf == null)
      {
         // hasn't yet been created.  Create and put in registry
         cf = instantiateFactory(cfClass);
         if (cf == null)
         {
            throw new ConfigurationException("Unable to locate component factory for component " + componentClass);
         }
         // we simply register this factory.  Registration will take care of constructing any dependencies.
         registerComponent(cf, cfClass);
      }

      // ensure the component factory is in the STARTED state!
      Component c = componentLookup.get(cfClass.getName());
      if (c.instance != cf)
      {
         throw new ConfigurationException("Component factory " + cfClass + " incorrectly registered!");
      }
      return cf;
   }

   /**
    * Scans the class path for classes annotated with {@link org.jboss.cache.factories.annotations.DefaultFactoryFor}, and
    * analyses which components can be created by such factories.
    */
   void scanDefaultFactories()
   {
      defaultFactories = new HashMap<Class, Class<? extends ComponentFactory>>();

      Set<Class<? extends ComponentFactory>> factories = getHardcodedFactories();

      for (Class<? extends ComponentFactory> factory : factories)
      {
         DefaultFactoryFor dFFAnnotation = factory.getAnnotation(DefaultFactoryFor.class);
         for (Class targetClass : dFFAnnotation.classes()) defaultFactories.put(targetClass, factory);
      }
   }

   /**
    * No such thing as a meta factory yet.  Factories are created using this method which attempts to use an empty public
    * constructor.
    *
    * @param factory class of factory to be created
    * @return factory instance
    */
   ComponentFactory instantiateFactory(Class<? extends ComponentFactory> factory)
   {
      try
      {
         return factory.newInstance();
      }
      catch (Exception e)
      {
         // unable to get a hold of an instance!!
         throw new ConfigurationException("Unable to instantiate factory " + factory, e);
      }
   }

   /**
    * registers a special "null" component that has no dependencies.
    *
    * @param type type of component to register as a null
    */
   void registerNullComponent(Class type)
   {
      registerComponent(NULL_COMPONENT, type);
   }

   /**
    * Retrieves a component from the {@link Configuration} or {@link RuntimeConfig}.
    *
    * @param componentClass component type
    * @return component, or null if it cannot be found
    */
   @SuppressWarnings("unchecked")
   protected <T> T getFromConfiguration(Class<T> componentClass)
   {
      if (log.isDebugEnabled())
      {
         log.debug("Looking in configuration for an instance of " + componentClass + " that may have been injected from an external source.");
      }
      Method getter = BeanUtils.getterMethod(Configuration.class, componentClass);
      T returnValue = null;

      if (getter != null)
      {
         try
         {
            returnValue = (T) getter.invoke(getConfiguration());
         }
         catch (Exception e)
         {
            log.warn("Unable to invoke getter " + getter + " on Configuration.class!", e);
         }
      }

      // now try the RuntimeConfig - a legacy "registry" of sorts.
      if (returnValue == null)
      {
         getter = BeanUtils.getterMethod(RuntimeConfig.class, componentClass);
         if (getter != null)
         {
            try
            {
               returnValue = (T) getter.invoke(getConfiguration().getRuntimeConfig());
            }
            catch (Exception e)
            {
               log.warn("Unable to invoke getter " + getter + " on RuntimeConfig.class!", e);
            }
         }
      }
      return returnValue;
   }

   /**
    * Retrieves the configuration component.
    *
    * @return a Configuration object
    */
   protected Configuration getConfiguration()
   {
      // this is assumed to always be present as a part of the bootstrap/construction of a ComponentRegistry.
      return getComponent(Configuration.class);
   }

   /**
    * Retrieves a component of a specified type from the registry, or null if it cannot be found.
    *
    * @param type type to find
    * @return component, or null
    */
   @SuppressWarnings("unchecked")
   public <T> T getComponent(Class<T> type)
   {
      Component wrapper = componentLookup.get(type.getName());
      if (wrapper == null) return null;

      return (T) (wrapper.instance == NULL_COMPONENT ? null : wrapper.instance);
   }

   /**
    * Rewires components.  Can only be called if the current state is WIRED or STARTED.
    */
   public void rewire()
   {
      // need to re-inject everything again.
      for (Component c : new HashSet<Component>(componentLookup.values()))
      {
         // inject dependencies for this component
         c.injectDependencies();
      }
   }

   /**
    * Scans each registered component for lifecycle methods, and adds them to the appropriate lists, and then sorts them
    * by priority.
    */
   private void populateLifecycleMethods()
   {
      for (Component c : componentLookup.values())
      {
         if (!c.methodsScanned)
         {
            c.methodsScanned = true;
            c.startMethods.clear();
            c.stopMethods.clear();
            c.destroyMethods.clear();

            List<Method> methods = ReflectionUtil.getAllMethods(c.instance.getClass(), Start.class);
            for (Method m : methods)
            {
               PrioritizedMethod em = new PrioritizedMethod();
               em.component = c;
               em.method = m;
               em.priority = m.getAnnotation(Start.class).priority();
               c.startMethods.add(em);
            }

            methods = ReflectionUtil.getAllMethods(c.instance.getClass(), Stop.class);
            for (Method m : methods)
            {
               PrioritizedMethod em = new PrioritizedMethod();
               em.component = c;
               em.method = m;
               em.priority = m.getAnnotation(Stop.class).priority();
               c.stopMethods.add(em);
            }

            methods = ReflectionUtil.getAllMethods(c.instance.getClass(), Destroy.class);
            for (Method m : methods)
            {
               PrioritizedMethod em = new PrioritizedMethod();
               em.component = c;
               em.method = m;
               em.priority = m.getAnnotation(Destroy.class).priority();
               c.destroyMethods.add(em);
            }
         }
      }
   }

   /**
    * Removes any components not annotated as @NonVolatile.
    */
   public void resetNonVolatile()
   {
      // destroy all components to clean up resources
      for (Component c : new HashSet<Component>(componentLookup.values()))
      {
         // the component is volatile!!
         if (!c.nonVolatile)
         {
            componentLookup.remove(c.name);
         }
      }

      if (trace) log.trace("Reset volatile components.  Registry now contains " + componentLookup.keySet());
   }

   // ------------------------------ START: Publicly available lifecycle methods -----------------------------
   //   These methods perform a check for appropriate transition and then delegate to similarly named internal methods.

   /**
    * Creates the components needed by a cache instance and sets the cache status to {@link org.jboss.cache.CacheStatus#CREATED}
    * when it is done.
    */
   public void create()
   {
      if (!state.createAllowed())
      {
         if (state.needToDestroyFailedCache())
         {
            destroy();
         }
         else
         {
            return;
         }
      }

      try
      {
         internalCreate();
      }
      catch (Throwable t)
      {
         handleLifecycleTransitionFailure(t);
      }
   }

   /**
    * This starts the components in the cache, connecting to channels, starting service threads, etc.  If the cache is
    * not in the {@link org.jboss.cache.CacheStatus#CREATED} state, {@link #create()} will be invoked first.
    */
   public void start()
   {
      boolean createdInStart = false;
      if (!state.startAllowed())
      {
         if (state.needToDestroyFailedCache())
         {
            destroy(); // this will take us back to DESTROYED
         }

         if (state.needCreateBeforeStart())
         {
            create();
            createdInStart = true;
         }
         else
         {
            return;
         }
      }

      try
      {
         internalStart(createdInStart);
      }
      catch (Throwable t)
      {
         handleLifecycleTransitionFailure(t);
      }
   }

   /**
    * Stops the cache and sets the cache status to {@link org.jboss.cache.CacheStatus#STOPPED} once it is done.  If the cache is not in
    * the {@link org.jboss.cache.CacheStatus#STARTED} state, this is a no-op.
    */
   public void stop()
   {
      if (!state.stopAllowed())
      {
         return;
      }

      // Trying to stop() from FAILED is valid, but may not work
      boolean failed = state == CacheStatus.FAILED;

      try
      {
         internalStop();
      }
      catch (Throwable t)
      {
         if (failed)
         {
            log.warn("Attempted to stop() from FAILED state, but caught exception; try calling destroy()", t);
         }
         failed = true;
         handleLifecycleTransitionFailure(t);
      }
      finally
      {
         if (!failed) state = CacheStatus.STOPPED;
      }
   }

   /**
    * Destroys the cache and frees up any resources.  Sets the cache status to {@link CacheStatus#DESTROYED} when it is done.
    * <p/>
    * If the cache is in {@link org.jboss.cache.CacheStatus#STARTED} when this method is called, it will first call {@link #stop()}
    * to stop the cache.
    */
   public void destroy()
   {
      if (!state.destroyAllowed())
      {
         if (state.needStopBeforeDestroy())
         {
            try
            {
               stop();
            }
            catch (CacheException e)
            {
               log.warn("Needed to call stop() before destroying but stop() threw exception. Proceeding to destroy", e);
            }
         }
         else
         {
            return;
         }
      }

      try
      {
         internalDestroy();
      }
      finally
      {
         // We always progress to destroyed
         state = CacheStatus.DESTROYED;
      }
   }
   // ------------------------------ END: Publicly available lifecycle methods -----------------------------

   // ------------------------------ START: Actual internal lifecycle methods --------------------------------

   /**
    * Sets the cacheStatus to FAILED and rethrows the problem as one
    * of the declared types. Converts any non-RuntimeException Exception
    * to CacheException.
    *
    * @param t throwable thrown during failure
    */
   private void handleLifecycleTransitionFailure(Throwable t)
   {
      state = CacheStatus.FAILED;
      if (t instanceof CacheException)
      {
         throw (CacheException) t;
      }
      else if (t instanceof RuntimeException)
      {
         throw (RuntimeException) t;
      }
      else if (t instanceof Error)
      {
         throw (Error) t;
      }
      else
      {
         throw new CacheException(t);
      }
   }

   /**
    * The actual create implementation.
    */
   private void internalCreate()
   {
      state = CacheStatus.CREATING;
      resetNonVolatile();
      rewire();
      state = CacheStatus.CREATED;
   }

   private void internalStart(boolean createdInStart) throws CacheException, IllegalArgumentException
   {
      if (!createdInStart)
      {
         // re-wire all dependencies in case stuff has changed since the cache was created
         // remove any components whose construction may have depended upon a configuration that may have changed.
         resetNonVolatile();
         rewire();
      }

      state = CacheStatus.STARTING;

      // start all internal components
      // first cache all start, stop and destroy methods.
      populateLifecycleMethods();

      List<PrioritizedMethod> startMethods = new ArrayList<PrioritizedMethod>(componentLookup.size());
      for (Component c : componentLookup.values()) startMethods.addAll(c.startMethods);

      // sort the start methods by priority
      Collections.sort(startMethods);

      // fire all START methods according to priority


      for (PrioritizedMethod em : startMethods) em.invoke();

      addShutdownHook();

      log.info("JBoss Cache version: " + Version.printVersion());
      state = CacheStatus.STARTED;
   }

   private void addShutdownHook()
   {
      ArrayList al = MBeanServerFactory.findMBeanServer(null);
      boolean registerShutdownHook = (getConfiguration().getShutdownHookBehavior() == Configuration.ShutdownHookBehavior.DEFAULT && al.size() == 0)
            || getConfiguration().getShutdownHookBehavior() == Configuration.ShutdownHookBehavior.REGISTER;

      if (registerShutdownHook)
      {
         if (log.isTraceEnabled())
         {
            log.trace("Registering a shutdown hook.  Configured behavior = " + getConfiguration().getShutdownHookBehavior());
         }
         shutdownHook = new Thread()
         {
            @Override
            public void run()
            {
               try
               {
                  invokedFromShutdownHook = true;
                  ComponentRegistry.this.stop();
               }
               finally
               {
                  invokedFromShutdownHook = false;
               }
            }
         };

         Runtime.getRuntime().addShutdownHook(shutdownHook);
      }
      else
      {
         if (log.isTraceEnabled())
         {
            log.trace("Not registering a shutdown hook.  Configured behavior = " + getConfiguration().getShutdownHookBehavior());
         }
      }
   }

   /**
    * Actual stop
    */
   private void internalStop()
   {
      state = CacheStatus.STOPPING;
      // if this is called from a source other than the shutdown hook, deregister the shutdown hook.
      if (!invokedFromShutdownHook && shutdownHook != null) Runtime.getRuntime().removeShutdownHook(shutdownHook);

      List<PrioritizedMethod> stopMethods = new ArrayList<PrioritizedMethod>(componentLookup.size());
      for (Component c : componentLookup.values()) stopMethods.addAll(c.stopMethods);

      Collections.sort(stopMethods);

      // fire all STOP methods according to priority
      for (PrioritizedMethod em : stopMethods) em.invoke();

      state = CacheStatus.STOPPED;
   }

   /**
    * Actual destroy
    */
   private void internalDestroy()
   {

      state = CacheStatus.DESTROYING;

      resetNonVolatile();

      List<PrioritizedMethod> destroyMethods = new ArrayList<PrioritizedMethod>(componentLookup.size());
      for (Component c : componentLookup.values()) destroyMethods.addAll(c.destroyMethods);

      Collections.sort(destroyMethods);

      // fire all DESTROY methods according to priority
      for (PrioritizedMethod em : destroyMethods) em.invoke();

      state = CacheStatus.DESTROYED;
   }

   // ------------------------------ END: Actual internal lifecycle methods --------------------------------

   /**
    * Asserts whether invocations are allowed on the cache or not.  Returns <tt>true</tt> if invocations are to be allowed,
    * <tt>false</tt> otherwise.  If the origin of the call is remote and the cache status is {@link org.jboss.cache.CacheStatus#STARTING},
    * this method will block for up to {@link org.jboss.cache.config.Configuration#getStateRetrievalTimeout()} millis, checking
    * for a valid state.
    *
    * @param originLocal true if the call originates locally (i.e., from the {@link org.jboss.cache.invocation.CacheInvocationDelegate} or false if it originates remotely, i.e., from the {@link org.jboss.cache.marshall.CommandAwareRpcDispatcher}.
    * @return true if invocations are allowed, false otherwise.
    */
   public boolean invocationsAllowed(boolean originLocal)
   {
      if (trace) log.trace("Testing if invocations are allowed.");
      if (state.allowInvocations()) return true;

      // if this is a locally originating call and the cache is not in a valid state, return false.
      if (originLocal) return false;

      if (trace) log.trace("Is remotely originating.");

      // else if this is a remote call and the status is STARTING, wait until the cache starts.
      if (statusCheckNecessary)
      {
         if (state == CacheStatus.STARTING)
         {
            if (trace) log.trace("Cache is starting; block.");
            try
            {
               blockUntilCacheStarts();
               return true;
            }
            catch (InterruptedException e)
            {
               Thread.currentThread().interrupt();
            }
         }
         else
         {
            log.warn("Received a remote call but the cache is not in STARTED state - ignoring call.");
         }
      }

      return false;
   }

   /**
    * Blocks until the current cache instance is in its {@link org.jboss.cache.CacheStatus#STARTED started} phase.  Blocks
    * for up to {@link org.jboss.cache.config.Configuration#getStateRetrievalTimeout()} milliseconds, throwing an IllegalStateException
    * if the cache doesn't reach this state even after this maximum wait time.
    *
    * @throws InterruptedException  if interrupted while waiting
    * @throws IllegalStateException if even after waiting the cache has not started.
    */
   private void blockUntilCacheStarts() throws InterruptedException, IllegalStateException
   {
      int pollFrequencyMS = 100;
      long startupWaitTime = getConfiguration().getStateRetrievalTimeout();
      long giveUpTime = System.currentTimeMillis() + startupWaitTime;

      while (System.currentTimeMillis() < giveUpTime)
      {
         if (state.allowInvocations()) break;
         Thread.sleep(pollFrequencyMS);
      }

      // check if we have started.
      if (!state.allowInvocations())
      {
         throw new IllegalStateException("Cache not in STARTED state, even after waiting " + getConfiguration().getStateRetrievalTimeout() + " millis.");
      }
   }

   /**
    * A wrapper representing a component in the registry
    */
   public class Component
   {
      /**
       * A reference to the object instance for this component.
       */
      Object instance;
      /**
       * The name of the component
       */
      String name;
      boolean methodsScanned;
      /**
       * List of injection methods used to inject dependencies into the component
       */
      List<Method> injectionMethods = new ArrayList<Method>(2);
      List<PrioritizedMethod> startMethods = new ArrayList<PrioritizedMethod>(2);
      List<PrioritizedMethod> stopMethods = new ArrayList<PrioritizedMethod>(2);
      List<PrioritizedMethod> destroyMethods = new ArrayList<PrioritizedMethod>(2);
      /**
       * If true, then this component is not flushed before starting the ComponentRegistry.
       */
      boolean nonVolatile;

      @Override
      public String toString()
      {
         return "Component{" +
               "instance=" + instance +
               ", name=" + name +
               ", nonVolatile=" + nonVolatile +
               '}';
      }

      /**
       * Injects dependencies into this component.
       */
      public void injectDependencies()
      {
         for (Method m : injectionMethods) invokeInjectionMethod(instance, m);
      }

      public Object getInstance()
      {
         return instance;
      }

      public String getName()
      {
         return name;
      }
   }


   /**
    * Wrapper to encapsulate a method along with a priority
    */
   static class PrioritizedMethod implements Comparable<PrioritizedMethod>
   {
      Method method;
      Component component;
      int priority;

      public int compareTo(PrioritizedMethod o)
      {
         return (priority < o.priority ? -1 : (priority == o.priority ? 0 : 1));
      }

      void invoke()
      {
         ReflectionUtil.invokeAccessibly(component.instance, method, null);
      }

      @Override
      public String toString()
      {
         return "PrioritizedMethod{" +
               "method=" + method +
               ", priority=" + priority +
               '}';
      }
   }

   /**
    * Returns an immutable set contating all the components that exists in the reporsitory at this moment.
    */
   public Set<Component> getRegisteredComponents()
   {
      HashSet<Component> defensiveCopy = new HashSet<Component>(componentLookup.values());
      return Collections.unmodifiableSet(defensiveCopy);
   }

   public void setStatusCheckNecessary(boolean statusCheckNecessary)
   {
      this.statusCheckNecessary = statusCheckNecessary;
   }
}
