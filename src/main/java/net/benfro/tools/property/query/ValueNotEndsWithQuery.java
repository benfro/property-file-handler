package net.benfro.tools.property.query;

import com.google.common.collect.Lists;
import net.benfro.tools.property.data.PropertyTable;

import java.util.List;
import java.util.Objects;


public class ValueNotEndsWithQuery implements PropertiesQuery {

   private static final List<String> NOT_END_WITH =
           Lists.newArrayList(".jpg", ".JPG", ".png", ".PNG", ".icon", ".htm", ".html");

   @Override
   public PropertyTable performQuery(PropertyTable propertyTable) {
      PropertyTable output = new PropertyTable();
      propertyTable.cellSet().forEach(it -> {
         boolean result = NOT_END_WITH
                 .stream()
                 .filter(item -> Objects.nonNull(it.getValue()))
                 .noneMatch(item -> it.getValue().endsWith(item));
         if (result) {
            output.put(it.getRowKey(), it.getColumnKey(), it.getValue());
         }
      });
      return output;
   }

   @Override
   public String getDescription() {
      return "Filter out depending on ending of property value";
   }

}
