package pt.vow.ui.enroll;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import pt.vow.R;

public class Pop extends AppCompatActivity {

    private Button buttonYes, buttonNo;
    private TextView textView1;
    private String activityTitle, location, time, duration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        textView1 = findViewById(R.id.textView5);
        textView1.setText(R.string.add_calendar);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .25));

        activityTitle = (String) getIntent().getSerializableExtra("title");
        location = (String) getIntent().getSerializableExtra("location");
        time = (String) getIntent().getSerializableExtra("time");
        duration = (String) getIntent().getSerializableExtra("duration");

        String[] dateTime = time.split(" ");
        String[] hours = dateTime[3].split(":");
        String[] dur = duration.split(":");

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(Integer.valueOf(dateTime[2]), monthToInteger(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));
                long startMillis = beginTime.getTimeInMillis();

                beginTime.set(Integer.valueOf(dateTime[2]), monthToInteger(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]) + Integer.valueOf(dur[0]), Integer.valueOf(hours[1]) + Integer.valueOf(dur[1]));
                long endMillis = beginTime.getTimeInMillis();

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(Events.CONTENT_URI);
                intent.putExtra(Events.TITLE, activityTitle);
                intent.putExtra(Events.EVENT_LOCATION, location);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
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
                finish();
            }
        });
    }

    private int monthToInteger(String month) {
        int result = 0;
        switch (month) {
            case "Jan":
                result = 0;
                break;
            case "Fev":
                result = 1;
                break;
            case "Mar":
                result = 2;
                break;
            case "Apr":
                result = 3;
                break;
            case "May":
                result = 4;
                break;
            case "Jun":
                result = 5;
                break;
            case "Jul":
                result = 6;
                break;
            case "Aug":
                result = 7;
                break;
            case "Sep":
                result = 8;
                break;
            case "Oct":
                result = 9;
                break;
            case "Nov":
                result = 10;
                break;
            case "Dec":
                result = 11;
                break;
        }
        return result;
    }
}