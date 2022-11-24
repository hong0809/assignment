package com.interview.assignment.controller;

import com.interview.assignment.model.SearchRequest;
import com.interview.assignment.model.SearchResult;
import com.interview.assignment.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("counter-api")
@Slf4j
@CrossOrigin
public class CounterController {

  @Autowired
  private SearchService searchService;

  @PostMapping("/search")
  public SearchResult getSearchResult(@RequestBody SearchRequest request) {
    SearchResult result = new SearchResult();
    result.setCounts(searchService.search(request.getSearchText()));
    return result;
  }

  @GetMapping(value = "/top/{value}", produces = "text/csv")
  public ResponseEntity<Resource> exportCSV(@PathVariable Integer value) {
    Map<String, Integer> map = searchService.getOccurrence();
//    list.sort(Map.Entry.comparingByValue());
    List<Map.Entry<String, Integer>> list = map.entrySet().stream()
      .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
      .collect(Collectors.toList());
    List<List<String>> csvBody = new ArrayList<>();
    // i < list.size --> prevent list index out of bound
    for (int i = 0; i < value && i < list.size(); i++) {
      csvBody.add(Arrays.asList(list.get(i).getKey(), list.get(i).getValue().toString()));
    }

    ByteArrayInputStream byteArrayOutputStream;

    try (
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      // defining the CSV printer
      CSVPrinter csvPrinter = new CSVPrinter(
        new PrintWriter(out),
        // withHeader is optional
        CSVFormat.DEFAULT
      );
    ) {
      // populating the CSV content
      for (List<String> record : csvBody) {
        csvPrinter.printRecord(record);
      }

      // writing the underlying stream
      csvPrinter.flush();
      byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "result.csv");
    return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
  }


}
