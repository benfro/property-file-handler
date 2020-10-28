package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertyTable;

import java.util.Map;



public class DefaultFrequencyTable implements Query<Map<String, Integer>> {
   @Override
   public Map<String, Integer> performQuery(PropertyTable propertyTable) {
      return null;
   }

   @Override
   public String getDescription() {
      return "";
   }
}
