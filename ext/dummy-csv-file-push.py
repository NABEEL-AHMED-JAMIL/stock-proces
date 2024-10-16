import os
import random

import requests
from faker import Faker
import pandas as pd

# Initialize Faker
fake = Faker()

# default num records 1000 and start date = 2024-10-10
def generate_fake_stock_data(num_records=1000, start_date='2024-10-10'):
    data = []
    for _ in range(num_records):
        # Generate random stock prices
        open_price = round(random.uniform(23.0, 25.0), 3)
        high_price = round(open_price + random.uniform(0.0, 1.0), 3)
        low_price = round(open_price - random.uniform(0.0, 1.0), 3)
        close_price = round(random.uniform(low_price, high_price), 3)
        volume = random.randint(1000, 50000)  # Random volume
        open_int = random.randint(0, 10000)  # Generate a random Open Interest value
        # Append data for the current date
        data.append([start_date, open_price, high_price, low_price, close_price, volume, open_int])
    return data


if __name__ == '__main__':
    while True:
        # Generate data and create a DataFrame
        random_date = fake.date_between(start_date='-1y', end_date='today')
        num_records = 1000  # Change this to generate more records
        fake_stock_data = generate_fake_stock_data(num_records, random_date)
        df = pd.DataFrame(fake_stock_data, columns=["Date", "Open", "High", "Low", "Close", "Volume", "OpenInt"])
        # Use the existing directory for saving the CSV file
        directory = r"C:\Users\njamil\Desktop\Nabeel RND\ren-data-gerator\stock-dump-file"
        file_path = os.path.join(directory, f"{random_date}.csv")
        # file_path = os.path.join(directory, f"{random_date}.pqt")
        df.to_parquet(file_path, index=False)
        # copy the file from directory and send this to apie
        response = requests.post("http://localhost:9097/action.json/uploadFile",
             files={
                 'files[0]': (f"{random_date}.csv", open(file_path, 'rb'), 'text/csv')
             }
             # files = {
             #     'files[0]': (f"{random_date}.pqt", open(file_path, 'rb'), 'application/x-parquet')
             # }
        )
        # Print the response from the server
        print(f"Response from server: {response.status_code}, {response}")
        # delete file after send
        os.remove(file_path)