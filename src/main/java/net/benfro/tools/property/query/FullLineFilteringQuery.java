package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertiesTable;



public class FullLineFilteringQuery implements PropertiesQuery {
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
   //   PropertiesTable output = new PropertiesTable()
   //   propertiesTable.cellSet().each {
   //      def result = !it.value.isEmpty() && !it.value.isAllWhitespace() &&
   //               !it.value.matches("(\\s*)((\\d{1,3}),\\s*(\\d{1,3}),\\s*(\\d{1,3})(\\s*))")
   //      if(result){
   //         output.put(it.rowKey, it.columnKey, it.value)
   //      }
   //   }
   //   output
   //}
//
   //@Override
   //String getDescription() {
   //   return "Filters out empty values and values that are RBG codes"
   //}
}
