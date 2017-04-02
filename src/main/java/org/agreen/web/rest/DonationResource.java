package org.agreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.agreen.domain.Donation;
import org.agreen.repository.DonationRepository;
import org.agreen.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Donation.
 */
@RestController
@RequestMapping("/api")
public class DonationResource {

    private final Logger log = LoggerFactory.getLogger(DonationResource.class);
        
    @Inject
    private DonationRepository donationRepository;
    
    /**
     * POST  /donations : Create a new donation.
     *
     * @param donation the donation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new donation, or with status 400 (Bad Request) if the donation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/donations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Donation> createDonation(@RequestBody Donation donation) throws URISyntaxException {
        log.debug("REST request to save Donation : {}", donation);
        if (donation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("donation", "idexists", "A new donation cannot already have an ID")).body(null);
        }
        Donation result = donationRepository.save(donation);
        return ResponseEntity.created(new URI("/api/donations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("donation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /donations : Updates an existing donation.
     *
     * @param donation the donation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated donation,
     * or with status 400 (Bad Request) if the donation is not valid,
     * or with status 500 (Internal Server Error) if the donation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/donations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Donation> updateDonation(@RequestBody Donation donation) throws URISyntaxException {
        log.debug("REST request to update Donation : {}", donation);
        if (donation.getId() == null) {
            return createDonation(donation);
        }
        Donation result = donationRepository.save(donation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("donation", donation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /donations : get all the donations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of donations in body
     */
    @RequestMapping(value = "/donations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Donation> getAllDonations() {
        log.debug("REST request to get all Donations");
        List<Donation> donations = donationRepository.findAll();
        return donations;
    }

    /**
     * GET  /donations/:id : get the "id" donation.
     *
     * @param id the id of the donation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the donation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/donations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Donation> getDonation(@PathVariable String id) {
        log.debug("REST request to get Donation : {}", id);
        Donation donation = donationRepository.findOne(id);
        return Optional.ofNullable(donation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /donations/:id : delete the "id" donation.
     *
     * @param id the id of the donation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/donations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDonation(@PathVariable String id) {
        log.debug("REST request to delete Donation : {}", id);
        donationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("donation", id.toString())).build();
    }

}
