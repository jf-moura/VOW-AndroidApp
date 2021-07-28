package pt.vow.ui.newActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.image.UploadImageViewModel;
import pt.vow.ui.image.UploadImageViewModelFactory;
import pt.vow.ui.route.NewRouteViewModel;
import pt.vow.ui.route.NewRouteViewModelFactory;

public class NewActivityActivity extends AppCompatActivity {
    private static final int RESULT_OK = -1;

    private EditText editTextName, editTextPartNum, editTextDescription;
    private TextView dateText;
    private RadioGroup rg1, rg2;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button imgPickBttn, dateBttn;

    private String date;
    private String address;
    private String timeZone;
    private String durationInMinutes;
    private Button confirmButton;
    private Uri imageUri;
    private static Bitmap bitmap;
    private List<String> coordinates;
    private String type;

    private NewActivityViewModel newActivityViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private NewRouteViewModel newRouteViewModel;

    private LoggedInUserView user;
    private NewActivityActivity mActivity;
    private Geocoder geocoder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        mActivity = this;

        newActivityViewModel = new ViewModelProvider(this, new NewActivityViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(NewActivityViewModel.class);
        newRouteViewModel = new ViewModelProvider(this, new NewRouteViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(NewRouteViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        coordinates = (ArrayList<String>) getIntent().getSerializableExtra("CoordinateArray");

        geocoder = new Geocoder(this);

        type = "";

        if (coordinates.isEmpty()) {
            address = null;
            Toast.makeText(this, R.string.location_unavailable, Toast.LENGTH_SHORT).show();
        } else {
            String[] latLng = coordinates.get(0).split(",");
            double lat = Double.parseDouble(latLng[0]);
            double lng = Double.parseDouble(latLng[1]);
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses.size() > 0)
                    address = addresses.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        editTextName = findViewById(R.id.editTextNameAct);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPartNum = findViewById(R.id.editTextParticipantNum);
        imageView = findViewById(R.id.imgView);
        imgPickBttn = findViewById(R.id.imgPickBttn);
        dateBttn = findViewById(R.id.bttnDate);
        confirmButton = findViewById(R.id.bttnSaveChanges);
        dateText = findViewById(R.id.dateText);

        rg1 = findViewById(R.id.group1);
        rg2 = findViewById(R.id.group2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

        progressBar = findViewById(R.id.progress_bar_new_activity);

        TimePicker durationPicker = findViewById(R.id.durationPicker);
        durationPicker.setIs24HourView(true);
        durationPicker.setHour(0);
        durationPicker.setMinute(0);

        timeZone = TimeZone.getTimeZone("GMT").getDisplayName(TimeZone.getDefault().observesDaylightTime(), TimeZone.SHORT);//getTimeZone("GMT").getDisplayName(false, TimeZone.SHORT);

        newActivityViewModel.getNewActFormState().observe(this, newActivityFormState -> {
            if (newActivityFormState == null) {
                return;
            }
            confirmButton.setEnabled(newActivityFormState.isDataValid());
            if (newActivityFormState.getNameError() != null) {
                editTextName.setError(getString(newActivityFormState.getNameError()));
            }
            if (newActivityFormState.getParticipantNumError() != null) {
                editTextPartNum.setError(getString(newActivityFormState.getParticipantNumError()));
            }
        });

        newActivityViewModel.getNewActResult().observe(this, newActResult -> {
            if (newActResult == null) {
                return;
            }
            if (newActResult.getError() != null) {
                showRegisterFailed(newActResult.getError());
            }
            if (newActResult.getSuccess() != null) {
                if (bitmap != null) {
                    try {
                        uploadImage("vow-project-311114", "vow_profile_pictures", String.valueOf(newActResult.getSuccess().getActID()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                registerActivitySuccess(newActResult.getSuccess());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        newRouteViewModel.getNewRouteResult().observe(this, newRouteResult -> {
            if (newRouteResult == null) {
                return;
            }
            if (newRouteResult.getError() != null) {
                showRegisterFailed(newRouteResult.getError());
            }
            if (newRouteResult.getSuccess() != null) {
                if (bitmap != null) {
                    try {
                        uploadImage("vow-project-311114", "vow_profile_pictures", String.valueOf(newRouteResult.getSuccess().getActID()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                registerActivitySuccess(newRouteResult.getSuccess());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                newActivityViewModel.newActivityDataChanged(editTextName.getText().toString(),
                        address, date, type, editTextPartNum.getText().toString(), durationInMinutes, editTextDescription.getText().toString());
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        editTextPartNum.addTextChangedListener(afterTextChangedListener);

        durationPicker.setOnTimeChangedListener(
                (view, hourOfDay, minute) -> {
                    int hour = durationPicker.getHour();
                    int minutes = durationPicker.getMinute();
                    int aux = hour * 60 + minutes;
                    durationInMinutes = "".concat(String.valueOf(aux));
                    newActivityViewModel.newActivityDataChanged(editTextName.getText().toString(),
                            address, date, type, editTextPartNum.getText().toString(), durationInMinutes, editTextDescription.getText().toString());
                });

        dateBttn.setOnClickListener(v -> showDatePickerDialog());

        /*textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_maps_key));
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getActivity());
                startActivityForResult(intent, 100);
            }
        });*/

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (coordinates.size() == 1)
                    newActivityViewModel.registerActivity(user.getUsername(), String.valueOf(user.getTokenID()), editTextName.getText().toString(),
                            address, coordinates.get(0), date, type, editTextPartNum.getText().toString(), durationInMinutes, editTextDescription.getText().toString());
                else if (coordinates.size() > 1)
                    newRouteViewModel.registerRoute(user.getUsername(), String.valueOf(user.getTokenID()), editTextName.getText().toString(),
                            address, date, type, editTextPartNum.getText().toString(), durationInMinutes, coordinates, editTextDescription.getText().toString());
            }
        });

        imgPickBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);

                someActivityResultLauncher.launch(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();

        DatePickerDialog dpd = new DatePickerDialog(mActivity, (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(mActivity, (view1, hourOfDay, minute) -> {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                date = "".concat(String.valueOf(dayOfMonth)).concat("/")
                        .concat(String.valueOf(monthOfYear + 1)).concat("/").concat(String.valueOf(year)).concat(" ").concat(String.valueOf(hourOfDay))
                        .concat(":").concat(String.valueOf(minute));

                dateText.setText(date);
                dateText.setVisibility(View.VISIBLE);

                date = date.concat(" ").concat(timeZone);

                newActivityViewModel.newActivityDataChanged(editTextName.getText().toString(),
                        editTextDescription.getText().toString(), date, type, editTextPartNum.getText().toString(), durationInMinutes, editTextDescription.getText().toString());
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();

    }

    private void registerActivitySuccess(RegisteredActivityView model) {
        String success = getString(R.string.activity_success);
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage(String projectId, String bucketName, String objectName) throws IOException {
        if (imageUri != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getResizedBitmap(bitmap, 1000);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] imageInByte = out.toByteArray();
            out.close();
            uploadImageViewModel.uploadImage(projectId, bucketName, objectName, imageInByte);
        }
    }

    public void getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        bitmap = Bitmap.createScaledBitmap(image, width, height, true);
    }

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception
                rg2.clearCheck(); // clear the second RadioGroup
                rg2.setOnCheckedChangeListener(listener2); //reset the listener
                updateType();
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
                updateType();
            }
        }
    };

    private void updateType() {
        int chkId1 = rg1.getCheckedRadioButtonId();
        int chkId2 = rg2.getCheckedRadioButtonId();
        int realCheck = chkId1 == -1 ? chkId2 : chkId1;
        switch (realCheck) {
            case R.id.radioAnimals:
                type = "animals";
                break;
            case R.id.radioChildren:
                type = "children";
                break;
            case R.id.radioHealth:
                type = "health";
                break;
            case R.id.radioNature:
                type = "nature";
                break;
            case R.id.radioHouseBuilding:
                type = "houseBuilding";
                break;
            case R.id.radioElderly:
                type = "elderly";
                break;
        }
        newActivityViewModel.newActivityDataChanged(editTextName.getText().toString(),
                editTextDescription.getText().toString(), date, type, editTextPartNum.getText().toString(), durationInMinutes, editTextDescription.getText().toString());
    }

    private void showSetImageFailed() {
        Toast.makeText(getApplicationContext(), R.string.set_image_failed, Toast.LENGTH_SHORT).show();
    }

}
