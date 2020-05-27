package net.benfro.tools.property.query;

import java.util.Map;
import net.benfro.tools.property.data.PropertiesTable;



public class DefaultFrequencyTable implements Query<Map<String, Integer>> {
   @Override
   public Map<String, Integer> performQuery(PropertiesTable propertiesTable) {
      return null;
   }

   @Override
   public String getDescription() {
      return "";
   }
}
