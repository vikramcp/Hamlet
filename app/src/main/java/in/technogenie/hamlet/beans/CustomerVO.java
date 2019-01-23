package in.technogenie.hamlet.beans;

import java.io.Serializable;
import java.util.Date;

import in.technogenie.hamlet.utils.Utility;

public class CustomerVO implements Serializable {
    private String customerId;
    private String membershipID;
	private String name;
	private String jcrtName;
	private String currentRole;
	private String lomName;
	private String mobile;
	private String emailID;
	private Date dateOfBirth;
	private Date anniversary;
	private String address;
	private String officeAddress;
	private Integer age;
	private String qualification;
    private String companyName;
    private String companyDesignation;
	private String category;
	private String products;
	private String website;
	private String bloodGroup;
	private Date joinedDate;
	private String imageURL;

    public String getCustomerId() {
		return customerId;
	}

    public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return Utility.toTitleCase(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentRole() {
		return Utility.toTitleCase(currentRole);
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getLomName() {
		return lomName;
	}

	public void setLomName(String lomName) {
		this.lomName = lomName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getAnniversary() {
		return anniversary;
	}

	public void setAnniversary(Date anniversary) {
		this.anniversary = anniversary;
	}

	public String getAddress() {
		return address.replaceAll("\\s+", " ");
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

    public String getCompanyName() {
        return Utility.toTitleCase(companyName);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

    public String getMembershipID() {
        return membershipID;
    }

    public void setMembershipID(String membershipID) {
        this.membershipID = membershipID;
	}

	public String getJcrtName() {
		return jcrtName;
	}

	public void setJcrtName(String jcrtName) {
		this.jcrtName = jcrtName;
	}

	public String getOfficeAddress() {
		return officeAddress.replaceAll("\\s+", " ");
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerVO{");
        sb.append("customerId=").append(customerId);
        sb.append(", membershipID='").append(membershipID).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", jcrtName='").append(jcrtName).append('\'');
        sb.append(", currentRole='").append(currentRole).append('\'');
        sb.append(", lomName='").append(lomName).append('\'');
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append(", emailID='").append(emailID).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", anniversary=").append(anniversary);
        sb.append(", address='").append(address).append('\'');
        sb.append(", officeAddress='").append(officeAddress).append('\'');
        sb.append(", age=").append(age);
        sb.append(", qualification='").append(qualification).append('\'');
        sb.append(", companyName='").append(companyName).append('\'');
        sb.append(", companyDesignation='").append(companyDesignation).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", products='").append(products).append('\'');
        sb.append(", website='").append(website).append('\'');
        sb.append(", bloodGroup='").append(bloodGroup).append('\'');
        sb.append(", joinedDate=").append(joinedDate);
        sb.append(", imageURL='").append(imageURL).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getCompanyDesignation() {
        return Utility.toTitleCase(companyDesignation);
    }

    public void setCompanyDesignation(String companyDesignation) {
        this.companyDesignation = companyDesignation;
    }

}
