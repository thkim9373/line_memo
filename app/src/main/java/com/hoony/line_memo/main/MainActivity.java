package com.hoony.line_memo.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.hoony.line_memo.R;
import com.hoony.line_memo.databinding.ActivityMainBinding;
import com.hoony.line_memo.main.fragments.editor.MemoEditorFragment;
import com.hoony.line_memo.main.fragments.list.MemoListFragment;
import com.hoony.line_memo.main.fragments.reader.MemoReaderFragment;

import java.util.ArrayList;
import java.util.List;

import static com.hoony.line_memo.main.MainViewModel.FRAGMENT_EDITOR;
import static com.hoony.line_memo.main.MainViewModel.FRAGMENT_LIST;
import static com.hoony.line_memo.main.MainViewModel.FRAGMENT_READER;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    List<Fragment> fragmentList = new ArrayList<>();
    private long backKeyPressedTime = 0;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        viewModel = new ViewModelProvider(MainActivity.this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);

        fragmentList.add(new MemoListFragment());
        fragmentList.add(new MemoReaderFragment());
        fragmentList.add(new MemoEditorFragment());

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

    @Override
    public void onBackPressed() {
        int fragmentIndex = viewModel.getFragmentIndex().getValue() != null ?
                viewModel.getFragmentIndex().getValue() : -1;
        switch (fragmentIndex) {
            case FRAGMENT_LIST:
//                if (viewModel.getListFragmentMode() == MainViewModel.LIST_MODE_SELECT) {
//                    MemoListFragment memoListFragment = (MemoListFragment) this.fragmentList.get(FRAGMENT_LIST);
//                    memoListFragment.setModeDefault();
//                } else {
//                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
//                        backKeyPressedTime = System.currentTimeMillis();
//                        showToast("한 번 더 누르면 종료됩니다.");
//                    } else {
//                        finish();
//                    }
//                }
                break;
            case FRAGMENT_READER:
                viewModel.setFragmentIndex(FRAGMENT_LIST);
                break;
            case FRAGMENT_EDITOR:
//                TODO
                if (viewModel.isEdit()) {

                } else {
                }
                viewModel.setFragmentIndex(FRAGMENT_LIST);
                break;
        }
    }

    private void showToast(String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
