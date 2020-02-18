package com.hoony.line_memo.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityMainBinding;
import com.hoony.line_memo.main.fragments.MemoReadFragment;
import com.hoony.line_memo.main.fragments.MemoWriteFragment;
import com.hoony.line_memo.main.fragments.list.MemoListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int FRAGMENT_LIST = 0;
    public static final int FRAGMENT_READ = 1;
    public static final int FRAGMENT_WRITE = 2;

    ActivityMainBinding binding;
    MainViewModel viewModel;
    List<Fragment> fragmentList = new ArrayList<>();
    private int currentFragmentIndex = FRAGMENT_LIST;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);

        fragmentList.add(new MemoListFragment());
        fragmentList.add(new MemoReadFragment());
        fragmentList.add(new MemoWriteFragment());

        replaceFragment(FRAGMENT_LIST);
    }

    public void replaceFragment(int fragmentIndex) {
        currentFragmentIndex = fragmentIndex;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.flMain.getId(), this.fragmentList.get(fragmentIndex)).commit();
    }

    @Override
    public void onBackPressed() {
        switch (currentFragmentIndex) {
            case FRAGMENT_LIST:
                finish();
                break;
            case FRAGMENT_READ:
                replaceFragment(FRAGMENT_LIST);
                break;
            case FRAGMENT_WRITE:
                replaceFragment(FRAGMENT_LIST);
                break;
        }
    }
}
