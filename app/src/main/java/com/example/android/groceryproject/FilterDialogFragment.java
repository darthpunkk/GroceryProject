package com.example.android.groceryproject;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener{


    public interface ApplyClickListener {
        void onApplyClick(ArrayList<String> filterList);
    }
    EditText mEditText;
    ImageView backImageView;
    TextView textView;
    ListView categoryList, subCategoryList;
    LinearLayout mContainer;
    RangeSeekBar<Number> rangeSeekBar;
    boolean content;
    ArrayList<String> subValues;
    ArrayList<Filter> groupList;
    ArrayList<String> values;
    Button ApplyButton, clearButton;
    SearchView searchView;
ArrayList<String> applyList;
    FilterSubCategoryAdapter[] subcategoryAdapter;


    public FilterDialogFragment() {
        // Required empty public constructor
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        //frag.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_dialog, container, false);
    }

    // @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryList = (ListView) view.findViewById(R.id.filter_listview1);
        subCategoryList = (ListView) view.findViewById(R.id.filter_listview2);
        searchView = (SearchView) view.findViewById(R.id.search_view_dialog);

        Bundle bundle = getArguments();
        groupList = (ArrayList<Filter>) bundle.getSerializable("filter");
        values = new ArrayList<>();

        //Log.i("size", groupList.size() + "");


        for (Filter filter : groupList) {
            values.add(filter.getFilterCategory());
        }
        final FilterCategoryAdapter categoryAdapter = new FilterCategoryAdapter(values, getActivity(), 0);

        categoryList.setAdapter(categoryAdapter);

        subcategoryAdapter = new FilterSubCategoryAdapter[groupList.size()];
        for (int i = 0; i < groupList.size(); i++) {
            subcategoryAdapter[i] = new FilterSubCategoryAdapter(groupList.get(i).getFilterSubCategory(), getActivity());
        }
        subCategoryList.setAdapter(subcategoryAdapter[0]);
        Log.i("Initial", subCategoryList.getCount() + " " + subCategoryList.getLastVisiblePosition() + " " + subCategoryList.getFirstVisiblePosition());
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (subcategoryAdapter[0].getCount() != subCategoryList.getLastVisiblePosition() + 1) {
                    searchView.setVisibility(View.VISIBLE);

                } else {
                    searchView.setVisibility(View.GONE);
                }
            }
        });
        searchView.setQueryHint("Search " + groupList.get(0).getFilterCategory() + "...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subcategoryAdapter[0].getFilter().filter(newText);
                return false;
            }
        });


        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                subcategoryAdapter[position].resetData();
                subCategoryList.setAdapter(subcategoryAdapter[position]);
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (subcategoryAdapter[position].getCount() != subCategoryList.getLastVisiblePosition() + 1) {
                            searchView.setVisibility(View.VISIBLE);
                            searchView.clearFocus();
                            searchView.setQuery("", false);
                            searchView.setQueryHint("Search " + groupList.get(position).getFilterCategory() + "...");


                        } else {
                            searchView.setVisibility(View.GONE);
                        }
                    }
                });

                categoryAdapter.setSelectPosition(position);
                categoryAdapter.notifyDataSetChanged();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        subcategoryAdapter[position].getFilter().filter(newText);
                        return false;
                    }
                });


            }
        });
        applyList = new ArrayList<>();
        ApplyButton = (Button) view.findViewById(R.id.apply_button);
        ApplyButton.setOnClickListener(this);

        clearButton = (Button) view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        backImageView = (ImageView) view.findViewById(R.id.back_button);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {

                onBackPress();
            }
        };
    }
    public void onBackPress(){
        //onApply();
        //Toast.makeText(getActivity(),"hi",Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.apply_button){
            onApply();
            getDialog().dismiss();
        }
        else
        {
            for (int i = 0; i < groupList.size(); i++) {
                int size = groupList.get(i).getFilterSubCategory().size();
                for (int j = 0; j < size; j++) {
                    groupList.get(i).getFilterSubCategory().get(j).setChecked(false);

                }
                subcategoryAdapter[i].notifyDataSetChanged();

            }
        }
    }
    private  void onApply(){
        applyList.clear();
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<subFilter> tempList = groupList.get(i).getFilterSubCategory();
            for (int j = 0; j < tempList.size(); j++) {
                if (tempList.get(j).isChecked()) {
                    applyList.add(tempList.get(j).getSubFilterName());
                }

            }

        }
        ApplyClickListener listener =(ApplyClickListener)getFragmentManager().findFragmentByTag("productList");
        listener.onApplyClick(applyList);
    }
}