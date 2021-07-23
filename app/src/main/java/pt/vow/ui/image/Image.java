package pt.vow.ui.image;

import java.io.Serializable;

public class Image implements Serializable {
    private byte[] image;
    private String objName;

    public Image (byte[] image, String objName) {
        this.image = image;
        this.objName = objName;
    }

    public byte[] getImageBytes() {
        return image;
    }

    public String getObjName() { return objName; }
}
