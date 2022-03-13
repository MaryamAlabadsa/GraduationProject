package com.example.graduationproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.Spinner.CategoriesSpinner;

import java.util.List;

public class SpinnerCategoriesAdapter extends BaseAdapter {
    private Context context;
    private List<CategoriesSpinner> categoriesSpinnerList;


    public SpinnerCategoriesAdapter(Context context, List<CategoriesSpinner> categoriesSpinners){
        this.context = context;
        this.categoriesSpinnerList = categoriesSpinners;
    }

    @Override
    public int getCount() {
        return categoriesSpinnerList != null ? categoriesSpinnerList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = LayoutInflater.from(context)
                .inflate(R.layout.activity_add_post, viewGroup, false);

        TextView name_cate = view1.findViewById(R.id.name_cat);
        TextView img = view1.findViewById(R.id.img_cat);

        name_cate.setText(categoriesSpinnerList.get(i).getCategories());
        img.setText(categoriesSpinnerList.get(i).getImage());
        return view1;
    }
}
