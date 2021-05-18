package pt.vow.ui.newActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import pt.vow.R;
import pt.vow.databinding.FragmentNewActivityBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;

public class NewActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText editTextName, editTextAddress, editTextPartNum, editTextDuration;
    private String date;
    private String username, tokenID;

    private NewActivityViewModel newActivityFragment;
    private FragmentNewActivityBinding binding;
    private LoggedInUserView user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //newActivityFragment =
                //new ViewModelProvider(this).get(NewActivityViewModel.class);

        binding = FragmentNewActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        editTextName = root.findViewById(R.id.editTextNameAct);
        editTextAddress = root.findViewById(R.id.editTextAddress);
        editTextPartNum = root.findViewById(R.id.editTextParticipantNum);
        editTextDuration = root.findViewById(R.id.editTextDuration);

        //date = new String().concat(String.valueOf(datePickerDateBirth.getDayOfMonth())).concat("/")
                //.concat(String.valueOf(datePickerDateBirth.getMonth() + 1)).concat("/").concat(String.valueOf(datePickerDateBirth.getYear()));

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
                if (newActivityFormState.getDurationError() != null) {
                    editTextDuration.setError(getString(newActivityFormState.getDurationError()));
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
                    //setResult(Activity.RESULT_OK);
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
                    newActivityFragment.newActivityDataChanged(editTextName.getText().toString(),
                            editTextAddress.getText().toString(), date, editTextPartNum.getText().toString(), editTextDuration.getText().toString());
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        editTextAddress.addTextChangedListener(afterTextChangedListener);
        editTextPartNum.addTextChangedListener(afterTextChangedListener);
        editTextDuration.addTextChangedListener(afterTextChangedListener);

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
                newActivityFragment.registerActivity(username, tokenID, editTextName.getText().toString(),
                        editTextAddress.getText().toString(), date, editTextPartNum.getText().toString(), editTextDuration.getText().toString());
            }
        });


        return root;
    }

    private void showDatePickerDialog() {
        Calendar date;
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void registerActivitySuccess(RegisteredActivityView model) {
        String success = getString(R.string.register_success);
        // TODO : initiate successful logged in experience
        //Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
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