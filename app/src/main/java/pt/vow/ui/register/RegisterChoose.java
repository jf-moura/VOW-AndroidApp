package pt.vow.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pt.vow.R;


public class RegisterChoose extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_choose);

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
                    EditText t = findViewById(R.id.editTextTextPersonName);

                    t.setVisibility(view.VISIBLE);
                    // Whatever you want to happen when the first item gets selected
                    break;
                case 1:
                    // Whatever you want to happen when the second item gets selected
                    EditText t2 = findViewById(R.id.editTextTextPersonName2);
                    t2.setVisibility(view.VISIBLE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
}