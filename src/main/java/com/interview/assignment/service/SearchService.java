package com.interview.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SearchService {

  @Autowired
  @Qualifier("sampleParagraph")
  private String paragraph;

  public List<Map<String, Integer>> search(List<String> targets) {
    List<Map<String, Integer>> result = new ArrayList<>();
    for (String target : targets) {
      Map<String, Integer> map = new HashMap<>();
      map.put(target, findCount(target));
      result.add(map);
    }
    return result;
  }

  public Map<String, Integer> getOccurrence() {
    Map <String, Integer> map = new HashMap<>();
    String result = paragraph.replaceAll("\n", " ").replaceAll("[^a-zA-Z0-9 _-]", " ");

    List<String> words = Arrays.stream(
      result.split("\\s+")).toList();
    for (String word : words) {
      if (map.containsKey(word)) {
        map.put(word, map.get(word) + 1);
      } else {
        map.put(word, 1);
      }
    }
    return map;
  }

  private int findCount(String target) {
    Matcher matcher = Pattern.compile(Pattern.quote(target), Pattern.CASE_INSENSITIVE)
      .matcher(paragraph);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }
}
