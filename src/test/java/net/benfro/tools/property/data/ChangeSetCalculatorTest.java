package net.benfro.tools.property.data;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;



class ChangeSetCalculatorTest {
   @Test
   void testEqualStatesYieldsEmptyDelta() throws IOException {
      final PropertyTable serverState = PropertyTable.readPathFiltered("src/test/resources/simpleData");
      final PropertyTable webSheetState =  PropertyTable.readPathFiltered("src/test/resources/simpleData");
      assertTrue(ChangeSetCalculator.INSTANCE.calculateChangeSet(serverState, webSheetState).isEmpty());
   }

   //def "changeSet: same state at server and web yields empty delta"() {
   //   given: "server and web state are identical"
   //   def serverState = PropertiesTable.readPathFiltered("src/test/resources/simpleData")
   //   def webSheetState = PropertiesTable.readPathFiltered("src/test/resources/simpleData")
   //   when: "a change set is calculated"
   //   def changeSet = PropertiesTable.calculateChangeSet(serverState, webSheetState)
   //   then: "it is empty"
   //   changeSet.isEmpty()
   //}

   @Test
   void testNonTranslatingPropertiesAtServerYieldsEmptyDelta() throws IOException {
      final PropertyTable serverState = PropertyTable.readPathFiltered("src/test/resources/simpleData");
      serverState.put(ClassKeyBean.of("/jobb/nobb/Kobb", "jupp.nupp"), "en", "filterClosure.jpg");
      final PropertyTable webSheetState =  PropertyTable.readPathFiltered("src/test/resources/simpleData");
      assertTrue(ChangeSetCalculator.INSTANCE.calculateChangeSet(serverState, webSheetState).isEmpty());
   }

   //def "changeSet: non-translating properties at server yields empty daya"() {
   //   given: "server state has some non translating properties"
   //   def serverState = PropertiesTable.readPathFiltered("src/test/resources/simpleData")
   //   serverState.put(new ClassKeyBean("/jobb/nobb/Kobb", "jupp.nupp"), "en", "filterClosure.jpg")
   //   and: "web state is as usual"
   //   def webSheetState = PropertiesTable.readPathFiltered("src/test/resources/simpleData")
   //   when: "a change set is calculated"
   //   def changeSet = PropertiesTable.calculateChangeSet(serverState, webSheetState)
   //   then: "it is empty"
   //   changeSet.isEmpty()
   //}

   @Test
   void nameRealSituationWithOriginalDataChanged() throws IOException {
      //PropertiesTable serverState = PropertiesTable.readPathFiltered(BIGGER_DATA_PATH);
      PropertyTable serverState = null; //PropertiesTable.readPathFiltered(BIGGER_DATA_PATH);
      serverState.put(ClassKeyBean.of("/src/main/test/Data", "firstPicture"), "en", "filterClosure.png");
      serverState.put(ClassKeyBean.of("/src/main/test/Data", "someUrl"), "en", "loremIpsum.html");

      PropertyTable webSheetState = PropertyTable.readPathFiltered("src/test/resources/simpleData");
      ClassKeyBean keyOneRowId = ClassKeyBean.of("/src/main/test/Data", "keyOne");
      webSheetState.put(keyOneRowId, "en", "filterClosure");
      webSheetState.put(keyOneRowId, "sv", "hoppla");
      ClassKeyBean keyTwoRowId = ClassKeyBean.of("/src/main/test/Data", "keyTwo");
      webSheetState.put(keyTwoRowId, "fi", "Paska!");

      PropertyTable changeSet = ChangeSetCalculator.INSTANCE.calculateChangeSet(serverState, webSheetState);

      assertEquals(3, changeSet.columnKeySet().size());

      ProtoLocale en = LocaleRegistry.INSTANCE.get("en");
      ProtoLocale sv = LocaleRegistry.INSTANCE.get("sv");
      ProtoLocale fi = LocaleRegistry.INSTANCE.get("fi");

      assertTrue(changeSet.columnKeySet().contains(en));
      assertTrue(changeSet.columnKeySet().contains(sv));
      assertTrue(changeSet.columnKeySet().contains(fi));

      assertEquals(5, changeSet.column(en).size());
      assertEquals(2, changeSet.column(fi).size());
      assertEquals(3, changeSet.column(sv).size());

      assertEquals("filterClosure", changeSet.column(en).get(keyOneRowId));
      assertEquals("valueTwo", changeSet.column(en).get(keyTwoRowId));
      assertEquals("hoppla", changeSet.column(sv).get(keyOneRowId));
      assertNotEquals("hoppla", changeSet.column(sv).get(keyTwoRowId));
      assertEquals("Paska!", changeSet.column(fi).get(keyTwoRowId));
   }


   //def "changeSet: simulate a real situation with original data changed"() {
   //   given: "there is a server state"
   //   def serverState = PropertiesTable.readPathFiltered(BIGGER_DATA_PATH)
   //   and: "the default locale file holds properties not meant for translation"
   //   serverState.put(new ClassKeyBean('/src/main/test/Data', 'firstPicture'), "en", "filterClosure.png")
   //   serverState.put(new ClassKeyBean('/src/main/test/Data', 'someUrl'), "en", "loremIpsum.html")
   //   and: "the web sheet has som changes compared to server state"
   //   def webSheetState = PropertiesTable.readPathFiltered("src/test/resources/simpleData")
   //   def keyOneRowId = new ClassKeyBean('/src/main/test/Data', 'keyOne')
   //   webSheetState.put(keyOneRowId, 'en', 'filterClosure')
   //   webSheetState.put(keyOneRowId, 'sv', 'hoppla')
   //   def keyTwoRowId = new ClassKeyBean('/src/main/test/Data', 'keyTwo')
   //   webSheetState.put(keyTwoRowId, 'fi', 'Paska!')
   //   when: "a change set is calculated"
   //   def changeSet = PropertiesTable.calculateChangeSet(serverState, webSheetState)
   //   then: "the number of locales are three"
   //   changeSet.columnKeySet().size() == 3
   //   and: "they are en, fi and sv"
   //   def enLocale = new ProtoLocale("en")
   //   def swLocale = new ProtoLocale("sv")
   //   def fiLocale = new ProtoLocale("fi")
   //   changeSet.columnKeySet().contains(enLocale)
   //   changeSet.columnKeySet().contains(swLocale)
   //   changeSet.columnKeySet().contains(fiLocale)
   //   and: "default locale have five entries due to non-translateable ones"
   //   changeSet.column(enLocale).size() == 5
   //   and: "the rest three entries"
   //   changeSet.column(fiLocale).size() == 2
   //   changeSet.column(swLocale).size() == 3
   //   and: "the changed values are correct"
   //   changeSet.column(enLocale).get(keyOneRowId) == "filterClosure"
   //   changeSet.column(enLocale).get(keyTwoRowId) == "valueTwo"
   //   changeSet.column(swLocale).get(keyOneRowId) == "hoppla"
   //   changeSet.column(swLocale).get(keyTwoRowId) != "hoppla"
   //   changeSet.column(fiLocale).get(keyTwoRowId) == "Paska!"
   //}

   //def "changeSet: empty value in web server state entered gives change set"() {
   //   given: "there is a server state"
   //   def serverState = PropertiesTable.readPathFiltered(BIGGER_DATA_PATH)
   //   and: "the default locale file holds properties not meant for translation"
   //   serverState.put(new ClassKeyBean('/src/main/test/Data', 'firstPicture'), "en", "filterClosure.png")
   //   serverState.put(new ClassKeyBean('/src/main/test/Data', 'someUrl'), "en", "loremIpsum.html")
   //   and: "web state has a formerly empty value entered"
   //   def webSheetState = PropertiesTable.readPathFiltered(BIGGER_DATA_PATH)
   //   webSheetState.put(new ClassKeyBean('/src/main/test/Data', 'keyOne'), "fi", "Yksi")
   //   when: "a change set is calculated"
   //   def changeSet = PropertiesTable.calculateChangeSet(serverState, webSheetState)
   //   then: "the number changes are one"
   //   println changeSet.toCSV()
   //   changeSet.size() == 3
   //}

   //def "changeSet: empty value in web server filled with zero length string equals no value"() {
   //   given: "there is a server state"
   //   def serverState = PropertiesTable.readPathFiltered(BIGGER_DATA_PATH)
   //   and: "the default locale file holds properties not meant for translation"
   //   serverState.put(new ClassKeyBean('/src/main/test/Data', 'firstPicture'), "en", "filterClosure.png")
   //   serverState.put(new ClassKeyBean('/src/main/test/Data', 'someUrl'), "en", "loremIpsum.html")
   //   and: "web state has a formerly empty value entered with a zero length string"
   //   def webSheetState = PropertiesTable.readPathFiltered(BIGGER_DATA_PATH)
   //   webSheetState.put(new ClassKeyBean('/src/main/test/Data', 'keyOne'), "fi", "")
   //   when: "a change set is calculated"
   //   def changeSet = PropertiesTable.calculateChangeSet(serverState, webSheetState)
   //   then: "the number changes are one"
   //   println changeSet.toCSV()
   //   changeSet.size() == 0
   //}

   //def "changeSet: weird bug that make data not correct in change set"() {
   //   given: "there is a server state"
   //   def serverState = PropertiesTable.readPathFiltered(PathUtil.toPlatformPath("src/test/resources/lastLineBug"))
   //   and: "web state has this magic value changed"
   //   def webState = serverState.filterCopy({ true })
   //   def bean = new ClassKeyBean("/src/main/se/thermocalc/unite/diffusionmodule/resources/DiffusionCalculatorConfiguration", "profileType.function.text")
   //   webState.put(bean, "sv", "Funktion")
   //   when:
   //   def changeSet = PropertiesTable.calculateChangeSet(serverState, webState)
   //   then:
   //   changeSet.get(bean, "sv") == "Funktion"
   //   serverState.get(bean, "sv") != "Funktion"
   //}

   //def "changeSet: exactly what is failing in production!!!"() {
   //   setup:
   //   def bean = new ClassKeyBean("/src/test/resources/lastLineBug/src/main/se/thermocalc/unite/diffusionmodule/resources/DiffusionCalculatorConfiguration", "profileType.function.text")
   //   def bean2 = new ClassKeyBean("/src/test/resources/lastLineBug/src/main/se/thermocalc/unite/diffusionmodule/resources/DiffusionCalculatorConfiguration", "regionPhaseNotSelected.invalidReason.text")
   //   def tempDir = File.createTempDir()
   //   println "Temp dir is ${tempDir.getAbsolutePath()}"
   //   copy(PathUtil.toPlatformPath("src/test/resources/lastLineBug"), tempDir.getAbsolutePath())
   //   and: "there is a server state"
   //   def database = new PropertyFileDatabase(tempDir.getAbsolutePath())
   //   database.readBasePathProperties()
   //   and: "server state has another value for current property"
   //   assert database.getPropertiesTable().get(bean, "sv") != "Funktion"
   //   assert !database.getPropertiesTable().get(bean2, "sv").contains("\\\\")
   //   and: "web state has this magic value changed"
   //   def webState = database.getPropertiesTable().filterCopy({ true })
   //   webState.put(bean, "sv", "Funktion")
   //   when: "data is pushed back"
   //   database.pushBasePathProperties(webState.asValueRange())
   //   and: "read in again"
   //   def fixture = new PropertyFileDatabase(tempDir.getAbsolutePath())
   //   fixture.readBasePathProperties()
   //   then: "the value has changed"
   //   fixture.getPropertiesTable().get(bean, "sv") == "Funktion"
   //   and: "remaining unicode values have not double backslashes"
   //   !fixture.getPropertiesTable().get(bean2, "sv").contains("\\\\")
   //   cleanup:
   //   tempDir.delete()
   //}

   //def "changeSet: testing change set calculation with real data"() {
   //   given:
   //   def bean = new ClassKeyBean("/DiffusionCalculatorConfiguration", "profileType.function.text")
   //   def server = new PropertiesTable()
   //   server.load(PathUtil.toPlatformPath("src/test/resources/testData/server.json"))
   //   assert server.get(bean, "sv") == "Funktion"
   //   and:
   //   def web = new PropertiesTable()
   //   web.load(PathUtil.toPlatformPath("src/test/resources/testData/web.json"))
   //   assert web.get(bean, "sv") != "Funktion"
   //   when:
   //   def changeSet = PropertiesTable.calculateChangeSet(server, web)
   //   then:
   //   changeSet.size() == 21
   //   changeSet.get(bean, "sv") != "Funktion"
   //}

}
