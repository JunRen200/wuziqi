package wuziqi.example.asus_pc.wuziqi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private wuziqi wuziqi1;
    private TextView txt_number;
    private TextView txt_time;
    private boolean gameover = false;
    private int time = 0;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wuziqi1 = (wuziqi) findViewById(R.id.wuziqi);
        wuziqi1.setNunberComput(new wuziqi.NumberComput() {
            @Override
            public void num(int number) {
                a = number;
                txt_number.setText("步数:" + number);
            }

            @Override
            public void getTime(final int time) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt_time.setText(String.valueOf("时间:"+time));
                    }
                });
            }

        });
        wuziqi1.setOverListener(new wuziqi.OverListener() {
            @Override
            public void GameOver(int number, int tiem,String Win) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(Win);
                builder.setMessage("时间:"+tiem+"   步数:"+number);
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        txt_number = (TextView) findViewById(R.id.txt_number);
        txt_time = (TextView) findViewById(R.id.txt_time);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.a) {
            wuziqi1.start();
            txt_time.setText("时间:"+0);
        }
        return true;
    }
}
