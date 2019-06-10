package com.adapter;

import java.io.Serializable;



public class Message implements Serializable {

    public int sno;
    public String cat;
    public String Que;
    public String Ans;

    public String getAns_q() {
        return Ans_q;
    }

    public void setAns_q(String ans_q) {
        Ans_q = ans_q;
    }

    public String Ans_q;
    public String Level;

    public String Opt_1;

    public String getOpt_1() {
        return Opt_1;
    }

    public void setOpt_1(String opt_1) {
        Opt_1 = opt_1;
    }

    public String getOpt_2() {
        return Opt_2;
    }

    public void setOpt_2(String opt_2) {
        Opt_2 = opt_2;
    }

    public String getOpt_3() {
        return Opt_3;
    }

    public void setOpt_3(String opt_3) {
        Opt_3 = opt_3;
    }

    public String Opt_2;
    public String Opt_3;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getQue() {
        return Que;
    }

    public void setQue(String que) {
        Que = que;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }



    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }






    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }


}












