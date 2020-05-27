package net.benfro.tools.property.data;

import java.util.List;



public interface PropertyFileDAO {
   /**
    * Read the .properties files into a table object and return it
    * @return A populated PropertiesTable object
    */
   PropertiesTable readBasePathProperties();

   /**
    * Push web sheet data back to the base path code tree
    * @param webSheetData The state from a web sheet
    * @return A report object over performed actions
    */
   FilePushBackReport pushBasePathProperties(List<List<String>> webSheetData);
}
