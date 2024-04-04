from google.cloud import storage
import os

bucket_name = "embedcraft"

def upload_model_to_gcs(client, destination_blob_name, model_bytes):
    """Uploads a file to the bucket."""
    
    bucket = client.bucket(bucket_name)

    blob = bucket.blob(destination_blob_name)
    blob.upload_from_string(model_bytes)


def read_file_from_gcs(client, blob_name):
    """Reads a file from Google Cloud Storage and returns its contents."""
    
    # Get the bucket and blob (file) from GCS
    bucket = client.bucket(bucket_name)
    blob = bucket.blob(blob_name)
    
    # Download the blob's contents as a string
    contents = blob.download_as_text()

    return contents

def initialize_client():
    # Authenticate to GCS using the service account key file
    return storage.Client.from_service_account_json("./util/service-account-file.json")
    

# if __name__ == "__main__":
#     client = initialize_client()
#     print(read_file_from_gcs(client, "dataset/35bc50b4-8794-4940-be73-d3307518eb71.txt"))