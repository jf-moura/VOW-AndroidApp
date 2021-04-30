package pt.vow.ui.frontPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pt.vow.R;
import pt.vow.ui.login.LoginActivity;

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
                Intent intent = new Intent(registerChooseAct, pt.vow.ui.register.RegisterChoose.class);
                startActivity(intent);
            }
        });
    }
}