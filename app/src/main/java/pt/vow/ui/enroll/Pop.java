package pt.vow.ui.enroll;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.vow.data.model.Activity;
import pt.vow.R;

public class Pop extends AppCompatActivity {

    private Button buttonYes, buttonNo;
    private String activityTitle, location, time;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (height * .3));

        activityTitle = (String) getIntent().getSerializableExtra("title");
        location = (String) getIntent().getSerializableExtra("location");
        time = (String) getIntent().getSerializableExtra("time");


        String[] act = activityTitle.split(":");
        String[] loc = location.split(":");

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, act[1]);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, loc[1]);
                intent.putExtra(CalendarContract.Events.DTSTART, time);
                intent.putExtra(CalendarContract.Events.DTEND, time);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(Pop.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}