package org.agreen.repository;

import org.agreen.domain.FileDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the FileDocument entity.
 */
@SuppressWarnings("unused")
public interface FileDocumentRepository extends MongoRepository<FileDocument,String> {

}
