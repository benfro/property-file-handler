package net.benfro.tools.property.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;



class ClassKeyBeanTest {

   @Test
   void testEquals() {
      assertEquals(ClassKeyBean.of("foo/bar/apa", "banan"), new ClassKeyBean("foo/bar/apa", "banan"));
   }
}