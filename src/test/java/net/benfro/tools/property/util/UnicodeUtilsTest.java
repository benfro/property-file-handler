package net.benfro.tools.property.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;



class UnicodeUtilsTest {

   //def "LoadConvert"() {
   //   given:
   //   String unicode = "\\u041F\\u043E\\u043A\\u0430\\u0437\\u0430\\u0442\\u044C \\u0436\\u0443\\u0440\\u043D\\u0430\\u043B \\u043E\\u0448\\u0438\\u0431\\u043E\\u043A"
   //   when:
   //   def len = unicode.count("\\u") + unicode.count(" ")
   //   def resultingString = UnicodeUtils.loadConvert(unicode)
   //   then:
   //   resultingString.length() == len
   //   println resultingString
//
   //}

   @Test
   void testLoadConvert() {
      String unicode = "\\u041F\\u043E\\u043A\\u0430\\u0437\\u0430\\u0442\\u044C \\u0436\\u0443\\u0440\\u043D\\u0430\\u043B \\u043E\\u0448\\u0438\\u0431\\u043E\\u043A";
      final int len = StringUtils.countMatches(unicode, "\\u") + StringUtils.countMatches(unicode, " ");
      final String s = UnicodeUtils.loadConvert(unicode);
      assertEquals(len, s.length());
   }


   //def "SaveConvert saves with lower case unicode Java string"() {
   //   given:
   //   String unicode = "\\u041F\\u043E\\u043A\\u0430\\u0437\\u0430\\u0442\\u044C \\u0436\\u0443\\u0440\\u043D\\u0430\\u043B \\u043E\\u0448\\u0438\\u0431\\u043E\\u043A"
   //   when:
   //   def humanReadable = UnicodeUtils.loadConvert(unicode)
   //   println humanReadable
   //   String fixture = unicode
   //   then:
   //   UnicodeUtils.saveConvert(humanReadable).equals(fixture)
   //}

   //@Ignore('unicode characters are weird animals where the a-e characters are sometimes upper-, sometimes lower case')
   //def "revealing Unicode character secrets"() {
   //   when:
   //   String unicode = "\u041F"
   //   String unicodeVariant = "\u041f"
   //   String unicode2 = "\\u041F"
//
   //   then:
   //   unicode == unicodeVariant
   //   !unicode.contains("F")
   //   unicode2.contains("F")
   //   unicode2.replace('F', 'f').contains("f")
   //}

   //@Ignore('unicode characters are weird animals where the a-e characters are sometimes upper-, sometimes lower case')
   //def "lower case to upper case - why?"() {
   //   given:
   //   String unicode = '\u041e\u0431\u0440\u0430\u0442\u043d\u043e\u0435 \u043e\u0442 \u0441\u0443\u043c\u043c\u044b \u0447\u0430\u0441\u0442\u043d\u044b\u0445'
   //   when:
   //   def humanReadable = UnicodeUtils.loadConvert(unicode)
   //   def convert = UnicodeUtils.saveConvert(humanReadable)
   //   then:
   //   def tempDir = File.createTempDir()
   //   def file = new File(tempDir, "output.properties")
   //   3.times {
   //      file << UnicodeUtils.saveConvert(humanReadable) << "\n"
   //   }
   //   //unicode == convert
   //   file.readLines().each {
   //      println it
   //   }
   //   cleanup:
   //   file.delete()
   //   tempDir.delete()
   //}

}