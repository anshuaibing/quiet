package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JNIActivity extends AppCompatActivity {

    private TextView input;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        input = this.findViewById(R.id.input);
        result = this.findViewById(R.id.result);
    }
    String input1 = "";
    String oper = "";
    String input2 = "";
    boolean flag = false;
    boolean input1_flag = false;
    boolean res_flag=false;
    public void oneOnClick(View view){
        if(view instanceof TextView){
            String oneText = ((TextView)view).getText().toString();
//            if(flag){
                switch (oneText){
                    case "C":
                        input.setText("");
                        result.setText("");
                        input1 = "";
                        input2 = "";
                        oper ="";
                        input1_flag=false;
                        flag=false;
                        break;
//                }
//            }else{
//                switch (oneText){
                    case "+/-":
                        break;
                    case "%":
                        break;
                    case "1":
                        if(!input1_flag){
                            input1 += "1";
                            input.setText(input1);
                        }else{
                            input2 += "1";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "2":
                        if(!input1_flag){
                            input1 += "2";
                            input.setText(input1);
                        }else{
                            input2 += "2";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "3":
                        if(!input1_flag){
                            input1 += "3";
                            input.setText(input1);
                        }else{
                            input2 += "3";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "4":
                        if(!input1_flag){
                            input1 += "4";
                            input.setText(input1);
                        }else{
                            input2 += "4";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "5":
                        if(!input1_flag){
                            input1 += "5";
                            input.setText(input1);
                        }else{
                            input2 += "5";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "6":
                        if(!input1_flag){
                            input1 += "6";
                            input.setText(input1);
                        }else{
                            input2 += "6";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "7":
                        if(!input1_flag){
                            input1 += "7";
                            input.setText(input1);
                        }else{
                            input2 += "7";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "8":
                        if(!input1_flag){
                            input1 += "8";
                            input.setText(input1);
                        }else{
                            input2 += "8";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "9":
                        if(!input1_flag){
                            input1 += "9";
                            input.setText(input1);
                        }else{
                            input2 += "9";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "0":
                        if(!input1_flag){
                            input1 += "0";
                            input.setText(input1);
                        }else{
                            input2 += "0";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case ".":
                        if(!input1_flag){
                            input1 += ".";
                            input.setText(input1);
                        }else{
                            input2 += ".";
                            input.setText(input1+oper+input2);
                        }
                        break;
                    case "÷":
                        oper = "÷";
                        input1_flag = true;
                        res_flag=false;
                        input.setText(input1+oper+input2);
                        break;
                    case "*":
                        oper = "*";
                        input1_flag = true;
                        res_flag=false;
                        input.setText(input1+oper+input2);
                        break;
                    case "-":
                        oper = "-";
                        input1_flag = true;
                        res_flag=false;
                        input.setText(input1+oper+input2);
                        break;
                    case "+":
                        oper = "+";
                        input1_flag = true;
                        res_flag=false;
                        input.setText(input1+oper+input2);
                        break;
                    case "=":

                        if(res_flag)
                        {
                            flag=false;
                            input1_flag = false;
                            input1 = result.getText().toString();
//                            input2 = "";
//                            oper = "";
//                            res_flag=false;

                        }
                        else
                        {


                            float num1 = Float.valueOf(input1).floatValue();
                            float num2 = Float.valueOf(input2).floatValue();

                            if (oper.equals("+")) {
                                result.setText(String.valueOf(add(num1 , num2)));
                                flag=true;
                            } else if (oper.equals("-")) {
                                result.setText(String.valueOf(sub(num1 , num2)));
                                flag=true;
                            } else if (oper.equals("*")) {
                                result.setText(String.valueOf(mul(num1 , num2)));
                                flag=true;
                            }
                            if (oper.equals("÷")) {
                                if (num2 == 0) {
                                    Toast.makeText(JNIActivity.this, "除数不能为0", Toast.LENGTH_LONG).show();
                                } else {
                                    result.setText(String.valueOf(div(num1 , num2)));
                                    flag=true;
                                }
                            }
                            flag=false;
                            input1_flag = false;
                            res_flag=true;
                            oper="";
                            input1 = result.getText().toString();
                            input2 = "";


                        }

                        break;
                }

//            }

        }

    }


//    private Button btnAdd,btnSub,btnMul,btnDiv;
//    private EditText inputA,inputB;
//    private TextView tvResult;
//    public int result=0;
//    public int a=1;
//    public int b=1;


    // Used to load the 'myapplication' library on application startup.
    static {
        System.loadLibrary("myapplication");
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        inputA=(EditText) findViewById(R.id.inputa);
//        inputB=(EditText)findViewById(R.id.inputb);
//        tvResult=this.findViewById(R.id.result);
//}

//    public void buttonClick(View view) {
//        switch (view.getId())
//        {
//            case R.id.add:
//                init();
//                result=MainActivity.add(a,b);
//                tvResult.setText(""+result);
//                break;
//            case R.id.sub:
//                init();
//                result=MainActivity.sub(a,b);
//                tvResult.setText(""+result);
//                break;
//            case R.id.mul:
//                init();
//                result=MainActivity.mul(a,b);
//                tvResult.setText(""+result);
//                break;
//            case R.id.div:
//                init();
//                result=MainActivity.div(a,b);
//                tvResult.setText(""+result);
//                break;
//            default:
//                break;
//        }
//    }
//
//public void init()
//    {
//        String strA=inputA.getText().toString();
//        String strB=inputB.getText().toString();
//        a=Integer.parseInt(strA);
//        b=Integer.parseInt(strB);
//    }

    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
   // public native String stringFromJNI();
    //加法
    public static native float  add(float a,float b);
    //减法

    public static native float sub(float a,float b);

    //乘法
    public static native float mul(float a,float b);

    //除法
    public static native float div(float a,float b);



}