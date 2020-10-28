package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertyTable;
import org.apache.commons.lang3.StringUtils;


public class FullLineFilteringQuery implements PropertiesQuery {

   @Override
   public PropertyTable performQuery(PropertyTable propertyTable) {
      PropertyTable output = new PropertyTable();
      propertyTable.cellSet().forEach(it -> {
         boolean result = !it.getValue().isEmpty() && !StringUtils.isAllBlank(it.getValue()) &&
                 !it.getValue().matches("(\\s*)((\\d{1,3}),\\s*(\\d{1,3}),\\s*(\\d{1,3})(\\s*))");
         if (result) {
            output.put(it.getRowKey(), it.getColumnKey(), it.getValue());
         }
      });
      return output;
   }

   @Override
   public String getDescription() {
      return "Filters out empty values and values that are RBG codes";
   }
}
