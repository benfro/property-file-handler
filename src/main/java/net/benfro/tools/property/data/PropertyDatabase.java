package net.benfro.tools.property.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;


public class PropertyDatabase {

   public static final String FS = File.separator;

   private final String codeTreeBasePath;
   private final PropertyTable propertyTable;

   public PropertyDatabase(String codeTreeBasePath) throws IOException {
      this.codeTreeBasePath = codeTreeBasePath;
      propertyTable = PropertyTable.readPathFiltered(codeTreeBasePath);
   }

   public FilePushBackReport pushBasePathProperties(List<List<String>> webSheetState) {
      // Make a report of work done during pushback
      FilePushBackReport pushBackReport = new FilePushBackReport();
      // Load current state of the properties file from file base path
      PropertyTable serverState = null;
      try {
         serverState = PropertyTable.readPathFiltered(codeTreeBasePath, false);
      } catch (IOException e) {
         e.printStackTrace();
      }
      loadDataIntoTable(webSheetState);
      PropertyTable changeSet = PropertyTable.calculateChangeSet(serverState, this.propertyTable);
      FileDataObject fileDataObject = new FileDataObject(changeSet);
      fileDataObject.keySet().forEach(filePathKey -> {
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
    * A CSV view of this database separated by semi-colons
    */
   public String asCSV() {
      return propertyTable.toCSV();
   }

   public List<String> asListOfCSVRows() {
      return propertyTable.toCSVListOfRows();
   }

   void loadDataIntoTable(List<List<String>> lists) {
      propertyTable.clear();
      propertyTable.loadDataIntoTable(lists);
   }

   public PropertyTable getPropertiesTable() {
      return propertyTable;
   }


}
