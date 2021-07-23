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

public class DeleteImageViewModel extends ViewModel {
    private final Executor executor;

    DeleteImageViewModel(Executor executor) {
        this.executor = executor;
    }

    public void deleteImage(String projectId, String bucketName, String objectName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
                storage.delete(bucketName, objectName);
            }
        });
        out.close();
    }

}
