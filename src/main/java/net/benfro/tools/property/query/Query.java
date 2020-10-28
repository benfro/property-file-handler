package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertyTable;



public interface Query<T> {
   /**
    * @param propertyTable Original data to perform query on
    * @return Copy containing only the items that meet the query
    */
   T performQuery(PropertyTable propertyTable);

   /**
    * @return A description of this query
    */
   String getDescription();
}
