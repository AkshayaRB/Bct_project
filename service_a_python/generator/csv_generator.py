import pandas as pd
import random
from faker import Faker
import os
from config.settings import DATA_DIR

fake = Faker()

def generate_csv(filename="students.csv", count=120):
    data = []

    for _ in range(count):
        name = fake.name()

        email = fake.email() if random.random() > 0.2 else "invalid_email"
        if random.random() < 0.1:
            email = None

        age = random.randint(18, 30)

        # NEW: phone number (with some edge cases)
        phone = fake.phone_number() if random.random() > 0.05 else None

        data.append({
            "name": name,
            "email": email,
            "age": age,
            "phone": phone
        })

    df = pd.DataFrame(data)
    os.makedirs(DATA_DIR, exist_ok=True)

    file_path = os.path.join(DATA_DIR, filename)
    df.to_csv(file_path, index=False)

    print(f"CSV generated at {file_path}")