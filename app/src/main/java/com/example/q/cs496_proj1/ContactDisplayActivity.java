package com.example.q.cs496_proj1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDisplayActivity extends AppCompatActivity {
    String name;
    String number;
    Integer picture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactdisplay);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        number = intent.getExtras().getString("number");
        picture = intent.getExtras().getInt("picture");

        TextView textView1 = (TextView)findViewById(R.id.textView1);
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        ImageView imageView1 = (ImageView)findViewById(R.id.imageView1);
        Button button1 = (Button)findViewById(R.id.button1);
        Button button2 = (Button)findViewById(R.id.button2);

        textView1.setText(name);
        textView2.setText(number);
        if(picture != 0) {
            imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView1.setImageResource(picture);
        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(picture != 0) {
                    Intent intent1 = new Intent(getApplicationContext(), ImageViewerActivity.class);
                    intent1.putExtra("picture", picture);
                    startActivity(intent1);
                }
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stripped = number.replace("-", "");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + stripped)));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stripped = number.replace("-", "");
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + stripped)));
            }
        });
    }
}
