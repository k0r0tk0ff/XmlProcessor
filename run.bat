@rem 1-й параметр (%1)- параметр options, который принимает значения  
@rem               -S (synchronize), -G (get xml file), -C (clear data) см. файл readme.txt
@rem 2-й параметр (%2)- параметр fileName, в котором должно быть указано имя файла (путь до файла) 
@rem                    с которым будет производиться операция, указанная в параметре options

java -Dfile.encoding=UTF-8 -jar XmlProcessor-2.5-jar-with-dependencies.jar %1 %2

