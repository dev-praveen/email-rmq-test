package com.email.container.controller;

import com.email.container.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailSender {

  @Autowired private EmailService emailService;

  @GetMapping("/send")
  public void sendEmail(
      @RequestParam String recipient, @RequestParam String subject, @RequestParam String content) {

    emailService.sendEmail(recipient, subject, content);
  }
}
