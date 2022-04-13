package com.example.gh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gh.R;
import com.example.gh.bean.SymxInfo;

import java.util.List;

public class SymxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<SymxInfo> infos;
    private Context context;
    private int ltype;
    private int colors[] = {};

    private final static int TYPE_CONTENT=0;//正常内容
    private final static int TYPE_FOOTER=1;//下拉刷新
    private int lType;
    private boolean loadmore = false;


    public List<SymxInfo> getInfos(){

        return infos;
    }

    public SymxAdapter(List<SymxInfo> infos, Context context, int ltype) {
        this.infos = infos;
        this.context = context;
        this.ltype = ltype;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==TYPE_FOOTER){

            View view = View.inflate(context, R.layout.list_foot,null);
            return new SymxAdapter.MyFootHolder(view);
        }else{

            View view = View.inflate(context, R.layout.symx_item,null);
            return new SymxAdapter.MyViewHolder(view);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        Log.d("onBindViewHolder", "position : " + position);

        if(getItemViewType(position) == TYPE_FOOTER){

            SymxAdapter.MyFootHolder holder = (SymxAdapter.MyFootHolder) viewHolder;

            if(infos.size() < 10 || !loadmore){

                holder.contentLoadingProgressBar.setVisibility(View.GONE);
            }else{


                holder.contentLoadingProgressBar.setVisibility(View.VISIBLE);
            }
        }else{

            SymxInfo info = infos.get(position);

            SymxAdapter.MyViewHolder holder = (SymxAdapter.MyViewHolder) viewHolder;

            if(info.ltype == 0){

                holder.tv_title.setText(info.title);

                if(info.type == 3){

                    if(info.status == 3){

                        holder.tv_amoney.setText("+"+info.money + "元");
                    }
                    else{

                        holder.tv_amoney.setText("-"+info.money + "元");
                    }
                }else{

                    if(info.money < 0){

                        holder.tv_amoney.setText(info.money + "元");
                    }else{

                        holder.tv_amoney.setText("+"+info.money + "元");
                    }
                }

                holder.tv_cmoney.setText("余额 "+info.cmoney + "元");
                holder.tv_time.setText("" + info.time);
            }
            else if(info.ltype == 1){

                holder.tv_title.setText(info.title);
                holder.tv_amoney.setText(""+info.money + "元");

                holder.tv_cmoney.setText(SymxInfo.statustip[info.status]);

                holder.tv_time.setText("" + info.time);
            }

            holder.itemView.setTag(position);
        }


    }

    @Override
    public int getItemCount() {
        return infos == null ? 1 : infos.size() + 1;
    }

    @Override
    public int getItemViewType(int position){

        if (position == infos.size()){

            return TYPE_FOOTER;
        }
        return TYPE_CONTENT;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title, tv_cmoney, tv_amoney, tv_time;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.id_title);
            tv_amoney = itemView.findViewById(R.id.id_amoney);
            tv_cmoney = itemView.findViewById(R.id.id_cmoney);
            tv_time = itemView.findViewById(R.id.id_time);

            itemView.setOnClickListener(SymxAdapter.this);
        }
    }


    public class MyFootHolder extends RecyclerView.ViewHolder {

        ContentLoadingProgressBar contentLoadingProgressBar;

        public MyFootHolder(@NonNull View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.id_sb_list_foot_progress);
        }
    }


    public void clearData(){


        int len = infos.size();

        Log.d("SbAdapter", " clearData " + infos.toString() +"   len  "+len);

        infos.clear();

        for(int i= 0; i < len; i ++){

            notifyItemRemoved(i);
        }
        notifyDataSetChanged();
    }

    public void reloadData(List<SymxInfo> list, boolean lm){

        loadmore = lm;
        Log.d("SbAdapter", " reloadData " + list.size());

        clearData();

        int len = list.size();

        for(int i= 0; i < len; i ++){

            infos.add(i, list.get(i));
            notifyItemInserted(i);
        }
    }

    public void addData(List<SymxInfo> list, int position, boolean lm){

        loadmore = lm;

        int len = infos.size();

        Log.d("SbAdapter", " addData " +len + " position " + position);

        int lenn = list.size();

        for(int i = 0, k = 0; i < lenn; i ++){

            int iv = list.get(i).id;

            boolean hv = false;
            for(int j = 0; j < infos.size(); j ++){

                if(infos.get(j).id == iv){

                    hv = true;
                    break;
                }
            }

            if(hv){
                continue;
            }

            infos.add(list.get(i));
            k ++;
        }
        notifyDataSetChanged();
    }


    public enum ViewName{
        TIEM,
        PRACTISE
    }

    public interface OnItemClickListener {
        void onClick(View v, SymxAdapter.ViewName practise, int position);
    }

    private SymxAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(SymxAdapter.OnItemClickListener listener){

        this.onItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {

        int position = (int) v.getTag();
        if(onItemClickListener != null){

            switch (v.getId()){

                default:
                    onItemClickListener.onClick(v, SymxAdapter.ViewName.TIEM, position);
                    break;
            }
        }
    }
}
