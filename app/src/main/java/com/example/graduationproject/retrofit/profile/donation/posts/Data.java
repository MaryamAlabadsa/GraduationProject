
package com.example.graduationproject.retrofit.profile.donation.posts;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Data implements Serializable
{

    @SerializedName("donations posts")
    @Expose
    private List<DonationsPost> donationsPosts = null;
    private final static long serialVersionUID = -8050799946207601285L;

    public List<DonationsPost> getDonationsPosts() {
        return donationsPosts;
    }

    public void setDonationsPosts(List<DonationsPost> donationsPosts) {
        this.donationsPosts = donationsPosts;
    }

}
