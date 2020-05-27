package net.benfro;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import net.benfro.tools.property.data.PropertiesTable;
import net.benfro.tools.property.data.PropertyDatabase;
import net.benfro.tools.property.query.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;



public class Application {

   private static PropertyDatabase propertyFileDatabase;
   private static final HelpFormatter formatter = new HelpFormatter();
   private static final Options options = new Options();
   private static final List<Query> queries = Lists.newArrayList();

   static {
      queries.add(new EmptyTranslationsQuery());
      queries.add(new PlaceHolderDiffQuery());
      queries.add(new DefaultFrequencyTable());

      options.addOption("h", "help", false, "print this message");
      options.addOption("q", "quit", false, "exit the application");
      options.addOption("s", "screen", false, "output the database to screen");
      options.addOption(Option.builder("r").longOpt("read").desc("create properties database from code base").numberOfArgs(1)
                              .argName("code_base_path").build());
      options.addOption(Option.builder("i").longOpt("import").desc("import *.tsv file into the code base").numberOfArgs(1)
                              .argName("path_to_file").build());
      options.addOption(Option.builder("e").longOpt("export").desc("output the database to *.csv file").numberOfArgs(1)
                              .argName("out_file_path").build());
      options.addOption(Option.builder("f").longOpt("find")
                              .desc("run query\n\t0 - empty entries\n\t1 - place holder diff\n\t2 - freq table").numberOfArgs(1)
                              .argName("queryindex").build());
   }

   private boolean running = true;

   public static void main(String[] args) throws ParseException, IOException {
      Application app = new Application();
      CommandLineParser parser = new DefaultParser();
      Scanner scanner = new Scanner(System.in);
      app.printHelp();
      while (app.running) {
         String input = scanner.nextLine();
         if (input.isEmpty()) {
            continue;
         }
         String[] argAr = input.split(" ");
         CommandLine line = parser.parse(options, argAr);

         if (line.hasOption("q")) {
            app.running = false;
         } else if (line.hasOption("h")) {
            app.printHelp();
         } else if (line.hasOption("s")) {
            System.out.println(propertyFileDatabase.asCSV());
         } else if (line.hasOption("r")) {
            String databasePath = line.getOptionValue("r");
            app.createDatabase(databasePath);
         } else if (line.hasOption("e")) {
            String filePath = line.getOptionValue("e");
            app.exportToFile(filePath);
         } else if (line.hasOption("i")) {
            String filePath = line.getOptionValue("i");
            app.importIntoCodeBase(filePath);
         } else if (line.hasOption("f")) {
            int index = Integer.parseInt(line.getOptionValue("f"));
            app.invokeQueryByIndex(index);
         } else {
            throw new IllegalArgumentException("Not an existing option");
         }
      }
   }

   private void invokeQueryByIndex(int index) {
      System.out.println("Following data found:");
      if (index < 0 || index > 2) {
         System.out.println("No such query!");
      }
      if (index <= 1) {
         final PropertiesQuery propertiesQuery = (PropertiesQuery)queries.get(index);
         final PropertiesTable resultFromQuery = propertiesQuery.performQuery(propertyFileDatabase.getPropertiesTable());
         System.out.println(resultFromQuery.toCSV());
      } else {
         final DefaultFrequencyTable fTable = (DefaultFrequencyTable) queries.get(index);
         for (Map.Entry<String, Integer> e : fTable.performQuery(propertyFileDatabase.getPropertiesTable()).
                  entrySet()) {
            System.out.println(e.getKey() + " instances: " + e.getValue());
         }
      }
   }

   private void createDatabase(String databasePath) {
      propertyFileDatabase = new PropertyDatabase(databasePath);
      propertyFileDatabase.readBasePathProperties();
      System.out.println("Database created");
   }

   private void exportToFile(String filePath) throws IOException {
      FileUtils.writeLines(new File(filePath), propertyFileDatabase.asListOfCSVRows());
      System.out.println("File written to " + filePath);
   }

   private void printHelp() {
      String header = StringUtils.center(" Translation Helper ", formatter.getWidth(), '*');
      String footer = StringUtils.center(" Thermo-Calc Software AB, 2019 ", formatter.getWidth(), '*');
      formatter.printHelp("{ -e | -f | -h | -i | -q | -r | -s }", header, options, footer);
   }

   private void importIntoCodeBase(String filePath) throws IOException {
      List<String> rows = FileUtils.readLines(new File(filePath), "UTF-8");
      List<List<String>> values = Lists.newArrayList();
      for (String row : rows) {
         String[] splitRow = row.split("\t");
         List<String> temp = Lists.newArrayList();
         temp.addAll(Arrays.stream(splitRow).collect(Collectors.toList()));
         values.add(temp);
      }
      System.out.println(propertyFileDatabase.pushBasePathProperties(values));
   }
}
