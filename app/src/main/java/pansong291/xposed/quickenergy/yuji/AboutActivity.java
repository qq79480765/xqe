package pansong291.xposed.quickenergy.yuji;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import pansong291.xposed.quickenergy.R;

public class AboutActivity   extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_yuji);
        init();
    }

    private void init() {
        info1();
        info2();
        info3();
        info4();
        info5();
        info6();
        TextView bt=findViewById(R.id.bt);
        bt.setOnClickListener(v -> {
            Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startapp?saId=10000007&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx1504358uixr8gaa0jxb3"));
            startActivity(it2);
        });
    }

    private void info1() {
        TextView tvInfo=findViewById(R.id.tv_info1);
        tvInfo.setText("1.本APP是为了学习研究用，不得进行任何形式的转发，发布，传播。\n" +
                "2.请于24小时内卸载本APP。若使用期间造成任何损失，作者不负任何责任。\n" +
                "3.本APP不篡改，不修改，不获取任何个人信息及其支付宝信息。\n" +
                "4.本APP使用者因为违反本声明的规定而触犯中华人民共和国法律的，一切后果自己负责，作者不承担任何责任。\n" +
                "5.本APP是基于开源项目XQuickEnergy修改而来。源码地址：https://github.com/pansong291/XQuickEnergy\n" +
                "6.作者只修复了蚂蚁森林，其他业务不感兴趣，也不会维护。\n" +
                "7.作者没任何义务修改bug完善功能，APP是为了学习研究用。\n" +
                "8.凡以任何方式直接、间接使用App着，视为自愿接受本声明的约束。\n" +
                "9.本App如无意中侵犯了某个媒体或个人的知识产权，请来信或来电告之，作者将立即删除。");
        tvInfo.setOnClickListener(v -> {
            int color= (int) (Math.random()*0x00FFFFFF+0xFF000001);
            tvInfo.setTextColor(color);
        });
    }
    private void info2() {
        TextView tvInfo=findViewById(R.id.tv_info2);
        tvInfo.setText("使用\n" +
                "1.自启请配合Xposed edge 使用\n" +
                "2.因为持续监测十分耗电，建议edge设置成早上7点强杀支付宝再启动。白天其余时段无需运行\n" +
                "感谢\n" +
                "1.感谢作者源码，让我们得以学习研究。\n" +
                "2.感谢蜡笔小新提供接口");
        tvInfo.setOnClickListener(v -> {
            int color= (int) (Math.random()*0x00FFFFFF+0xFF000001);
            tvInfo.setTextColor(color);
        });
    }
    private void info3() {
        TextView tvInfo=findViewById(R.id.tv_info3);
        tvInfo.setText("点击跳转到原来GitHub地址");
        tvInfo.setOnClickListener(v -> {
            Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pansong291/XQuickEnergy"));
            startActivity(it2);
        });
    }
    private void info4() {
        TextView tvInfo=findViewById(R.id.tv_info4);
        tvInfo.setText("点击跳转到新的GitHub地址");
        tvInfo.setOnClickListener(v -> {
            Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/yutils/xqe"));
            startActivity(it2);
        });
    }

    private void info5() {
        TextView tvInfo=findViewById(R.id.tv_info5);
        tvInfo.setText("点击下载最新版");
        tvInfo.setOnClickListener(v -> {
            Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t00y.com/dir/12572488-39789131-a174b0"));
            startActivity(it2);
        });
    }
    private void info6() {
        TextView tvInfo=findViewById(R.id.tv_info6);
        tvInfo.setText("");
    }
}

