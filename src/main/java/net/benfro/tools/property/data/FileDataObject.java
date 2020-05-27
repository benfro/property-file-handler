package net.benfro.tools.property.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;



public class FileDataObject {
   final ListMultimap<String, String> multimap = ArrayListMultimap.create();

   FileDataObject(PropertiesTable propertiesTable) {
      propertiesTable.columnKeySet().forEach(columnKey -> {
         Map<ClassKeyBean, String> columnValues = propertiesTable.column(columnKey);
         columnValues.keySet().forEach (rowId -> {
            String keyValuePairString = new KeyValueBean(rowId.key, columnValues.get(rowId)).toString();
            //String osClassString = rowId.getClazzForOS();
            if (columnKey == PropertyDatabase.DEFAULT_LOCALE) {
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
}
