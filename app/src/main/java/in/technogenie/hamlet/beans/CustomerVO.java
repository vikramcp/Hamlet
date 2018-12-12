package in.technogenie.hamlet.beans;

import java.io.Serializable;
import java.sql.Date;

import in.technogenie.hamlet.utils.Utility;

public class CustomerVO implements Serializable {
	private int customerId;
	private String jciID;
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
	private String occupation;
	private String category;
	private String products;
	private String website;
	private String bloodGroup;
	private Date joinedDate;
	private String imageURL;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
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

	public String getOccupation() {
		return Utility.toTitleCase(occupation);
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
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

	public String getJciID() {
		return jciID;
	}

	public void setJciID(String jciID) {
		this.jciID = jciID;
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
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerVO [customerId=");
		builder.append(customerId);
		builder.append(", jciID=");
		builder.append(jciID);
		builder.append(", ");
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (jcrtName != null) {
			builder.append("jcrtName=");
			builder.append(jcrtName);
			builder.append(", ");
		}
		if (currentRole != null) {
			builder.append("currentRole=");
			builder.append(currentRole);
			builder.append(", ");
		}
		if (lomName != null) {
			builder.append("lomName=");
			builder.append(lomName);
			builder.append(", ");
		}
		if (mobile != null) {
			builder.append("mobile=");
			builder.append(mobile);
			builder.append(", ");
		}
		if (emailID != null) {
			builder.append("emailID=");
			builder.append(emailID);
			builder.append(", ");
		}
		if (dateOfBirth != null) {
			builder.append("dateOfBirth=");
			builder.append(dateOfBirth);
			builder.append(", ");
		}
		if (anniversary != null) {
			builder.append("anniversary=");
			builder.append(anniversary);
			builder.append(", ");
		}
		if (address != null) {
			builder.append("address=");
			builder.append(address.replaceAll("\\s+", " "));
			builder.append(", ");
		}
		if (officeAddress != null) {
			builder.append("officeAddress=");
			builder.append(officeAddress.replaceAll("\\s+", " "));
			builder.append(", ");
		}
		if (age != null) {
			builder.append("age=");
			builder.append(age);
			builder.append(", ");
		}
		if (qualification != null) {
			builder.append("qualification=");
			builder.append(qualification);
			builder.append(", ");
		}
		if (occupation != null) {
			builder.append("occupation=");
			builder.append(occupation);
			builder.append(", ");
		}
		if (category != null) {
			builder.append("category=");
			builder.append(category);
			builder.append(", ");
		}
		if (bloodGroup != null) {
			builder.append("bloodGroup=");
			builder.append(bloodGroup);
			builder.append(", ");
		}
		if (joinedDate != null) {
			builder.append("joinedDate=");
			builder.append(joinedDate);
			builder.append(", ");
		}
		if (products != null) {
			builder.append("products=");
			builder.append(products);
			builder.append(", ");
		}
		if (website != null) {
			builder.append("website=");
			builder.append(website);
			builder.append(", ");
		}
		if (imageURL != null) {
			builder.append("imageURL=");
			builder.append(imageURL);
		}
		builder.append("]");
		return builder.toString();
	}

}
