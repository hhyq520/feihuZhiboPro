package cn.feihutv.zhibofeihu.service;

import android.content.Intent;
import android.os.IBinder;

import com.allenliu.versionchecklib.core.AVersionService;

import cn.feihutv.zhibofeihu.FeihuZhiboApplication;
import cn.feihutv.zhibofeihu.utils.SharePreferenceUtil;

public class DemoService extends AVersionService {
    public DemoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        String downLoadUrl= SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(),"apkUrlName");
        showVersionDialog(downLoadUrl, "v" + SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(), "versionName"), SharePreferenceUtil.getSession(FeihuZhiboApplication.getApplication(),"versionDesc"));
    }

}
