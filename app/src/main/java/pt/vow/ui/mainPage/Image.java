package pt.vow.ui.mainPage;

public class Image {
    private byte[] image;
    private int pos;

    Image (byte[] image) {
        this.image = image;
    }

    Image (byte[] image, int pos) {
        this.image = image;
        this.pos = pos;
    }

    public byte[] getImage() {
        return image;
    }

    public int getPosition() { return pos; }
}
