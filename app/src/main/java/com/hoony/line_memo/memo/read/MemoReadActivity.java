package com.hoony.line_memo.memo.read;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityNoteReadBinding;

public class MemoReadActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityNoteReadBinding binding;
    MemoReadViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MemoReadActivity.this, R.layout.activity_note_read);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MemoReadViewModel.class);

        setObserve();
        setListener();
    }

    private void setObserve() {
        viewModel.getIsCompleteMutableDate().observe(MemoReadActivity.this, isComplete -> {
            Toast.makeText(this, "Save complete", Toast.LENGTH_SHORT).show();
        });
    }

    private void setListener() {
        binding.ibEdit.setOnClickListener(MemoReadActivity.this);
    }

    @Override
    public void onClick(View view) {

    }
}
