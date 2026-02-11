# FILE_FILTER_UTIL

FILE_FILTER_UTIL - утилита предназначена для обработки содержимого текстовых файлов.  
Распределяет данные(целые числа, вещественные числа и строки) из входных файлов в соответствующие выходные файлы.

---

## Оглавление
- [Процесс запуска](#startup-process)
- [Возможные флаги запуска](#flags)
- [Технические требования](#tech-stack)

## Процесс запуска <a id="startup-process"></a>

1. Создать jar файл

``` bash
.\gradlew clean build 
 ```

2. Передать требуемые файлы для анализа

``` bash
 java -jar build/libs/file_filter_util.jar -s -a -p sample- example_text/exampleText1.txt example_text/exampleText2.txt
 ```

3. На выходе до 3 файлов (`integers.txt` `floats.txt` `strings.txt`). Файлы создаются при условии что будут не пустыми.

## Возможные флаги запуска <a id="flags"></a>

| Флаг | Описание                            |      Поведение по умолчанию       |     
|:----:|-------------------------------------|:---------------------------------:|
|  s   | выводит краткую статистику          | без `-s` и `-f` полная статистика |
|  f   | выводит полную статистику           | без `-s` и `-f` полная статистика |
|  а   | дополняет существующий файл         |        перезаписывает файл        |
|  o   | задаёт каталог для выходных файлов  |          текущий каталог          |
|  p   | задаёт префикс имен выходных файлов |           префикса нет            |

## Технический стек <a id="tech-stack"></a>
* Окружение:
  * Java 21
  * gradle 9.2.0
* Библиотеки:
  * commons-cli version 1.5.0
  * slf4j-api version 2.0.17
  * slf4j-simple version 2.0.17

Пример работы утилиты:
* Запуск с флагом `-s`
<img width="2133" height="166" alt="img_1" src="https://github.com/user-attachments/assets/7a376b6f-8883-4df8-bcbd-7efdf9150b9f" />

* Запуск с флагом `-f`
<img width="2127" height="246" alt="image" src="https://github.com/user-attachments/assets/076022b1-defa-4f3a-8aa6-d1c8d37ae280" />

