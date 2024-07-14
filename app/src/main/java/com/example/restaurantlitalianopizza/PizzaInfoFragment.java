package com.example.restaurantlitalianopizza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PizzaInfoFragment extends Fragment {

    private static final String ARG_PIZZA_NAME = "pizza_name";
    private static final String ARG_PIZZA_PRICE = "pizza_price";
    private static final String ARG_PIZZA_DESCRIPTION = "pizza_description";
    private static final String ARG_PIZZA_IMAGE = "pizza_image";

    private String pizzaName;
    private double pizzaPrice;
    private String pizzaDescription;
    private String pizzaImage;

    public PizzaInfoFragment() {
        // Required empty public constructor
    }

    public static PizzaInfoFragment newInstance(String name, double price, String description, String image) {
        PizzaInfoFragment fragment = new PizzaInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PIZZA_NAME, name);
        args.putDouble(ARG_PIZZA_PRICE, price);
        args.putString(ARG_PIZZA_DESCRIPTION, description);
        args.putString(ARG_PIZZA_IMAGE, image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaName = getArguments().getString(ARG_PIZZA_NAME);
            pizzaPrice = getArguments().getDouble(ARG_PIZZA_PRICE);
            pizzaDescription = getArguments().getString(ARG_PIZZA_DESCRIPTION);
            pizzaImage = getArguments().getString(ARG_PIZZA_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_info, container, false);

        ImageView pizzaImageView = view.findViewById(R.id.pizza_image);
        TextView pizzaNameView = view.findViewById(R.id.pizza_name);
        TextView pizzaPriceView = view.findViewById(R.id.pizza_price);
        TextView pizzaDescriptionView = view.findViewById(R.id.pizza_description);
        Button closeButton = view.findViewById(R.id.close_button);

        int imageResourceId = getResources().getIdentifier(pizzaImage, "drawable", getContext().getPackageName());
        pizzaImageView.setImageResource(imageResourceId);
        pizzaNameView.setText(pizzaName);
        pizzaPriceView.setText("$" + pizzaPrice);
        pizzaDescriptionView.setText(pizzaDescription);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(PizzaInfoFragment.this).commit();
                getActivity().finish();
            }
        });

        return view;
    }
}
