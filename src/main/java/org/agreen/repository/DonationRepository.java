package org.agreen.repository;

import org.agreen.domain.Donation;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Donation entity.
 */
@SuppressWarnings("unused")
public interface DonationRepository extends MongoRepository<Donation,String> {

}
