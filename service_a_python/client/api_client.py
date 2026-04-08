import requests
from config.settings import SERVICE_B_URL, BATCH_SIZE

def send_in_batches(data):
    for i in range(0, len(data), BATCH_SIZE):
        batch = data[i:i+BATCH_SIZE]
        try:
            res = requests.post(SERVICE_B_URL, json=batch)
            print(f"Sent batch {i//BATCH_SIZE + 1}, status: {res.status_code}")
        except Exception as e:
            print(f"Error sending batch: {e}")