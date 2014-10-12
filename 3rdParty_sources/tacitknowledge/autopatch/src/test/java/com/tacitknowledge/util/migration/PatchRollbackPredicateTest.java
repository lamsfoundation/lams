/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration;

import junit.framework.TestCase;

import org.apache.commons.collections.Predicate;

import com.tacitknowledge.util.migration.tasks.rollback.TestRollbackableTask1;

/**
 * this test cases test the PatchRollbackPredicate
 * 
 * @author Artie Pesh-Imam (apeshimam@tacitknowledge.com)
 */
public class PatchRollbackPredicateTest extends TestCase {

	public void testPredicateReturnsInRange() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,1);
		TestRollbackableTask1 task = new TestRollbackableTask1(4);
		
		boolean result = patchRollbackPredicate.evaluate(task);
		assertTrue("PatchRollbackPredicate returned false unexpectedly",result);
	}
	
	public void testPredicateReturnsLessThanRollback() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,2);
		TestRollbackableTask1 task = new TestRollbackableTask1(1);
		
		boolean result = patchRollbackPredicate.evaluate(task);
		assertFalse("PatchRollbackPredicate returned true unexpectedly",result);
	}
	
	public void testPredicateReturnsGreaterThanCurrent() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,2);
		TestRollbackableTask1 task = new TestRollbackableTask1(11);
		
		boolean result = patchRollbackPredicate.evaluate(task);
		assertFalse("PatchRollbackPredicate returned true unexpectedly",result);
	}
	
	public void testPredicateReturnsLowerBoundary() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,2);
		TestRollbackableTask1 task = new TestRollbackableTask1(2);
		
		boolean result = patchRollbackPredicate.evaluate(task);
		assertFalse("PatchRollbackPredicate returned true unexpectedly",result);
	}
	public void testPredicateReturnsUpperBoundary() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,2);
		TestRollbackableTask1 task = new TestRollbackableTask1(10);
		
		boolean result = patchRollbackPredicate.evaluate(task);
		assertTrue("PatchRollbackPredicate returned false unexpectedly",result);
	}
	public void testPatchRollbackPredicateNull() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,2);
		
		boolean result = patchRollbackPredicate.evaluate(null);
		assertFalse("PatchRollbackPredicate returned true unexpectedly",result);
	}
	
	public void testPatchRollbackPredicateWrongClass() {
		
		//create a new predicate
		Predicate patchRollbackPredicate = new PatchRollbackPredicate(10,2);
		
		boolean result = patchRollbackPredicate.evaluate(new Object());
		assertFalse("testPatchRollbackPredicateWrongClass returned true unexpectedly",result);
	}
}
