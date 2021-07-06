package pt.vow.ui.frontPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pt.vow.R;
import pt.vow.ui.login.LoginActivity;
import pt.vow.ui.register.RegisterActivity;

public class FrontPageActivity extends AppCompatActivity {
    private FrontPageActivity loginAct;
    private FrontPageActivity registerChooseAct;
    private Button loginBttn, createAccBttn;

    private SharedPreferences loginPreferences;

    private boolean saveLogin, saveLogin2, saveLoginExtra;
    private String username, tokenId;
    private Long role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
        loginAct = this;
        registerChooseAct = this;
        loginBttn = findViewById(R.id.loginBttn);
        createAccBttn = findViewById(R.id.createAccBttn);
        final TextView textViewInfoSite = findViewById(R.id.textViewInfoSite);

        loginPreferences = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        Boolean test = (Boolean) getIntent().getSerializableExtra("test");
        if(test == null){
            test = true;
        }

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        saveLogin2 = loginPreferences.getBoolean("saveLogin2", true);

        if(username==null) {
            username = loginPreferences.getString("username", "username");
            role = loginPreferences.getLong("role", 1);
            tokenId = loginPreferences.getString("tokenId", "tokenId");
        }

       /* if (saveLogin && test) {
            Intent intent;
            LoggedInUserView user = new LoggedInUserView(role, username, tokenId);
            if (role == 0) { //volunteer
                intent = new Intent(loginAct, MainPageVolunteer.class);
                intent.putExtra("UserLogged", user);
            } else { //organization
                intent = new Intent(loginAct, MainPage.class);
                intent.putExtra("UserLogged", user);
            }
            startActivity(intent);
        }*/

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginAct, LoginActivity.class);
                startActivity(intent);
            }
        });

        createAccBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerChooseAct, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("username", username);
        savedInstanceState.putLong("role", role);
        savedInstanceState.putString("tokenId", tokenId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        username = savedInstanceState.getString("username");
        role = savedInstanceState.getLong("role");
        tokenId = savedInstanceState.getString("tokenId");
    }
}