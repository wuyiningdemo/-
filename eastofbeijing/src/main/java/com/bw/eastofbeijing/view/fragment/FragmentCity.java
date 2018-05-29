package com.bw.eastofbeijing.view.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.ProvinceBean;
import com.bw.eastofbeijing.model.db.AddrDao;
import com.bw.eastofbeijing.view.adapter.ProvinceAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dash on 2018/2/27.
 */
public class FragmentCity extends Fragment {

    private ListView list_view_addr;
    private ProvinceBean provinceBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addr_choose_layout,container,false);
        list_view_addr = view.findViewById(R.id.list_view_addr);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        provinceBean = (ProvinceBean) getArguments().getSerializable("provinceBean");

        //根据省的数据 查询市的数据
        selectData();

    }

    private void selectData() {
        final List<ProvinceBean> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = new AddrDao(getActivity()).initAddrDao();

        //select * from table where parentid = regionid
        Cursor cursor = sqLiteDatabase.query("bma_regions", null, "parentid = ?", new String[]{String.valueOf(provinceBean.getRegionid())}, null, null, null);
        while (cursor.moveToNext()) {
            int regionid = cursor.getInt(cursor.getColumnIndex("regionid"));//获取数据的方法里面的参数是列的角标...根据列名去获取列的角标
            String name = cursor.getString(cursor.getColumnIndex("name"));

            //封装province对象
            ProvinceBean cityBean = new ProvinceBean(regionid, name);

            list.add(cityBean);
        }

        cursor.close();
        sqLiteDatabase.close();

        //设置适配器
        ProvinceAdapter provinceAdapter = new ProvinceAdapter(getActivity(), list);
        list_view_addr.setAdapter(provinceAdapter);

        //条目点击事件
        list_view_addr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //需要穿省provinceBean  和点击的市的数据list.get(i)
                //FragmentXian.getInstance(provinceBean,list.get(i))

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,FragmentXian.getInstance(provinceBean,list.get(i))).addToBackStack(null).commit();
            }
        });

    }

    public static FragmentCity getInstance(ProvinceBean provinceBean) {
        FragmentCity fragmentCity = new FragmentCity();

        Bundle bundle = new Bundle();
        bundle.putSerializable("provinceBean",provinceBean);

        //
        fragmentCity.setArguments(bundle);

        return fragmentCity;
    }
}
