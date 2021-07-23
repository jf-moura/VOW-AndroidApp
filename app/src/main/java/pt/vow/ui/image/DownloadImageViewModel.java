package pt.vow.ui.image;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;

import pt.vow.R;

public class DownloadImageViewModel extends ViewModel {
    private final Executor executor;
    private MutableLiveData<GetImageResult> downloadResult = new MutableLiveData<>();
    private MutableLiveData<Image> image = new MutableLiveData<>();

    DownloadImageViewModel(Executor executor) {
        this.executor = executor;
    }

    public LiveData<GetImageResult> getDownloadResult() {
        return downloadResult;
    }

    public void downloadImage(String projectId, String bucketName, String objectName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
                Blob blob = storage.get(BlobId.of(bucketName, objectName));
                if (blob != null) {
                    blob.downloadTo(out);
                    byte[] imageInByte = out.toByteArray();
                    if (imageInByte != null) {
                        Image img = new Image(imageInByte, objectName);
                        image.postValue(img);
                        downloadResult.postValue(new GetImageResult(new Image(imageInByte, objectName)));
                    } else {
                        downloadResult.postValue(new GetImageResult(R.string.image_download_failed));
                    }
                }
            }
        });
        out.close();
    }

    public MutableLiveData<Image> getImage() { return image; }

}
