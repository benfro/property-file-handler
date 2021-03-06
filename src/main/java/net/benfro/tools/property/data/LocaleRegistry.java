package net.benfro.tools.property.data;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum LocaleRegistry {
   INSTANCE;

   private List<String> languages = List.of("en", "sv", "ru", "fi", "fr", "zh", "es");
   final Map<String, ProtoLocale> reg;

   LocaleRegistry() {
      this.reg = languages.stream().collect(Collectors.toUnmodifiableMap(Function.identity(), ProtoLocale::of));
   }

   public ProtoLocale get(String lang) {
      return reg.getOrDefault(lang, reg.get("en"));
   }

   public ProtoLocale getDefault() {
      return reg.get("en");
   }

   /**
    * Put English locale first in column list
    */
   public List<String> customSortedLocaleKeyList() {
      ProtoLocale locale = getDefault();
      List<ProtoLocale> list = Lists.newArrayList(reg.values());
      boolean localeWasInColumnKeySet = list.remove(locale);
      List<String> out = Lists.newArrayList();
      if (localeWasInColumnKeySet) {
         out.add(locale.getLanguage());
      }
      list.forEach(it -> out.add(it.getLanguage()));
      return out;
   }
}
