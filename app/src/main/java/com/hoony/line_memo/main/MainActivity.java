package com.hoony.line_memo.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityMainBinding;
import com.hoony.line_memo.memo.MemoActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);

        setListener();
    }

    private void setListener() {
        binding.fabCreateMemo.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_create_memo) {
            Intent intent = new Intent(MainActivity.this, MemoActivity.class);
            startActivity(intent);
        }
    }
}
