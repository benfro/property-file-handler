package net.benfro.tools.property.data;

public enum PropertiesTableFactory {

   INSTANCE;

   String tempClazzString = "";
   ProtoLocale tempCurrentLocale;
   boolean lineHasContinuationMark;
   ClassKeyBean tempRowId;
   PropertiesTable instance = new PropertiesTable();

   PropertiesTable readPathFiltered(String codeTreeBasePath, boolean applyFilterForNonTranslationableData) {

      return instance;
   }

   boolean hasContinuationMark(String line) {
      return line.endsWith("\\");
   }

   boolean lineHasPropertyKey(String line) {
      return line.contains("=");
   }

   void processLineInPropertyFile(String line) {
      if (hasContinuationMark(line)) {
         //String valueString = instance.get(tempRowId, tempCurrentLocale);
      }
   }

   /**
    * Closure applied to each line in a processed properties file
    * Handles a string in the typical format <code>this.is.the.key=value of property</code>
    * with line continuation markings as well as empty value strings.
    */
   // def forEachLineInPropertyFile = { String propertyLine ->
   // if (lineHasContinuationMark) {
   //    def valueString = instance.get(tempRowId, tempCurrentLocale)
   //    valueString += propertyLine.trim()
   //    instance.put(tempRowId, tempCurrentLocale, valueString)
   //    lineHasContinuationMark = hasContinuationMarker propertyLine
   // } else if (lineHasPropertyKey(propertyLine)) {
   //    def splitPropertyEntry = propertyLine.split("=")
   //    def propertyKey = splitPropertyEntry[0].trim()
   //    tempRowId = new ClassKeyBean(tempClazzString, propertyKey)
   //    if (splitPropertyEntry.length == 1) {
   //       instance.put(tempRowId, tempCurrentLocale, "")
   //    } else if (splitPropertyEntry.length == 2) {
   //       def propertyValue = splitPropertyEntry[1].trim()
   //       instance.put(tempRowId, tempCurrentLocale, propertyValue)
   //       lineHasContinuationMark = hasContinuationMarker propertyValue
   //    }
   // }
   //   }





   /**
    * Grand factory method
    * @param codeTreeBasePath
    * @param applyFilterForNonTranslationableData If <code>true</code>, resources like pictures
    * and URL will not be included in the search as they are not intended for translation
    */
   //static PropertiesTable readPathFiltered(String codeTreeBasePath, boolean applyFilterForNonTranslationableData) {

      //String tempClazzString = ""
      //ProtoLocale tempCurrentLocale
      //boolean lineHasContinuationMark
      //ClassKeyBean tempRowId
      //PropertiesTable instance = new PropertiesTable()

      /**
       * Standard Java property continuation is a backslash sign
       */
   //  def hasContinuationMarker = { line ->
   //           line.endsWith("\\")
   //  }

      /**
       * Our properties MUST have an equal sign in them.
       * TODO Is this a problem? Does not adhere to Java properties standards which allow a blank step too
       */
   //   def lineHasPropertyKey = { line ->
   //            line.contains("=")
   //   }

      /**
       * Closure applied to each line in a processed properties file
       * Handles a string in the typical format <code>this.is.the.key=value of property</code>
       * with line continuation markings as well as empty value strings.
       */
   // def forEachLineInPropertyFile = { String propertyLine ->
   // if (lineHasContinuationMark) {
   //    def valueString = instance.get(tempRowId, tempCurrentLocale)
   //    valueString += propertyLine.trim()
   //    instance.put(tempRowId, tempCurrentLocale, valueString)
   //    lineHasContinuationMark = hasContinuationMarker propertyLine
   // } else if (lineHasPropertyKey(propertyLine)) {
   //    def splitPropertyEntry = propertyLine.split("=")
   //    def propertyKey = splitPropertyEntry[0].trim()
   //    tempRowId = new ClassKeyBean(tempClazzString, propertyKey)
   //    if (splitPropertyEntry.length == 1) {
   //       instance.put(tempRowId, tempCurrentLocale, "")
   //    } else if (splitPropertyEntry.length == 2) {
   //       def propertyValue = splitPropertyEntry[1].trim()
   //       instance.put(tempRowId, tempCurrentLocale, propertyValue)
   //       lineHasContinuationMark = hasContinuationMarker propertyValue
   //    }
   // }
   //   }

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
//
   //   new File(codeTreeBasePath).traverse type: FileType.FILES, visit: fileProcessingClosure, nameFilter: ~/.*\.properties$/
//
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
