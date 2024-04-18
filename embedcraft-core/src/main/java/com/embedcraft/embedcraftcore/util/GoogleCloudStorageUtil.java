package com.embedcraft.embedcraftcore.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GoogleCloudStorageUtil {
    private static Storage storage;
    private final static String bucketName= "embedcraft";
    private static final String datasetFolderPath = "dataset/"; // dataset folder path within the bucket

    static {
        // Key file inside the resources folder
        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.fromStream(new ClassPathResource("service-account-file.json").getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Initializes the GCS client
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    public static String uploadTextFile(byte[] content) {
        // Generate a random file name using UUID
        String objectName = datasetFolderPath + UUID.randomUUID() + ".txt";

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        // Upload the file
        storage.create(blobInfo, content);

//         Alternatively, the blob's ID (bucketName/objectName) as the identifier:

        return blobId.getName(); // Return the URL or fileId
    }
}
