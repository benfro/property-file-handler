package net.benfro.tools.property.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;



class ClassKeyBeanTest {

   @Test
   void testEquals() {
      assertEquals(ClassKeyBean.of("foo/bar/apa", "banan"), ClassKeyBean.of("foo/bar/apa", "banan"));
   }
}
