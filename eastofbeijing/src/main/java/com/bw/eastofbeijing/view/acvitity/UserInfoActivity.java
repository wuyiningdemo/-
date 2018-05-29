package com.bw.eastofbeijing.view.acvitity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.eastofbeijing.R;
import com.bw.eastofbeijing.model.bean.UpLoadPicBean;
import com.bw.eastofbeijing.model.bean.UserInfoBean;
import com.bw.eastofbeijing.model.linear.IUpLoadLinter;
import com.bw.eastofbeijing.persenter.UpLoadPicPresenter;
import com.bw.eastofbeijing.persenter.UserInfoPresenter;
import com.bw.eastofbeijing.utils.CommonUtils;
import com.bw.eastofbeijing.utils.Constants;
import com.bw.eastofbeijing.utils.GlideImgManager;
import com.bw.eastofbeijing.view.iview.IHome;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener,IHome,IUpLoadLinter{
   @BindView(R.id.detail_image_back)
   ImageView detail_image_back;
    @BindView(R.id.setting_icon)
     ImageView setting_icon;
    @BindView(R.id.linear_upload)
     LinearLayout linear_upload;

    private PopupWindow popupWindow;
    //拍照保存的路径任意
    private String pic_path = Environment.getExternalStorageDirectory()+"/head.jpg";
    //裁剪完成之后图片保存的路径
    private String crop_icon_path = Environment.getExternalStorageDirectory()+"/head_icon.jpg";
    private TextView text_camera;
    private TextView text_local_pic;
    private TextView text_cancel;
    private View parent;
    private UpLoadPicPresenter upLoadPicPresenter;
    private UserInfoPresenter userInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        detail_image_back.setOnClickListener(this);
        linear_upload.setOnClickListener(this);

        //初始化popupWindown
        initPopupWindown();
        initData();

        //设置点击事件
        linear_upload.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detail_image_back:

                finish();
                break;
            case R.id.linear_upload://只做一个弹出的动作..popupWindown

                popupWindow.showAtLocation(parent, Gravity.BOTTOM,0,0);
                break;
            case R.id.text_camera://拍照

                paiZhao();

                popupWindow.dismiss();
                break;
            case R.id.text_local_pic://相册

                getLocalPic();

                popupWindow.dismiss();
                break;
            case R.id.text_cancel://取消

                popupWindow.dismiss();
                break;
        }
    }
    /**
     * 获取相册的图片
     */
    private void getLocalPic() {

        Intent intent = new Intent();
        //指定选择/获取的动作...PICK获取,拿
        intent.setAction(Intent.ACTION_PICK);
        //指定获取的数据的类型
        intent.setType("image/*");

        startActivityForResult(intent, 3000);

    }

    /**
     * 调用相机拍照的操作...隐式意图去调用相机
     */
    private void paiZhao() {

        Intent intent = new Intent();
        //指定动作...拍照的动作 CAPTURE...捕获
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        //给相机传递一个指令,,,告诉他拍照之后保存..MediaStore.EXTRA_OUTPUT向外输出的指令,,,指定存放的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pic_path)));

        //拍照的目的是拿到拍的图片
        startActivityForResult(intent, 1000);

    }
    /**
     * 初始化popupWindown
     */
    private void initPopupWindown() {

        //父窗体的视图
        parent = View.inflate(UserInfoActivity.this, R.layout.activity_user_info, null);

        View contentView = View.inflate(UserInfoActivity.this,R.layout.upload_pop_layout,null);

        //第一个参数展示的popupwindown的视图,第二个参数宽度,第三参数是高度..
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //设置
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);//外边可以触摸
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //找到控件
        text_camera = contentView.findViewById(R.id.text_camera);
        text_local_pic = contentView.findViewById(R.id.text_local_pic);
        text_cancel = contentView.findViewById(R.id.text_cancel);

        //设置点击事件
        text_camera.setOnClickListener(this);
        text_local_pic.setOnClickListener(this);
        text_cancel.setOnClickListener(this);

    }
    private void initData() {

        //加载圆形
        GlideImgManager.glideLoader(UserInfoActivity.this, CommonUtils.getString("iconUrl"), R.drawable.user, R.drawable.user, setting_icon, 0);

        //mvp
        upLoadPicPresenter = new UpLoadPicPresenter(this);
        userInfoPresenter = new UserInfoPresenter(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {

            //拍照保存之后进行裁剪....根据图片的uri路径
            crop(Uri.fromFile(new File(pic_path)));
        }

        //获取相册图片
        if (requestCode == 3000 && resultCode == RESULT_OK) {
            //获取的是相册里面某一张图片的uri地址
            Uri uri = data.getData();

            //根据这个uri地址进行裁剪
            crop(uri);
        }

        if (requestCode == 2000 && resultCode == RESULT_OK) {
            //获取到裁剪完的图片
            Bitmap bitmap = data.getParcelableExtra("data");

            //拿到了bitmap图片 ..需要把bitmap图片压缩保存到文件中去..应该去做上传到服务器的操作了
            File saveIconFile = new File(crop_icon_path);

            if(saveIconFile.exists()){
                saveIconFile.delete();
            }

            try {
                //创建出新的文件
                saveIconFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(saveIconFile);
                //把bitmap通过流的形式压缩到文件中
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

                //保存到sd卡中之后再去把文件上传到服务器
                upLoadPicPresenter.uploadPic(Constants.uploadUrl,saveIconFile,CommonUtils.getString("uid"),"touxiang.jpg");

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 根据图片的uri路径进行裁剪
     * @param uri
     */
    private void crop(Uri uri) {

        Intent intent = new Intent();

        //指定裁剪的动作
        intent.setAction("com.android.camera.action.CROP");

        //设置裁剪的数据(uri路径)....裁剪的类型(image/*)
        intent.setDataAndType(uri, "image/*");

        //执行裁剪的指令
        intent.putExtra("crop", "true");
        //指定裁剪框的宽高比
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //指定输出的时候宽度和高度
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        //设置取消人脸识别
        intent.putExtra("noFaceDetection", false);
        //设置返回数据
        intent.putExtra("return-data", true);

        //
        startActivityForResult(intent, 2000);

    }

    @Override
    public void onSuccess(ResponseBody responseBody) {


        try {
            String json = responseBody.string();

        final UserInfoBean userInfoBean = new Gson().fromJson(json, UserInfoBean.class);


            if ("0".equals(userInfoBean.getCode())) {

                //获取用户信息成功....拿到icon在服务器上的路径,然后加载显示头像
                GlideImgManager.glideLoader(UserInfoActivity.this, userInfoBean.getData().getIcon(), R.drawable.user, R.drawable.user, setting_icon, 0);

                //并且需要保存icon的新路径
                CommonUtils.saveString("iconUrl", userInfoBean.getData().getIcon());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }





    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void getLoginSuccessByQQ(ResponseBody responseBody, String ni_cheng, String iconurl) {

    }

    @Override
    public void onFenLeiSuccess(ResponseBody responseBody) {

    }

    @Override
    public void getCartDataNull() {

    }

    /**
     * 上传图片
     * @param responseBody
     */
    @Override
    public void uploadPicSuccess(ResponseBody responseBody) {
        try {
            String json=responseBody.string();
            UpLoadPicBean upLoadPicBean=new Gson().fromJson(json,UpLoadPicBean.class);
            if ("0".equals(upLoadPicBean.getCode())) {
                Toast.makeText(UserInfoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();

                //上传成功之后需要获取用户信息,,,
                userInfoPresenter.getUserInfo(Constants.userInfoUrl, CommonUtils.getString("uid"));


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubcribe() {
        userInfoPresenter.unsubcribe();
    }
}
