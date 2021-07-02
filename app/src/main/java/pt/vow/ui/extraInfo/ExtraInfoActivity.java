package pt.vow.ui.extraInfo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import pt.vow.R;
import pt.vow.ui.login.LoginActivity;

public class ExtraInfoActivity extends AppCompatActivity {

    private ImageView imageView;
    private CheckBox checkBoxChildren, checkBoxHouseBuilding, checkBoxNature, checkBoxHealth, checkBoxElderly, checkBoxAnimals;
    private Button imageBttn, saveImgBttn;
    private Button nextBttn;
    private ExtraInfoActivity mActivity;
    private int IMG_REQUEST = 21;
    private String username;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_info);

        mActivity = this;

        username = (String) getIntent().getSerializableExtra("LoggedUser");

        imageView = findViewById(R.id.imageView);
        imageBttn = findViewById(R.id.imageBttn);
        saveImgBttn = findViewById(R.id.saveImageBttn);
        nextBttn = findViewById(R.id.finishBttn);

        checkBoxChildren = findViewById(R.id.checkBoxChildren);
        checkBoxAnimals = findViewById(R.id.checkBoxAnimals);
        checkBoxNature = findViewById(R.id.checkBoxNature);
        checkBoxHealth = findViewById(R.id.checkBoxHealth);
        checkBoxHouseBuilding = findViewById(R.id.checkBoxHouseBuilding);
        checkBoxElderly = findViewById(R.id.checkBoxElderly);

        checkBoxChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxChildren.isChecked()) {
                    checkBoxAnimals.setEnabled(false);
                    checkBoxElderly.setEnabled(false);
                    checkBoxHealth.setEnabled(false);
                    checkBoxHouseBuilding.setEnabled(false);
                    checkBoxNature.setEnabled(false);
                }
            }
        });

        checkBoxAnimals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        checkBoxNature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        checkBoxHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        checkBoxHouseBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        checkBoxElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        imageBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image"), IMG_REQUEST);
            }
        });

        saveImgBttn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    uploadImage("vow-project-311114", "vow_profile_pictures", "manu", imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        nextBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void itemClickedExtraInfo(View v) {
        switch (v.getId()) {
            case R.id.checkBoxChildren:
                if (checkBoxChildren.isChecked()) {
                    checkBoxAnimals.setEnabled(false);
                    checkBoxElderly.setEnabled(false);
                    checkBoxHealth.setEnabled(false);
                    checkBoxHouseBuilding.setEnabled(false);
                    checkBoxNature.setEnabled(false);
                    Toast.makeText(this,
                            "Checked", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.checkBoxAnimals:
                if (checkBoxAnimals.isChecked()) {
                    checkBoxChildren.setEnabled(false);
                    checkBoxElderly.setEnabled(false);
                    checkBoxHealth.setEnabled(false);
                    checkBoxHouseBuilding.setEnabled(false);
                    checkBoxNature.setEnabled(false);
                }
                break;
            case R.id.checkBoxElderly:
                if (checkBoxElderly.isChecked()) {
                    checkBoxAnimals.setEnabled(false);
                    checkBoxChildren.setEnabled(false);
                    checkBoxHealth.setEnabled(false);
                    checkBoxHouseBuilding.setEnabled(false);
                    checkBoxNature.setEnabled(false);
                }
                break;
            case R.id.checkBoxHealth:
                if (checkBoxHealth.isChecked()) {
                    checkBoxAnimals.setEnabled(false);
                    checkBoxElderly.setEnabled(false);
                    checkBoxChildren.setEnabled(false);
                    checkBoxHouseBuilding.setEnabled(false);
                    checkBoxNature.setEnabled(false);
                }
                break;
            case R.id.checkBoxHouseBuilding:
                if (checkBoxHouseBuilding.isChecked()) {
                    checkBoxAnimals.setEnabled(false);
                    checkBoxElderly.setEnabled(false);
                    checkBoxHealth.setEnabled(false);
                    checkBoxChildren.setEnabled(false);
                    checkBoxNature.setEnabled(false);
                }
                break;
            case R.id.checkBoxNature:
                if (checkBoxNature.isChecked()) {
                    checkBoxAnimals.setEnabled(false);
                    checkBoxElderly.setEnabled(false);
                    checkBoxHealth.setEnabled(false);
                    checkBoxHouseBuilding.setEnabled(false);
                    checkBoxChildren.setEnabled(false);
                }
                break;
            default: {
                checkBoxNature.setEnabled(true);
                checkBoxAnimals.setEnabled(true);
                checkBoxElderly.setEnabled(true);
                checkBoxHealth.setEnabled(true);
                checkBoxHouseBuilding.setEnabled(true);
                checkBoxChildren.setEnabled(true);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                imageBttn.setEnabled(false);
                saveImgBttn.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*private void uploadImage() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            byte[] imageInByte = out.toByteArray();
            String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            }*/

            /*String path = getPathFromUri(this, uri);
            String name = getFileName(uri);

            try {
                uploadImage(path, name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);

                // The ID of your GCP project
                String projectId = "vow-project-311114";

                // The ID of your GCS bucket
                String bucketName = "vow_profile_pictures";

                // The ID of your GCS object
                String objectName = username;


                Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
                BlobId blobId = BlobId.of(bucketName, objectName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    storage.create(blobInfo, Files.readAllBytes((Path) imageView));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage(String projectId, String bucketName, String objectName, Uri uri) throws IOException {
        if (imageUri != null) {
            Storage storage = StorageOptions.getDefaultInstance().getService();
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, Files.readAllBytes(Paths.get(String.valueOf(uri))));
            System.out.println("File "  + " uploaded to bucket " + bucketName + " as " + objectName);
        }


        /*FileOutputStream fos = openFileOutput(name, MODE_APPEND);

        File file = new File(path);

        byte[] bytes = getBytesFromFile(file);

        fos.write(bytes);
        fos.close();*/
    }

    /*private byte[] getBytesFromFile(File file) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(file);
        return data;
    }*/

    /*private String getPathFromUri(Context context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        }
        return null;
    }*/

    /*private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst())
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            } finally {
                cursor.close();
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1)
                result = result.substring((cut + 1));
        }
        return result;
    }*/

}