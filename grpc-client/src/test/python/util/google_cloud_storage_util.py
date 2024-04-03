from google.cloud import storage
import os

bucket_name = "embedcraft"

def upload_file_to_gcs(source_file_name, destination_blob_name):
    """Uploads a file to the bucket."""
    # The ID of your GCS bucket
    # The path to your file to upload
    # The ID to give your GCS blob
    
    storage_client = storage.Client()
    bucket = storage_client.bucket(bucket_name)
    blob = bucket.blob(destination_blob_name)

    blob.upload_from_filename(source_file_name)

    print(f"File {source_file_name} uploaded to {destination_blob_name}.")


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