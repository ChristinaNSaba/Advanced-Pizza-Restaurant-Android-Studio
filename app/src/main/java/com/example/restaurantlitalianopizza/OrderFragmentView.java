package com.example.restaurantlitalianopizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class OrderFragmentView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view_fragment);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("order_name")) {
            String orderName = intent.getStringExtra("order_name");
            double orderPrice = intent.getDoubleExtra("order_price", 0.0);
            String orderType = intent.getStringExtra("order_type");
            String orderSize = intent.getStringExtra("order_size");
            int orderQuantity = intent.getIntExtra("order_quantity", 0);
            String orderDate = intent.getStringExtra("order_date");
            String orderImage = intent.getStringExtra("order_image");

            OrderInfoFragment orderInfoFragment = OrderInfoFragment.newInstance(
                    orderName,
                    orderPrice,
                    orderType,
                    orderSize,
                    orderQuantity,
                    orderDate,
                    orderImage
            );
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container1, orderInfoFragment)
                    .commit();
        }
    }
}
