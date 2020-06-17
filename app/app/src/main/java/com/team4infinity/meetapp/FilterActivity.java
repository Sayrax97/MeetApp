package com.team4infinity.meetapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FilterActivity extends AppCompatActivity {

    EditText distance;
    JSONObject sendBack=new JSONObject();
    Button btn;
    Switch price;
    Switch specialR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        LinearLayout ll=findViewById(R.id.Categories);
        final LinearLayout all=findViewById(R.id.groupLL);
        btn=findViewById(R.id.filterBtn);
        distance=findViewById(R.id.DistanceEditFi);
        price=findViewById(R.id.PriceSwitch);
        specialR=findViewById(R.id.SpecialReqSwitch);
        int br=0;
        for (String c:Singleton.getInstance().getCategories()) {
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setId(br++);
            cb.setText(c);
            ll.addView(cb);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendBack.put("Distance",distance.getText());
                    sendBack.put("Price",price.isChecked());
                    sendBack.put("Special requirement",specialR.isChecked());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                check(all,0,new ArrayList<String>());
                System.out.println(sendBack.toString());

            }
        });
    }

    private void check(LinearLayout ll, int i, ArrayList<String> list) {
        if (i == ll.getChildCount()) {
            String key = ll.getTag().toString();
            try {
                sendBack.put(key, list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        View view = ll.getChildAt(i);
        if(view instanceof LinearLayout)
            check((LinearLayout) view,0,new ArrayList<String>());
        if (view instanceof CheckBox) {
            if (((CheckBox) view).isChecked()){
                list.add(((CheckBox) view).getText().toString());
            }
        }
        check(ll,++i,list);
    }



}