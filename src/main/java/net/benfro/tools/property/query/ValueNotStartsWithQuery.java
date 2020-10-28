package net.benfro.tools.property.query;

import com.google.common.collect.Sets;
import net.benfro.tools.property.data.PropertyTable;

import java.util.Objects;
import java.util.Set;


public class ValueNotStartsWithQuery implements PropertiesQuery {

   Set<String> valueNotStartsWith = Sets.newHashSet("http://", "https://", "http\\://", "https\\://", "\\${");
   Set<String> keyNotstartsWith = Sets.newHashSet("#", "notranslate");

   @Override
   public PropertyTable performQuery(PropertyTable propertyTable) {
      PropertyTable output = new PropertyTable();
      propertyTable.cellSet().forEach(it -> {
         if (Objects.nonNull(it.getValue()) && Objects.nonNull(it.getRowKey())) {
            boolean valueNotStarts = valueNotStartsWith.stream().noneMatch(item -> it.getValue().startsWith(item));
            boolean keyNotStarts = keyNotstartsWith.stream().noneMatch(item -> it.getRowKey().key.startsWith(item));
            if (valueNotStarts && keyNotStarts) {
               output.put(it.getRowKey(), it.getColumnKey(), it.getValue());
            }
         }
      });
      return output;
   }

   @Override
   public String getDescription() {
      return "Remove entries whose values start or whose keys start in specific ways";
   }

}
