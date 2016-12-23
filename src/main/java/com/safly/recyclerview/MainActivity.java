package com.safly.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FinalRecycleView finalRecycleView;

    View emptyView;

    final ArrayList<String> list = new ArrayList<>();

    private FinalAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        emptyView = findViewById(R.id.emptyView);
        finalRecycleView = (FinalRecycleView) findViewById(R.id.finalRecycleView);

        myAdapter = new FinalAdapter(this, list, finalRecycleView);

        finalRecycleView.setAdapter(myAdapter);
        finalRecycleView.setEmptyView(emptyView);


        if (list.size() == 0) {
            for (int i = 0; i < 30; i++) {
                list.add("" + i);
            }
        } else {
            list.clear();
        }
        myAdapter.notifyDataSetChanged();


        myAdapter.setOnItemClickLitener(new BaseRecyleAdapter.OnItemClickLitener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
                myAdapter.addData(position,"insert"+position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
                myAdapter.removeData(position);
            }
        });


    }

}
