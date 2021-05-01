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

            EditText entName = findViewById(R.id.entityName);
            EditText persName = findViewById(R.id.personName);
            EditText entWebsite = findViewById(R.id.entitySite);
            EditText dateBirth = findViewById(R.id.dateBirth);
            switch (position) {
                case 0:
                    entName.setVisibility(view.VISIBLE);
                    entWebsite.setVisibility(view.VISIBLE);
                    dateBirth.setVisibility(view.GONE);
                    persName.setVisibility(view.GONE);
                    break;
                case 1:
                    persName.setVisibility(view.VISIBLE);
                    entWebsite.setVisibility(view.GONE);
                    dateBirth.setVisibility(view.VISIBLE);
                    entName.setVisibility(view.GONE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
}