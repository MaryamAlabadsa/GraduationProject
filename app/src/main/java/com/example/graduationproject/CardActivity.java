package com.example.graduationproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.graduationproject.databinding.ActivityCardBinding;
import com.example.graduationproject.databinding.ActivityMainBinding;
import com.manojbhadane.PaymentCardView;

public class CardActivity extends AppCompatActivity {
    ActivityCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Callbacks
        binding.creditCard.setOnPaymentCardEventListener(new PaymentCardView.OnPaymentCardEventListener() {
            @Override
            public void onCardDetailsSubmit(String month, String year, String cardNumber, String cvv) {

            }

            @Override
            public void onError(String error) {
                Toast.makeText(CardActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelClick() {
                Toast.makeText(CardActivity.this, "close", Toast.LENGTH_SHORT).show();
            }
        });















//        binding.payview.setPayOnclickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////            Toast.makeText(CardActivity.this, binding.payview.isFillAllComponents()+"", Toast.LENGTH_SHORT).show();
////                Toast.makeText(CardActivity.this, "pay", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        binding.payview.setOnDataChangedListener(new Payview.OnChangelistener() {
//            @Override
//            public void onChangelistener(@Nullable PayModel payModel, boolean b) {
//                payModel.setCardMonth(0);
//                payModel.setCardYear(0);
////            Toast.makeText(CardActivity.this, payModel.getCardMonth()+"", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

}