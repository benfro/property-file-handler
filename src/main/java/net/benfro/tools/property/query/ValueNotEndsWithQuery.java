package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertiesTable;



public class ValueNotEndsWithQuery implements PropertiesQuery {
   @Override
   public PropertiesTable performQuery(PropertiesTable propertiesTable) {
      return null;
   }

   @Override
   public String getDescription() {
      return null;
   }

   //@Override
   //PropertiesTable performQuery(PropertiesTable propertiesTable) {
   //   def notEndsWith = [".jpg", ".JPG", ".png", ".PNG", ".icon", ".htm", ".html"]
   //   PropertiesTable output = new PropertiesTable()
   //   propertiesTable.cellSet().each {
   //      def result = true
   //      notEndsWith.each { item ->
   //               result &= !it.value.endsWith(item)
   //      }
   //      if (result) {
   //         output.put(it.rowKey, it.columnKey, it.value)
   //      }
   //   }
   //   output
   //}
//
   //@Override
   //String getDescription() {
   //   return "Filter out depending on ending of property value"
   //}
}
