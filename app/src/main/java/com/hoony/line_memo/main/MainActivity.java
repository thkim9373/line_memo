package com.hoony.line_memo.main;

import android.os.Bundle;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityMainBinding;
import com.hoony.line_memo.main.fragments.editor.EditorFragment;
import com.hoony.line_memo.main.fragments.list.ListFragment;
import com.hoony.line_memo.main.fragments.viewer.ViewerFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        viewModel = new ViewModelProvider(MainActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);

        fragmentList.add(new ListFragment());
        fragmentList.add(new ViewerFragment());
        fragmentList.add(new EditorFragment());

        setObserve();
    }

    private void setObserve() {
        viewModel.getFragmentIndex().observe(MainActivity.this, this::replaceFragment);
    }

    public void replaceFragment(int fragmentIndex) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.flMain.getId(), this.fragmentList.get(fragmentIndex)).commit();
    }
}
