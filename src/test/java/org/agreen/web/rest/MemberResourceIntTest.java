package org.agreen.web.rest;

import org.agreen.AgreenApp;
import org.agreen.domain.Member;
import org.agreen.repository.MemberRepository;

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
 * Test class for the MemberResource REST controller.
 *
 * @see MemberResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AgreenApp.class)
@WebAppConfiguration
@IntegrationTest
public class MemberResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_MIDDLE_NAMES = "AAAAA";
    private static final String UPDATED_MIDDLE_NAMES = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_MOBILE_PHONE = "AAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBB";
    private static final String DEFAULT_TELEPHONE_NUMBER = "AAAAA";
    private static final String UPDATED_TELEPHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_DISPLAY = "AAAAA";
    private static final String UPDATED_DISPLAY = "BBBBB";
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";
    private static final String DEFAULT_DONATIONS = "AAAAA";
    private static final String UPDATED_DONATIONS = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATED_DATE);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFIED_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFIED_DATE);

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberMockMvc;

    private Member member;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberResource memberResource = new MemberResource();
        ReflectionTestUtils.setField(memberResource, "memberRepository", memberRepository);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberRepository.deleteAll();
        member = new Member();
        member.setFirstName(DEFAULT_FIRST_NAME);
        member.setMiddleNames(DEFAULT_MIDDLE_NAMES);
        member.setLastName(DEFAULT_LAST_NAME);
        member.setEmail(DEFAULT_EMAIL);
        member.setTitle(DEFAULT_TITLE);
        member.setMobilePhone(DEFAULT_MOBILE_PHONE);
        member.setTelephoneNumber(DEFAULT_TELEPHONE_NUMBER);
        member.setDisplay(DEFAULT_DISPLAY);
        member.setStatus(DEFAULT_STATUS);
        member.setDonations(DEFAULT_DONATIONS);
        member.setCreatedDate(DEFAULT_CREATED_DATE);
        member.setModifiedDate(DEFAULT_MODIFIED_DATE);
    }

    @Test
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member

        restMemberMockMvc.perform(post("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(member)))
                .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = members.get(members.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMember.getMiddleNames()).isEqualTo(DEFAULT_MIDDLE_NAMES);
        assertThat(testMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMember.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMember.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMember.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testMember.getTelephoneNumber()).isEqualTo(DEFAULT_TELEPHONE_NUMBER);
        assertThat(testMember.getDisplay()).isEqualTo(DEFAULT_DISPLAY);
        assertThat(testMember.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMember.getDonations()).isEqualTo(DEFAULT_DONATIONS);
        assertThat(testMember.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMember.getModifiedDate()).isEqualTo(DEFAULT_MODIFIED_DATE);
    }

    @Test
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.save(member);

        // Get all the members
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].middleNames").value(hasItem(DEFAULT_MIDDLE_NAMES.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE.toString())))
                .andExpect(jsonPath("$.[*].telephoneNumber").value(hasItem(DEFAULT_TELEPHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].display").value(hasItem(DEFAULT_DISPLAY.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].donations").value(hasItem(DEFAULT_DONATIONS.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE_STR)))
                .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(DEFAULT_MODIFIED_DATE_STR)));
    }

    @Test
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.save(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(member.getId()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.middleNames").value(DEFAULT_MIDDLE_NAMES.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE.toString()))
            .andExpect(jsonPath("$.telephoneNumber").value(DEFAULT_TELEPHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.display").value(DEFAULT_DISPLAY.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.donations").value(DEFAULT_DONATIONS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR))
            .andExpect(jsonPath("$.modifiedDate").value(DEFAULT_MODIFIED_DATE_STR));
    }

    @Test
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateMember() throws Exception {
        // Initialize the database
        memberRepository.save(member);
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = new Member();
        updatedMember.setId(member.getId());
        updatedMember.setFirstName(UPDATED_FIRST_NAME);
        updatedMember.setMiddleNames(UPDATED_MIDDLE_NAMES);
        updatedMember.setLastName(UPDATED_LAST_NAME);
        updatedMember.setEmail(UPDATED_EMAIL);
        updatedMember.setTitle(UPDATED_TITLE);
        updatedMember.setMobilePhone(UPDATED_MOBILE_PHONE);
        updatedMember.setTelephoneNumber(UPDATED_TELEPHONE_NUMBER);
        updatedMember.setDisplay(UPDATED_DISPLAY);
        updatedMember.setStatus(UPDATED_STATUS);
        updatedMember.setDonations(UPDATED_DONATIONS);
        updatedMember.setCreatedDate(UPDATED_CREATED_DATE);
        updatedMember.setModifiedDate(UPDATED_MODIFIED_DATE);

        restMemberMockMvc.perform(put("/api/members")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMember)))
                .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeUpdate);
        Member testMember = members.get(members.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getMiddleNames()).isEqualTo(UPDATED_MIDDLE_NAMES);
        assertThat(testMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMember.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMember.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMember.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testMember.getTelephoneNumber()).isEqualTo(UPDATED_TELEPHONE_NUMBER);
        assertThat(testMember.getDisplay()).isEqualTo(UPDATED_DISPLAY);
        assertThat(testMember.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMember.getDonations()).isEqualTo(UPDATED_DONATIONS);
        assertThat(testMember.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMember.getModifiedDate()).isEqualTo(UPDATED_MODIFIED_DATE);
    }

    @Test
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.save(member);
        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Get the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(databaseSizeBeforeDelete - 1);
    }
}
