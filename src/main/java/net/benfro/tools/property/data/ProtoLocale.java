package net.benfro.tools.property.data;

import java.util.Locale;



public class ProtoLocale implements Comparable<ProtoLocale> {

   public static ProtoLocale of(String language) {
      return new ProtoLocale(language);
   }

   final Locale locale;

   private ProtoLocale(String language) {
      locale = new Locale(language);
   }

   @Override
   public int compareTo(ProtoLocale other) {
      return getLanguage().compareTo(other.getLanguage());
   }

   String getLanguage() {
      return locale.getLanguage();
   }

   @Override
   public String toString() {
      return locale.getLanguage();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!getClass().equals(o.getClass())) {
         return false;
      }
      ProtoLocale that = (ProtoLocale)o;
      return locale.equals(that.locale);
   }

   @Override
   public int hashCode() {
      return locale.hashCode();
   }
}
