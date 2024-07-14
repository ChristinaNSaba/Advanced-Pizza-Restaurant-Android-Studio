package com.example.restaurantlitalianopizza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class OrderInfoFragment extends Fragment {

    private static final String ARG_ORDER_NAME = "order_name";
    private static final String ARG_ORDER_PRICE = "order_price";
    private static final String ARG_ORDER_TYPE = "order_type";
    private static final String ARG_ORDER_SIZE = "order_size";
    private static final String ARG_ORDER_QUANTITY = "order_quantity";
    private static final String ARG_ORDER_DATE = "order_date";
    private static final String ARG_ORDER_IMAGE = "order_image";

    private String orderName;
    private double orderPrice;
    private String orderType;
    private String orderSize;
    private int orderQuantity;
    private String orderDate;
    private String orderImage;

    public OrderInfoFragment() {
    }

    public static OrderInfoFragment newInstance(String name, double price, String type, String size, int quantity, String date, String image) {
        OrderInfoFragment fragment = new OrderInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_NAME, name);
        args.putDouble(ARG_ORDER_PRICE, price);
        args.putString(ARG_ORDER_TYPE, type);
        args.putString(ARG_ORDER_SIZE, size);
        args.putInt(ARG_ORDER_QUANTITY, quantity);
        args.putString(ARG_ORDER_DATE, date);
        args.putString(ARG_ORDER_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderName = getArguments().getString(ARG_ORDER_NAME);
            orderPrice = getArguments().getDouble(ARG_ORDER_PRICE);
            orderType = getArguments().getString(ARG_ORDER_TYPE);
            orderSize = getArguments().getString(ARG_ORDER_SIZE);
            orderQuantity = getArguments().getInt(ARG_ORDER_QUANTITY);
            orderDate = getArguments().getString(ARG_ORDER_DATE);
            orderImage = getArguments().getString(ARG_ORDER_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_info, container, false);

        ImageView orderImageView = view.findViewById(R.id.order_image);
        TextView orderNameView = view.findViewById(R.id.order_name);
        TextView orderPriceView = view.findViewById(R.id.order_price);
        TextView orderTypeView = view.findViewById(R.id.order_type);
        TextView orderSizeView = view.findViewById(R.id.order_size);
        TextView orderQuantityView = view.findViewById(R.id.order_quantity);
        TextView orderDateView = view.findViewById(R.id.order_date);
        Button closeButton = view.findViewById(R.id.close_button);

        int imageResourceId = getResources().getIdentifier(orderImage, "drawable", getContext().getPackageName());
        orderImageView.setImageResource(imageResourceId);
        orderNameView.setText(orderName);
        orderPriceView.setText("$" + orderPrice);
        orderTypeView.setText("Type: " + orderType);
        orderSizeView.setText("Size: " + orderSize);
        orderQuantityView.setText("Quantity: " + orderQuantity);
        orderDateView.setText("Date: " + orderDate);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(OrderInfoFragment.this).commit();
                getActivity().finish();
            }
        });

        return view;
    }
}
