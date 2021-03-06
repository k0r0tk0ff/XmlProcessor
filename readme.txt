     Перед запуском проекта необходимо собрать артефакт с помощью
сборщика maven (необходима версия не ниже 3.1) командой "mvn package".
В созданную папку "target", где лежит собранный артефакт
"XmlProcessor-2.5-jar-with-dependencies.jar" необходимо скопировать
файл с настройками приложения "app.properties", и по желанию скрипт запуска
приложения "run.bat".

    Перед запуском, по необходимости, можно указать уровень логирования
в файле настроек приложения "app.properties".
    Допустимые значения:
TRACE
DEBUG
INFO
WARN
ERROR
FATAL
OFF
По умолчанию выставлен уровень "DEBUG".
(См. https://logging.apache.org/log4j/2.0/manual/architecture.html)

    Запускать приложение можно как скриптом запуска -
run.bat [options] <fileName>

    так и непосредственно с командной строки -
java -jar XmlProcessor-2.5-jar-with-dependencies.jar [options] <fileName>

    ВАЖНО!
    Условием успешного запуска приложения путем запуска скрипта "run.bat"  является наличие
в системно переменной PATH пути до java.exe. В случае его отсутствия,
вместо "java" необходимо прописать полный путь до java.exe.

   options:
        -s (synchronize) - синхронизировать данные в БД с данными из xml файла.

        -u (upload data from database to xml file) - выгрузить данные из БД в xml файл.

        -c (clear data) - очистить данные в таблице (второй параметр игнорируется)

Примеры:
run.bat -s dataCollection.xml     - синхронизировать данные в бд с данными из файла dataCollection.xml
run.bat -u dataFromDb.xml         - выгрузить данные из бд в файл dataFromDb.xml
run.bat -c                        - очистить данные в бд

