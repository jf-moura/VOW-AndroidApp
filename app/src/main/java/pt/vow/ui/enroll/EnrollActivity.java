package pt.vow.ui.enroll;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.getActivities.ActivitiesRegisteredView;
import pt.vow.ui.login.LoggedInUserView;

public class EnrollActivity extends AppCompatActivity {

    private TextView textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button enrollButton;
    private EnrollViewModel enrollViewModel;
    private LoggedInUserView user;
    private ActivitiesRegisteredView activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        textViewActName = findViewById(R.id.textViewActName);
        textViewActOwner = findViewById(R.id.textViewActOwner);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewNumPart = findViewById(R.id.textViewNumParticipants);
        textViewTime = findViewById(R.id.textViewTime);

        enrollButton = findViewById(R.id.enrollButton);

        enrollViewModel = new ViewModelProvider(this, new EnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(EnrollViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (ActivitiesRegisteredView) getIntent().getSerializableExtra("ActivityInfo");


      /*  enrollViewModel.getEnrollResult().observe(this, new Observer<EnrollResult>() {

            @Override
            public void onChanged(EnrollResult enrollResult) {
                if (enrollResult == null) {
                    return;
                }
                if (enrollResult.getError() != null) {
                    showEnrollFailed(enrollResult.getError());
                }
                if (enrollResult.getSuccess() != null) {
                    updateUiWithActivities(enrollResult.getSuccess());
                    getActivity().setResult(android.app.Activity.RESULT_OK);
                    // getActivity().finish();
                }
                //Complete and destroy login activity once successful
                //finish();
            }

            private void showEnrollFailed(@StringRes Integer error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

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
                    enrollViewModel.enrollInActivity(textV.getText().toString(),
                            editTextUsername.getText().toString(), editTextEmail.getText().toString(),
                            editTextPassword.getText().toString());

            }
        };*/
    }
}
