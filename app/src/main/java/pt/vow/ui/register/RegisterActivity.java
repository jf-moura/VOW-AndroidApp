package pt.vow.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.R;
import pt.vow.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivity loginAct;
    private RegisterActivity frontPageAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc1);
        loginAct = this;
        frontPageAct = this;
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
                Intent intent = new Intent(frontPageAct, FrontPageActivity.class);
                startActivity(intent);
            }
        });
    }
}