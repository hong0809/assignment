package com.interview.assignment.configuration;

import com.interview.assignment.util.ResourceReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadResourceConfiguration {

  @Bean
  public String sampleParagraph() {
    return ResourceReader.readFileToString("sample_paragraph.txt");
  }
}
