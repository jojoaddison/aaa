package org.agreen.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Donation.
 */

@Document(collection = "donation")
public class Donation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("amount")
    private Double amount;

    @Field("member_id")
    private String memberId;

    @Field("donated_date")
    private ZonedDateTime donatedDate;

    @Field("project_id")
    private String projectId;

    @Field("type")
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public ZonedDateTime getDonatedDate() {
        return donatedDate;
    }

    public void setDonatedDate(ZonedDateTime donatedDate) {
        this.donatedDate = donatedDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Donation donation = (Donation) o;
        if(donation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, donation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Donation{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", memberId='" + memberId + "'" +
            ", donatedDate='" + donatedDate + "'" +
            ", projectId='" + projectId + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
