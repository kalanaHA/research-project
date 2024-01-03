import numpy as np
import pickle
import tensorflow as tf
import pandas as pd
import pymongo
from bson import ObjectId


# Load the trained model and scaler
model = tf.keras.models.load_model('weights/health_data_dementia.h5')
with open('weights/scalar_health_data.pkl', 'rb') as f:
    scalar = pickle.load(f)

# Function to process new data and make predictions
def predict_dementia(new_data):
    # Preprocess the new data
    new_data = scalar.transform(new_data.reshape(1, -1))

    # Reshape the data to match the model's input shape
    n_steps, n_features = model.input_shape[1:]
    X = np.zeros((1, n_steps, n_features))
    X[0] = new_data

    # Make predictions
    predictions = model.predict(X)

    # Check the prediction
    if predictions[0][0] >= 0.5:
        result = "Patient has dementia"
    else:
        result = "Patient doesn't have dementia"

    return result



# Set up MongoDB connection
try:
    client = pymongo.MongoClient("mongodb+srv://ITP:89DbKo5LYRogaw96@cluster0.3agof.mongodb.net/?retryWrites=true&w=majority")
    db = client["Heal"]
    print("DB accessed!")

except Exception as e:
    print("Error while connecting to MongoDB:", e)
    exit()  # Exit the script if there's a connection error


# Create a collection for user data
user_data_collection = db["health"]

# Function to retrieve user data from MongoDB
def get_latest_user_data():
    try:
        # Sort the documents in descending order by _id (assuming _id is a timestamp)
        latest_user_data = user_data_collection.find().sort([("_id", pymongo.DESCENDING)]).limit(1)

        if latest_user_data:
            return latest_user_data[0]
        else:
            return None
    except Exception as e:
        print("Error while retrieving user data:", e)
        return None

latest_user_data = get_latest_user_data()


if latest_user_data:

    for key, value in latest_user_data.items():
        print(f"{key}: {value}")

else:
    
    print("User data not found.")

# Function to create a new array 
def get_Android_data(api_data):
    if latest_user_data is not None and api_data is not None:
        # Create an array with the required features
        new_data = np.array([api_data["Diabetic"], api_data["Alcohol_Level"], api_data["Weight"], api_data["MRI_Delay"],
                              latest_user_data["HeartRate"], latest_user_data["BloodOxygenLevel"], latest_user_data["BodyTemperature"]])

        result = predict_dementia(new_data)
        
        return result
        
    else:
        print("User data not found or API data is missing.")






