package net.yatopia.bot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.yatopia.bot.mappings.Mapping;
import net.yatopia.bot.mappings.MappingType;
import net.yatopia.bot.mappings.NameType;
import org.jetbrains.annotations.Nullable;

public final class Utils {

  public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
  public static final TriPredicate<NameType, Mapping, String> EXACT =
      (type, mapping, input) -> type.get(mapping).equalsIgnoreCase(input);
  public static final TriPredicate<NameType, Mapping, String> ENDS_WITH =
      (type, mapping, input) -> type.get(mapping).endsWith(input);

  public static <T> List<List<T>> getPages(Collection<T> c, int pageSize) {
    List<T> list = new ArrayList<>(c);
    if (pageSize <= 0 || pageSize > list.size()) {
      pageSize = list.size();
    }
    int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
    List<List<T>> pages = new ArrayList<>(numPages);
    for (int pageNum = 0; pageNum < numPages; ) {
      pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
    }
    return pages;
  }

  public static List<Mapping> parseMappings(
      List<Mapping> mappings,
      @Nullable NameType nameType,
      MappingType mappingType,
      String input,
      TriPredicate<NameType, Mapping, String> filter) {
    String parentSearched = null;
    if (input.indexOf('#') != -1) {
      parentSearched = input.substring(0, input.indexOf('#'));
    }
    if (input.indexOf('.') != -1 && parentSearched == null) {
      parentSearched = input.substring(0, input.indexOf('.'));
    }
    List<Mapping> ret = new ArrayList<>();
    String parentSearchCut =
        parentSearched == null
            ? null
            : input.substring(parentSearched.length() + 1);
    if (parentSearched != null && nameType == null) {
      for (Mapping mapping : mappings) {
        if (mapping.getMappingType() == mappingType && mapping.getParentMapping() != null) {
          for (NameType type : NameType.values()) {
            String parent = type.get(mapping.getParentMapping());
            if (parent != null && parent.endsWith(parentSearched)) {
              String t = type.get(mapping);
              if (t != null && filter.test(type, mapping, parentSearchCut)) {
                ret.add(mapping);
              }
            }
          }
        }
      }
    } else if (parentSearched == null && nameType == null) {
      for (Mapping mapping : mappings) {
        if (mapping.getMappingType() == mappingType) {
          for (NameType type : NameType.values()) {
            if (type.get(mapping) != null && filter.test(type, mapping, input)) {
              ret.add(mapping);
            }
          }
        }
      }
    } else if (parentSearched == null) {
      for (Mapping mapping : mappings) {
        if (mapping.getMappingType() == mappingType
            && nameType.get(mapping) != null
            && filter.test(nameType, mapping, input)) {
          ret.add(mapping);
        }
      }
    } else {
      for (Mapping mapping : mappings) {
        if (mapping.getMappingType() == mappingType && mapping.getParentMapping() != null) {
          String parent = nameType.get(mapping.getParentMapping());
          if (parent != null && parent.endsWith(parentSearched)) {
            String t = nameType.get(mapping);
            if (t != null && filter.test(nameType, mapping, parentSearchCut)) {
              ret.add(mapping);
            }
          }
        }
      }
    }
    return ret;
  }
}
