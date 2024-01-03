import json
import pickle
import numpy as np
import pandas as pd
import tensorflow as tf
from math import sin, cos, sqrt, atan2, radians, asin
from pymongo import MongoClient
from datetime import datetime

try:
    client = MongoClient(
        "mongodb+srv://eshan:78NCxoxRgTBQAVyg@health.7yovxrt.mongodb.net/?retryWrites=true&w=majority")
    # client = MongoClient(
    #     "mongodb+srv://admin:admin@early-detection.jevtz.mongodb.net/")
    db = client["Heal"]
    client.server_info()
    print("DB accessed !")

except Exception as e:
    print("*************************************************************")
    print(e)
    print("*************************************************************")

##################################### PARAMS #########################################

risk_level_mapping = {
    0: "situation doesn't exist",
    1: "Manageable",
    2: "Normal Healthy",
    3: "Risky"
}

excercise_plan_mapping = {
    1: '''{"plan": "No exercises", "exercise": "Restful activities: Encourage relaxation and restful activities such as gentle stretching, listening to calming music, or engaging in hobbies that promote mental well-being."}''',
    0: '''{"plan": "Low Risk Exercises", "exercises": "Seated Stretching: Perform gentle seated stretches, focusing on different muscle groups to promote flexibility and blood circulation. Deep Breathing: Practice deep breathing exercises in a seated or lying position to promote relaxation and reduce stress. Hand and Finger Exercises: Engage in simple exercises like finger taps, wrist circles, or squeezing a stress ball to maintain dexterity and mobility."}''',
    2: '''{"plan": "Low to Moderate Risk Exercises", "exercises": "Chair Exercises: Perform seated exercises that focus on gentle movements and light resistance, such as leg lifts, arm curls with light weights or resistance bands, and seated side bends. Slow Walking: Take slow-paced walks indoors or in a safe outdoor area with proper support and supervision. Modified Yoga: Practice modified yoga poses adapted to a seated or supported position, focusing on gentle stretching and breathing techniques."}''',
    3: '''{"plan": "Moderate to High Risk Exercises", "exercises": "Water-Based Activities: Engage in supervised water exercises in a shallow pool, such as water walking, arm circles, or leg swings. Dancing or Movement to Music: Encourage guided movements or dancing to familiar music, adapting the pace and intensity based on individual abilities. Sit-to-Stand Transitions: Practice sit-to-stand exercises using a sturdy chair, gradually increasing repetitions to enhance lower body strength and mobility"}'''
}


diet_plan_mapping = {
    1: '''{"plan": "No food plan"}''',
    0: '''{"plan": "Low Cholesterol Food", "breakfast": "Vegetable roti (flatbread filled with a mix of grated vegetables) with a side of fresh fruit. Egg hoppers (appa) with a side of tomato and cucumber salad. Rava upma (semolina cooked with vegetables) with a side of fresh fruit. Oatmeal topped with sliced almonds and fresh berries.", "lunch": "Grilled fish curry with tempered green beans and steamed red rice. Lentil curry (parippu) with tempered spinach and brown rice. Sambar (lentil and vegetable stew) with brown rice and a cucumber salad. Grilled vegetable wrap with whole wheat tortilla, hummus, and avocado.", "dinner": "Stir-fried chicken and vegetable curry with quinoa. Grilled shrimp skewers with mixed vegetable stir-fry and red rice. Millet dosa with tomato chutney and a mixed vegetable curry. Baked chicken breast with quinoa and steamed asparagus"}''',
    2: '''{"plan": "Balanced Diet with All Nutrients", "breakfast": "Coconut milk rice (kiribath) with lunu miris (spicy onion sambol) and boiled eggs. String hoppers (idiyappam) with dhal curry and coconut chutney. Greek yogurt with granola, banana slices, and a drizzle of honey. Idli (steamed rice cakes) with coconut chutney and sambar.", "lunch": "Chicken curry with a side of mixed vegetable curry and quinoa. Fish ambul thiyal (sour fish curry) with red rice and gotu kola salad. Quinoa salad with mixed vegetables (cucumbers, tomatoes, bell peppers) and a lemon-tahini dressing. Vegetable biryani with cucumber raita and a side of mixed vegetable kurma", "dinner": "Eggplant curry with coconut roti and a cucumber and tomato salad Mixed vegetable kottu roti with a side of chicken curry. Channa masala (spiced chickpea curry) with roti and a spinach salad. Grilled lean steak with roasted potatoes, sautéed kale, and a side of mixed beans."}''',
    3: '''{"plan": "Hot Beverages & Meals Rich in Potassium", "breakfast": "Spiced tea (Ceylon tea with spices like cinnamon and cardamom) with string hoppers and coconut sambol. Coconut water with hopper pancakes (appa) and lunu miris (spicy onion sambol). Banana and spinach smoothie with almond milk, chia seeds, and a pinch of cinnamon. Masala chai with medu vada (lentil fritters) and coconut chutney.", "lunch": "Jackfruit curry with basmati rice and a side of tempered spinach. Chicken curry with potato and green bean curry, served with red rice. Lentil soup with carrots, celery, and a squeeze of lemon juice Avial (mixed vegetable curry in coconut gravy) with adai (mixed lentil dosa) and a beetroot poriyal.", "dinner": " Lentil soup with pumpkin and a side of papadums Beetroot curry with coconut milk rice(kiribath) and a cucumber salad. Baked cod with steamed brown rice, roasted Brussels sprouts, and a sprinkle of sesame seeds. Tomato rasam(spicy tomato soup) with lemon rice and a spinach poriyal"}'''
}

##################################### LOAD MODELS #########################################

risk_model = tf.keras.models.load_model('weights/risk_prediction.h5')
risk_model.compile(
    optimizer='adam',
    loss='categorical_crossentropy',
    metrics=[
        tf.keras.metrics.CategoricalAccuracy(),
        tf.keras.metrics.Precision(),
        tf.keras.metrics.Recall(),
        tf.keras.metrics.AUC()
    ])

with open('weights/scalar - risk_prediction.pkl', 'rb') as f:
    risk_scalar = pickle.load(f)


diet_workout_model = tf.keras.models.load_model(
    'weights/diet_workout_prediction.h5')
diet_workout_model.compile(
    loss='sparse_categorical_crossentropy',
    optimizer='adam',
    metrics=['accuracy']
)

with open('weights/scalar - diet_workout_prediction.pkl', 'rb') as f:
    diet_workout_scalar = pickle.load(f)

##################################### PREDICTION #########################################


def risk_level_prediction():
    health_collection = db["health"]
    health_data = pd.DataFrame(list(health_collection.find()))
    sample_data = health_data[-50:][["HeartRate",
                                     "BloodOxygenLevel", "BodyTemperature"]]
    # sample_data = health_data.sample(
    #     frac=50/len(health_data))[["HeartRate", "BloodOxygenLevel", "BodyTemperature"]]

    HeartRateArr = sample_data["HeartRate"].values.tolist()
    BloodOxygenLevelArr = sample_data["BloodOxygenLevel"].values.tolist()
    BodyTemperatureArr = sample_data["BodyTemperature"].values.tolist()

    Xinf = sample_data.values
    Xinf = risk_scalar.transform(Xinf)
    Xinf = Xinf.reshape(1, 50, 3)

    risk_level = risk_model.predict(Xinf).squeeze()
    risk_level = np.argmax(risk_level)
    risk_level = risk_level_mapping[risk_level]
    risk_level_collection = db['risk_level']
    risk_level_document = {
        "risk_level": risk_level,
        "created_at": datetime.now(),
        "updated_at": datetime.now()
    }
    risk_level_collection.insert_one(risk_level_document)
    return risk_level, HeartRateArr, BloodOxygenLevelArr, BodyTemperatureArr


def diet_workout_prediction():
    health_collection = db["health"]
    health_data = pd.DataFrame(list(health_collection.find()))
    sample_data = health_data[-50:][["HeartRate",
                                     "BloodOxygenLevel", "BodyTemperature"]]

    HeartRateArr = sample_data["HeartRate"].values.tolist()
    BloodOxygenLevelArr = sample_data["BloodOxygenLevel"].values.tolist()
    BodyTemperatureArr = sample_data["BodyTemperature"].values.tolist()

    Xinf = sample_data.values
    Xinf = diet_workout_scalar.transform(Xinf)

    exercise_plan_idxs, diet_plan_idxs = diet_workout_model.predict(Xinf)
    exercise_plan_idxs = exercise_plan_idxs.squeeze()
    diet_plan_idxs = diet_plan_idxs.squeeze()

    exercise_plan_idxs = np.argmax(exercise_plan_idxs, axis=1)
    diet_plan_idxs = np.argmax(diet_plan_idxs, axis=1)

    exercise_plans = [excercise_plan_mapping[idx]
                      for idx in exercise_plan_idxs]
    diet_plans = [diet_plan_mapping[idx] for idx in diet_plan_idxs]

    mode_exercise_plan = max(set(exercise_plans), key=exercise_plans.count)
    mode_diet_plan = max(set(diet_plans), key=diet_plans.count)

    diet_plan_collection = db['diet_plan']
    diet_plan_document = {
        "diet_plan": mode_diet_plan,
        "created_at": datetime.now(),
        "updated_at": datetime.now()
    }
    diet_plan_collection.insert_one(diet_plan_document)
    exercise_plan_collection = db['exercise_plan']
    exercise_plan_document = {
        "exercise_plan": mode_exercise_plan,
        "created_at": datetime.now(),
        "updated_at": datetime.now()
    }
    exercise_plan_collection.insert_one(exercise_plan_document)
    return mode_exercise_plan, mode_diet_plan, HeartRateArr, BloodOxygenLevelArr, BodyTemperatureArr


def haversine(p1, p2):
    lat1, lon1 = p1
    lat2, lon2 = p2

    lat1 = float(lat1)
    lon1 = float(lon1)

    lat2 = float(lat2)
    lon2 = float(lon2)

    # convert decimal degrees to radians
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    # haversine formula
    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2

    c = 2 * asin(sqrt(a))
    # Radius of earth in kilometers. Use 3956 for miles. Determines return value units.
    r = 6371
    distance_km = c * r
    return distance_km
