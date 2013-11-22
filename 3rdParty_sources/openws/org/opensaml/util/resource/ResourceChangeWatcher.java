/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.util.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.opensaml.util.resource.ResourceChangeListener.ResourceChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A watcher that invokes a callback when a resource update/deletion has been detected.
 */
public class ResourceChangeWatcher extends TimerTask {

    /** Default polling frequency, 12 hours. */
    public static final long DEFAULT_POLL_FREQUENCY = 1000 * 60 * 60 * 12;

    /** Default maximum retry attempts, 0. */
    public static final int DEFAULT_MAX_RETRY_ATTEMPTS = 0;

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(ResourceChangeWatcher.class);

    /** Resource being watched. */
    private Resource watchedResource;

    /** Frequency, in milliseconds, the resource is polled for changes. */
    private long pollFrequency;

    /** Max number of polls to try before considering the resource inaccessible. */
    private int maxRetryAttempts;

    /** Number of times the resource has been polled but generated an error. */
    private int currentRetryAttempts;

    /** Whether the resource currently exists. */
    private boolean resourceExist;

    /** Last time the resource was modified. */
    private DateTime lastModification;

    /** Registered listeners of resource change notifications. */
    private List<ResourceChangeListener> resourceListeners;

    /**
     * Constructor.
     * 
     * @param resource the resource to be watched
     * 
     * @throws ResourceException thrown if resource existence or last modification time can not be determined
     */
    public ResourceChangeWatcher(Resource resource) throws ResourceException {
        this(resource, DEFAULT_POLL_FREQUENCY, DEFAULT_MAX_RETRY_ATTEMPTS);
    }

    /**
     * Constructor.
     * 
     * @param resource the resource to be watched
     * @param pollingFrequency the frequency, in milliseconds, to poll the resource for changes
     * 
     * @throws ResourceException thrown if resource existence or last modification time can not be determined
     */
    public ResourceChangeWatcher(Resource resource, long pollingFrequency) throws ResourceException {
        this(resource, pollingFrequency, DEFAULT_MAX_RETRY_ATTEMPTS);
    }

    /**
     * Constructor.
     * 
     * @param resource the resource to be watched
     * @param pollingFrequency the frequency, in milliseconds, to poll the resource for changes
     * @param retryAttempts maximum number of poll attempts before the resource is considered inaccessible
     * 
     * @throws ResourceException thrown if resource existence or last modification time can not be determined
     */
    public ResourceChangeWatcher(Resource resource, long pollingFrequency, int retryAttempts) throws ResourceException {
        if (resource == null) {
            throw new NullPointerException("Watched resource is null");
        }

        if (pollingFrequency <= 0) {
            throw new IllegalArgumentException("Polling frequency must be greater than zero");
        }

        if (retryAttempts < 0) {
            throw new IllegalArgumentException("Max retry attempts must be greater than, or equal to, zero");
        }

        watchedResource = resource;
        pollFrequency = pollingFrequency;
        maxRetryAttempts = retryAttempts;
        currentRetryAttempts = 0;

        resourceListeners = new ArrayList<ResourceChangeListener>(5);
        log.debug("Watching resource: " + watchedResource.getLocation()
                + ", polling frequency: {}ms, max retry attempts: {}", pollFrequency, maxRetryAttempts);
        
        try {
            if (watchedResource.exists()) {
                resourceExist = true;
                lastModification = watchedResource.getLastModifiedTime();
            } else {
                resourceExist = false;
            }
        } catch (ResourceException e) {
            log.warn("Resource " + watchedResource.getLocation() + " could not be accessed", e);
            currentRetryAttempts++;
            if (currentRetryAttempts >= maxRetryAttempts) {
                log.error("Resource {} was not accessible at time of ResourceChangeWatcher construction and max retrys are exceeded",
                        watchedResource.getLocation());
                throw e;
            }
        }

    }

    /**
     * Gets the frequency, in milliseconds, the watched resource should be polled.
     * 
     * @return frequency the watched resource should be polled
     */
    public long getPollingFrequency() {
        return pollFrequency;
    }

    /**
     * Gets the list of registered resource listeners. New listeners may be registered with the list or old ones
     * removed.
     * 
     * @return list of registered resource listeners
     */
    public List<ResourceChangeListener> getResourceListeners() {
        return resourceListeners;
    }

    /** {@inheritDoc} */
    public void run() {
        try {
            log.trace("Checking resource for changes: {}", watchedResource.getLocation());
            if (watchedResource.exists()) {
                if (!resourceExist) {
                    resourceExist = true;
                    signalListeners(ResourceChange.CREATION);
                    lastModification = watchedResource.getLastModifiedTime();
                } else {
                    if (lastModification.isBefore(watchedResource.getLastModifiedTime())) {
                        signalListeners(ResourceChange.UPDATE);
                        lastModification = watchedResource.getLastModifiedTime();
                    }
                }
            } else {
                if (resourceExist) {
                    resourceExist = false;
                    signalListeners(ResourceChange.DELETE);
                }
            }
            currentRetryAttempts = 0;
        } catch (ResourceException e) {
            log.warn("Resource " + watchedResource.getLocation() + " could not be accessed", e);
            currentRetryAttempts++;
            if (currentRetryAttempts >= maxRetryAttempts) {
                cancel();
                log.error("Resource {} was not accessible for max number of retry attempts.  This resource will no longer be watched",
                        watchedResource.getLocation());
            }
        }
    }

    /**
     * Signals all registered listeners of a resource change.
     * 
     * @param changeType the resource change type
     */
    protected void signalListeners(ResourceChange changeType) {
        synchronized (resourceListeners) {
            switch (changeType) {
                case CREATION:
                    log.debug("Publishing creation event for resource: {}", watchedResource.getLocation());
                    for (ResourceChangeListener listener : resourceListeners) {
                        listener.onResourceCreate(watchedResource);
                    }
                    break;
                case UPDATE:
                    log.debug("Publishing update event for resource: {}", watchedResource.getLocation());
                    for (ResourceChangeListener listener : resourceListeners) {
                        listener.onResourceUpdate(watchedResource);
                    }
                    break;
                case DELETE:
                    log.debug("Publishing delete event for resource: {}", watchedResource.getLocation());
                    for (ResourceChangeListener listener : resourceListeners) {
                        listener.onResourceDelete(watchedResource);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}