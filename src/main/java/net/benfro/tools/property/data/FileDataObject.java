package net.benfro.tools.property.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class FileDataObject {


   final ListMultimap<String, String> multimap = ArrayListMultimap.create();

   FileDataObject(PropertyTable propertyTable) {
      propertyTable.columnKeySet().forEach(columnKey -> {
         Map<ClassKeyBean, String> columnValues = propertyTable.column(columnKey);
         columnValues.keySet().forEach (rowId -> {
            String keyValuePairString = new KeyValueBean(rowId.key, columnValues.get(rowId)).toString();
            //String osClassString = rowId.getClazzForOS();
            if (columnKey == LocaleRegistry.INSTANCE.getDefault()) {
               multimap.put(String.format("%s.properties", rowId.getClazzForOS()), keyValuePairString);
            } else {
               //String language = columnKey.getLanguage()
               multimap.put(String.format("%s_%s.properties",rowId.getClazzForOS(),columnKey.getLanguage()),  keyValuePairString);
            }
         });
      });
   }

   List<String> get(String key) {
      return multimap.get(key);
   }

   int size() {
      return multimap.size();
   }

   Set<String> keySet() {
      return multimap.keySet();
   }

   Map<String, Collection<String>> asMap() {
      return multimap.asMap();
   }

   public static class KeyValueBean {

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
}
