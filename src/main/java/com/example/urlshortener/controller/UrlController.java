package com.example.urlshortener.controller;

import com.example.urlshortener.model.Url;
import com.example.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UrlController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/shorten")
    @ResponseBody
    public String shortenUrl(@RequestParam("url") String originalUrl) {
        return urlService.shortenUrl(originalUrl);
    }

    @GetMapping("/{shortUrl}")
    public void redirectToOriginalUrl(@PathVariable String shortUrl,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String originalUrl = urlService.getOriginalUrl(shortUrl, request);
        if (originalUrl != null) {
            response.sendRedirect(originalUrl);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @GetMapping("/stats/{shortUrl}")
    public String showStats(@PathVariable String shortUrl, Model model) {
        Url url = urlService.getUrlStats(shortUrl);
        if (url == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found");
        }

        model.addAttribute("url", url);
        return "stats";
    }

    @GetMapping(value = "/qr/{shortUrl}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getQRCode(@PathVariable String shortUrl,
                            @RequestParam(defaultValue = "200") int width,
                            @RequestParam(defaultValue = "200") int height,
                            HttpServletResponse response) throws Exception {
        String originalUrl = urlService.getOriginalUrl(shortUrl, null);
        if (originalUrl == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        return urlService.generateQRCodeImage(originalUrl, width, height);
    }
}