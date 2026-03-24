package sg.vantagepoint.jnidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public native String helloFromJNI();
    public native int factorial(int n);
    public native String reverseString(String s);
    public native int sumArray(int[] values);

    static {
        System.loadLibrary("jnidemo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvHello;
        tvHello = findViewById(R.id.tvHello);
        TextView tvFact = findViewById(R.id.tvFact);
        TextView tvReverse = findViewById(R.id.tvReverse);
        TextView tvArray = findViewById(R.id.tvArray);
        tvHello.setText(helloFromJNI());
        int fact10 = factorial(20);
        if (fact10 >= 0) {
            tvFact.setText("Factoriel de 10 = " + fact10);
        } else {
            tvFact.setText("Erreur factoriel, code = " + fact10);
        }
        String reversed = reverseString("");
        tvReverse.setText("Texte inverse : " + reversed);
        int[] numbers = {};
        int sum = sumArray(numbers);
        tvArray.setText("Somme du tableau = " + sum);
    }
}