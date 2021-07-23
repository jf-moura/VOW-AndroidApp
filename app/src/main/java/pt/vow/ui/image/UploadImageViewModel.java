package pt.vow.ui.image;

import androidx.lifecycle.ViewModel;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.util.concurrent.Executor;

public class UploadImageViewModel extends ViewModel {
    private final Executor executor;

    UploadImageViewModel(Executor executor) {
        this.executor = executor;
    }


    public void uploadImage(String projectId, String bucketName, String objectName, byte[] image) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

                BlobId blobId = BlobId.of(bucketName, objectName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
                storage.create(blobInfo, image);
            }
        });
    }

}
