package pt.vow.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import pt.vow.R;
import pt.vow.data.CreateAccSource;


public class RegisterChoose extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText editTextEmail, editTextEntName, editTextPersName, editTextEntWebsite, editTextDateBirth, editTextPassword, editTextConfirmation, editTextUsername;
    private boolean isEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choose);
        isEntity = false;

        editTextEmail = findViewById(R.id.emailAddress);
        editTextEntName = findViewById(R.id.entityName);
        editTextUsername = findViewById(R.id.username);
        editTextPersName = findViewById(R.id.personName);
        editTextEntWebsite = findViewById(R.id.entitySite);
        editTextDateBirth = findViewById(R.id.dateBirth);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmation = findViewById(R.id.passwordConfirmation);

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.typeUser, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        switch (position) {
            case 0:
                editTextEntName.setVisibility(view.VISIBLE);
                editTextEntWebsite.setVisibility(view.VISIBLE);
                editTextDateBirth.setVisibility(view.GONE);
                editTextPersName.setVisibility(view.GONE);
                isEntity = true;
                findViewById(R.id.confirmBttn).setOnClickListener(this);
                break;

            case 1:
                editTextPersName.setVisibility(view.VISIBLE);
                editTextEntWebsite.setVisibility(view.GONE);
                editTextDateBirth.setVisibility(view.VISIBLE);
                editTextEntName.setVisibility(view.GONE);
                isEntity = false;
                findViewById(R.id.confirmBttn).setOnClickListener(this);
                break;
        }
    }


    private void userSignUpEntity() {
        String email = editTextEmail.getText().toString().trim();
        String entityName = editTextEntName.getText().toString().trim();
        String website = editTextEntWebsite.getText().toString().trim();

        if (entityName.isEmpty()) {
            editTextEntName.setError("Entity's name is required");
            editTextEntName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (website.isEmpty()) {
            editTextEntWebsite.setError("Website is required");
            editTextEntWebsite.requestFocus();
            return;
        }

        if (!URLUtil.isValidUrl(website)) {
            editTextEntWebsite.setError("Website must be valid");
            editTextEntWebsite.requestFocus();
            return;
        }

    }


    private void userSignUpPerson() {
        String email = editTextEmail.getText().toString().trim();
        String persName = editTextPersName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmation = editTextConfirmation.getText().toString().trim();
        String dateBirth = editTextDateBirth.getText().toString().trim();

        if (persName.isEmpty()) {
            editTextPersName.setError("Name is required");
            editTextPersName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            editTextUsername.setError("Error");
            editTextUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Error");
            editTextPassword.requestFocus();
            return;
        }

        //TODO ver como o resto do grupo
        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 character long");
            editTextPassword.requestFocus();
            return;
        }

        if (confirmation.isEmpty()) {
            editTextConfirmation.setError("Error");
            editTextConfirmation.requestFocus();
            return;
        }
        if (!confirmation.equals(password)) {
            editTextConfirmation.setError("Must be equal to password.");
            editTextConfirmation.requestFocus();
            return;
        }
        if (dateBirth.isEmpty()) {
            editTextDateBirth.setError("Error");
            editTextDateBirth.requestFocus();
            return;
        }
        Call<ResponseBody> call = CreateAccSource
                .getInstance()
                .getApi()
                .createUser(persName, username, email, password, confirmation, dateBirth);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = response.body().string();
                    Toast.makeText(RegisterChoose.this, s, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(RegisterChoose.this, "User already exist", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterChoose.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmBttn:
                if (isEntity)
                    userSignUpEntity();
                else
                    userSignUpPerson();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}