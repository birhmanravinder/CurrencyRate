package com.birhman.currencyrate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.birhman.currencyrate.R;
import com.birhman.currencyrate.listener.DeleteListener;
import com.birhman.currencyrate.local.SqliteHelperDatabase;
import com.birhman.currencyrate.model.MyCustomModel;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CustomViewHolder> {
    private static final String TAG = CurrencyAdapter.class.getSimpleName();
    private List<MyCustomModel> menuCategoryList;
    private Context context;
    DeleteListener deleteListener;
    private int rowLayout;
    SqliteHelperDatabase helperDatabase;

    public CurrencyAdapter(List<MyCustomModel> menuCategoryList, int rowLayout, Context context) {
        this.menuCategoryList = menuCategoryList;
        this.context = context;
        this.rowLayout = rowLayout;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        helperDatabase = new SqliteHelperDatabase(context);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        try{
            (holder).txtName.setText(menuCategoryList.get(position).getStrKey().toUpperCase());
            (holder).txtValue.setText(menuCategoryList.get(position).getaDouble());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName, txtValue;
        ImageView imgDelete;

        public CustomViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_name);
            txtValue = view.findViewById(R.id.txt_value);
            imgDelete = view.findViewById(R.id.img_delete);

            imgDelete.setOnClickListener(this);

            view.setTag(view);
        }
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            switch (view.getId()){
                case R.id.img_delete:
                //    String id = menuCategoryList.get(getAdapterPosition()).getId();
                //    helperDatabase.deleteEntry(id);
                //    this.notifyDataSetChanged();
                    String titleDetail = "detail";
                    if (deleteListener != null) deleteListener.OnDeleteItemClicked(view, getAdapterPosition(), titleDetail);
                    notifyDataSetChanged();
                    break;
            }
        }
    }

    public void setClickListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    public int getItemCount()  {
        return menuCategoryList == null ? 0 : menuCategoryList.size();
    }

    public void removeAt(int position) {
        menuCategoryList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, menuCategoryList.size());
    }
}
