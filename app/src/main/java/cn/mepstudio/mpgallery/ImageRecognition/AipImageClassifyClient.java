package cn.mepstudio.mpgallery.ImageRecognition;

import com.baidu.aip.imageclassify.AipImageClassify;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import javax.sql.StatementEvent;

public class AipImageClassifyClient extends AipImageClassify {

        //设置APPID/AK/SK
        public static final String APP_ID = "15635090";
        public static final String API_KEY = "f1OgPXxnd8NidYxEfqywvxxY";
        public static final String SECRET_KEY = "14ppmGbwIRginwQGW6TryoV6SZpqCHW2";


        public AipImageClassifyClient(String appID, String apiKey, String secretKey){
            super(appID,apiKey,secretKey);
            this.setConnectionTimeoutInMillis(60000);
            this.setSocketTimeoutInMillis(60000);
            }
            public AipImageClassifyClient(){
            super(APP_ID,API_KEY,SECRET_KEY);
            this.setConnectionTimeoutInMillis(60000);
            this.setSocketTimeoutInMillis(60000);
}
/*
public static void main(String[] args) {
        // 初始化一个AipImageClassifyClient
        AipImageClassifyClient client = new AipImageClassifyClient(APP_ID, API_KEY, SECRET_KEY);


        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "465840.jpg";
        JSONObject res = client.objectDetect(path, new HashMap<String, String>());
            try {
                System.out.println(res.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


*/

}
