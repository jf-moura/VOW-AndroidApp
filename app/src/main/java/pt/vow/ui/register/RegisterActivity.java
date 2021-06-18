package pt.vow.ui.register;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.extraInfo.ExtraInfoActivity;
import pt.vow.ui.extraInfo.ExtraInfoOrganizationActivity;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextEmail, editTextName, editTextOrgWebsite, editTextPassword, editTextConfirmation, editTextUsername, editTextPhoneNumber;
    private DatePicker datePickerDateBirth;
    private TextView textViewDateBirth;
    private boolean isOrganization;
    private RegisterActivity extraInfoActP;
    private RegisterViewModel registerViewModel;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        isOrganization = true;
        extraInfoActP = this;

        editTextEmail = findViewById(R.id.emailAddress);
        editTextUsername = findViewById(R.id.username);
        editTextName = findViewById(R.id.name);
        editTextOrgWebsite = findViewById(R.id.organizationSite);
        datePickerDateBirth = findViewById(R.id.dateBirth);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmation = findViewById(R.id.passwordConfirmation);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        textViewDateBirth = findViewById(R.id.textViewDateBirth);



        date = new String().concat(String.valueOf(datePickerDateBirth.getDayOfMonth())).concat("/")
                .concat(String.valueOf(datePickerDateBirth.getMonth() + 1)).concat("/").concat(String.valueOf(datePickerDateBirth.getYear()));

        final Button confirmButton = findViewById(R.id.confirmBttn);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.typeUser, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(RegisterViewModel.class);


        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState newActivityFormState) {
                if (newActivityFormState == null) {
                    return;
                }
                confirmButton.setEnabled(newActivityFormState.isDataValid());
                if (newActivityFormState.getNameError() != null) {
                    editTextName.setError(getString(newActivityFormState.getNameError()));
                }
                if (newActivityFormState.getUsernameError() != null) {
                    editTextUsername.setError(getString(newActivityFormState.getUsernameError()));
                }
                if (newActivityFormState.getEmailError() != null) {
                    editTextEmail.setError(getString(newActivityFormState.getEmailError()));
                }
                if (newActivityFormState.getPasswordError() != null) {
                    editTextPassword.setError(getString(newActivityFormState.getPasswordError()));
                }
                if (newActivityFormState.getPasswordConfirmationError() != null) {
                    editTextConfirmation.setError(getString(newActivityFormState.getPasswordConfirmationError()));
                }
                if (isOrganization) {
                    if (newActivityFormState.getPhoneNumberError() != null) {
                        editTextPhoneNumber.setError(getString(newActivityFormState.getPhoneNumberError()));
                    }
                    if (newActivityFormState.getWebsiteError() != null) {
                        editTextOrgWebsite.setError(getString(newActivityFormState.getWebsiteError()));
                    }
                }
            }
        });

        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                if (registerResult.getError() != null) {
                    showRegisterFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    registerUserSuccess(registerResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                    if (isOrganization) {
                        Intent intent = new Intent(extraInfoActP, ExtraInfoOrganizationActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(extraInfoActP, ExtraInfoActivity.class);
                        startActivity(intent);

                    }
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
                if (isOrganization)
                    registerViewModel.registerDataChangedOrganization(editTextName.getText().toString(),
                            editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(), editTextConfirmation.getText().toString(),
                            editTextPhoneNumber.getText().toString(), editTextOrgWebsite.getText().toString());
                else {
                    registerViewModel.registerDataChangedPerson(editTextName.getText().toString(),
                            editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(), editTextConfirmation.getText().toString(), editTextPhoneNumber.getText().toString());
                }
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        editTextUsername.addTextChangedListener(afterTextChangedListener);
        editTextEmail.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);
        editTextConfirmation.addTextChangedListener(afterTextChangedListener);
        editTextPhoneNumber.addTextChangedListener(afterTextChangedListener);
        editTextOrgWebsite.addTextChangedListener(afterTextChangedListener);
        datePickerDateBirth.init( datePickerDateBirth.getYear(), datePickerDateBirth.getMonth(), datePickerDateBirth.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                date = new String().concat(String.valueOf(datePickerDateBirth.getDayOfMonth())).concat("/")
                        .concat(String.valueOf(datePickerDateBirth.getMonth() + 1)).concat("/").concat(String.valueOf(datePickerDateBirth.getYear()));
                }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOrganization)
                    registerViewModel.registerOrganization(editTextName.getText().toString(), editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(), editTextPhoneNumber.getText().toString(),
                            editTextOrgWebsite.getText().toString());
                else
                    registerViewModel.registerPerson(editTextName.getText().toString(), editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(), editTextPhoneNumber.getText().toString(), date);
            }
        });
    }

    private void registerUserSuccess(RegisteredUserView model) {
        String success = getString(R.string.register_success) + " " + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:
                editTextOrgWebsite.setVisibility(view.VISIBLE);
                datePickerDateBirth.setVisibility(view.GONE);
                textViewDateBirth.setVisibility(view.GONE);
                isOrganization = true;
                break;

            case 1:
                editTextOrgWebsite.setVisibility(view.GONE);
                datePickerDateBirth.setVisibility(view.VISIBLE);
                textViewDateBirth.setVisibility(view.VISIBLE);
                isOrganization = false;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}