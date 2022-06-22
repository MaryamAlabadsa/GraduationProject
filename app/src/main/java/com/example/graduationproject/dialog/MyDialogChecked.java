package com.example.graduationproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.graduationproject.databinding.LayoutDialogChangeFullterBinding;
import com.example.graduationproject.utils.Constant;

import es.dmoral.toasty.Toasty;


public class MyDialogChecked extends Dialog {
    LayoutDialogChangeFullterBinding binding;
    Context context;
    DialogCheckedInterface dialogCheckedInterface;
    Constant checkedNum = Constant.EMPTY_CHOICE;//2==all , 1==donation , 0==request
    Constant postStatus = Constant.EMPTY_CHOICE;//2==all , 1==donation , 0==request

    public MyDialogChecked(Context context, Constant checkedNum, Constant postStatus, DialogCheckedInterface dialogCheckedInterface) {
        super(context);
//        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.context = context;
        this.checkedNum = checkedNum;
        this.postStatus = postStatus;
        this.dialogCheckedInterface = dialogCheckedInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        binding = LayoutDialogChangeFullterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (checkedNum == Constant.DONATION_CHECKED) {
            binding.donationCheck.setChecked(true);
        } else if (checkedNum == Constant.REQUEST_CHECKED) {
            binding.requestCheck.setChecked(true);
        } else {
            binding.donationCheck.setChecked(true);
            binding.requestCheck.setChecked(true);
        }

        if (postStatus == Constant.COMPLETE_POST)
            binding.completeCheck.setChecked(true);
        else if (postStatus == Constant.PENDING_POST)
            binding.pendingCheck.setChecked(true);
        else{
            binding.completeCheck.setChecked(true);
            binding.pendingCheck.setChecked(true);
        }




        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // all btn checked
                if (binding.requestCheck.isChecked() && binding.donationCheck.isChecked() &&
                        binding.completeCheck.isChecked() && binding.requestCheck.isChecked()) {
                    checkedNum = Constant.ALL_CHECKED;
                    postStatus = Constant.ALL_CHECKED;
                }
                // check donation & request btns
                if (binding.requestCheck.isChecked() && binding.donationCheck.isChecked()) {
                    checkedNum = Constant.ALL_CHECKED;
                    postStatus = Constant.ALL_CHECKED;
                } else if (binding.requestCheck.isChecked()) {
                    checkedNum = Constant.REQUEST_CHECKED;
                } else if (binding.donationCheck.isChecked()) {
                    checkedNum = Constant.DONATION_CHECKED;
                }
                // check donation & request btns
                if (binding.completeCheck.isChecked() && binding.pendingCheck.isChecked()) {
//                    checkedNum = Constant.ALL_CHECKED;
                    postStatus = Constant.ALL_CHECKED;
                } else if (binding.completeCheck.isChecked()) {
                    postStatus = Constant.COMPLETE_POST;
                } else if (binding.pendingCheck.isChecked()) {
                    postStatus = Constant.PENDING_POST;
                }
                if (postStatus == Constant.EMPTY_CHOICE || postStatus == Constant.EMPTY_CHOICE) {
                    Toast.makeText(context, "you must chose a section", Toast.LENGTH_SHORT).show();
                } else {
                    dialogCheckedInterface.yes(checkedNum, postStatus);
                    dismiss();
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

}
