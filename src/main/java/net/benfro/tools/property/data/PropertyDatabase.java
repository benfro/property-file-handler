package net.benfro.tools.property.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;



public class PropertyDatabase implements PropertyFileDAO {

   public static final ProtoLocale DEFAULT_LOCALE = new ProtoLocale("en");
   public static final String FS = File.separator;

   private final String codeTreeBasePath;
   private PropertiesTable propertiesTable = new PropertiesTable();

   public PropertyDatabase(String codeTreeBasePath) {
      this.codeTreeBasePath = codeTreeBasePath;
   }

   @Override
   public PropertiesTable readBasePathProperties() {
      try {
         propertiesTable = PropertiesTable.readPathFiltered(codeTreeBasePath);
      } catch (IOException e) {
         e.printStackTrace();
      }
      return propertiesTable;
   }

   PropertiesTable readBasePathPropertiesWithoutFilter() {
      try {
         propertiesTable = PropertiesTable.readPathFiltered(codeTreeBasePath);
      } catch (IOException e) {
         e.printStackTrace();
      }
      return propertiesTable;
   }

   @Override
   public FilePushBackReport pushBasePathProperties(List<List<String>> webSheetState) {
      // Make a report of work done during pushback
      FilePushBackReport pushBackReport = new FilePushBackReport();
      // Load current state of the properties file from file base path
      PropertiesTable serverState = null;
      try {
         serverState = PropertiesTable.readPathFiltered(codeTreeBasePath, false);
      } catch (IOException e) {
         e.printStackTrace();
      }
      loadDataIntoTable(webSheetState);
      PropertiesTable changeSet = PropertiesTable.calculateChangeSet(serverState, this.propertiesTable);
      FileDataObject fileDataObject = new FileDataObject(changeSet);
      fileDataObject.keySet().forEach ( filePathKey -> {
         String directory = filePathKey.substring(0, filePathKey.lastIndexOf(FS));
         String fileName = filePathKey.substring(filePathKey.lastIndexOf(FS) + 1);
         Path file = null;
         try {
            file = Files.createFile(Paths.get(codeTreeBasePath, directory, FS, fileName));
         } catch (IOException e) {
            e.printStackTrace();
         }
         if (!file.toFile().exists()) {
            Path basePathOfThisFile = Paths.get(codeTreeBasePath + directory);
            try {
               Files.createDirectories(basePathOfThisFile);
            } catch (IOException e) {
               e.printStackTrace();
            }
            try {
               file = Files.createFile(Paths.get(basePathOfThisFile.toString(), fileName));
            } catch (IOException e) {
               e.printStackTrace();
            }
            //pushBackReport.numCreatedFiles++
         } else {
            //pushBackReport.numFoundExistingFiles++
         }
         Collection<String> list = fileDataObject.asMap().get(filePathKey);
         try {
            Files.write(file, list, StandardCharsets.UTF_8);
         } catch (IOException e) {
            e.printStackTrace();
         }
      });
      return pushBackReport;
   }

   /**
    * A view of this database as a <code>List</code> of <code>List</code>s. Inner list are rows
    */
   List<List<String>> asValueRangeValue() {
      return propertiesTable.asValueRange();
   }

   /**
    * A CSV view of this database separated by semi-colons
    */
   public String asCSV() {
      return propertiesTable.toCSV();
   }

   public List<String> asListOfCSVRows() {
      return propertiesTable.toCSVListOfRows();
   }

   void loadDataIntoTable(List<List<String>> lists) {
      propertiesTable.clear();
      propertiesTable.loadDataIntoTable(lists);
   }

   public PropertiesTable getPropertiesTable() {
      return propertiesTable;
   }


}
