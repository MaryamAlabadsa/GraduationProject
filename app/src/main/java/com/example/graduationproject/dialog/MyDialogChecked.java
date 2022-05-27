package com.example.graduationproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.LayoutDialogChangeFullterBinding;
import com.example.graduationproject.utils.AppSharedPreferences;


public class MyDialogChecked extends Dialog {
    LayoutDialogChangeFullterBinding binding;
    Context context;
    DialogCheckedInterface dialogCheckedInterface;

    public MyDialogChecked(Context context, DialogCheckedInterface dialogCheckedInterface) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.context = context;
        this.dialogCheckedInterface = dialogCheckedInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        binding = LayoutDialogChangeFullterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.coustom_blue)); //change status bar background

        AppSharedPreferences sharedPreferences = new AppSharedPreferences(context);
        int readInt = sharedPreferences.readInt(AppSharedPreferences.IS_DONATION);
        if (readInt == 0) {
            binding.donationCheck.setChecked(true);
            Toast.makeText(context, readInt+"", Toast.LENGTH_SHORT).show();

        } else{
            binding.requestCheck.setChecked(true);

        }

        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int isDonation = 0;
                if (binding.requestCheck.isChecked()) {
                    isDonation = 1;
                }
                sharedPreferences.writeInt(AppSharedPreferences.IS_DONATION, isDonation);

                dialogCheckedInterface.yes(isDonation);
                dismiss();

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

}
