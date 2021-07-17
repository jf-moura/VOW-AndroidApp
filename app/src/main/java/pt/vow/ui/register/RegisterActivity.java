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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextName, editTextOrgWebsite, editTextPassword, editTextConfirmation, editTextUsername, editTextPhoneNumber;
    private DatePicker datePickerDateBirth;
    private TextView textViewDateBirth;
    private boolean isOrganization;
    private RegisterActivity mActivity;
    private RegisterViewModel registerViewModel;
    private String date;
    private TextInputLayout textInputLayout;
    private Switch switchMode;
    private Boolean mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        isOrganization = true;
        mActivity = this;
        mode = false;

        editTextEmail = findViewById(R.id.emailAddress);
        editTextUsername = findViewById(R.id.username);
        editTextName = findViewById(R.id.name);
        editTextOrgWebsite = findViewById(R.id.organizationSite);
        datePickerDateBirth = findViewById(R.id.dateBirth);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmation = findViewById(R.id.passwordConfirmation);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        textViewDateBirth = findViewById(R.id.textViewDateBirth);
        textInputLayout = findViewById(R.id.textInputLayout);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        switchMode = findViewById(R.id.switch1);

        date = new String().concat(String.valueOf(datePickerDateBirth.getDayOfMonth())).concat("/")
                .concat(String.valueOf(datePickerDateBirth.getMonth() + 1)).concat("/").concat(String.valueOf(datePickerDateBirth.getYear()));

        final Button confirmButton = findViewById(R.id.confirmBttn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.typeUser, R.layout.dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        });

        // spinner.setOnItemSelectedListener(this);
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
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
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
        datePickerDateBirth.init(datePickerDateBirth.getYear(), datePickerDateBirth.getMonth(), datePickerDateBirth.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                        date = new String().concat(String.valueOf(datePickerDateBirth.getDayOfMonth())).concat("/")
                                .concat(String.valueOf(datePickerDateBirth.getMonth() + 1)).concat("/").concat(String.valueOf(datePickerDateBirth.getYear()));
                    }
                });

        switchMode.isChecked();
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mode = true;
                } else {
                    mode = false;
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOrganization)
                    registerViewModel.registerOrganization(editTextName.getText().toString(), editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(), editTextPhoneNumber.getText().toString(),
                            editTextOrgWebsite.getText().toString(), mode, "");
                else
                    registerViewModel.registerPerson(editTextName.getText().toString(), editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString(), editTextPhoneNumber.getText().toString(), date, mode, "");
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

}