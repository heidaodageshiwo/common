package com.common.common.zhangqiang.minio;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.minio
 * @ClassName: FileUploader
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-11-14  16:35
 * @UpdateDate: 2022-11-14  16:35
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/*
docker部署minio

        docker run -p 9090:9090 -p 9000:9000 --name minio \
        -d --restart=always \
        -e MINIO_ACCESS_KEY=minio \
        -e MINIO_SECRET_KEY=minio@321 \
        -v /data/docker/minio/data:/data \
        -v /data/docker/minio/config:/root/.minio \
        minio/minio server /data \
        --console-address ":9000" --address ":9090"
        #https://blog.csdn.net/BThinker/article/details/125412751
        #https://docs.min.io/docs/
        # javaSDK：https://docs.min.io/docs/java-client-quickstart-guide.html*/
public class FileUploader {
    public static void main(String[] args)
            throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint("http://139.196.11.84:9090")
                            .credentials("UjYM22uPHWACtTFk", "KsXjM425Kv3Y9KuqzTxy52qLMiY61nyA")
                            .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
            } else {
                System.out.println("Bucket 'asiatrip' already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("asiatrip")
                            .object("微信图片_202210200850131.jpg")
                            .filename("C:\\Users\\Administrator\\Desktop\\微信图片_20221104084041111111111110.jpg")
                            .build());
            System.out.println(
                    "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
                            + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.httpTrace());
        }
    }
}