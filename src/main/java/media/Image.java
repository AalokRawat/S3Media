package media;/*
 * Project Horizon
 * (c) 2018-2019 VMware, Inc. All rights reserved.
 * VMware Confidential.
 */

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@CrossOrigin
@RestController
@RequestMapping("/image")
public class Image {
    String BucketName = "aalok-created";

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String upload(@RequestParam("name") String fileName, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        AmazonS3 s3Client = new AmazonS3Client(new PropertiesCredentials(Image.class.getClassLoader().getResourceAsStream("AwsCredentials.properties")));
        InputStream stream = file.getInputStream();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest putObjectRequest = new PutObjectRequest(BucketName, fileName, stream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(putObjectRequest);
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(BucketName, fileName));

        //redirectAttributes.addAttribute("url", s3Object.getObjectContent().getHttpRequest().toString());
        System.out.println(s3Object.getObjectContent().getHttpRequest());

        return s3Object.getObjectContent().getHttpRequest().getURI().toString() ;
    }

    @RequestMapping(value="/{image}", method = RequestMethod.GET)
    public void download(@PathVariable("image") String keyName) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new PropertiesCredentials(Image.class.getClassLoader().getResourceAsStream("AwsCredentials.properties")));
        keyName+=".jpg";
        GetObjectRequest request = new GetObjectRequest(BucketName,
                keyName);
        S3Object object = s3Client.getObject(request);
        S3ObjectInputStream objectContent = object.getObjectContent();
        IOUtils.copy(objectContent, new FileOutputStream(keyName));
    }
}
