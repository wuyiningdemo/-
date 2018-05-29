package com.bw.eastofbeijing.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.CartBean;
import com.bw.eastofbeijing.model.bean.CountPriceBean;
import com.bw.eastofbeijing.persenter.FragmentCartPresenter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.RetrofitUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Dash on 2018/1/31.
 */
public class MyExpanableAdapter extends BaseExpandableListAdapter{
    private FragmentCartPresenter fragmentCartPresenter;
    private RelativeLayout relative_progress;
    private Handler handler;
    private CartBean cartBean;
    private Context context;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int childIndex;
    private int allIndex;

    public MyExpanableAdapter(Context context, CartBean cartBean, Handler handler, RelativeLayout relative_progress, FragmentCartPresenter fragmentCartPresenter) {
        this.context = context;
        this.cartBean = cartBean;
        this.handler = handler;
        this.relative_progress = relative_progress;
        this.fragmentCartPresenter = fragmentCartPresenter;
    }

    @Override
    public int getGroupCount() {
        return cartBean.getData().size();
    }

    //一组里面有多少个子条目...cartBean.getData().get(groupPosition).getList(),,某一个组里面子条目的数据
    @Override
    public int getChildrenCount(int groupPosition) {
        return cartBean.getData().get(groupPosition).getList().size();
    }

    //cartBean.getData().get(groupPosition)某个组的数据
    @Override
    public Object getGroup(int groupPosition) {
        return cartBean.getData().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cartBean.getData().get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        final GroupHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.cart_group_item_layout,null);
            holder = new GroupHolder();

            holder.group_check_box = view.findViewById(R.id.group_check_box);
            holder.group_shop_name = view.findViewById(R.id.group_shop_name);

            view.setTag(holder);
        }else {
            holder = (GroupHolder) view.getTag();
        }

        //赋值..checkBox设置是否选中...商家设置名字
        final CartBean.DataBean dataBean = cartBean.getData().get(groupPosition);

        holder.group_shop_name.setText(dataBean.getSellerName());
        holder.group_check_box.setChecked(dataBean.isGroupChecked());

        //设置点击事件
        holder.group_check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //显示progress
                relative_progress.setVisibility(View.VISIBLE);

                childIndex = 0;
                //根据商家是否选中的状态,,,去更新所有子孩子的状态
                updateChildInGroup(holder.group_check_box.isChecked(),dataBean);

            }
        });

        return view;
    }

    /**
     * 根据组的状态...根据所有里面的子孩子
     * @param checked 表示组是否选中
     * @param dataBean
     */
    private void updateChildInGroup(final boolean checked, final CartBean.DataBean dataBean) {

        CartBean.DataBean.ListBean listBean = dataBean.getList().get(childIndex);

        //更新购物车
        //更新购物车....跟新当前商品是否能选中的状态...selected值
        Map<String, String> params = new HashMap<>();
        //?uid=71&sellerid=1&pid=1&selected=0&num=10
        params.put("uid", CommonUtils.getString("uid"));
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));

        params.put("selected", String.valueOf(checked ? 1:0));

        params.put("num", String.valueOf(listBean.getNum()));


        RetrofitUtil.getService().doPost(Constants.UPDATE_CART_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        childIndex ++;

                        if (childIndex < dataBean.getList().size()) {
                            //可以继续更新
                            updateChildInGroup(checked,dataBean);


                        }else {
                            //已经全部更新完成....重新查询购物车数据,显示
                            fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));

                        }

                    }


                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

        ChildHolder holder;
        if (view == null) {
            view = View.inflate(context,R.layout.cart_child_item_layout,null);
            holder = new ChildHolder();

            holder.child_check_box = view.findViewById(R.id.child_check_box);
            holder.child_image = view.findViewById(R.id.child_image);
            holder.child_title = view.findViewById(R.id.child_title);
            holder.child_text_add = view.findViewById(R.id.child_text_add);
            holder.child_text_num = view.findViewById(R.id.child_text_num);
            holder.child_text_jian = view.findViewById(R.id.child_text_jian);
            holder.child_price = view.findViewById(R.id.child_price);
            holder.child_text_delete = view.findViewById(R.id.child_text_delete);

            view.setTag(holder);
        }else {
            holder = (ChildHolder) view.getTag();
        }

        //赋值
        final CartBean.DataBean.ListBean listBean = cartBean.getData().get(groupPosition).getList().get(childPosition);
        ////设置子条目是否选中...selected : 1,,0表示不选中,1表示选中
        holder.child_check_box.setChecked(listBean.getSelected() == 1 ? true :false);

        Glide.with(context).load(listBean.getImages().split("\\|")[0]).into(holder.child_image);
        holder.child_title.setText(listBean.getTitle());

        //一个double类型的值保留两位小数
        holder.child_price.setText("¥"+decimalFormat.format(listBean.getBargainPrice()));
        //注意:----text设置文本的时候一定要是string类型
        holder.child_text_num.setText(String.valueOf(listBean.getNum()));

        //checkBox点击事件
        holder.child_check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示progressBar
                relative_progress.setVisibility(View.VISIBLE);
                //更新购物车....跟新当前商品是否能选中的状态...selected值
                Map<String, String> params = new HashMap<>();
                //?uid=71&sellerid=1&pid=1&selected=0&num=10
                params.put("uid", CommonUtils.getString("uid"));
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));

                //此时需要selected是否选中...传什么值过去?如果当前是1 传0,,如果是0,,传1
                params.put("selected", String.valueOf(listBean.getSelected()==0 ? 1:0));
                params.put("num", String.valueOf(listBean.getNum()));

                RetrofitUtil.getService().doPost(Constants.UPDATE_CART_URL, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {

                    //更新成功之后....重新查询一下购物车的数据,然后进行展示
                                fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

        //加
        holder.child_text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示progressBar
                relative_progress.setVisibility(View.VISIBLE);
                //更新购物车....跟新当前商品是否能选中的状态...selected值
                Map<String, String> params = new HashMap<>();
                //?uid=71&sellerid=1&pid=1&selected=0&num=10
                params.put("uid", CommonUtils.getString("uid"));
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()));

                //num+1
                params.put("num", String.valueOf(listBean.getNum() +1));

                RetrofitUtil.getService().doPost(Constants.UPDATE_CART_URL, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {

                                //更新成功之后....重新查询一下购物车的数据,然后进行展示
                                fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        //减
        holder.child_text_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int num = listBean.getNum();
                if (num == 1) {
                    return;
                }

                //显示progressBar
                relative_progress.setVisibility(View.VISIBLE);

                //更新购物车....跟新当前商品是否能选中的状态...selected值
                Map<String, String> params = new HashMap<>();
                //?uid=71&sellerid=1&pid=1&selected=0&num=10
                params.put("uid", CommonUtils.getString("uid"));
                params.put("sellerid", String.valueOf(listBean.getSellerid()));
                params.put("pid", String.valueOf(listBean.getPid()));
                params.put("selected", String.valueOf(listBean.getSelected()));

                params.put("num", String.valueOf(num -1));

                RetrofitUtil.getService().doPost(Constants.UPDATE_CART_URL, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {

                                //更新成功之后....重新查询一下购物车的数据,然后进行展示
                                fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        //删除
        holder.child_text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示progress
                relative_progress.setVisibility(View.VISIBLE);
                //请求删除购物车的接口...删除成功之后 再次请求查询购物车
                Map<String, String> params = new HashMap<>();
                //?uid=72&pid=1
                params.put("uid",CommonUtils.getString("uid"));
                params.put("pid", String.valueOf(listBean.getPid()));

                RetrofitUtil.getService().doPost(Constants.DELETE_CART_URL, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {

                                //更新成功之后....重新查询一下购物车的数据,然后进行展示
                                fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        return view;
    }

    //如果返回false 子条目不可以点击
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 计算价格和数量....发送给fragment进行显示
     */
    public void sendPriceAndCount() {

        double price = 0;
        int count = 0;

        for (int  i= 0;i<cartBean.getData().size();i++) {
            //遍历每一组里面的子孩子
            List<CartBean.DataBean.ListBean> list = cartBean.getData().get(i).getList();
            for (int j = 0; j< list.size(); j++) {
                //判断每一个子孩子是否选中,,,如果选中计算价格和数量
                if (list.get(j).getSelected() == 1) {
                    price = price + list.get(j).getBargainPrice() * list.get(j).getNum();

                    count += list.get(j).getNum();
                }
            }
        }

        String priceString = decimalFormat.format(price);

        CountPriceBean countPriceBean = new CountPriceBean(priceString, count);
        //去显示价格和数量
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = countPriceBean;

        handler.sendMessage(msg);

    }

    /**
     * 点击全选的时候,,,,根据全选的状态 改变购物车所有商品的选中状态
     * @param checked 全选的状态
     */
    public void setAllChildChecked(boolean checked) {

        //改变购物车所有商品的选中状态...要有这些所有的商品...创建一个结合装所有的商品
        List<CartBean.DataBean.ListBean> allList = new ArrayList<>();
        for (int i = 0;i<cartBean.getData().size();i++) {
            List<CartBean.DataBean.ListBean> list = cartBean.getData().get(i).getList();
            for (int j = 0; j< list.size(); j++) {

                allList.add(list.get(j));
            }
        }

        //progress
        relative_progress.setVisibility(View.VISIBLE);

        allIndex = 0;//从第一条开始  索引是0
        //操作这个allList集合
        updateAllChild(allList,checked);


    }

    /**
     * 跟新所有的子条目
     * @param allList
     * @param checked
     */
    private void updateAllChild(final List<CartBean.DataBean.ListBean> allList, final boolean checked) {
        //第一条开始
        CartBean.DataBean.ListBean listBean = allList.get(allIndex);

        //更新购物车....跟新当前商品是否能选中的状态...selected值
        Map<String, String> params = new HashMap<>();
        //?uid=71&sellerid=1&pid=1&selected=0&num=10
        params.put("uid", CommonUtils.getString("uid"));
        params.put("sellerid", String.valueOf(listBean.getSellerid()));
        params.put("pid", String.valueOf(listBean.getPid()));

        params.put("selected", String.valueOf(checked ? 1:0));

        params.put("num", String.valueOf(listBean.getNum()));

        RetrofitUtil.getService().doPost(Constants.UPDATE_CART_URL, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                       //成功之后索引+1
                        allIndex ++;
                        if (allIndex <allList.size()) {
                            //继续更新下一条
                            updateAllChild(allList,checked);
                        }else {
                            //代表全部更新完毕....请求查询购物车的数据
                            fragmentCartPresenter.getCartData(Constants.selectCartUrl,CommonUtils.getString("uid"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private class GroupHolder{
        CheckBox group_check_box;
        TextView group_shop_name;
    }

    private class ChildHolder{
        CheckBox child_check_box;
        ImageView child_image;
        TextView child_title;
        TextView child_price;
        TextView child_text_jian;
        TextView child_text_num;
        TextView child_text_add;
        TextView child_text_delete;
    }
}
