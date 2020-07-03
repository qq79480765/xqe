package pansong291.xposed.quickenergy.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.util.FileUtils;
import pansong291.xposed.quickenergy.util.RandomUtils;
import pansong291.xposed.quickenergy.util.Statistics;
import pansong291.xposed.quickenergy.yuji.AboutActivity;

public class MainActivity extends Activity {
    private static String[] strArray;
    TextView tv_statistics;
    Button btn_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_unactive = findViewById(R.id.tv_unactive);
        tv_unactive.setVisibility(isModuleActive() ? View.GONE : View.VISIBLE);

        tv_statistics = findViewById(R.id.tv_statistics);
        btn_help = findViewById(R.id.btn_help);
        if (strArray == null)
            strArray = getResources().getStringArray(R.array.sentences);
        btn_help.setText(strArray[RandomUtils.nextInt(0, strArray.length)]);
        btn_help.setOnClickListener(v -> {
            btn_help.setText(strArray[RandomUtils.nextInt(0, strArray.length)]);
            int color = (int) (Math.random() * 0x00FFFFFF + 0xFF000001);
            btn_help.setTextColor(color);
        });
        if (isUpdate(this)){
            Toast.makeText(this,"本APP只为学习研究用，请于24小时内卸载本APP。",Toast.LENGTH_LONG).show();
        }
        //获取权限
        requestPermission(this);
    }

    private void requestPermission(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if (packageInfo == null) return;
        String[] permissions = packageInfo.requestedPermissions;
        ArrayList<String> toApplyList = new ArrayList<>();
        for (String item : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, item)) {
                toApplyList.add(item); // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 888);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_statistics.setText(Statistics.getText());
    }

    public void onClick(View v) {
        String data = "file://";
        switch (v.getId()) {
            case R.id.btn_forest_log:
                data += FileUtils.getForestLogFile().getAbsolutePath();
                break;

            case R.id.btn_farm_log:
                data += FileUtils.getFarmLogFile().getAbsolutePath();
                break;

            case R.id.btn_other_log:
                data += FileUtils.getOtherLogFile().getAbsolutePath();
                break;

            case R.id.btn_help:
                //data = "https://github.com/pansong291/XQuickEnergy/wiki";
                return;
        }
        Intent it = new Intent(this, HtmlViewerActivity.class);
        it.setData(Uri.parse(data));
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int state = getPackageManager()
                .getComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"));
        menu.add(0, 1, 0, "隐藏本APP图标")
                .setCheckable(true)
                .setChecked(state > PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        menu.add(0, 2, 0, "导出统计文件");
        menu.add(0, 3, 0, "导入统计文件");
        menu.add(0, 4, 0, "设置");
        menu.add(0, 5, 0, "关于雨季版");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                int state = item.isChecked() ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                getPackageManager()
                        .setComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"), state, PackageManager.DONT_KILL_APP);
                item.setChecked(!item.isChecked());
                break;
            case 2:
                if (FileUtils.copyTo(FileUtils.getStatisticsFile(), FileUtils.getExportedStatisticsFile()))
                    Toast.makeText(this, "Export success", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                if (FileUtils.copyTo(FileUtils.getExportedStatisticsFile(), FileUtils.getStatisticsFile())) {
                    tv_statistics.setText(Statistics.getText());
                    Toast.makeText(this, "Import success", Toast.LENGTH_SHORT).show();
                }
                break;

            case 4:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, AboutActivity.class));
                TextView tv_unactive = findViewById(R.id.tv_unactive);
                tv_unactive.setTag(isModuleActive());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //为了不被混淆给干掉此方法。多处调用
    public boolean isModuleActive() {
        return (findViewById(R.id.tv_unactive).getVisibility() == View.GONE);
    }

    //是否更新
    public static boolean isUpdate(Context context) {
        int versionCode = getVersionCode(context, context.getPackageName());
        String versionName = getVersionName(context, context.getPackageName());
        SharedPreferences shared = context.getSharedPreferences("AppVersion", 0);
        if (versionCode != shared.getInt("versionCode", -1) || !versionName.equals(shared.getString("versionName", ""))) {
            // 如果版本号有变化
            SharedPreferences.Editor share = context.getSharedPreferences("AppVersion", 0).edit();
            share.putString("versionName", versionName);// 写入数据
            share.putInt("versionCode", versionCode);
            share.apply();
            return true;
        }
        return false;
    }

    public static int getVersionCode(Context context, String packageName) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return verCode;
    }

    public static String getVersionName(Context context, String packageName) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return verName;
    }
}
