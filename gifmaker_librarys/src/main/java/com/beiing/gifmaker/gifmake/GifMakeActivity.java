package com.beiing.gifmaker.gifmake;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.beiing.gifmaker.R;
import com.beiing.gifmaker.adapter.PictureAdapter;

import com.beiing.gifmaker.constant.Constant;
import com.beiing.gifmaker.image_selector.MultiImageSelector;
import com.beiing.gifmaker.utils.DialogUtil;
import com.beiing.gifmaker.utils.FileUtil;
import com.beiing.gifmaker.utils.FileUtil0;
import com.felipecsl.gifimageview.library.GifImageView;

import java.util.List;


import me.drakeet.materialdialog.MaterialDialog;

public class GifMakeActivity extends AppCompatActivity implements IGifMakeView{

   public static String rename_path;

    public static final String TAG = "GifMakeActivity";
    public static final int START_ALBUM_CODE = 0x21;

    private ImageAdapter adapter0;
    private GifMakePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifmake);
        initData();
     //   initEvent();

        TextView tv_add = findViewById(R.id.tv_add);
        TextView generate = findViewById(R.id.tv_generate);
        TextView clear =  findViewById(R.id.clear);
        TextView tv_preview =  findViewById(R.id. tv_preview);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final EditText Etext = new EditText(GifMakeActivity.this);
                Etext.setText("故事相册");//这里更改输入框里的内容
                Etext.requestFocus(); //输入框激活
                Etext.setSelection(Etext.getText().length()); //将输入点挪到最后
                new  AlertDialog.Builder(GifMakeActivity.this)

                        .setTitle("相册名称" )
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(Etext)
                        .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 点击确认传递文件夹名
                                 rename_path = Etext.getText().toString();

                                int size = presenter.getGifImages().size();
                                if (size > 1) {
                                    Toast.makeText(GifMakeActivity.this, "开始生成故事相册", Toast.LENGTH_SHORT).show();
                                    presenter.createGif(300, 500, 500);
                                    DialogUtil.showLoading(GifMakeActivity.this);
                                } else {
                                    Toast.makeText(GifMakeActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消" ,  null )
                        .show();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clear();
               // adapter.notifyDataSetChanged();
            }
        });

        tv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isHasPreview()) {
                    View contentView = LayoutInflater.from(GifMakeActivity.this).inflate(R.layout.layout_gif_preview, null);
                    GifImageView gifView = contentView.findViewById(R.id.gif_view);
                    byte[] fileBytes = FileUtil.getFileBytes(presenter.getPreViewFile());
                    if (fileBytes != null) {

                        gifView.setBytes(fileBytes);
                        gifView.startAnimation();
                    }

                    MaterialDialog mMaterialDialog = new MaterialDialog(GifMakeActivity.this)
                            .setView(contentView)
                            .setCanceledOnTouchOutside(true);
                    mMaterialDialog.show();
                } else {
                    Toast.makeText(GifMakeActivity.this, "没有预览图", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiImageSelector.create()
                        .showCamera(true) // show camera or not. true by default
                        .count(9) // max select image size, 9 by default. used width #.multi()
                        .multi() // multi mode, default mode;
                        .start(GifMakeActivity.this, Constant.REQUEST_CODE_SELECT_IMAGE);
            }
        });
    }

    protected void initData(){

        String[] titles = FileUtil0.getImageNames(Environment.getExternalStorageDirectory().getPath()+"/魔方相册/故事相册");
        String[] imagePaths = new String[titles.length];
        for (int i = 0; i < titles.length; i++) {
            imagePaths[i]=Environment.getExternalStorageDirectory().getPath()+"/魔方相册/故事相册/"+titles[i];
        }
        LayoutInflater inflater = LayoutInflater.from(this);
    //    View view = inflater.inflate(R.layout.activity_gifmake, null);
        GridView gridView = findViewById(R.id.grid_view);

        //   gridView = findViewById(R.id.grid_view);
        PictureAdapter adapter = new PictureAdapter(titles, imagePaths, this);

        gridView.setAdapter(adapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id) {

                return true;
            }
        });
        presenter = new GifMakePresenter(this);
        adapter0 = new ImageAdapter(this, presenter.getGifImages());
      //  adapter0 = new ImageAdapter(this, presenter.getGifImages());
        /*
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_gifmake, null);
       // View ImageView_list= inflater.inflate(R.layout.item_gif_image, null);
        RecyclerView gridView = view.findViewById(R.id.grid_view);
        gridView.setLayoutManager(new GridLayoutManager(this, 3));

        gridView.setAdapter(adapter);
*/

    }
/*
    protected void initEvent() {
        adapter.setOnItemClickListener(new OnItemClickListener<GifImageFrame>() {
            @Override
            public void onItemClick(@NonNull ViewGroup parent, @NonNull View view, GifImageFrame gifImage, int position) {
                if(gifImage.getType() == GifImageFrame.TYPE_ICON){
                    MultiImageSelector.create()
                            .showCamera(true) // show camera or not. true by default
                            .count(9) // max select image size, 9 by default. used width #.multi()
                            .multi() // multi mode, default mode;
                            .start(GifMakeActivity.this, Constant.REQUEST_CODE_SELECT_IMAGE);
                }
            }

            @Override
            public boolean onItemLongClick(@NonNull ViewGroup parent, @NonNull View view, GifImageFrame gifImage, int position) {
                if(adapter.getMode() == ImageAdapter.MODE_COMMON){
                    adapter.setMode(ImageAdapter.MODE_DELETE);
                } else if(adapter.getMode() == ImageAdapter.MODE_DELETE){
                    adapter.setMode(ImageAdapter.MODE_COMMON);
                }
                return false;
            }
        });

        adapter.setClickListener(new OnClickListener<GifImageFrame>() {
            @Override
            public void onClick(int position, int id, GifImageFrame gifImage) {
                if(id == R.id.iv_delete){
                    presenter.getGifImages().remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.REQUEST_CODE_SELECT_IMAGE){
            if(resultCode == RESULT_OK){
                List<String> paths = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                presenter.solveImages(paths);
            }
        }
    }

    @Override
    public void finishPaths() {
      //  adapter.notifyDataSetChanged();
    }

    @Override
    public void finishCreate(boolean b) {
        DialogUtil.dimiss();
        if(b){
            Toast.makeText(this, "制作成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "制作失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
