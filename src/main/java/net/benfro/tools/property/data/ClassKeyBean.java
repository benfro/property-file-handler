package net.benfro.tools.property.data;

import java.io.File;
import java.util.regex.Pattern;



public class ClassKeyBean implements Comparable<ClassKeyBean> {

   final String clazz;
   final String key;

   static ClassKeyBean of(String clazz, String key) {
      return new ClassKeyBean(clazz, key);
   }

   ClassKeyBean(String clazz, String key) {
      this.clazz = clazz.replaceAll(Pattern.quote(File.separator), "/");
      this.key = key;
   }

   String getClazzForOS() {
      return this.clazz.replace("/", File.separator);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (getClass() != o.getClass()) {
         return false;
      }
      ClassKeyBean that = (ClassKeyBean)o;
      return clazz.equals(that.clazz) && key.equals(that.key);
   }

   @Override
   public int hashCode() {
      int result = 0;
      result = (clazz != null ? clazz.hashCode() : 0);
      result = 31*result + (key != null ? key.hashCode() : 0);
      return result;
   }

   @Override
   public int compareTo(ClassKeyBean other) {
      return clazz.compareTo(other.clazz) + key.compareTo(other.key);
   }

   @Override
   public String toString() {
      return "ClassKeyBean{" +
               "clazz='" + clazz + '\'' +
               ", key='" + key + '\'' +
               '}';
   }
}
