package com.example.firebase;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

//사진 관련 부분은 일단 주석처리 했습니다. 굳이 그룹에서 데이터베이스 연동할 이유 없어보여서요.
public class memo_Adapter extends RecyclerView.Adapter<memo_Adapter.CustomViewHoler> {


    private ArrayList<memo_list> arrayList;
    private Context context;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    String key; //리스트 키값



    Dialog Dialog;//그룹 가입을 위한 Dialog
    TextView dia_content; //다이얼로그 내용


    public memo_Adapter(ArrayList<memo_list> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_list, parent, false);
        CustomViewHoler holder = new CustomViewHoler(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final CustomViewHoler holder, int position) {

        //다이얼로그 관련 설정
        Dialog=new Dialog(context); //context로 하니까 잘 됩니다.
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//제목 제거
        Dialog.setContentView(R.layout.dialog);

        dia_content = (TextView)Dialog.findViewById(R.id.dia_content);

        holder.mname.setText(arrayList.get(position).getmname());

        String memo_name = holder.mname.getText().toString();

        //클릭 이벤트
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = arrayList.get(position).getkey(); //키값 구하기

                Intent intent;
                intent = new Intent(context, look_memo.class); //그룹 상세 화면으로 연결
                intent.putExtra("key", key); //해당 키값을 넘긴다
                context.startActivity(intent); //액티비티 열기

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                key = arrayList.get(position).getkey(); //키값 구하기
                dia_content.setText(memo_name + " 메모를 삭제하시겠습니까?");
                showPlanDialog(key);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        //삼항 연산자, 배열이 비어있지 않으면 왼쪽이 실행!
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class CustomViewHoler extends RecyclerView.ViewHolder {

        TextView mname;


        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.mname = itemView.findViewById(R.id.mname);

        }
    }

    //메모 삭제 다이얼로그 호출
    public void showPlanDialog(String key){
        Dialog.show(); //다이얼로그 출력
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//끝부분을 둥굴게 하기 위해 투명색 지정
        Button noBtn= Dialog.findViewById(R.id.noBtn);//취소 버튼
        Button yesBtn=Dialog.findViewById(R.id.yesBtn);//저장 버튼

        //취소 버튼
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();//다이얼로그 닫기
            }
        });

        //확인 버튼
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                    databaseReference = database.getReference();
                    databaseReference.child("memo").child(key).removeValue(); //삭제


                }catch(Exception e){//예외
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext().getContext(),"오류발생",Toast.LENGTH_SHORT).show();//토스메세지 출력
                }
                Dialog.dismiss();//다이얼로그 닫기
            }
        });

    }
}