package pt.vow.ui.newActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import pt.vow.R;
import pt.vow.databinding.FragmentNewActivityBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.maps.MapsFragment;

public class NewActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText editTextName, editTextAddress, editTextPartNum;
    private LatLng latLng;
    private String date;
    private String timeZone;
    private String durationInMinutes;

    private NewActivityViewModel newActivityFragment;
    private FragmentNewActivityBinding binding;
    private LoggedInUserView user;
    private Geocoder geocoder;

    private static final String TAG = NewActivityFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentNewActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");
        geocoder = new Geocoder(getActivity());

        editTextName = root.findViewById(R.id.editTextNameAct);
        editTextAddress = root.findViewById(R.id.editTextAddress);
        editTextPartNum = root.findViewById(R.id.editTextParticipantNum);

        TimePicker durationPicker=(TimePicker)root.findViewById(R.id.durationPicker);
        durationPicker.setIs24HourView(true);

        Calendar currentDate = Calendar.getInstance();
        timeZone = TimeZone.getTimeZone("GMT").getDisplayName(false, TimeZone.SHORT);

        String curdate = new String().concat(String.valueOf(currentDate.get(Calendar.DAY_OF_MONTH))).concat("/")
                .concat(String.valueOf(currentDate.get(Calendar.MONTH) + 1)).concat("/").concat(String.valueOf(currentDate.get(Calendar.YEAR))).concat(" ")
                .concat(String.valueOf(currentDate.get(Calendar.HOUR_OF_DAY))).concat(":").concat(String.valueOf(currentDate.get(Calendar.MINUTE)))
                .concat(" ").concat(timeZone);

        Toast.makeText(getActivity().getApplicationContext(), curdate, Toast.LENGTH_LONG).show();

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
                if (newActivityFormState.getAddressError() != null) {
                    editTextAddress.setError(getString(newActivityFormState.getAddressError()));
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
                }

                //Complete and destroy login activity once successful
                //finish();
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
                LatLng ad = new LatLng(0, 0);
                if (!editTextAddress.getText().toString().isEmpty()) {
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(editTextAddress.getText().toString(), 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            ad = new LatLng(address.getLatitude(), address.getLongitude());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                newActivityFragment.newActivityDataChanged(editTextName.getText().toString(),
                            ad.toString(), date, editTextPartNum.getText().toString(), durationInMinutes);
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        editTextAddress.addTextChangedListener(afterTextChangedListener);
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


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: ver do username, tokenID e date
                newActivityFragment.registerActivity(user.getUsername(), String.valueOf(user.getTokenID()), editTextName.getText().toString(),
                        editTextAddress.getText().toString(), date, editTextPartNum.getText().toString(), durationInMinutes);
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

                        // TODO: timezone
                        //timeZone = TimeZone.getTimeZone("GMT").getDisplayName(false, TimeZone.SHORT);
                        date = new String().concat(String.valueOf(dayOfMonth)).concat("/")
                                .concat(String.valueOf(monthOfYear + 1)).concat("/").concat(String.valueOf(year)).concat(" ").concat(String.valueOf(hourOfDay))
                                        .concat(":").concat(String.valueOf(minute)).concat(" ").concat(timeZone);

                        newActivityFragment.newActivityDataChanged(editTextName.getText().toString(),
                                editTextAddress.getText().toString(), date, editTextPartNum.getText().toString(), durationInMinutes);
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();}
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }

    private void registerActivitySuccess(RegisteredActivityView model) {
        String success = getString(R.string.activity_success);
        // TODO : initiate successful logged in experience
        Toast.makeText(getActivity().getApplicationContext(), success, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        //Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
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
}