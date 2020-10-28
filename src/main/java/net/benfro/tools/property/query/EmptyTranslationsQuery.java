package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertyTable;



public class EmptyTranslationsQuery implements PropertiesQuery {
   @Override
   public PropertyTable performQuery(PropertyTable propertyTable) {
      return null;
   }

   @Override
   public String getDescription() {
      return null;
   }
}
