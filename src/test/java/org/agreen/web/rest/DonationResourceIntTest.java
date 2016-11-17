package org.agreen.web.rest;

import org.agreen.AgreenApp;
import org.agreen.domain.Donation;
import org.agreen.repository.DonationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DonationResource REST controller.
 *
 * @see DonationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AgreenApp.class)
@WebAppConfiguration
@IntegrationTest
public class DonationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final String DEFAULT_MEMBER_ID = "AAAAA";
    private static final String UPDATED_MEMBER_ID = "BBBBB";

    private static final ZonedDateTime DEFAULT_DONATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DONATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DONATED_DATE_STR = dateTimeFormatter.format(DEFAULT_DONATED_DATE);
    private static final String DEFAULT_PROJECT_ID = "AAAAA";
    private static final String UPDATED_PROJECT_ID = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    @Inject
    private DonationRepository donationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDonationMockMvc;

    private Donation donation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DonationResource donationResource = new DonationResource();
        ReflectionTestUtils.setField(donationResource, "donationRepository", donationRepository);
        this.restDonationMockMvc = MockMvcBuilders.standaloneSetup(donationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        donationRepository.deleteAll();
        donation = new Donation();
        donation.setAmount(DEFAULT_AMOUNT);
        donation.setMemberId(DEFAULT_MEMBER_ID);
        donation.setDonatedDate(DEFAULT_DONATED_DATE);
        donation.setProjectId(DEFAULT_PROJECT_ID);
        donation.setType(DEFAULT_TYPE);
    }

    @Test
    public void createDonation() throws Exception {
        int databaseSizeBeforeCreate = donationRepository.findAll().size();

        // Create the Donation

        restDonationMockMvc.perform(post("/api/donations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(donation)))
                .andExpect(status().isCreated());

        // Validate the Donation in the database
        List<Donation> donations = donationRepository.findAll();
        assertThat(donations).hasSize(databaseSizeBeforeCreate + 1);
        Donation testDonation = donations.get(donations.size() - 1);
        assertThat(testDonation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDonation.getMemberId()).isEqualTo(DEFAULT_MEMBER_ID);
        assertThat(testDonation.getDonatedDate()).isEqualTo(DEFAULT_DONATED_DATE);
        assertThat(testDonation.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testDonation.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    public void getAllDonations() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        // Get all the donations
        restDonationMockMvc.perform(get("/api/donations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(donation.getId())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].memberId").value(hasItem(DEFAULT_MEMBER_ID.toString())))
                .andExpect(jsonPath("$.[*].donatedDate").value(hasItem(DEFAULT_DONATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    public void getDonation() throws Exception {
        // Initialize the database
        donationRepository.save(donation);

        // Get the donation
        restDonationMockMvc.perform(get("/api/donations/{id}", donation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(donation.getId()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.memberId").value(DEFAULT_MEMBER_ID.toString()))
            .andExpect(jsonPath("$.donatedDate").value(DEFAULT_DONATED_DATE_STR))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    public void getNonExistingDonation() throws Exception {
        // Get the donation
        restDonationMockMvc.perform(get("/api/donations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateDonation() throws Exception {
        // Initialize the database
        donationRepository.save(donation);
        int databaseSizeBeforeUpdate = donationRepository.findAll().size();

        // Update the donation
        Donation updatedDonation = new Donation();
        updatedDonation.setId(donation.getId());
        updatedDonation.setAmount(UPDATED_AMOUNT);
        updatedDonation.setMemberId(UPDATED_MEMBER_ID);
        updatedDonation.setDonatedDate(UPDATED_DONATED_DATE);
        updatedDonation.setProjectId(UPDATED_PROJECT_ID);
        updatedDonation.setType(UPDATED_TYPE);

        restDonationMockMvc.perform(put("/api/donations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDonation)))
                .andExpect(status().isOk());

        // Validate the Donation in the database
        List<Donation> donations = donationRepository.findAll();
        assertThat(donations).hasSize(databaseSizeBeforeUpdate);
        Donation testDonation = donations.get(donations.size() - 1);
        assertThat(testDonation.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDonation.getMemberId()).isEqualTo(UPDATED_MEMBER_ID);
        assertThat(testDonation.getDonatedDate()).isEqualTo(UPDATED_DONATED_DATE);
        assertThat(testDonation.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testDonation.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    public void deleteDonation() throws Exception {
        // Initialize the database
        donationRepository.save(donation);
        int databaseSizeBeforeDelete = donationRepository.findAll().size();

        // Get the donation
        restDonationMockMvc.perform(delete("/api/donations/{id}", donation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Donation> donations = donationRepository.findAll();
        assertThat(donations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
