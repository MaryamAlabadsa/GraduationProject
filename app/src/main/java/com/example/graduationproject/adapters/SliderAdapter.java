package com.example.graduationproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.graduationproject.R;
import com.example.graduationproject.databinding.ImageSliderLayoutItemBinding;
import com.example.graduationproject.databinding.ImagesPostItemBinding;
import com.example.graduationproject.databinding.LayoutPostItemBinding;
import com.example.graduationproject.model.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        ImageSliderLayoutItemBinding binding = ImageSliderLayoutItemBinding.inflate(LayoutInflater
                        .from(parent.getContext())
                , parent, false);
        return new SliderAdapterVH(binding);

    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        SliderItem sliderItem = mSliderItems.get(0);
        viewHolder.binding.tvPostDesImageSlider.setText(sliderItem.getDescription());
        viewHolder.binding.tvPostTitleImageSlider.setText(sliderItem.getTitle());

        for (int i = 0; i < sliderItem.getImageList().size(); i++) {

            Glide.with(viewHolder.itemView)
                    .load(sliderItem.getImageList().get(i))
                    .fitCenter()
                    .into(viewHolder.binding.ivAutoImageSlider);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.get(0).getImageList().size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageSliderLayoutItemBinding binding;

        public SliderAdapterVH(ImageSliderLayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}