package net.benfro.tools.property.query;

import net.benfro.tools.property.data.PropertiesTable;



public interface Query<T> {
   /**
    * @param propertiesTable Original data to perform query on
    * @return Copy containing only the items that meet the query
    */
   T performQuery(PropertiesTable propertiesTable);

   /**
    * @return A description of this query
    */
   String getDescription();
}
