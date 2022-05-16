package com.example.graduationproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutDialogAddOrderBinding;

import java.util.ArrayList;


public class MyDialogAddOrder extends Dialog {
    LayoutDialogAddOrderBinding binding;
    Context context;
 Dialoginterface dialoginterface;

    public MyDialogAddOrder(Context context, Dialoginterface dialoginterface) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.context = context;
        this.dialoginterface = dialoginterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        binding = LayoutDialogAddOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.coustom_blue)); //change status bar background

        binding.addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()!=null) {
                    dialoginterface.yes(validation());
                }
            }
        });
        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private String validation() {
        if (binding.addOrderEt.getText().toString().trim().isEmpty()) {
            binding.addOrderEt.requestFocus();
            binding.addOrderEt.setError("THIS FIELD CANNOT BE EMPTY");
            return null;
        } else {
            return binding.addOrderEt.getText().toString();
        }

    }
}
