package net.benfro.tools.property.util;

public class UnicodeUtils {

   public static String loadConvert(String stringWithUnicode) {
      return loadConvertInternal(stringWithUnicode.toCharArray(), 0, stringWithUnicode.length(), new char[1024]);
   }

   public static String saveConvert(String stringToBeUnicoded) {
      return saveConvertInternal(stringToBeUnicoded, false, true);
   }

   /**
    * Converts encoded &#92;uxxxx to unicode chars
    *  and changes special saved chars to their original forms
    */
   private static String loadConvertInternal(char[] inbuf, int off, int len, char[] convtBuf) {
      if (convtBuf.length < len) {
         int newLen = len * 2;
         if (newLen < 0) {
            newLen = Integer.MAX_VALUE;
         }
         convtBuf = new char[newLen];
      }
      Character aChar;
      char[] out = convtBuf;
      int outLen = 0;
      int end = off + len;

      while (off < end) {
         aChar = inbuf[off++];
         if (aChar == '\\') {
            aChar = inbuf[off++];
            if (aChar == 'u') {
               // Read the xxxx
               int value = 0;
               for (int i = 0; i < 4; i++) {
                  aChar = inbuf[off++];
                  switch (aChar) {
                     case '0':
                     case '1':
                     case '2':
                     case '3':
                     case '4':
                     case '5':
                     case '6':
                     case '7':
                     case '8':
                     case '9':
                        value = (value << 4) + aChar - '0';
                        break;
                     case 'a':
                     case 'b':
                     case 'c':
                     case 'd':
                     case 'e':
                     case 'f':
                        value = (value << 4) + 10 + aChar - 'a';
                        break;
                     case 'A':
                     case 'B':
                     case 'C':
                     case 'D':
                     case 'E':
                     case 'F':
                        value = (value << 4) + 10 + aChar -'A';
                        break;
                     default:
                        throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                  }
               }
               out[outLen++] = (char) value;
            } else {
               if (aChar == 't') aChar = '\t';
               else if (aChar == 'r') aChar = '\r';
               else if (aChar == 'n') aChar = '\n';
               else if (aChar == 'f') aChar = '\f';
               out[outLen++] = aChar;
            }
         } else {
            out[outLen++] = aChar;
         }
      }
      return new String(out, 0, outLen);
   }

   /*
    * Converts unicodes to encoded &#92;uxxxx and escapes
    * special characters with a preceding slash
    */
   private static String saveConvertInternal(String theString,
            boolean escapeSpace,
            boolean escapeUnicode) {
      int len = theString.length();
      int bufLen = len * 2;
      if (bufLen < 0) {
         bufLen = Integer.MAX_VALUE;
      }
      StringBuilder outBuffer = new StringBuilder(bufLen);

      for (int x = 0; x < len; x++) {
         char aChar = theString.charAt(x);
         // Handle common case first, selecting largest block that
         // avoids the specials below
         if ((aChar > 61) && (aChar < 127)) {
            if (aChar == '\\') {
               outBuffer.append('\\');
               outBuffer.append('\\');
               continue;
            }
            outBuffer.append(aChar);
            continue;
         }
         switch (aChar) {
            case ' ':
               if (x == 0 || escapeSpace)
                  outBuffer.append('\\');
               outBuffer.append(' ');
               break;
            case '\t':
               outBuffer.append('\\');
               outBuffer.append('t');
               break;
            case '\n':
               outBuffer.append('\\');
               outBuffer.append('n');
               break;
            case '\r':
               outBuffer.append('\\');
               outBuffer.append('r');
               break;
            case '\f':
               outBuffer.append('\\');
               outBuffer.append('f');
               break;
            case '=': // Fall through
            case ':': // Fall through
            case '#': // Fall through
            case '!':
               outBuffer.append('\\');
               outBuffer.append(aChar);
               break;
            default:
               if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode) {
                  outBuffer.append('\\');
                  outBuffer.append('u');
                  outBuffer.append(toHex((aChar >> 12) & 0xF));
                  outBuffer.append(toHex((aChar >> 8) & 0xF));
                  outBuffer.append(toHex((aChar >> 4) & 0xF));
                  outBuffer.append(toHex(aChar & 0xF));
               } else {
                  outBuffer.append(aChar);
               }
         }
      }
      return outBuffer.toString();
   }

   private static char toHex(int nibble) {
      return hexDigit[(nibble & 0xF)];
   }

   private static final char[] hexDigit = "0123456789ABCDEF".toCharArray();
}
