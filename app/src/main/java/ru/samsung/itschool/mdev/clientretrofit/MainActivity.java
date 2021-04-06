package ru.samsung.itschool.mdev.clientretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private TextView result;
    private Button button;
    private final String URL_BASE = "http://10.0.2.2:8000";
    private static Retrofit retrofit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        button = findViewById(R.id.sendPOST);
        button.setOnClickListener(v -> {
            EditText lastname = findViewById(R.id.lastname);
            EditText firstname = findViewById(R.id.firstname);
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            StudentAPIService api = retrofit.create(StudentAPIService.class);
            // Создаем объект типа Student
            Student student = new Student();
            student.setFirstName(firstname.getText().toString());
            student.setLastName(lastname.getText().toString());
            // Готовим вызов
            Call<Answer> call = api.addStudent(student);
            // Выполняем асинхронный запрос
            call.enqueue(new Callback<Answer>() {
                @Override
                public void onResponse(Call<Answer> call, Response<Answer> response) {
                    if (response.isSuccessful()) {
                        // Получили ответ в виде JSON в теле
                        Answer body = response.body();
                        result.setText(String.format("%s: %s %s", body.getStatus(), body.getStudent().getFirstName(), body.getStudent().getLastName()));
                    }
                }
                @Override
                public void onFailure(Call<Answer> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }
}