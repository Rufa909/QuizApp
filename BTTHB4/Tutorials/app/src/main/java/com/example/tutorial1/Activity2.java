package com.example.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Activity2 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_layout);
        final EditText receiveValueEdit = (EditText) findViewById(R.id.value_receive);
        final Button callReceiverButton = (Button) findViewById(R.id.call_button);

        Bundle receiveBundle = this.getIntent().getExtras();
        final long receiveValue = receiveBundle.getLong("value");
        receiveValueEdit.setText(String.valueOf(receiveValue));

        callReceiverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Activity2.this, Receiver.class);
                i.putExtra("new value", receiveValue - 10);
                sendBroadcast(i);
            }
        });
    }
}
