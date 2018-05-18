package www.coders.org.qr_fintech_client;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ManageStoreDetail extends AppCompatActivity {

    Button delete_button;
    Button modify_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_store_detail);

        delete_button = (Button) findViewById(R.id.cancel_button);
        delete_button.setOnClickListener(mDeleteClickListener);

        modify_button = (Button) findViewById(R.id.modify_button);
        modify_button.setOnClickListener(mModifyClickListener);
    }

    View.OnClickListener mDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(ManageStoreDetail.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("상점을 삭제하시겠습니까?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            finish();
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener mModifyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            modifyStoreInfo();
            finish();
        }
    };

    public void onBackPressed() {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("변경된 내용을 저장하시겠습니까??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modifyStoreInfo();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        finish();
                    }
                })
                .show();
    }

    private void modifyStoreInfo()
    {

    }
}