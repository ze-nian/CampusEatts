package com.example.campuseats.ui.s2;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuseats.R;
import com.example.campuseats.Room.MainAdapter;
import com.example.campuseats.Room.MainData;
import com.example.campuseats.Room.RoomDB;

import java.util.Arrays;
import java.util.List;

public class S2Fragment extends Fragment {

    private S2ViewModel mViewModel;

    List<MainData> dataList;
    RoomDB database;
    MainAdapter adapter;
    List product_name_list;


    View RootView;

    public static S2Fragment newInstance() {
        return new S2Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View S2View = inflater.inflate(R.layout.fragment_s2, container, false);
        RootView = S2View;

        database=RoomDB.getInstance(getActivity());
        dataList=database.mainDao().getAll();
        adapter = new MainAdapter(getActivity(),dataList);

        final List<String> namesList = Arrays.asList( "原味雞排", "辣味雞排", "椒鹽雞排","海苔雞排","梅粉雞排");
        final List<Integer> priceList = Arrays.asList(80,80,80,80,80);
        //final List<String> pastList=new ArrayList<>();

        //數量紀錄(不會跳出變0)
        for (int j=2;j<7;j++){
            final int quantity_id=getResources().getIdentifier("s2_"+j, "id", getActivity().getPackageName());
            TextView tt = S2View.findViewById(quantity_id);
            try {
                int q=database.mainDao().getquantity(namesList.get(j-2));
                System.out.println(q);
                tt.setText(String.format("%d",q));
            }catch (Exception e){
                System.out.println(quantity_id+"error");
                continue;
            }
        }

        //add
        for (int i = 2; i < 7; i++) {
            final int id = getResources().getIdentifier("s2_"+i+"_add", "id", getActivity().getPackageName());
            Button btnAdd = S2View.findViewById(id);

            final int finalI = i;
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int tid = getResources().getIdentifier("s2_"+finalI, "id", getActivity().getPackageName());
                    TextView TV = RootView.findViewById(tid);
                    int TV_NUM=Integer.parseInt(TV.getText().toString());
                    TV_NUM+=1;
                    TV.setText(String.format("%d",TV_NUM));


                    String sName = namesList.get(finalI -2);
                    int sPrice=priceList.get(finalI-2);
                    int sQuantity=TV_NUM;
                    String sMode="外送";
                    String sAddress="abc";

                    MainData data =new MainData();
                    data.setName(sName);
                    data.setPrice(sPrice);
                    data.setQuantity(sQuantity);
                    data.setMode(sMode);
                    data.setAddress(sAddress);

                    //更新數量or增加item
                    int item_count=adapter.getItemCount();
                    if (item_count!=0){
                        for (int i = 0; i <= item_count; i++){
                            MainData vv=dataList.get(i);
                            if (vv.getName()==sName){
                                //Toast.makeText(RootView.getContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
                                database.mainDao().update(sName,sQuantity);
                                dataList.clear();
                                adapter.notifyDataSetChanged();
                                break;
                            }else{
                                database.mainDao().update(sName,sQuantity);
                                dataList.clear();
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }else {
                        database.mainDao().insert(data);
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                    }

                }
            });
        }

        //minus
        for (int i = 2; i < 7; i++) {
            final int id = getResources().getIdentifier("s2_"+i+"_minus", "id", getActivity().getPackageName());
            Button btnAdd = S2View.findViewById(id);

            final int finalI = i;
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int tid = getResources().getIdentifier("s2_"+finalI, "id", getActivity().getPackageName());
                    TextView TV = RootView.findViewById(tid);
                    int TV_NUM=Integer.parseInt(TV.getText().toString());
                    if (TV_NUM>0)
                    {
                        TV_NUM-=1;

                        if(TV_NUM==0){
                            String ssName=namesList.get(finalI-2);
                            database.mainDao().delete(ssName);
                            dataList.clear();
                            adapter.notifyDataSetChanged();
                        }

                        TV.setText(String.format("%d",TV_NUM));
                        String sName = namesList.get(finalI -2);
                        int sQuantity=TV_NUM;
                        database.mainDao().update(sName,sQuantity);
                        dataList.clear();
                        adapter.notifyDataSetChanged();

                    }
                    else
                    {
                        Toast.makeText(RootView.getContext(), "目前數量為0，無法再減少", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        return S2View;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(S2ViewModel.class);
        // TODO: Use the ViewModel
    }

}