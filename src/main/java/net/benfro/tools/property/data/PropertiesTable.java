package net.benfro.tools.property.data;

import com.google.common.collect.*;
import com.google.common.collect.Table.Cell;
import net.benfro.tools.property.util.UnicodeUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class PropertiesTable {

   static PropertiesTable readPathFiltered(String codeTreeBasePath) {
      return PropertiesTableFactory.INSTANCE.readPathFiltered(codeTreeBasePath, true);
   }

   public static final ProtoLocale DEFAULT_LOCALE = new ProtoLocale("en");
   public static final String FS = File.separator;
   private static final String CSV_DELIMITER = ";";
   public static final String NEWLINE_SYMBOL_STRING = "[NL]";
   public static final String CLASS_COLUMN_HEADER = "class";
   public static final String PROPERTY_KEY_COLUMN_HEADER = "propertyKey";
   public static final String NEWLINE_CHARACTER = "\n";
   final Table<ClassKeyBean, ProtoLocale, String> table = TreeBasedTable.create();


   PropertiesTable() {}

   PropertiesTable filterCopy(Filter filter) {
      Set<Cell<ClassKeyBean, ProtoLocale, String>> all = Sets.newHashSet();
      for (Cell<ClassKeyBean, ProtoLocale, String> cell : cellSet()) {
         if (filter.apply(cell.getValue())) {
            all.add(cell);
         }
      }

      PropertiesTable copy = new PropertiesTable();
      all.forEach(it -> copy.put(it.getRowKey(), it.getColumnKey(), it.getValue()));

      return copy;
   }

   void put(ClassKeyBean classKeyBean, ProtoLocale locale, String value) {
      try {
         table.put(classKeyBean, locale, value);
      } catch (NullPointerException e) {
         System.out.println(classKeyBean);
      }
   }

   void put(ClassKeyBean classKeyBean, String locale, String value) {
      this.put(classKeyBean, new ProtoLocale(locale), value);
   }

   void put(String classString, String key, String locale, String value) {
      this.put(new ClassKeyBean(classString, key), locale, value);
   }

   Map<ClassKeyBean, Map<ProtoLocale, String>> rowMap() {
      return table.rowMap();
   }

   Map<ProtoLocale, String> row(ClassKeyBean classKeyBean) {
      return table.row(classKeyBean);
   }

   Map<ClassKeyBean, String> column(ProtoLocale locale) {
      return table.column(locale);
   }

   String get(ClassKeyBean classKeyBean, ProtoLocale locale) {
      String get = table.get(classKeyBean, locale);
      return get != null ? get : "";
   }

   String get(ClassKeyBean classKeyBean, String locale) {
      return this.get(classKeyBean, new ProtoLocale(locale));
   }

   String get(String clazzString, String propertyKey, String locale) {
      return this.get(new ClassKeyBean(clazzString, propertyKey), new ProtoLocale(locale));
   }

   Set<ProtoLocale> columnKeySet() {
      return table.columnKeySet();
   }

   void clear() {
      table.clear();
   }

   int size() {
      return table.size();
   }

   boolean isEmpty() {
      return table.isEmpty();
   }

   Set<Cell<ClassKeyBean, ProtoLocale, String>> cellSet() {
      return table.cellSet();
   }

   /**
    * Find all entries for a class by locale
    * @param clazzString The first argument to ClassKeyBean
    * @param locale The locale to look for
    * @return A Map<ClassKeyBean, String> i.e [row ID, value]
    */
   Map<ClassKeyBean, String> findAllEntriesForClassByLocale(String clazzString, ProtoLocale locale) {
      final Map<ClassKeyBean, String> result = Maps.newHashMap();
      final Map<ClassKeyBean, String> rowId2ValueMap = column(locale);
      rowId2ValueMap.entrySet().forEach(e -> {
                  if (clazzString.equals(e.getKey().getClazzForOS())) {
                     result.put(e.getKey(), e.getValue());
                  }
               }
      );
      return result;
   }

   @Override
   public String toString() {
      //return """Number of rows: ${table.rowKeySet().size()}
      //          Number of columns: ${table.columnKeySet().size()}
      //          Number of cells: ${table.cellSet().size()}"""

      return "";
   }

   /**
    * Put English locale first in column list
    */
   List<String> customSortedLocaleKeyList() {
      ProtoLocale locale = new ProtoLocale("en");
      List<ProtoLocale> list = Lists.newArrayList(columnKeySet());
      boolean localeWasInColumnKeySet = list.remove(locale);
      List<String> out = Lists.newArrayList();
      if (localeWasInColumnKeySet) {
         out.add(locale.getLanguage());
      }
      list.forEach(it -> out.add(it.getLanguage()));
      return out;
   }

   /**
    * Export this table as a comma separated vector string
    * @return A String containing data in this table
    */
   public String toCSV() {
      StringBuilder stringBuilder = new StringBuilder();
      List<String> headerList = Lists.newArrayList(CLASS_COLUMN_HEADER, PROPERTY_KEY_COLUMN_HEADER);
      List<String> columnsSortedWithEnFirst = customSortedLocaleKeyList();
      headerList.addAll(columnsSortedWithEnFirst);
      headerList.forEach(it -> stringBuilder.append(it).append(CSV_DELIMITER));
      stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(CSV_DELIMITER));
      stringBuilder.append(NEWLINE_CHARACTER);
      table.rowMap().entrySet().forEach(e -> {
         stringBuilder.append(e.getKey().getClazzForOS().replace(File.separator, "/")).append(CSV_DELIMITER);
         stringBuilder.append(e.getKey().key).append(CSV_DELIMITER);
         columnsSortedWithEnFirst.forEach(localeColumnKey -> {
            String valueString = get(e.getKey(), localeColumnKey);
            if (valueString != null) {
               String valueStrongNewLineEscaped = escapeNewLine(valueString);
               String convertedValue = UnicodeUtils.loadConvert(valueStrongNewLineEscaped);
               stringBuilder.append(convertedValue);
               //log.debug "Converted value added to CSV: ${convertedValue}"
            }
            stringBuilder.append(CSV_DELIMITER);
         });
         stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(CSV_DELIMITER));
         stringBuilder.append(NEWLINE_CHARACTER);
      });
      return stringBuilder.toString();
   }

   /**
    * Export this table as a comma separated vector string
    * @return A String containing data in this table
    */
   //String toCSV() {
   //   def stringBuilder = new StringBuilder()
   //   def headerList = [] << CLASS_COLUMN_HEADER << PROPERTY_KEY_COLUMN_HEADER
   //   def columnSortedWithEnFirstList = customSortedLocaleKeyList()
   //   columnSortedWithEnFirstList.each { headerList << it }
   //   headerList.each { stringBuilder.append(it).append(CSV_DELIMITER) }
   //   stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(CSV_DELIMITER))
   //   stringBuilder.append(NEWLINE_CHARACTER)
   //   table.rowMap().each {
   //      stringBuilder.append(it.key.clazzForOS.replace(File.separator, "/")).append(CSV_DELIMITER)
   //      stringBuilder.append(it.key.key).append(CSV_DELIMITER)
   //      columnSortedWithEnFirstList.each { localeColumnKey ->
   //               def valueString = get(it.key, localeColumnKey as String)
   //         if (valueString != null) {
   //            String valueStrongNewLineEscaped = escapeNewLine(valueString)
   //            String convertedValue = UnicodeUtils.loadConvert(valueStrongNewLineEscaped)
   //            stringBuilder.append(convertedValue)
   //            log.debug "Converted value added to CSV: ${convertedValue}"
   //         }
   //         stringBuilder.append(CSV_DELIMITER)
   //      }
   //      stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(CSV_DELIMITER))
   //      stringBuilder.append(NEWLINE_CHARACTER)
   //   }
   //   stringBuilder.toString()
   //}

   //List<List<Object>> toCSVListOfRows() {
   //   def out = []
   //   toCSV().split(NEWLINE_CHARACTER).each {
   //      out.add(it)
   //   }
   //   out
   //}

   static String escapeNewLine(String valueString) {
      return valueString.replace("\\n", NEWLINE_SYMBOL_STRING);
   }

   /**
    * @return A List<List<Object>> representing data in this table
    */
   List<List<Object>> asValueRange() {
      List<List<Object>> output = Lists.newArrayList();
      //def rows = toCSV().split(NEWLINE_CHARACTER)
      //rows.each { row ->
      //         def rowList = []
      //   def rowSplit = row.split(CSV_DELIMITER)
      //   rowSplit.each { item ->
      //            rowList << item
      //   }
      //   output << rowList
      //}
      return output;
   }

   /**
    * Load data into this table
    * @param dataToLoad Inner list is row, outer is columns
    */
   void loadDataIntoTable(List<List<String>> dataToLoad) {
      new DataLoader().load(dataToLoad, table);
   }

   static class DataLoader {

      Map<Integer, ProtoLocale> indexToLocaleMap = Maps.newHashMap();

      boolean isHeaderRow(List<String> row){
         return row.get(0).equals(CLASS_COLUMN_HEADER);
      }

      void parseHeaderRow(final List<String> row) {
         for (int i = 0; i < row.size(); i++) {
            String s = row.get(i);
            if (s.length() == 2) {
               indexToLocaleMap.put(i, new ProtoLocale(s));
            }
         }
      }

      String restoreNewLine(String valueString) {
         return valueString.replace(NEWLINE_SYMBOL_STRING, NEWLINE_CHARACTER);
      }

      void parseDataRowToTableEntry(final List<String> row, Table<ClassKeyBean, ProtoLocale, String> table) {
         String tempClassStringHolder = "";
         ClassKeyBean tempPrimaryKeyHolder = null;
         for (int i = 0; i < row.size(); i++) {
            final String value = row.get(i);
            switch (i) {
               case 0:
                  tempClassStringHolder = value;
                  break;
               case 1:
                  tempPrimaryKeyHolder = new ClassKeyBean(tempClassStringHolder, value);
                  break;
               default:
                  String restoredNewLineValueString = restoreNewLine(value);
                  table.put(tempPrimaryKeyHolder, indexToLocaleMap.get(i), UnicodeUtils.saveConvert(restoredNewLineValueString));
                  break;
            }
         }
      }

      public void load(List<List<String>> dataToLoad, Table<ClassKeyBean, ProtoLocale, String> table) {
         for (List<String> row : dataToLoad) {
            if (isHeaderRow(row)) {
               parseHeaderRow(row);
            } else {
               parseDataRowToTableEntry(row, table);
            }
         }
      }
   }

   //void save(String filePath) {
   //   new File(filePath).write(new JsonBuilder(asValueRange()).toPrettyString())
   //}

   //Object load(String filePath) {
   //   def table = (List<List<Object>>) new JsonSlurper().parseText(new File(filePath).text)
   //   this.loadDataIntoTable(table)
   //   this
   //}




}
