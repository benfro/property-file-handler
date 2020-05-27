package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertiesTable;



public class EmptyTranslationsQuery implements PropertiesQuery {
   @Override
   public PropertiesTable performQuery(PropertiesTable propertiesTable) {
      return null;
   }

   @Override
   public String getDescription() {
      return null;
   }
}
