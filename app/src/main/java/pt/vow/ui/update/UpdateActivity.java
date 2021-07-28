package pt.vow.ui.update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.maps.MapsFragment;
import pt.vow.ui.profile.GetProfileViewModelFactory;
import pt.vow.ui.profile.GetProfileViewModel;
import pt.vow.ui.profile.ProfileFragment;
import pt.vow.ui.profile.ProfileInfoView;

public class UpdateActivity extends AppCompatActivity {
    private EditText editTextName, editTextEntWebsite, editTextPassword, editTextConfirmation, editTextNewPassword, editTextPhoneNumber, editTextBio;
    private TextView textViewWebsiteSett;
    private UpdateViewModel updateViewModel;
    private LoggedInUserView user;
    private ProfileInfoView profileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        profileInfo = (ProfileInfoView) getIntent().getSerializableExtra("ProfileInfo");

        editTextName = findViewById(R.id.editTextName);
        editTextEntWebsite = findViewById(R.id.editTextWebsite);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmation = findViewById(R.id.editTextConfirmPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        textViewWebsiteSett = findViewById(R.id.textViewWebsite);
        editTextBio = findViewById(R.id.editTextBio);

        if (user.getRole() == 1) {
            editTextEntWebsite.setVisibility(View.VISIBLE);
            textViewWebsiteSett.setVisibility(View.VISIBLE);
            editTextEntWebsite.setHint(profileInfo.getWebsite());
        }

        final Button confirmButton = findViewById(R.id.bttnSaveChanges);

        editTextBio.setHint(profileInfo.getBio());
        editTextName.setHint(profileInfo.getName());
        editTextPhoneNumber.setHint(profileInfo.getPhoneNumber());
        if (profileInfo.getPhoneNumber().isEmpty()) {
            editTextPhoneNumber.setHint(getResources().getString(R.string.add_number));
        }

        updateViewModel = new ViewModelProvider(this, new UpdateViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UpdateViewModel.class);


        updateViewModel.getUpdateFormState().observe(this, new Observer<UpdateFormState>() {
            @Override
            public void onChanged(@Nullable UpdateFormState updateFormState) {
                if (updateFormState == null) {
                    return;
                }
                confirmButton.setEnabled(updateFormState.isDataValid());
                if (updateFormState.getPasswordError() != null) {
                    editTextPassword.setError(getString(updateFormState.getPasswordError()));
                }
                if (updateFormState.getNewPasswordError() != null) {
                    editTextNewPassword.setError(getString(updateFormState.getNewPasswordError()));
                }
                if (updateFormState.getConfirmPasswordError() != null) {
                    editTextConfirmation.setError(getString(updateFormState.getConfirmPasswordError()));
                }
                if (updateFormState.getPhoneNumberError() != null) {
                    editTextPhoneNumber.setError(getString(updateFormState.getPhoneNumberError()));
                }
            }
        });

        updateViewModel.getUpdateResult().observe(this, new Observer<UpdateResult>() {
            @Override
            public void onChanged(@Nullable UpdateResult updateResult) {
                if (updateResult == null) {
                    return;
                }
                if (updateResult.getError() != null) {
                    showUpdateFailed(updateResult.getError());
                }
                if (updateResult.getSuccess() != null) {
                    updateUserSuccess(updateResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                    finish();
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
                updateViewModel.updateDataChanged(editTextPassword.getText().toString(), editTextNewPassword.getText().toString(),
                        editTextConfirmation.getText().toString(), editTextPhoneNumber.getText().toString());
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        editTextBio.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);
        editTextNewPassword.addTextChangedListener(afterTextChangedListener);
        editTextConfirmation.addTextChangedListener(afterTextChangedListener);
        editTextPhoneNumber.addTextChangedListener(afterTextChangedListener);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getRole() == 0) { //person
                    updateViewModel.update(user.getUsername(), user.getTokenID(), editTextName.getText().toString(), editTextPassword.getText().toString(), editTextNewPassword.getText().toString(),
                            editTextPhoneNumber.getText().toString(), "", editTextBio.getText().toString(),
                            "");
                } else
                    updateViewModel.update(user.getUsername(), user.getTokenID(), editTextName.getText().toString(), editTextPassword.getText().toString(), editTextNewPassword.getText().toString(),
                            editTextPhoneNumber.getText().toString(), "", editTextBio.getText().toString(), editTextEntWebsite.getText().toString());
            }
        });
    }

    private void updateUserSuccess(UpdatedUserView model) {
        String success = getString(R.string.update_success) + " " + model.getName();
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
    }

    private void showUpdateFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void itemClicked(View view) {
    }
}