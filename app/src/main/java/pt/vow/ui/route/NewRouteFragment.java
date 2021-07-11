package pt.vow.ui.route;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.TimeZone;

import pt.vow.R;
import pt.vow.databinding.FragmentNewRouteBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class NewRouteFragment extends Fragment {
    private EditText editTextName, editTextPartNum;
    private String date;
    private String timeZone;
    private String durationInMinutes;
    private Button chooseRouteBttn, dateBttn, confirmButton;

    private LoggedInUserView user;
    private String type;
    private RadioGroup rg1, rg2;
    private NewRouteViewModel newRouteViewModel;
    private String[] coordinateArray;

    private FragmentNewRouteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentNewRouteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        newRouteViewModel = new ViewModelProvider(this, new NewRouteViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(NewRouteViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        coordinateArray = getArguments().getStringArray("CoordinateArray");
        type = "";

        editTextName = root.findViewById(R.id.editTextNameRoute);
        editTextPartNum = root.findViewById(R.id.editTextParticipantNumRoute);
        //chooseRouteBttn = root.findViewById(R.id.bttnChooseRoute);
        dateBttn = root.findViewById(R.id.bttnDateRoute);

        rg1 = (RadioGroup) root.findViewById(R.id.group1Route);
        rg2 = (RadioGroup) root.findViewById(R.id.group2Route);
        rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);


        TimePicker durationPicker = (TimePicker) root.findViewById(R.id.durationPickerRoute);
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

        confirmButton = root.findViewById(R.id.bttnSaveChangesRoute);


        newRouteViewModel.getNewRouteFormState().observe(getActivity(), new Observer<NewRouteFormState>() {
            @Override
            public void onChanged(@Nullable NewRouteFormState newRouteFormState) {
                if (newRouteFormState == null) {
                    return;
                }
                confirmButton.setEnabled(newRouteFormState.isDataValid());
                if (newRouteFormState.getNameError() != null) {
                    editTextName.setError(getString(newRouteFormState.getNameError()));
                }
                if (newRouteFormState.getParticipantNumError() != null) {
                    editTextPartNum.setError(getString(newRouteFormState.getParticipantNumError()));
                }
            }
        });

        newRouteViewModel.getNewRouteResult().observe(getActivity(), new Observer<NewRouteResult>() {
            @Override
            public void onChanged(@Nullable NewRouteResult newRouteResult) {
                if (newRouteResult == null) {
                    return;
                }
                if (newRouteResult.getError() != null) {
                    showRegisterFailed(newRouteResult.getError());
                }
                if (newRouteResult.getSuccess() != null) {
                    registerActivitySuccess(newRouteResult.getSuccess());
                    getActivity().setResult(Activity.RESULT_OK);
                    editTextName.setText("");
                    editTextPartNum.setText("");
                    durationPicker.setMinute(0);
                    durationPicker.setHour(0);
                    rg1.clearCheck(); // this is so we can start fresh, with no selection on both RadioGroups
                    rg2.clearCheck();

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
                //TODO verify if array coordinates is empty
                newRouteViewModel.newRouteDataChanged(editTextName.getText().toString(),
                        date, type, editTextPartNum.getText().toString(), durationInMinutes);
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
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

        binding.bttnDateRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        /*chooseRouteBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChooseRouteActivity.class);
                startActivity(intent);
            }
        });*/

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: change address
                newRouteViewModel.registerRoute(user.getUsername(), String.valueOf(user.getTokenID()), editTextName.getText().toString(),
                        "lisbon", date, type, editTextPartNum.getText().toString(), durationInMinutes, coordinateArray);

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

                        newRouteViewModel.newRouteDataChanged(editTextName.getText().toString(),
                                date, type, editTextPartNum.getText().toString(), durationInMinutes);
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


    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
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

    }
}
