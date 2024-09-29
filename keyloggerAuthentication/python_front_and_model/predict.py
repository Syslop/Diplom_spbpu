import sys
import json
import joblib
import numpy as np

def main():
    # Получаем JSON аргумент из командной строки
    json_input = sys.argv[1]
    metrics = json.loads(json_input)

    # Преобразуем входные данные в массив
    input_data = np.array([[metrics['Speed'], metrics['Error'], metrics['Time'], metrics['Average Pause']]])

    # Загрузка нормализатора и модели
    scaler = joblib.load('./models/scaler.pkl')
    model = joblib.load('./models/your_trained_model.pkl')

    # Нормализация входных данных
    input_data = scaler.transform(input_data)
    print(input_data)

    # Предсказание
    predicted_user_id = model.predict(input_data)[0]

    # Вывод предсказанного значения
    print(predicted_user_id)

if __name__ == "__main__":
    main()
