package com.epam.audio_streaming.service.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.audio_streaming.annotation.StorageType;
import com.epam.audio_streaming.model.Source;
import com.epam.audio_streaming.model.StorageTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@StorageType(StorageTypes.AMAZON_S3)
public class StorageAmazonS3 implements StorageSourceService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${secret}")
    private String secret;

    @Value("${access}")
    private String access;

    @Value("${pass}")
    private String pass;

    @Value("${salt}")
    private String salt;


    @Override
    public InputStream get(Source source) throws Exception {

        S3Object s3object = s3Client().getObject(source.getPath(), source.getName());
        byte[] content = IOUtils.toByteArray(s3object.getObjectContent());
        return new ByteArrayResource(content).getInputStream();
    }

    @Override
    public Source save(Resource resource, String name, String typeContent) throws Exception {

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(resource.contentLength());
        meta.setContentMD5(new String(Base64.encodeBase64(DigestUtils.md5(resource.getInputStream()))));
        s3Client().putObject(bucketName, name, resource.getInputStream(), meta);
        Source source = new Source(name, resource.contentLength(), DigestUtils.md5Hex(resource.getInputStream()),
                bucketName, typeContent);
        source.setStorageTypes(StorageTypes.AMAZON_S3);
        return source;

    }

    @Override
    public void delete(Source source) throws Exception {
        s3Client().deleteObject(source.getPath(), source.getName());
    }

    @Override
    public boolean exist(Source source) {
        return s3Client().doesObjectExist(source.getPath(), source.getName());
    }

    public AmazonS3 s3Client() {
        TextEncryptor encryptor = Encryptors.text(this.pass, this.salt);
        AWSCredentials credentials = new BasicAWSCredentials(encryptor.decrypt(this.access),
                encryptor.decrypt(this.secret));
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

}



