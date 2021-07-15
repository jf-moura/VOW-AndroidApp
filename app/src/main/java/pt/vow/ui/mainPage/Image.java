package pt.vow.ui.mainPage;

public class Image {
    private byte[] image;
    private String objName;

    Image (byte[] image, String objName) {
        this.image = image;
        this.objName = objName;
    }

    public byte[] getImage() {
        return image;
    }

    public String getObjName() { return objName; }
}
