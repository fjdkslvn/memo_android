package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class make_memo extends AppCompatActivity {

    private FirebaseDatabase database; // 파이어베이스 데이터베이스 연동

    //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
    //현재 연결은 데이터베이스에만 딱 연결해놓고 키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
    private DatabaseReference databaseReference;


    Button btn;
    ImageButton back;
    EditText mname, mcontent;
    String key="", mname1, mcontent1;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_memo);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        back = findViewById(R.id.back);
        btn = findViewById(R.id.btn);
        mname = findViewById(R.id.mname);
        mcontent = findViewById(R.id.mcontent);


        intent = getIntent();
        key = intent.getStringExtra("key");
        mname1 = intent.getStringExtra("mname");
        mcontent1 = intent.getStringExtra("mcontent");

        long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // ex) 2021-01-01 21:04
        String formatDate = mFormat.format(mReDate);

        if(key!=null) { //메모 수정이라면

            databaseReference.child("memo").child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    memo_list list = dataSnapshot.getValue(memo_list.class);
                    mname1 = list.getmname();
                    mcontent1 = list.getmcontent();
                    mname.setText(mname1);
                    mcontent.setText(mcontent1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 디비를 가져오던중 에러 발생 시
                    //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                }

            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child("memo").child(key).child("mcontent").setValue(mcontent.getText().toString());
                    databaseReference.child("memo").child(key).child("mname").setValue(mname.getText().toString());
                    databaseReference.child("memo").child(key).child("date").setValue(formatDate);

                    Intent intent = new Intent(getApplicationContext(), look_memo.class); //그룹 만들기 화면으로 연결
                    intent.putExtra("key", key); //해당 키값을 넘긴다
                    startActivity(intent); //액티비티 열기
                    finish();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), look_memo.class); //그룹 만들기 화면으로 연결
                    intent.putExtra("key", key); //해당 키값을 넘긴다
                    startActivity(intent); //액티비티 열기
                    finish();
                }
            });

        }
        else{
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    memo_list(mname.getText().toString(), mcontent.getText().toString(),formatDate);
                    finish();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }





    }


    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void memo_list(String mname, String mcontent, String date) {

        //animal.java에서 선언했던 함수.
        memo_list memo_list = new memo_list(mname,mcontent,date);

        //child는 해당 키 위치로 이동하는 함수입니다. 키가 없는데 "zoo"와 같이 값을 지정한 경우 자동으로 생성합니다.
        //push()는 값을 넣을때 상위 키값을 랜덤으로 설정해 주는 함수입니다. 채팅기능을 만들떄 사용하면 좋습니다.
        databaseReference.child("memo").push().setValue(memo_list);

    }

    @Override
    public void onBackPressed() {

        if(key!=null) {
            Intent intent = new Intent(getApplicationContext(), look_memo.class); //그룹 만들기 화면으로 연결
            intent.putExtra("key", key); //해당 키값을 넘긴다
            startActivity(intent); //액티비티 열기
            finish();
        }
        super.onBackPressed();
    }


}