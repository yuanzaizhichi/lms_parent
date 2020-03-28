package com.cxf.common.utils;

import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 用户头像上传工具类（七牛云，30天失效，注意及时修改过期七牛云域名）
 */
public class QiniuUploadUtil {

    private static final String accessKey = "55vBjJuzc0R6bDafHyjvHTlluW7gxxkttEX7DGFq";
    private static final String secretKey = "NPLkuCZRDCE8j04MZqnW-7wshM2jgoAUkHy-QbPl";
    private static final String bucket = "cxf-lms";
    private static final String prix = "http://q7w2afkbr.bkt.clouddn.com/";
    private UploadManager manager;

    public String getPrix(){
        return this.prix;
    }

    public QiniuUploadUtil() {
        //初始化基本配置
        Configuration cfg = new Configuration(Region.region2());
        //创建上传管理器
        manager = new UploadManager(cfg);
    }

	//文件名 = key
	//文件的byte数组
    public String upload(String imgName , byte [] bytes) {
        Auth auth = Auth.create(accessKey, secretKey);
        //构造覆盖上传token
        String upToken = auth.uploadToken(bucket,imgName);
        try {
            Response response = manager.put(bytes, imgName, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回请求地址
            return prix+putRet.key+"?t="+System.currentTimeMillis();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
