package com.example.urlshortener.repository;

import com.example.urlshortener.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {
    Url findByShortUrl(String shortUrl);
    Url findByOriginalUrl(String originalUrl);
    boolean existsByShortUrl(String shortUrl);
}