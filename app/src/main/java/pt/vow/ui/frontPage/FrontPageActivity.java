package pt.vow.ui.frontPage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class FrontPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
    }

    public void onRegisterBtnCLick (View view){
        TextView txtName = findViewById(R.id.txtName);
        TextView txtUsername = findViewById(R.id.txtUsername);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtPassword = findViewById(R.id.txtPassword);
        TextView txtConfirmation = findViewById(R.id.txtConfirmation);
        TextView txtDate = findViewById(R.id.txtDate);
    }
}
