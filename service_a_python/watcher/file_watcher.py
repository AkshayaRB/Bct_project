from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler
import time
import os
import pandas as pd

from parser.csv_parser import parse_csv
from validator.validator import validate_records
from client.api_client import send_in_batches

class CSVHandler(FileSystemEventHandler):
    def on_created(self, event):
        if event.src_path.endswith(".csv") and not event.src_path.endswith(("valid_students.csv", "invalid_students.csv")):
            self.process_csv(event.src_path)

    def on_modified(self, event):
        if event.src_path.endswith(".csv") and not event.src_path.endswith(("valid_students.csv", "invalid_students.csv")):
            self.process_csv(event.src_path)

    def process_csv(self, file_path):
        print(f"Processing file: {file_path}")

        records = parse_csv(file_path)
        valid, invalid = validate_records(records)

        # Save valid records to CSV
        if valid:
            valid_df = pd.DataFrame(valid)
            valid_path = os.path.join(os.path.dirname(file_path), "valid_students.csv")
            valid_df.to_csv(valid_path, index=False)
            print(f"Saved {len(valid)} valid records to {valid_path}")

        # Save invalid records to CSV
        if invalid:
            invalid_df = pd.DataFrame(invalid)
            invalid_path = os.path.join(os.path.dirname(file_path), "invalid_students.csv")
            invalid_df.to_csv(invalid_path, index=False)
            print(f"Saved {len(invalid)} invalid records to {invalid_path}")

        # Send only valid records to API
        send_in_batches(valid)

def start_watcher(path):
    event_handler = CSVHandler()
    observer = Observer()
    observer.schedule(event_handler, path=path, recursive=False)
    observer.start()

    print("Watching directory:", path)

    try:
        while True:
            time.sleep(2)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()