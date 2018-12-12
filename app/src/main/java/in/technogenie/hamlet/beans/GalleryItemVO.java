package in.technogenie.hamlet.beans;

public class GalleryItemVO {

    private String imageUri;
    private String imageName;
    private boolean isSelected = false;

    public GalleryItemVO(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }

    public GalleryItemVO() {

    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GalleryItemVO{");
        sb.append("imageUri='").append(imageUri).append('\'');
        sb.append(", imageName='").append(imageName).append('\'');
        sb.append(", isSelected=").append(isSelected);
        sb.append('}');
        return sb.toString();
    }
}