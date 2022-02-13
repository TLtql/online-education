package osstest;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.tianle.oss.utils.ConstantPropertiesUtil;
import org.junit.Test;

/**
 * @Author: Tianle
 * @Date: 2021/11/24 0:13
 * @Description:  测试oss存储空间是否存在
 */
public class TestOssExist {
    @Test
    public void testExist(){
        OSS ossClient = new OSSClientBuilder().build(ConstantPropertiesUtil.END_POINT, ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        boolean b = ossClient.doesBucketExist(ConstantPropertiesUtil.BUCKET_NAME);
        System.out.println(b ? "....true" : "...false");
        ossClient.shutdown();
    }
}
