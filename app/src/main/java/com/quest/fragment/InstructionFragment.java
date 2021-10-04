package com.quest.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.quest.R;
import com.quest.activity.Drawer_Activity;


public class InstructionFragment extends AppCompatActivity {
Button button_start;
FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_instruction);
        button_start=(Button)findViewById(R.id.start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InstructionFragment.this, Drawer_Activity.class));
                finish();
            }
        });
        ImageView cross=(ImageView)findViewById(R.id.cross);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InstructionFragment.this, Drawer_Activity.class));
                finish();
            }
        });
    }

}
