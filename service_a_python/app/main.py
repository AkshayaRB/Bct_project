import os
import sys

from fastapi import FastAPI
import threading

# Ensure the service root is on sys.path when running from service_a_python/app
ROOT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
if ROOT_DIR not in sys.path:
    sys.path.insert(0, ROOT_DIR)

from generator.csv_generator import generate_csv
from watcher.file_watcher import start_watcher
from config.settings import DATA_DIR

app = FastAPI()

@app.on_event("startup")
def startup_event():
    thread = threading.Thread(target=start_watcher, args=(DATA_DIR,), daemon=True)
    thread.start()

@app.get("/generate")
def generate():
    generate_csv()
    return {"message": "CSV generated"}