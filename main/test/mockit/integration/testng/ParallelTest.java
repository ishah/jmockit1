/*
 * Copyright (c) 2006-2014 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Ignore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import mockit.Expectations;

// Just to make sure no NPEs or other exceptions occur from JMockit-TestNG integration.
@Ignore
public final class ParallelTest
{
   final AtomicInteger counter = new AtomicInteger();

   static class Collaborator
   {
      final int value;

      Collaborator() { value = -1; }
      Collaborator(int value) { this.value = value; }

      int getValue() { return value; }
      final boolean simpleOperation(int a, String b, Date c) { return true; }
      static void doSomething(boolean b, String s) { throw new IllegalStateException(); }
   }

   @Test(threadPoolSize = 4, invocationCount = 10)
   public void parallelExecution()
   {
      counter.incrementAndGet();
   }

   @Test(threadPoolSize = 10, invocationCount = 10)
   public void parallelExpectations() {
       final Collaborator collaborator = new Collaborator(2);

       new Expectations(collaborator) {{
          collaborator.getValue(); result = 123;
          collaborator.simpleOperation(1, "", null); result = false;
       }};

       // Mocked:
       assertEquals(123, collaborator.getValue());
       assertFalse(collaborator.simpleOperation(1, "", null));

       // Not mocked:
       assertEquals(2, collaborator.value);
       assertEquals(45, new Collaborator(45).getValue());
       assertEquals(-1, new Collaborator().getValue());
   }

   @AfterClass
   public void checkCounter()
   {
      assertEquals(counter.get(), 10);
   }
}