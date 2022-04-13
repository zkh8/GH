package com.example.gh.bean;

import com.example.gh.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class SymxInfo {

    public int id;
    public String title;
    //当前余额
    public Double cmoney;
    //变动金额
    public Double money;
    public String time;
    public String remark;
    public int status;
    public int type;
    public String remarks;
    public int ltype = 0;

    public static String[] statustip = {"发起", "已审核", "成功", "失败", "取消"};


    public SymxInfo() {

        id = 0;
        title = "";
        cmoney = 0.0;
        money = 0.0;
        time = "";
        remark = "";
        status = 0;
        type = 0;
        remarks = "";
        ltype = 0;
    }



    public static List<SymxInfo> getDefaultListSz(JSONArray datas) {

        List<SymxInfo> infoList = new ArrayList<SymxInfo>();


        try {
            for(int i = 0; i < datas.length(); i ++){

                JSONObject item = datas.getJSONObject(i);

                SymxInfo info = new SymxInfo();

                info.id = item.getInt("id");
                info.money = item.getDouble("money");
                info.status = item.getInt("status");

                if(item.has("time") && !item.getString("time").isEmpty() && item.getInt("time") > 0){

                    info.time = DateUtil.formatDate(item.getLong("time") * 1000);
                }


                info.type = item.getInt("type");
                info.cmoney = item.getDouble("cmoney");


                if(info.type == 1){

                    info.title = "充值";
                }else if(info.type == 2){

                    info.title = "抽奖";
                }else if(info.type == 2){

                    info.title = "抽奖";
                }else if(info.type == 3){


                    if(info.status == 3){

                        info.title = "提现取消";
                    }
                    else if(info.status == 2){

                        info.title = "提现冻结";
                    }else{

                        info.title = "提现";
                    }
                }

                infoList.add(info);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
        return infoList;
    }

    public static List<SymxInfo> getDefaultListTx(JSONArray datas) {

        List<SymxInfo> infoList = new ArrayList<SymxInfo>();


        try {
            for(int i = 0; i < datas.length(); i ++){

                JSONObject item = datas.getJSONObject(i);

                SymxInfo info = new SymxInfo();

                info.ltype = 1;
                info.id = item.getInt("id");
                info.money = item.getDouble("money");
                info.status = item.getInt("status");
                info.remarks = item.getString("remarks");

                if(item.has("time") && !item.getString("time").isEmpty() && item.getInt("time") > 0){

                    info.time = DateUtil.formatDate(item.getLong("time") * 1000);
                }

                info.title = "提现";

                infoList.add(info);
            }
        }catch (Exception e){

            e.printStackTrace();
        }
        return infoList;
    }
}
