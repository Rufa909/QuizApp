package com.example.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1_layout);
        final EditText editValue = (EditText) findViewById(R.id.value_edit);
        final Button sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String valueString = editValue.getText().toString();
                long value = valueString.isEmpty() ? 0 : Long.parseLong(valueString);

                Bundle sendBundle = new Bundle();
                sendBundle.putLong("value", value);

                Intent i = new Intent(Activity1.this, Activity2.class);
                i.putExtras(sendBundle);
                startActivity(i);
                finish();
            }
        });
    }
}