package www.coders.org.qr_fintech_client;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageShopDetail extends AppCompatActivity {
    Intent intent;
    Button delete_button;
    Button modify_button;
    int mode;
    String num, delete_button_str;
    EditText name_editText;
    EditText balance_editText;
    EditText place_editText;
    EditText about_editText;
    Button product_button;
    ImageView shop_image;
    String imagePath;
    private static final String TAG_SUCCESS = "result";
    private static final String TAG_MESSAGE = "msg";
    private String PATH_READ, PATH_APPLY, PATH_UPDATE, PATH_DELETE;
    public static final String my_shared_preferences = "login_information";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shop_detail);
        name_editText = findViewById(R.id.name_editText);
        balance_editText = findViewById(R.id.balance_editText);
        place_editText = findViewById(R.id.place_exitText);
        about_editText = findViewById(R.id.about_editText);

        delete_button = (Button) findViewById(R.id.delete_button);
        delete_button.setOnClickListener(mDeleteClickListener);

        modify_button = (Button) findViewById(R.id.modify_button);
        modify_button.setOnClickListener(mModifyClickListener);

        product_button = (Button) findViewById(R.id.product_button);
        product_button.setOnClickListener(mProductClickListener);

        shop_image = (ImageView) findViewById(R.id.shop_image);
        shop_image.setOnClickListener(mShopImageListener);

        Context context = this;
        PATH_READ = context.getString(R.string.server_ip) + "/shop_detail";
        PATH_APPLY = context.getString(R.string.server_ip) + "/shop_insert";
        PATH_UPDATE = context.getString(R.string.server_ip) + "/shop_update";
        PATH_DELETE = context.getString(R.string.server_ip) + "/shop_delete";

        intent = getIntent();
        mode = intent.getIntExtra("mode", 0);

        switch (mode) {
            case CONST.MODE_UPDATE:
                num = intent.getStringExtra("item");
                delete_button_str = "삭제";
                readStoreInfo();
                break;

            case CONST.MODE_CREATE:
                delete_button_str = "취소";
                delete_button.setText(delete_button_str);
                break;
        }
    }

    View.OnClickListener mProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*
            Intent intent = new Intent(getApplicationContext(), ManageProductFragment.class);
            intent.putExtra("num", num);
            startActivity(intent);
            */
            // 액티비티 새로만들거나 없애거나 해야할듯
            Toast.makeText(getApplicationContext(), "액티비티 새로만들거나 없애거나 해야할듯 시간이많다면 완성예정...", Toast.LENGTH_LONG).show();
            /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_namagement_product_c_layout, new ManageProductFragment());
            fragmentTransaction.commit();
            */
        }
    };

    View.OnClickListener mDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(ManageShopDetail.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage(delete_button_str + "하시겠습니까?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mode == CONST.MODE_UPDATE) {
                                deleteStoreInfo();
                                finishWithResult();
                            } else finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener mModifyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createOrUpdate();
            finishWithResult();
        }
    };

    public void onBackPressed() {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("변경된 내용을 저장하시겠습니까?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createOrUpdate();
                        finishWithResult();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void deleteStoreInfo() {
        Log.e("hihi", "delete");
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_DELETE).get();

            // Log.e("hihi3333",result);

            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));

            switch (r) {
                case 1:
                    Toast.makeText(getApplicationContext(), "삭제 완료!", Toast.LENGTH_LONG).show();
                    finishWithResult();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case -2:
                    Toast.makeText(getApplicationContext(), "삭제 불가능 : 잔액이 남아있습니다.", Toast.LENGTH_LONG).show();
                    break;
                case -3:
                    Toast.makeText(getApplicationContext(), "삭제 불가능 : 잘못된 상점번호입니다.", Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private void readStoreInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

            String userid = sp.getString("id", null);
            String userpw = sp.getString("pw", null);
            jsonObject.accumulate("id", userid);// 아이디 비번 받아와야함
            jsonObject.accumulate("pw", userpw);
            jsonObject.accumulate("num", num);
            HttpAsyncTask httpTask = new HttpAsyncTask(jsonObject);
            String result = httpTask.execute(PATH_READ).get();
            // Log.e("hihi3333",result);
            JSONObject store = new JSONObject(result);
            int r = Integer.parseInt(store.getString("result"));
            switch (r) {
                case 1:
                    name_editText.setText(store.getString("name"));
                    about_editText.setText(store.getString("about"));
                    balance_editText.setText(store.getString("balance"));
                    String img_name = store.getString("img");
                    String img_url = CONST.IMG_URL + img_name;
                    Picasso.get().load(img_url).into(shop_image);
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "로그인 되어있지 않습니다.", Toast.LENGTH_LONG).show();
                    finish();
                case -2:
                    Toast.makeText(getApplicationContext(), "조회 불가능 : 잘못된 상점번호입니다.", Toast.LENGTH_LONG).show();
                    finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    void finishWithResult() {
        Intent intent = new Intent();
        intent.putExtra("num", num);
        setResult(CONST.RESULT_UPDATED, intent);
        finish();
    }

    void createOrUpdate() {
        switch (mode) {
            case CONST.MODE_CREATE:
                createStoreInfo();
                break;
            case CONST.MODE_UPDATE:
                updateStoreInfo();
                break;
        }
    }

    ////

    View.OnClickListener mShopImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new android.app.AlertDialog.Builder(ManageShopDetail.this).setTitle("업로드할 이미지 선택")
                    .setPositiveButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }
    };

    void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, CONST.PICK_FROM_ALBUM);
    }

    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            doTakeAlbumAction();
        }
    };

    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CONST.PICK_FROM_ALBUM: {
                applyPicture(data.getData()); //갤러리에서 가져오기
                break;
            }
        }
    }

    private void applyPicture(Uri imgUri) {
        // path 경로
        imagePath = getRealPathFromURI(imgUri);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        shop_image.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    private void createStoreInfo() {

        SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        String id = sp.getString("id", null);
        String password = sp.getString("pw", null);

        ///
        HashMap<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put("id", RequestBody.create(MediaType.parse("text/plain"), id));
        params.put("pw", RequestBody.create(MediaType.parse("text/plain"), password));
        params.put("name", RequestBody.create(MediaType.parse("text/plain"), name_editText.getText().toString()));
        params.put("about", RequestBody.create(MediaType.parse("text/plain"), about_editText.getText().toString()));

        File file = null;
        if (imagePath != null) {
            file = new File(imagePath);
        } else {
            Resources res = getApplicationContext().getResources();
            int r_id = R.drawable.user_icon_main;
            Drawable drawable = res.getDrawable(r_id);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            String fileName = "baseImage.jpg";
            try {
                FileOutputStream out = openFileOutput(fileName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                file = getFileStreamPath(fileName);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("img", file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));

        NetRetrofit.getEndPoint().do_shop_insert(params, filePart).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jObj = new JSONObject(response.body().toString());
                        int success = jObj.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            Log.e("Successfully insert!", jObj.toString());

                            Toast.makeText(getApplicationContext(), "상점 등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                            finishWithResult();
                        } else {
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("model", "Failed");
            }
        });
    }

    private void updateStoreInfo() {

        SharedPreferences sp = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        String id = sp.getString("id", null);
        String password = sp.getString("pw", null);

        ///
        HashMap<String, RequestBody> params = new HashMap<String, RequestBody>();
        params.put("id", RequestBody.create(MediaType.parse("text/plain"), id));
        params.put("pw", RequestBody.create(MediaType.parse("text/plain"), password));
        params.put("num", RequestBody.create(MediaType.parse("text/plain"), num));
        params.put("name", RequestBody.create(MediaType.parse("text/plain"), name_editText.getText().toString()));
        params.put("about", RequestBody.create(MediaType.parse("text/plain"), about_editText.getText().toString()));

        MultipartBody.Part filePart;
        if (imagePath != null) {
            File file = new File(imagePath);
            filePart = MultipartBody.Part.createFormData("img", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            filePart = null;
        }


        NetRetrofit.getEndPoint().do_shop_update(params, filePart).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jObj = new JSONObject(response.body().toString());
                        int success = jObj.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            Log.e("Successfully update!", jObj.toString());

                            Toast.makeText(getApplicationContext(),
                                    "상점 수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                            finishWithResult();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("model", "Failed");
            }
        });
    }

}