import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "..", "data")

SERVICE_B_URL = "http://localhost:8080/students/batch"
BATCH_SIZE = 50