package com.seher.shoppingregionlocator.navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.seher.shoppingregionlocator.Adapters.HomeAdapter;
import com.seher.shoppingregionlocator.GridItemView;
import com.seher.shoppingregionlocator.R;
import com.seher.shoppingregionlocator.Map.view.MainActivity;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {

    private GridView itemList;
    ArrayList<String> items = new ArrayList<String>();
    private ArrayList<String> selectedStrings;
    Button nextBtn;
    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.home_fragment, container, false);
        itemList = (GridView) mainView.findViewById(R.id.itemList);
        nextBtn = (Button) mainView.findViewById(R.id.nextBtn);
        //getResources().getColor(R.color.primaryColor)
        //Intialize
        intiliaze();
        final HomeAdapter adapter = new HomeAdapter(getActivity(), items);
        itemList.setAdapter(adapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    selectedStrings.remove((String) parent.getItemAtPosition(position));
                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    selectedStrings.add((String) parent.getItemAtPosition(position));
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putStringArrayListExtra("SELECTED_PLACES", selectedStrings);
                startActivity(intent);
            //    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapsActivity()).addToBackStack("MapsFragment").commit();
//
            }
        });

      return  mainView;
    }


    public void intiliaze(){
        selectedStrings = new ArrayList<>();
        LoadData();
    }
    public void LoadData(){
        items.add("Electronics Store");
        items.add("Cafe");
        items.add("Food");
        items.add("ATM");
        items.add("Apparels");
        items.add("Jewelry Store");
        items.add("Shopping Mall");
        items.add("Department Store");



    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //  mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}