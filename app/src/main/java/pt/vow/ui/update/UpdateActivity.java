package pt.vow.ui.update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.profile.ProfileFragment;

public class UpdateActivity extends AppCompatActivity {
    private EditText editTextName, editTextEntWebsite, editTextPassword, editTextConfirmation, editTextNewPassword, editTextPhoneNumber;
    private UpdateActivity mActivity;
    private UpdateViewModel updateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mActivity = this;

        editTextName = findViewById(R.id.editTextName);
        //editTextEntWebsite = findViewById(R.id.entitySite);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmation = findViewById(R.id.editTextConfirmPassword);
        editTextPhoneNumber = findViewById(R.id.editTextPhone);

        // TODO: Interests

        final Button confirmButton = findViewById(R.id.bttnSaveChanges);

        updateViewModel = new ViewModelProvider(this, new UpdateViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UpdateViewModel.class);


        updateViewModel.getUpdateFormState().observe(this, new Observer<UpdateFormState>() {
            @Override
            public void onChanged(@Nullable UpdateFormState updateFormState) {
                if (updateFormState == null) {
                    return;
                }
                confirmButton.setEnabled(updateFormState.isDataValid());
                if (updateFormState.getNameError() != null) {
                    editTextName.setError(getString(updateFormState.getNameError()));
                }
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

                    Intent intent = new Intent(mActivity, ProfileFragment.class);
                    startActivity(intent);
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
                    updateViewModel.updateDataChanged(editTextName.getText().toString(),
                            editTextPassword.getText().toString(), editTextNewPassword.getText().toString(),
                            editTextConfirmation.getText().toString(), editTextPhoneNumber.getText().toString());
            }
        };

        editTextName.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);
        editTextNewPassword.addTextChangedListener(afterTextChangedListener);
        editTextConfirmation.addTextChangedListener(afterTextChangedListener);
        editTextPhoneNumber.addTextChangedListener(afterTextChangedListener);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    updateViewModel.update(editTextName.getText().toString(), editTextPassword.getText().toString(),
                            editTextNewPassword.getText().toString(), editTextPhoneNumber.getText().toString());
            }
        });
    }

    private void updateUserSuccess(UpdatedUserView model) {
        String success = getString(R.string.update_success) + " " + model.getName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
    }

    private void showUpdateFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}