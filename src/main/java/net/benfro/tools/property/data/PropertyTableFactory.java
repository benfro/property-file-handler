package net.benfro.tools.property.data;

import net.benfro.tools.property.query.FullLineFilteringQuery;
import net.benfro.tools.property.query.ValueNotEndsWithQuery;
import net.benfro.tools.property.query.ValueNotStartsWithQuery;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;



public enum PropertyTableFactory {

   INSTANCE;

   String tempClazzString = "";
   ProtoLocale tempCurrentLocale;
   boolean lineHasContinuationMark;
   ClassKeyBean tempRowId;
   public static final String FS = File.separator;

   /**
    * Grand factory method
    * @param codeTreeBasePath
    * @param applyFilterForNonTranslationableData If <code>true</code>, resources like pictures
    * and URL will not be included in the search as they are not intended for translation
    */
   PropertyTable readPathFiltered(String codeTreeBasePath, boolean applyFilterForNonTranslationableData) throws IOException {
      PropertyTable instance = new PropertyTable();

      Files.walkFileTree(Paths.get(codeTreeBasePath), new FileVisitor<Path>() {
         @Override
         public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return CONTINUE;
         }

         @Override
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toFile().isFile() && !file.toFile().isHidden() && file.toFile().getName().matches("~/.*\\.properties$")) {
               fileProcessingClojure(file, codeTreeBasePath, instance);
            }
            return CONTINUE;
         }

         @Override
         public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            System.err.println(exc);
            return CONTINUE;
         }

         @Override
         public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return CONTINUE;
         }
      });

      if (applyFilterForNonTranslationableData) {
         PropertyTable copy = new FullLineFilteringQuery().performQuery(instance);
         copy = new ValueNotEndsWithQuery().performQuery(copy);
         copy = new ValueNotStartsWithQuery().performQuery(copy);
         return copy;
      } else {
         return instance;
      }
   }

   /**
    * Standard Java property continuation is a backslash sign
    */
   boolean hasContinuationMark(String line) {
      return line.endsWith("\\");
   }

   /**
    * Our properties MUST have an equal sign in them.
    * TODO Is this a problem? Does not adhere to Java properties standards which allow a blank step too
    */
   boolean lineHasPropertyKey(String line) {
      return line.contains("=");
   }

   /**
    * Closure applied to each line in a processed properties file
    * Handles a string in the typical format <code>this.is.the.key=value of property</code>
    * with line continuation markings as well as empty value strings.
    */
   void forEachLineInPropertyFile(String propertyLine, PropertyTable instance) {
      propertyLine = propertyLine.trim();
      if (lineHasContinuationMark) {
         String valueString = instance.get(tempRowId, tempCurrentLocale);
         valueString += propertyLine.trim();
         instance.put(tempRowId, tempCurrentLocale, valueString);
         lineHasContinuationMark = hasContinuationMark(propertyLine);
      } else if (lineHasPropertyKey(propertyLine)) {
         String[] splitPropertyEntry = propertyLine.split("=");
         String propertyKey = splitPropertyEntry[0].trim();
         ClassKeyBean tempRowId = ClassKeyBean.of(tempClazzString, propertyKey);
         if (splitPropertyEntry.length == 1) {
            instance.put(tempRowId, tempCurrentLocale, "");
         } else if (splitPropertyEntry.length == 2) {
            String propertyValue = splitPropertyEntry[1].trim();
            instance.put(tempRowId, tempCurrentLocale, propertyValue);
            lineHasContinuationMark = hasContinuationMark(propertyLine);
         }
      }
   }

   void fileProcessingClojure(Path file, String codeTreeBasePath, PropertyTable instance) {
      final String filePath = file.toFile().toString();
      if (filePath.contains(FS + "src" + FS + "main" + FS)) {
         int lastIndexOfUnderscore = filePath.lastIndexOf("_");
         if (!filePath.substring(lastIndexOfUnderscore + 3).equals(".properties")) {
            lastIndexOfUnderscore = -1;
         }
         if (lastIndexOfUnderscore == -1) {
            tempCurrentLocale = PropertyTable.DEFAULT_LOCALE;
            tempClazzString = filePath
                     .substring(0, filePath.lastIndexOf('.'))
                     .substring(codeTreeBasePath.length())
                     .replace(File.separator, "/");
         } else {
            tempCurrentLocale = LocaleRegistry.INSTANCE.get(filePath.substring(lastIndexOfUnderscore + 1, lastIndexOfUnderscore + 3));
            tempClazzString = filePath
                     .substring(0, lastIndexOfUnderscore)
                     .substring(codeTreeBasePath.length())
                     .replace(File.separator, "/");
         }
         try {
            Files.readAllLines(file, StandardCharsets.UTF_8).forEach(line -> forEachLineInPropertyFile(line, instance));
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
      /**
       * Closure applied to each found file, i.e. xxx.properties file
       */
   //  def fileProcessingClosure = { file ->
   //           String filePath = file.path
   //  if (filePath.contains(FS + "src" + FS + "main" + FS)) {
   //     def lastIndexOfUnderscore = filePath.lastIndexOf("_")
   //     if (!filePath.substring(lastIndexOfUnderscore + 3).equals(".properties")) {
   //        lastIndexOfUnderscore = -1
   //     }
   //     if (lastIndexOfUnderscore == -1) {
   //        tempCurrentLocale = DEFAULT_LOCALE
   //        tempClazzString = filePath.substring(0, filePath.lastIndexOf('.')).substring(codeTreeBasePath.length()).replace(File.separator, "/")
   //     } else {
   //        tempCurrentLocale = new ProtoLocale(filePath.substring(lastIndexOfUnderscore + 1, lastIndexOfUnderscore + 3))
   //        tempClazzString = filePath.substring(0, lastIndexOfUnderscore).substring(codeTreeBasePath.length()).replace(File.separator, "/")
   //     }
   //     file.eachLine('UTF-8', forEachLineInPropertyFile)
   //  }
   //     }

   //   new File(codeTreeBasePath).traverse type: FileType.FILES, visit: fileProcessingClosure, nameFilter: ~/.*\.properties$/

   //   if (applyFilterForNonTranslationableData) {
   //      def copy = new FullLineFilteringQuery().performQuery(instance)
   //      copy = new ValueNotEndsWithQuery().performQuery(copy)
   //      copy = new ValueNotStartsWithQuery().performQuery(copy)
   //      copy
   //   } else {
   //      instance
   //   }
//
   //}
}
