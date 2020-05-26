package net.benfro.tools.property.data;

public class KeyValueBean {

   final String key;
   final String value;

   public KeyValueBean(String key, String value) {
      this.key = key;
      this.value = value;
   }

   @Override
   public String toString() {
      return key + "=" + value;
   }
}
