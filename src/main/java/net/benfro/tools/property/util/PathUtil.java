package net.benfro.tools.property.util;

import java.io.File;



public class PathUtil {
   static String toPlatformPath(String instr) {
      return instr.replace("/", File.separator);
   }
}
