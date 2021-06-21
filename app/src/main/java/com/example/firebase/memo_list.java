package com.example.firebase;

//테이블이라고 생각하고, 테이블에 들어갈 속성값을 넣기
//파이어베이스는 RDBMS와 다르기 때문에 테이블이라는 개념이 없음. 원래는 키값이라고 부름
public class memo_list {
    String mname; //제목
    String mcontent; //내용
    String date; //날짜
    String key; //키값

    public memo_list(){} //이건 기본적으로 쓰더라구요.


    //get, set 함수는 커스텀 리스트 뷰를 사용하시는 분들과.. 필요하신 분만 작성하시면 좋습니다.
    public String getmname() {
        return mname;
    }

    public void setmname(String mname) {
        this.mname = mname;
    }

    public String getmcontent() {
        return mcontent;
    }

    public void setmcontent(String mcontent) {
        this.mcontent = mcontent;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getkey() {
        return key;
    }

    public void setkey(String key) {
        this.key = key;
    }




    //값을 추가할때 쓰는 함수, MainActivity에서 addanimal함수에서 사용할 것임.
    public memo_list(String mname, String mcontent, String date){
        this.mname = mname;
        this.mcontent = mcontent;
        this.date = date;
    }
}
