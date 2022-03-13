package com.example.graduationproject.Spinner;

import com.example.graduationproject.R;

import java.util.ArrayList;
import java.util.List;

public class DataSpinner {
    public static List<CategoriesSpinner> getCategoriesSpinner(){
        List<CategoriesSpinner> categoriesSpinnerList = new ArrayList<>();

        CategoriesSpinner clothes = new CategoriesSpinner();
        clothes.setCategories("Clothes");
        clothes.setImage(R.drawable.clothes);
        categoriesSpinnerList.add(clothes);

        CategoriesSpinner money = new CategoriesSpinner();
        clothes.setCategories("Money");
        clothes.setImage(R.drawable.money);
        categoriesSpinnerList.add(money);


        return categoriesSpinnerList;
    }
}
