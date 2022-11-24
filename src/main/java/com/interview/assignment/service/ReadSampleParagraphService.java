package com.interview.assignment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
@Slf4j
public class ReadSampleParagraphService {

  @Autowired
  @Qualifier("sampleParagraph")
  private String paragraph;

  @PostConstruct
  public void init() {
    log.info("paragraph:\n" + paragraph);
  }






}
