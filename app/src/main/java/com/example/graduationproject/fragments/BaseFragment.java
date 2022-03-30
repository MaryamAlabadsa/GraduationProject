package com.example.graduationproject.fragments;


import androidx.fragment.app.Fragment;

import com.example.graduationproject.activities.MainActivity;

public abstract class BaseFragment extends Fragment {


    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }

    public abstract int getFragmentTitle();

    public void setTitle() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setTitle(getFragmentTitle());
        }
    }

}

