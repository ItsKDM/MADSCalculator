package com.example.kdm.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class CalculatorActivity extends AppCompatActivity {

    FirebaseUser currentuser;
    FirebaseAuth firebaseAuth;
    private EditText eT1,eT2;
    private Button btnEqual,btnHistory;
    private int count=0;
    private String expression="";
    private String text="";
    private int result=0;
    History historyData;

    @Override
    protected void onStart() {
        super.onStart();
        if (currentuser == null){
            Intent i = new Intent(CalculatorActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                firebaseAuth.signOut();
                Intent i = new Intent(CalculatorActivity.this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        firebaseAuth=FirebaseAuth.getInstance();
        currentuser=firebaseAuth.getCurrentUser();
        eT1=findViewById(R.id.edittext1);
        eT2=findViewById(R.id.edittext2);
        btnEqual=findViewById(R.id.operatorEquals);
        historyData=new History();
        btnHistory=findViewById(R.id.btnHistory);

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eT2.length()!=0)
                {
                    text= eT2.getText().toString();
                    expression=eT1.getText().toString()+text;
                    eT1.setText(expression);
                }
                eT2.setText("");
                if(expression.length()==0)
                {
                    expression="0.0";
                }
                else {
                    //Passing the expression to solve function//
                    result=solve(expression);

                    //Storing the data to Firebase Realtime Database data upto 10 values/user
                    count=count+1;
                    if(count>=0 && count<=10)
                    {
                        History p=new History(expression,result,count);
                        FirebaseDatabase.getInstance().getReference("History").child(firebaseAuth.getUid())
                                .child(String.valueOf(count)).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        Toast.makeText(CalculatorActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(CalculatorActivity.this, "Error Occurred : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
                eT1.setText(""+result);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalculatorActivity.this, ViewHistory.class);
                startActivity(i);
            }
        });
    }
    public void onClick(View view){
        switch(view.getId())
        {
            case R.id.num0:
                eT2.setText(eT2.getText()+"0");
                break;
            case R.id.num1:
                eT2.setText(eT2.getText()+"1");
                break;
            case R.id.num2:
                eT2.setText(eT2.getText()+"2");
                break;
            case R.id.num3:
                eT2.setText(eT2.getText()+"3");
                break;
            case R.id.num4:
                eT2.setText(eT2.getText()+"4");
                break;
            case R.id.num5:
                eT2.setText(eT2.getText()+"5");
                break;
            case R.id.num6:
                eT2.setText(eT2.getText()+"6");
                break;
            case R.id.num7:
                eT2.setText(eT2.getText()+"7");
                break;
            case R.id.num8:
                eT2.setText(eT2.getText()+"8");
                break;
            case R.id.num9:
                eT2.setText(eT2.getText()+"9");
                break;
            case R.id.clearText:
                eT1.setText("");
                eT1.setText("");
                expression="";
                break;
            case R.id.backspace:
                text=eT2.getText().toString();
                if(text.length()>0)
                {
                    eT2.setText(text.substring(0,text.length()-1));
                }
                break;
            case R.id.operatorAdd:
                op("+");
                break;
            case R.id.operatorSub:
                op("-");
                break;
            case R.id.operatorMul:
                op("*");
                break;
            case R.id.operatorDiv:
                op("/");
                break;
        }
    }

    public void op(String oprtr){
        if(eT2.length()!=0){
            String text = eT2.getText().toString();
            eT1.setText(eT1.getText()+text+oprtr);
            eT2.setText("");
        }
        else{
            String text = eT1.getText().toString();
            if (text.length()>0)
            {
                eT1.setText(text+oprtr);
            }
        }
    }
    public static int solve(String exp)
    {
        char[] tokens=exp.toCharArray();
        Stack<Integer> numbers=new Stack<Integer>();
        Stack<Character> operators=new Stack<Character>();

        for(int i=0;i<tokens.length;i++)
        {
            if(tokens[i]==' ')
                continue;
            if(tokens[i]>='0' && tokens[i]<='9')
            {
                StringBuffer stringBuffer=new StringBuffer();
                while(i<tokens.length && tokens[i]>='0' && tokens[i]<='9')
                    stringBuffer.append(tokens[i++]);
                numbers.push(Integer.parseInt(stringBuffer.toString()));
                i--;
            }

            else if(tokens[i]=='+'||tokens[i]=='-'||tokens[i]=='*'||tokens[i]=='/')
            {
                while(!operators.empty() && hasPrecedence(tokens[i],operators.peek()))
                {
                    numbers.push(applyOperation(operators.pop(),numbers.pop(),numbers.pop()));
                }
                operators.push(tokens[i]);
            }
        }
        while(!operators.empty())
        {
            numbers.push(applyOperation(operators.pop(),numbers.pop(),numbers.pop()));
        }
        return numbers.pop();
    }

    public static boolean hasPrecedence(
            char op1, char op2)
    {
        if ((op1 == '*' ) && (op2 == '/' || op2 == '-'|| op1 == '+'))
            return false;
        else if ((op1 == '+' ) && (op2 == '/' || op2 == '-'))
            return false;
        else if ((op1 == '/' ) && (op2 == '-'))
            return false;
        else
            return true;
    }

    public static int applyOperation(char op, int y, int x)
    {
        switch (op)
        {
            case '+':
                return x + y;
            case '-':
                return x - y;
            case '*':
                return x * y;
            case '/':
                if (y == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return x / y;
        }
        return 0;
    }
}