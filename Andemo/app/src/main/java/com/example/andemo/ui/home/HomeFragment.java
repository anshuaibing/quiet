package com.example.andemo.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.andemo.greendao.GreeenDaoDemo;
import com.example.andemo.ui.recycleview.ListviewTest;
import com.example.andemo.databinding.FragmentHomeBinding;
import com.example.andemo.ui.webview.PicActivity;
import com.example.andemo.ui.webview.webvieactivity;
import com.example.andemo.ui.MvvmDemo.DataActivity;
import com.example.andemo.R;

public class HomeFragment extends Fragment  {

    private FragmentHomeBinding binding;
    private Button button;
    private Button btnTwo;

    private EditText editText1;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();





        final TextView textView = binding.textHome;

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.btnOne);
        Button btnTwo = (Button) getActivity().findViewById(R.id.btnTwo);
        Button btnTre = (Button) getActivity().findViewById(R.id.btnThre);
        Button btnFour = (Button) getActivity().findViewById(R.id.btnFour);
        Button btnFive = (Button) getActivity().findViewById(R.id.btnFive);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                editText1 =(EditText) getActivity().findViewById(R.id.edit2);
//                editText1.setText("http://www.baidu.com");
//                String urlString = editText1.getText().toString();

                Intent intent1 = new Intent(getActivity(), PicActivity.class);
                startActivity(intent1);

            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent2 = new Intent(getActivity(), webvieactivity.class);
                startActivity(intent2);
            }
        });

        btnTre.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent3 = new Intent(getActivity(), DataActivity.class);
                startActivity(intent3);
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent4 = new Intent(getActivity(), ListviewTest.class);
                startActivity(intent4);
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent4 = new Intent(getActivity(), GreeenDaoDemo.class);
                startActivity(intent4);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}