

java -jar project.jar [options] <fileName>
   options:
        -L (loadSqlFile) - загрузить данные в БД из файла.
                        Если в БД имеются данные, то они будут стерты.

        -S (sync) - синхронизировать данные в БД с данными из xml файла.

        -G (getXmlFile) - выгрузить данные из БД в xml файл.

Примеры:
run.bat -L initialData.sql
run.bat -S dataCollection.xml
run.bat -G dataFromDb.xml
