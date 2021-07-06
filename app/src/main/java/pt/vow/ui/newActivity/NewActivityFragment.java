package pt.vow.ui.newActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import pt.vow.R;
import pt.vow.databinding.FragmentNewActivityBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;

public class NewActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText editTextName, editTextPartNum;
    private TextView textAddress;
    private String latLng;
    private String date;
    private String timeZone;
    private String durationInMinutes;

    private NewActivityViewModel newActivityFragment;
    private FragmentNewActivityBinding binding;
    private LoggedInUserView user;
    private Geocoder geocoder;
    private String type;
    private RadioGroup rg1, rg2;
    private ProgressBar progressBar;


    private static final String TAG = NewActivityFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentNewActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");
        geocoder = new Geocoder(getActivity());

        type = "";

        editTextName = root.findViewById(R.id.editTextNameAct);
        textAddress = root.findViewById(R.id.editTextAddress);
        editTextPartNum = root.findViewById(R.id.editTextParticipantNum);

        rg1 = (RadioGroup) root.findViewById(R.id.group1);
        rg2 = (RadioGroup) root.findViewById(R.id.group2);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

        progressBar = root.findViewById(R.id.progress_bar_new_activity);


        TimePicker durationPicker = (TimePicker) root.findViewById(R.id.durationPicker);
        durationPicker.setIs24HourView(true);
        durationPicker.setHour(0);
        durationPicker.setMinute(0);

        Calendar currentDate = Calendar.getInstance();
        timeZone = TimeZone.getTimeZone("GMT").getDisplayName(false, TimeZone.SHORT);

        // TODO: verify date is after curDate
        String curDate = new String().concat(String.valueOf(currentDate.get(Calendar.DAY_OF_MONTH))).concat("/")
                .concat(String.valueOf(currentDate.get(Calendar.MONTH) + 1)).concat("/").concat(String.valueOf(currentDate.get(Calendar.YEAR))).concat(" ")
                .concat(String.valueOf(currentDate.get(Calendar.HOUR_OF_DAY))).concat(":").concat(String.valueOf(currentDate.get(Calendar.MINUTE)))
                .concat(" ").concat(timeZone);

        final Button confirmButton = root.findViewById(R.id.bttnSaveChanges);

        newActivityFragment = new ViewModelProvider(this, new NewActivityViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(NewActivityViewModel.class);

        newActivityFragment.getNewActFormState().observe(getViewLifecycleOwner(), new Observer<NewActivityFormState>() {
            @Override
            public void onChanged(@Nullable NewActivityFormState newActivityFormState) {
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
            }
        });

        newActivityFragment.getNewActResult().observe(getViewLifecycleOwner(), new Observer<NewActivityResult>() {
            @Override
            public void onChanged(@Nullable NewActivityResult newActResult) {
                if (newActResult == null) {
                    return;
                }
                if (newActResult.getError() != null) {
                    showRegisterFailed(newActResult.getError());
                }
                if (newActResult.getSuccess() != null) {
                    registerActivitySuccess(newActResult.getSuccess());
                    getActivity().setResult(Activity.RESULT_OK);
                    progressBar.setVisibility(View.GONE);
                }
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
                if (!textAddress.getText().toString().isEmpty()) {
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(textAddress.getText().toString(), 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            latLng = new String().concat(address.getLatitude() + "," + address.getLongitude());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                newActivityFragment.newActivityDataChanged(editTextName.getText().toString(),
                        textAddress.getText().toString(), date, type, editTextPartNum.getText().toString(), durationInMinutes);
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        textAddress.addTextChangedListener(afterTextChangedListener);
        editTextPartNum.addTextChangedListener(afterTextChangedListener);

        durationPicker.setOnTimeChangedListener(
                new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        int hour = durationPicker.getHour();
                        int minutes = durationPicker.getMinute();
                        int aux = hour * 60 + minutes;
                        durationInMinutes = new String().concat(String.valueOf(aux));
                    }
                });

        binding.bttnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        textAddress.setOnClickListener(new View.OnClickListener() {
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
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                newActivityFragment.registerActivity(user.getUsername(), String.valueOf(user.getTokenID()), editTextName.getText().toString(),
                        textAddress.getText().toString(), latLng, date, type, editTextPartNum.getText().toString(), durationInMinutes);
                editTextName.setText("");
                editTextPartNum.setText("");
                textAddress.setText("");
                durationPicker.setMinute(0);
                durationPicker.setHour(0);
                rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
                rg2.clearCheck();


            }
        });

        return root;
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();

        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);

                        // TODO: timezone does not yet change due to winter/summer time
                        date = new String().concat(String.valueOf(dayOfMonth)).concat("/")
                                .concat(String.valueOf(monthOfYear + 1)).concat("/").concat(String.valueOf(year)).concat(" ").concat(String.valueOf(hourOfDay))
                                .concat(":").concat(String.valueOf(minute)).concat(" ").concat(timeZone);

                        newActivityFragment.newActivityDataChanged(editTextName.getText().toString(),
                                textAddress.getText().toString(), date, type, editTextPartNum.getText().toString(), durationInMinutes);
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }

    private void registerActivitySuccess(RegisteredActivityView model) {
        String success = getString(R.string.activity_success);
        // TODO : initiate successful logged in experience
        Toast.makeText(getActivity().getApplicationContext(), success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK) {
            Place pl = Autocomplete.getPlaceFromIntent(data);
            Log.i(TAG, "Place: " + pl.getName() + ", " + pl.getId());
            textAddress.setText(pl.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i(TAG, status.getStatusMessage());
        }
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private void updateType(){
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

    }
}