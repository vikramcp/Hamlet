package in.technogenie.hamlet.beans;

import java.io.Serializable;

import java.sql.Date;

public class EventVO implements Serializable {
	private int eventId;
	private String eventName;
	private String description;
	private String location;
	private Date startTime;
	private Date endTime;
	private String imageURL;

	public EventVO(int eventId, String eventName, String description,
			String location, Date startTime, Date endTime,
			String imageURL) {
		super();
		this.eventId = eventId;
		this.eventName = eventName;
		this.description = description;
		this.location = location;
		this.startTime = startTime;
		this.endTime = endTime;
		this.imageURL = imageURL;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EventVO [");
		if (eventId != 0) {
			builder.append("eventId=");
			builder.append(eventId);
			builder.append(", ");
		}
		if (eventName != null) {
			builder.append("eventName=");
			builder.append(eventName);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (location != null) {
			builder.append("location=");
			builder.append(location);
			builder.append(", ");
		}
		if (startTime != null) {
			builder.append("startTime=");
			builder.append(startTime);
			builder.append(", ");
		}
		if (endTime != null) {
			builder.append("endTime=");
			builder.append(endTime);
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
