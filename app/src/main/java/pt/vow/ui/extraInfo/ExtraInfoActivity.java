package pt.vow.ui.extraInfo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoginActivity;

public class ExtraInfoActivity extends AppCompatActivity {

    private ImageView imageView;
    private CheckBox checkBoxChildren, checkBoxHouseBuilding, checkBoxNature, checkBoxHealth, checkBoxElderly, checkBoxAnimals;
    private Button imageBttn, saveImgBttn;
    private Button nextBttn;
    private ExtraInfoActivity mActivity;
    private static final int IMG_REQUEST = 21;
    private String username;
    private Uri imageUri;
    private Bitmap bitmap;
    private UploadImageViewModel uploadImageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_info);

        mActivity = this;
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

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
                    uploadImage("vow-project-311114", "vow_profile_pictures", username);
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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                imageBttn.setEnabled(false);
                saveImgBttn.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage(String projectId, String bucketName, String objectName) throws IOException {
        if (imageUri != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] imageInByte = out.toByteArray();
            out.close();
            uploadImageViewModel.uploadImage(projectId, bucketName, objectName, imageInByte);
        }
        saveImgBttn.setEnabled(false);
    }


}