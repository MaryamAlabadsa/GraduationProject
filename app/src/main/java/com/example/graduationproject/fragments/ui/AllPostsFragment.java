package com.example.graduationproject.fragments.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graduationproject.R;
import com.example.graduationproject.databinding.FragmentAddPostBinding;
import com.example.graduationproject.databinding.FragmentAllPostsBinding;
import com.example.graduationproject.fragments.BaseFragment;

public class AllPostsFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ALL_POSTS";
    FragmentAllPostsBinding binding;
    Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllPostsFragment() {
        // Required empty public constructor
    }


    public static AllPostsFragment newInstance(String param1, String param2) {
        AllPostsFragment fragment = new AllPostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllPostsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getActivity();

        return view;
    }

    @Override
    public int getFragmentTitle() {
        return R.string.allPosts;
    }
//    private void setPostsRv() {
//        ArrayList<String> listImages = new ArrayList<>();
//        ArrayList<Post> list = new ArrayList<>();
//        listImages.add("https://mir-s3-cdn-cf.behance.net/project_modules/1400/ebcdfd44216545.56076cdf0ed14.jpg");
//        listImages.add("https://mir-s3-cdn-cf.behance.net/project_modules/1400/ebcdfd44216545.56076cdf0ed14.jpg");
//        listImages.add("https://mir-s3-cdn-cf.behance.net/project_modules/1400/ebcdfd44216545.56076cdf0ed14.jpg");
////        list.add(new Post("book", "Book in good condition"
////                , new User("maryam", "Khan Younis", "https://png.pngtree.com/png-vector/20190114/ourlarge/pngtree-vector-add-user-icon-png-image_313043.jpg"
////                , "maryam@hotmail.com", null, null)
////                , 4, 0, 0, 2
////                , listImages));
////        list.add(new Post("book", "Book in good condition"
////                , new User("maryam", "Khan Younis", "https://png.pngtree.com/png-vector/20190114/ourlarge/pngtree-vector-add-user-icon-png-image_313043.jpg"
////                , "maryam@hotmail.com", null, null)
////                , 8, 1, 1, 2
////                , listImages));
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
//                context, RecyclerView.VERTICAL, false);
//        binding.rvPost.setLayoutManager(layoutManager);
//        PostsAdapter adapter = new PostsAdapter(context, new PostRequestInterface() {
//            @Override
//            public void layout(Post post) {
//
//            }
//        });
//        adapter.setList(list);
//        binding.rvPost.setAdapter(adapter);
//    }
//
//    private void setCategoryRv() {
//        ArrayList<Category> list = new ArrayList<>();
//        list.add(new Category(0, "all", ""));
//        list.add(new Category(1, "books", ""));
//        list.add(new Category(1, "books", ""));
//        list.add(new Category(1, "books", ""));
//        list.add(new Category(1, "books", ""));
//        list.add(new Category(2, "cash", ""));
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
//                context, RecyclerView.HORIZONTAL, false);
//        binding.rvCategory.setLayoutManager(layoutManager);
//        CategoriesAdapter adapter = new CategoriesAdapter(context, new CategoryInterface() {
//            @Override
//            public void layout(int id) {
//                category_id = id;
//            }
//        });
////        adapter.setList(list.get(position).getImagesList());
//        binding.rvCategory.setAdapter(adapter);
//    }
}