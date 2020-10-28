package net.benfro.tools.property.data;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.util.Map;



public enum ChangeSetCalculator {

   INSTANCE;

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
   public PropertyTable calculateChangeSet(PropertyTable serverState, PropertyTable webFormState) {
      PropertyTable changeSet = new PropertyTable();

      final Map<ClassKeyBean, MapDifference.ValueDifference<Map<ProtoLocale, String>>> rowDifferenceBetweenStatesMap =
               Maps.difference(serverState.rowMap(), webFormState.rowMap()).entriesDiffering();

      rowDifferenceBetweenStatesMap.keySet().forEach(k -> keyInRowDifferenceBetweenStatesMap(k, serverState, webFormState, changeSet));

      return changeSet;
   }

   void keyInRowDifferenceBetweenStatesMap(
            ClassKeyBean differingRowPK,
            PropertyTable serverState,
            PropertyTable webFormState,
            PropertyTable changeSet) {
      // Gets a Map<Locale, [serverValue(String), webValue(String)]>
      final MapDifference<ProtoLocale, String> differenceForCurrentRowMap =
               Maps.difference(serverState.row(differingRowPK), webFormState.row(differingRowPK));
      // Gets a Map<Locale, webValue(String)>
      final Map<ProtoLocale, String> localeToWebSheetStateMap =
               extractChangedWebSheetEntries(differenceForCurrentRowMap.entriesDiffering());
      // Add all changes from empty to non-empty on server side iff they are (!= null && !isEmpty())
      differenceForCurrentRowMap.entriesOnlyOnRight().forEach((key, value) -> {
         if (value != null && !value.isEmpty()) {
            localeToWebSheetStateMap.put(key, value);
         }
      });
      // Put all the changed values into the change set
      localeToWebSheetStateMap.forEach((webStateLocaleKey,webStateValue) -> changeSet.put(differingRowPK, webStateLocaleKey, webStateValue));

      localeToWebSheetStateMap.forEach((localeKey, webSheetStateValue) -> {
         // Gets a Map<ClassBeanKey, String> from server for specific locale
         final Map<ClassKeyBean, String> rowIdToServerValueMap = serverState.findAllEntriesForClassByLocale(differingRowPK
                  .getClazzForOS(), localeKey);
         rowIdToServerValueMap.forEach((rowPK, serverStateValue) -> {
            // Put server state into change set for this class and locale
            if (changeSet.get(rowPK, localeKey).isEmpty()) {
               changeSet.put(rowPK, localeKey, serverStateValue);
            }
         });
      });
   }

   private Map<ProtoLocale, String> extractChangedWebSheetEntries(Map<ProtoLocale, MapDifference.ValueDifference<String>> entriesDiffering) {
      Map<ProtoLocale, String> out = Maps.newHashMap();
      entriesDiffering.forEach((key, value) -> {
         final String serverStateValue = value.rightValue();
         out.put(key, serverStateValue);
      });
      return out;
   }

}
