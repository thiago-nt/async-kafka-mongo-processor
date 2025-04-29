package com.processor.async.repository;

import com.processor.async.domain.DocumentMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentMongoRepository extends MongoRepository<DocumentMongo, String> {
}
