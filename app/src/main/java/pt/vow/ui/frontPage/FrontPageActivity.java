package pt.vow.ui.frontPage;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
        loginAct = this;
        registerChooseAct = this;
        final Button loginBttn = findViewById(R.id.loginBttn);
        final Button createAccBttn = findViewById(R.id.createAccBttn);
        final TextView textViewInfoSite = findViewById(R.id.textViewInfoSite);



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
}