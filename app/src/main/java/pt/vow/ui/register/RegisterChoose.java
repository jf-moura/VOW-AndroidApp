package pt.vow.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    private EditText editTextEmail, editTextEntName, editTextPersName, editTextEntWebsite, editTextPassword, editTextConfirmation, editTextUsername, editTextPhoneNumber;
    private DatePicker editTextDateBirth;
    private TextView textViewDateBirth;
    private boolean isEntity;
    private RegisterChoose extraInfoActP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choose);
        isEntity = false;

        extraInfoActP = this;

        editTextEmail = findViewById(R.id.emailAddress);
        editTextEntName = findViewById(R.id.entityName);
        editTextUsername = findViewById(R.id.username);
        editTextPersName = findViewById(R.id.personName);
        editTextEntWebsite = findViewById(R.id.entitySite);
        editTextDateBirth = findViewById(R.id.dateBirth);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmation = findViewById(R.id.passwordConfirmation);
        editTextPhoneNumber = findViewById(R.id.phoneNumber);
        textViewDateBirth = findViewById(R.id.textViewDateBirth);

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
                textViewDateBirth.setVisibility(view.GONE);
                isEntity = true;
                findViewById(R.id.confirmBttn).setOnClickListener(this);
                break;

            case 1:
                editTextPersName.setVisibility(view.VISIBLE);
                editTextEntWebsite.setVisibility(view.GONE);
                editTextDateBirth.setVisibility(view.VISIBLE);
                editTextEntName.setVisibility(view.GONE);
                textViewDateBirth.setVisibility(view.VISIBLE);
                isEntity = false;
                findViewById(R.id.confirmBttn).setOnClickListener(this);
                break;
        }
    }


    private void userSignUpEntity() {
        String entityName = editTextEntName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmation = editTextConfirmation.getText().toString().trim();
        String website = editTextEntWebsite.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (entityName.isEmpty()) {
            editTextEntName.setError("Entity's name is required");
            editTextEntName.requestFocus();
            return;
        }

        this.validateData(email, username, password, confirmation, phoneNumber);

        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Phone number is required");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (website.isEmpty()) {
            editTextEntWebsite.setError("Website is required");
            editTextEntWebsite.requestFocus();
            return;
        }

        // TODO: verificar url
        if (!URLUtil.isValidUrl(website)) {
            editTextEntWebsite.setError("Website must be valid");
            editTextEntWebsite.requestFocus();
            return;
        }

        this.createAcc(entityName, username, email, password, phoneNumber, website, null, "ENTITY");
    }


    private void userSignUpPerson() {
        String persName = editTextPersName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmation = editTextConfirmation.getText().toString().trim();


        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        String dateBirth = new String().concat(String.valueOf(editTextDateBirth.getDayOfMonth())).concat("/")
                .concat(String.valueOf(editTextDateBirth.getMonth() + 1)).concat("/").concat(String.valueOf(editTextDateBirth.getYear()));

        if (persName.isEmpty()) {
            editTextPersName.setError("Name is required");
            editTextPersName.requestFocus();
            return;
        }


        this.validateData(email, username, password, confirmation, phoneNumber);

        this.createAcc(persName, username, email, password, phoneNumber, null, dateBirth, "PERSON");
    }

    private void validateData(String email, String username, String password, String confirmation, String phoneNumber) {
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
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

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
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
            editTextConfirmation.setError("Password verification is required");
            editTextConfirmation.requestFocus();
            return;
        }

        if (!confirmation.equals(password)) {
            editTextConfirmation.setError("Must be equal to password.");
            editTextConfirmation.requestFocus();
            return;
        }

        if (!phoneNumber.isEmpty() && phoneNumber.length() != 9) {
            editTextPassword.setError("Invalid number.");
            editTextPassword.requestFocus();
            return;
        }
    }

    private void createAcc(String name, String username, String email, String password, String phoneNumber, String website, String dateBirth, String role) {
        Call<ResponseBody> call = CreateAccSource
                .getInstance()
                .getApi()
                .createUser(name, username, email, password, phoneNumber, phoneNumber, dateBirth, role);


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
                else {
                    userSignUpPerson();
                    //TODO corrigir erro com o retrofit e register
                    Intent intent = new Intent(extraInfoActP, pt.vow.ui.extraInfo.ExtraInfoActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}