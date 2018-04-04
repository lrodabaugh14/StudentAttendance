package com.example.lrodabaugh14.studentattendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AdminEmergency extends AppCompatActivity {
    Button btnEmergency;
    TextView txtEmState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_emergency);
        btnEmergency = (Button) findViewById(R.id.btnEmergency);
        txtEmState = (TextView) findViewById(R.id.txtEmState);
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEmergencyButtonClick();
            }
        });
        adjustView();

    }

    private void onEmergencyButtonClick() {
        studentDatabase db = new studentDatabase();
        db.setEmergency();
        adjustView();
    }
    private void adjustView(){

        if(AppUtil.emergencyActive){
            txtEmState.setText("Emergency State: Urgent");
            txtEmState.setTextColor(getResources().getColor(R.color.red));
            btnEmergency.setText("End Emergency");
        } else {
            txtEmState.setText("Emergency State: Normal");
            txtEmState.setTextColor(getResources().getColor(R.color.Green));
            btnEmergency.setText("Set Emergency");
        }
    }
    public void onLogoutClick(){

    }
}
