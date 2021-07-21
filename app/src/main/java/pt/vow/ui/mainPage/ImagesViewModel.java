package pt.vow.ui.mainPage;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class ImagesViewModel extends ViewModel {
    private final Executor executor;
    private Map<String, Image> images = new HashMap<>();

    ImagesViewModel(Executor executor) {
        this.executor = executor;
    }

    public void addImage(Image image) {
        images.put(image.getObjName(), image);
    }

    public Image getImage(String objName) {
        return images.get(objName);
    }

    public Map<String, Image> getImages() { return images; }
}
