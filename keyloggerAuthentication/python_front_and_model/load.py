import os
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import pickle

class YourClass:
    def train_and_save_model(self):
        print("Начало загрузки данных...")
        data = pd.read_csv("data.csv")

        X = data[['Speed', 'Error', 'Time', 'Average Pause']]
        y = data['User ID']

        print("Нормализация признаков...")
        scaler = StandardScaler()
        X = scaler.fit_transform(X)

        print("Разделение данных на обучающий и тестовый наборы...")
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        print("Создание и обучение модели случайного леса...")
        model = RandomForestClassifier(n_estimators=100, random_state=42)
        model.fit(X_train, y_train)

        # Получение пути к текущей директории
        current_dir = os.getcwd()
        print("Текущая директория:", current_dir)

        # Создание директории для сохранения модели, если она не существует
        model_dir = os.path.join(current_dir, 'models')
        if not os.path.exists(model_dir):
            os.makedirs(model_dir)

        # Сохранение модели в файл
        model_file_path = os.path.join(model_dir, 'your_trained_model.pkl')
        with open(model_file_path, 'wb') as file:
            pickle.dump(model, file)

        # Сохранение нормализатора в файл
        scaler_file_path = os.path.join(model_dir, 'scaler.pkl')
        with open(scaler_file_path, 'wb') as file:
            pickle.dump(scaler, file)

        print("Модель и нормализатор успешно обучены и сохранены.")

if __name__ == "__main__":
    model_trainer = YourClass()
    model_trainer.train_and_save_model()
