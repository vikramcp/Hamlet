package in.technogenie.hamlet.beans;

import java.io.Serializable;
import java.util.Date;

public class Upload implements Serializable {
    private String name;
    private String url;
    private Date uploadDate;
    private boolean isSelected = false;

    public Upload() {
    }

    public Upload(String name, String url, Date uploadDate) {
        this.name = name;
        this.url = url;
        this.uploadDate = uploadDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Upload{");
        sb.append("name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", uploadDate=").append(uploadDate);
        sb.append(", isSelected=").append(isSelected);
        sb.append('}');
        return sb.toString();
    }
}
