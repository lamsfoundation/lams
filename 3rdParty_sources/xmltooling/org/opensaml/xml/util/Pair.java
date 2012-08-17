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

package org.opensaml.xml.util;

/**
 * Container for a pair of objects.
 * 
 * @param <T1> type of the first object in the pair
 * @param <T2> type of the second object in the pair
 */
public class Pair<T1, T2> {

    /** First object in pair. */
    private T1 first;

    /** Second object in pair. */
    private T2 second;

    /**
     * Constructor.
     * 
     * @param newFirst first object in the pair
     * @param newSecond second object in the pair
     */
    public Pair(T1 newFirst, T2 newSecond) {
        first = newFirst;
        second = newSecond;
    }

    /**
     * Gets the first object in the pair.
     * 
     * @return first object in the pair
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * Sets the first object in the pair.
     * 
     * @param newFirst first object in the pair
     */
    public void setFirst(T1 newFirst) {
        first = newFirst;
    }

    /**
     * Gets the second object in the pair.
     * 
     * @return second object in the pair
     */
    public T2 getSecond() {
        return second;
    }

    /**
     * Sets the second object in the pair.
     * 
     * @param newSecond second object in the pair
     */
    public void setSecond(T2 newSecond) {
        second = newSecond;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if(o == this){
            return true;
        }
        
        if (o instanceof Pair) {
            Pair<T1, T2> otherPair = (Pair<T1, T2>) o;
            return DatatypeHelper.safeEquals(getFirst(), otherPair.getFirst())
                    && DatatypeHelper.safeEquals(getSecond(), otherPair.getSecond());
        }

        return false;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        int result = 17;
        if (first != null) {
            result = 37 * result + first.hashCode();
        }
        if (second != null) {
            result = 37 * result + second.hashCode();
        }
        return result;
    }

    /** {@inheritDoc} */
    public String toString() {
        return "(" + getFirst() + "," + getSecond() + ")";
    }
}