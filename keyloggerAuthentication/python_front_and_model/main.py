import json
import time
import random
import requests
import tkinter as tk
from tkinter import ttk, messagebox
import ttkbootstrap as ttkb


class TextMetricsTracker:
    def __init__(self):
        self.start_time = time.time()
        self.last_key_time = self.start_time
        self.total_keys = 0
        self.total_errors = 0
        self.total_pause_time = 0

    def track_key_press(self, event):
        current_time = time.time()
        self.total_keys += 1
        if event.keysym in ["BackSpace", "Delete"]:
            self.total_errors += 1
        self.total_pause_time += current_time - self.last_key_time
        self.last_key_time = current_time

    def calculate_metrics(self):
        total_time = time.time() - self.start_time
        speed = self.total_keys * 60.0 / total_time if total_time > 0 else 0
        error = self.total_errors
        average_pause = self.total_pause_time / (self.total_keys - 1) if self.total_keys > 1 else 0

        return {
            "speed": speed,
            "error": error,
            "time": total_time,
            "averagePause": average_pause
        }


def start_tracking(tab_index):
    global tracker
    if tab_index == 0:
        text_widget = text_entry1
    else:
        text_widget = text_entry2

    tracker = TextMetricsTracker()
    text_widget.bind("<KeyPress>", tracker.track_key_press)
    text_widget.bind("<KeyRelease>", lambda event: check_text(tab_index))


def check_text(tab_index):
    if tab_index == 0:
        text_widget = text_entry1
    else:
        text_widget = text_entry2

    entered_text = text_widget.get("1.0", "end-1c")
    if entered_text == example_text:
        text_widget.tag_configure("correct", foreground="green")
        text_widget.tag_add("correct", "1.0", "end")
    else:
        text_widget.tag_configure("incorrect", foreground="red")
        text_widget.tag_add("incorrect", "1.0", "end")


def update_status_icon(tab_index, success):
    if tab_index == 0:
        login_status_label = login_status_label1
        password_status_label = password_status_label1
    else:
        login_status_label = login_status_label2
        password_status_label = password_status_label2

    icon = "✔" if success else "✘"
    color = "green" if success else "red"

    login_status_label.config(text=icon, foreground=color)
    password_status_label.config(text=icon, foreground=color)


def submit_metrics(tab_index):
    if tab_index == 0:
        text = text_entry1.get("1.0", "end-1c")
        login = login_entry1.get()
        password = password_entry1.get()
        url = "http://localhost:8080/user/login/"
    else:
        text = text_entry2.get("1.0", "end-1c")
        login = login_entry2.get()
        password = password_entry2.get()
        url = "http://localhost:8080/user/add/"

    if text.strip() == "":
        messagebox.showerror("Error", "Введите текст.")
        update_status_icon(tab_index, False)
        return

    metrics = tracker.calculate_metrics()

    data = {
        "user": {
            "login": login,
            "password": password
        },
        "metric": metrics
    }
    headers = {"Content-Type": "application/json"}
    response = requests.post(url, json=data, headers=headers)

    if response.status_code == 200:
        update_status_icon(tab_index, True)
    else:
        update_status_icon(tab_index, False)

check_strs = (
    "Скажи-ка, дядя, ведь недаром\nМосква, спаленная пожаром,\n"
    "Французу отдана?\n"
    "Ведь были ж схватки боевые,\n"
    "Да, говорят, еще какие!\n"
    "Недаром помнит вся Россия\n"
    "Про день Бородина!\n"
    "Да, были люди в наше время,\n"
    "Не то, что нынешнее племя:\n"
    "Богатыри – не вы!\n"
    "Плохая им досталась доля:\n"
    "Не многие вернулись с поля…\n"
    "Не будь на то господня воля,\n"
    "Не отдали б Москвы!\n"
    "Мы долго молча отступали,\n"
    "Досадно было, боя ждали,\n"
    "Ворчали старики:\n"
    "Что ж мы? на зимние квартиры?\n"
    "Не смеют, что ли, командиры\n"
    "Чужие изорвать мундиры\n"
    "О русские штыки?»\n"
    "И вот нашли большое поле:\n"
    "Есть разгуляться где на воле!\n"
    "Построили редут.\n"
    "У наших ушки на макушке!\n"
    "Чуть утро осветило пушки\n"
    "И леса синие верхушки —\n"
    "Французы тут как тут.\n"
    "Забил заряд я в пушку туго\n"
    "И думал: угощу я друга!\n"
    "Постой-ка, брат мусью!\n"
    "Что тут хитрить, пожалуй к бою;\n"
    "Уж мы пойдем ломить стеною,\n"
    "Уж постоим мы головою\n"
    "За родину свою!\n"
    "Два дня мы были в перестрелке.\n"
    "Что толку в этакой безделке?\n"
    "Мы ждали третий день.\n"
    "Повсюду стали слышны речи:\n\n"
    "Пора добраться до картечи\n"
    "И вот на поле грозной сечи\n"
    "Ночная пала тень.\n"
    "Прилег вздремнуть я у лафета,\n"
    "И слышно было до рассвета,\n"
    "Как ликовал француз.\n"
    "Но тих был наш бивак открытый:\n"
    "Кто кивер чистил весь избитый,\n"
    "Кто штык точил, ворча сердито,\n"
    "Кусая длинный ус.\n"
    "И только небо засветилось,\n"
    "Все шумно вдруг зашевелилось,\n"
    "Сверкнул за строем строй.\n"
    "Полковник наш рожден был хватом\n"
    "Слуга царю, отец солдатам…\n"
    "Да, жаль его: сражен булатом,\n"
    "Он спит в земле сырой.\n"
    "И молвил он, сверкнув очами:\n"
    "Ребята! не Москва ль за нами?\n"
    "Умремте ж под Москвой,\n"
    "Как наши братья умирали!»\n"
    "И умереть мы обещали,\н"
    "И клятву верности сдержали\n"
    "Мы в Бородинский бой.\n"
    "Ну ж был денек! Сквозь дым летучий\n"
    "Французы двинулись, как тучи,\n"
    "И все на наш редут.\н"
    "Уланы с пестрыми значками,\н"
    "Драгуны с конскими хвостами,\н"
    "Все промелькнули перед нами,\н"
    "Все побывали тут.\н"
    "Вам не видать таких сражений!..\н"
    "Носились знамена, как тени,\н"
    "В дыму огонь блестел,\н"
    "Звучал булат, картечь визжала,\н"
    "Рука бойцов колоть устала,\н"
    "И ядрам пролетать мешала\n"
    "Гора кровавых тел.\n"
    "Изведал враг в тот день немало,\н"
    "Что значит русский бой удалый,\н"
    "Наш рукопашный бой!..\н"
    "Земля тряслась – как наши груди;\н"
    "Смешались в кучу кони, люди,\н"
    "И залпы тысячи орудий\n"
    "Слились в протяжный вой…\н"
    "Вот смерклось. Были все готовы\n"
    "Заутра бой затеять новый\n"
    "И до конца стоять…\н"
    "Вот затрещали барабаны —\н"
    "И отступили басурманы.\н"
    "Тогда считать мы стали раны,\н"
    "Товарищей считать.\н"
    "Да, были люди в наше время,\н"
    "Могучее, лихое племя:\н"
    "Богатыри – не вы.\н"
    "Плохая им досталась доля:\н"
    "Не многие вернулись с поля,\н"
    "Когда б на то не божья воля,\н"
    "Не отдали б Москвы!"
)

example_texts = check_strs.split('\n')

max_start_index = len(example_texts) - 4
start_index = random.randint(0, max_start_index)

quatrain = example_texts[start_index:start_index + 4]

quatrain_str = '\n'.join(quatrain)

example_text = quatrain_str

# Создание графического интерфейса
root = ttkb.Window(themename="superhero")
root.title("Keylogger Authentication Application")

tab_control = ttkb.Notebook(root)

tab1 = ttkb.Frame(tab_control)
tab2 = ttkb.Frame(tab_control)

tab_control.add(tab1, text='Окно входа')
tab_control.add(tab2, text='Окно регистрации')
tab_control.pack(expand=1, fill="both")

# Tab 1: Окно входа
login_label1 = ttkb.Label(tab1, text="Логин:")
login_label1.grid(row=0, column=0, sticky="w")
login_entry1 = ttkb.Entry(tab1)
login_entry1.grid(row=0, column=1)

login_status_label1 = ttkb.Label(tab1, text="", font=("Helvetica", 12))
login_status_label1.grid(row=0, column=2, padx=5)

password_label1 = ttkb.Label(tab1, text="Пароль:")
password_label1.grid(row=1, column=0, sticky="w")
password_entry1 = ttkb.Entry(tab1, show="*")
password_entry1.grid(row=1, column=1)

password_status_label1 = ttkb.Label(tab1, text="", font=("Helvetica", 12))
password_status_label1.grid(row=1, column=2, padx=5)

prompt_label1 = ttkb.Label(tab1, text="Введите этот текст:", font=("Helvetica", 10, "bold"))
prompt_label1.grid(row=2, column=1, padx=5, pady=(10, 0), sticky="w")

example_label1 = ttkb.Label(tab1, text=example_text, justify="left")
example_label1.grid(row=3, column=1, padx=5, pady=5, sticky="w")

text_label1 = ttkb.Label(tab1, text="Текст:")
text_label1.grid(row=4, column=0, sticky="w")
text_entry1 = ttkb.Text(tab1, height=5, width=30)
text_entry1.grid(row=5, column=1, padx=5, pady=5)
text_entry1.bind("<FocusIn>", lambda event: start_tracking(0))

submit_button1 = ttkb.Button(tab1, text="Вход", command=lambda: submit_metrics(0))
submit_button1.grid(row=7, columnspan=2)

# Tab 2: Окно регистрации
login_label2 = ttkb.Label(tab2, text="Логин:")
login_label2.grid(row=0, column=0, sticky="w")
login_entry2 = ttkb.Entry(tab2)
login_entry2.grid(row=0, column=1)

login_status_label2 = ttkb.Label(tab2, text="", font=("Helvetica", 12))
login_status_label2.grid(row=0, column=2, padx=5)

password_label2 = ttkb.Label(tab2, text="Пароль:")
password_label2.grid(row=1, column=0, sticky="w")
password_entry2 = ttkb.Entry(tab2, show="*")
password_entry2.grid(row=1, column=1)

password_status_label2 = ttkb.Label(tab2, text="", font=("Helvetica", 12))
password_status_label2.grid(row=1, column=2, padx=5)

prompt_label2 = ttkb.Label(tab2, text="Введите этот текст:", font=("Helvetica", 10, "bold"))
prompt_label2.grid(row=2, column=1, padx=5, pady=(10, 0), sticky="w")

example_label2 = ttkb.Label(tab2, text=example_text, justify="left")
example_label2.grid(row=3, column=1, padx=5, pady=5, sticky="w")

text_label2 = ttkb.Label(tab2, text="Текст:")
text_label2.grid(row=4, column=0, sticky="w")
text_entry2 = ttkb.Text(tab2, height=5, width=30)
text_entry2.grid(row=5, column=1, padx=5, pady=5)
text_entry2.bind("<FocusIn>", lambda event: start_tracking(1))

submit_button2 = ttkb.Button(tab2, text="Зарегистрироваться", command=lambda: submit_metrics(1))
submit_button2.grid(row=6, columnspan=2)

tracker = None  # Переменная для отслеживания метрик

root.mainloop()
