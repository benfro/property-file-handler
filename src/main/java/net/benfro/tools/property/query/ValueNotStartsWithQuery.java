package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertiesTable;



public class ValueNotStartsWithQuery implements PropertiesQuery {
   @Override
   public PropertiesTable performQuery(PropertiesTable propertiesTable) {
      return null;
   }

   @Override
   public String getDescription() {
      return null;
   }

   //def valueNotStartsWith = ["http://", "https://", "http\\://", "https\\://", "\${"]
   //def keyNotstartsWith = ["#", "notranslate"]
//
   //@Override
   //PropertiesTable performQuery(PropertiesTable propertiesTable) {
   //   PropertiesTable output = new PropertiesTable()
   //   propertiesTable.cellSet().each {
   //      def result = true
   //      valueNotStartsWith.each { item ->
   //               result &= !it.value.startsWith(item)
   //      }
   //      keyNotstartsWith.each { item ->
   //               result &= !it.rowKey.key.startsWith(item)
   //      }
   //      if (result) output.put(it.rowKey, it.columnKey, it.value)
   //   }
   //   output
   //}
//
   //@Override
   //String getDescription() {
   //   return "Remove entries whose values start or whose keys start in specific ways"
   //}
}
