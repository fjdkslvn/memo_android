package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class look_memo extends AppCompatActivity {

    private Intent intent;
    private FirebaseDatabase database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
    private DatabaseReference databaseReference = database.getReference();

    TextView mname,mcontent, date;
    String mname1,mcontent1,key, date1;
    ImageButton back, setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_memo);

        intent = getIntent();
        key = intent.getStringExtra("key");
        mname = findViewById(R.id.mname);
        back = findViewById(R.id.back);
        setting = findViewById(R.id.setting);
        mcontent = findViewById(R.id.mcontent);
        date = findViewById(R.id.date);

        databaseReference.child("memo").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 memo_list memo = dataSnapshot.getValue(memo_list.class);

                mname1 = memo.getmname();
                mcontent1 = memo.getmcontent();
                date1 = memo.getdate();

                //텍스트뷰에 받아온 문자열 대입하기
                mname.setText(mname1);
                mcontent.setText(mcontent1);
                date.setText(date1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });


        //뒤로가기
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //수정하기
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), make_memo.class); //그룹 만들기 화면으로 연결
                intent.putExtra("key", key); //해당 키값을 넘긴다
                intent.putExtra("mname", mname1); //해당 키값을 넘긴다
                intent.putExtra("mcontent", mcontent1); //해당 키값을 넘긴다
                startActivity(intent); //액티비티 열기
                finish();
            }
        });





    }
}