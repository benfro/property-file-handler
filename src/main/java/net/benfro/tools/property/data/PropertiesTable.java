package net.benfro.tools.property.data;

import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.google.common.collect.Table.Cell;
import net.benfro.tools.property.util.UnicodeUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class PropertiesTable {

   /**
    * Calculate a change set to be written back to the base path, i.e the "effective delta" between server and web form state.
    * Note that order of arguments is important; server state first, web form state after.
    * This method can be hard to understand and can (should?) be treated as a black box, but some clarity can be
    * obtained in documentation tied to Guava <code>Maps.difference</code>. Lots of Maps are used to describe
    * differences at different levels of the data structures, and lots of nested closures are used to get results.
    * @param serverState MUST be the first argument!
    * @param webFormState MUST be the second argument!
    * @return A PropertiesTable representing the effective delta between the two input states
    */
   static PropertiesTable calculateChangeSet(PropertiesTable serverState, PropertiesTable webFormState) {
      return ChangeSetCalculator.INSTANCE.calculateChangeSet(serverState, webFormState);
   }

   static PropertiesTable readPathFiltered(String codeTreeBasePath) throws IOException {
      return PropertiesTableFactory.INSTANCE.readPathFiltered(codeTreeBasePath, true);
   }

   static PropertiesTable readPathFiltered(String codeTreeBasePath, boolean applyFilterForNonTranslatableData) throws IOException {
      return PropertiesTableFactory.INSTANCE.readPathFiltered(codeTreeBasePath, applyFilterForNonTranslatableData);
   }

   public static final ProtoLocale DEFAULT_LOCALE = new ProtoLocale("en");
   public static final String FS = File.separator;
   private static final String CSV_DELIMITER = ";";
   public static final String NEWLINE_SYMBOL_STRING = "[NL]";
   public static final String CLASS_COLUMN_HEADER = "class";
   public static final String PROPERTY_KEY_COLUMN_HEADER = "propertyKey";
   public static final String NEWLINE_CHARACTER = "\n";
   final Table<ClassKeyBean, ProtoLocale, String> table = TreeBasedTable.create();

   public PropertiesTable() {}

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

   public void put(ClassKeyBean classKeyBean, ProtoLocale locale, String value) {
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

   public Set<Cell<ClassKeyBean, ProtoLocale, String>> cellSet() {
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
      rowId2ValueMap.forEach((key, value) -> {
         if (clazzString.equals(key.getClazzForOS())) {
            result.put(key, value);
         }
      });
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
      table.rowMap().forEach((key, value) -> {
         stringBuilder.append(key.getClazzForOS().replace(File.separator, "/")).append(CSV_DELIMITER);
         stringBuilder.append(key.key).append(CSV_DELIMITER);
         columnsSortedWithEnFirst.forEach(localeColumnKey -> {
            String valueString = get(key, localeColumnKey);
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

   List<String> toCSVListOfRows() {
      return Splitter.on(NEWLINE_CHARACTER).splitToList(toCSV());
   }

   static String escapeNewLine(String valueString) {
      return valueString.replace("\\n", NEWLINE_SYMBOL_STRING);
   }

   /**
    * @return A List<List<String>> representing data in this table
    */
   List<List<String>> asValueRange() {
      List<List<String>> output = Lists.newArrayList();
      final List<String> listOfRowStrings = toCSVListOfRows();
      listOfRowStrings.forEach(rowStr -> output.add(Splitter.on(CSV_DELIMITER).splitToList(rowStr)));
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
