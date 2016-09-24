# HelloBackEnd

[![Build Status](https://travis-ci.org/aleksandrbogomolov/HelloBackEnd.svg?branch=master)](https://travis-ci.org/aleksandrbogomolov/HelloBackEnd)

REST сервис HelloBackEnd

Сервис - Spring Boot 1.4.1.  
SQL БД - PostgreSQL 9.4.1211.  
Сборка - Maven.  
Интеграционные тесты -  используется библиотека [REST Assured](http://rest-assured.io).

Для работы приложения необходима БД **helloBackEnd**. Настройки доступа расположены в файле **/resources/application.yml**.   
Таблица **contacts** имеет два поля  
id - 64 bit integer  
name - varchar  
, инициализация и заполнение выполняются автоматически при старте приложения или тестов. Скрипты **schema.sql** и **data.sql** расположены в директории **resources**.

Для обработки множества запросов в файл **/resources/application.yml** добавлены настройки **Tomcat connection pool**.

Пример запроса к таблице **contacts**: 

__/hello/contacts?nameFilter=^A.*$&offset=0&limit=5__

Запрос  возвращает контакты из таблицы БД. Массив контактов возвращается в json формате.
  
Параметр **nameFilter** обязателен. В него передаётся регулярное выражение. В возвращаемых данных нет записей, в которых contacts.name совпадает с регулярным выражением, фильтрация производится в приложении на уровне service layer.
  
Параметры **offset** и **limit** добавлены для исключения единовременной загрузки большого количества данных из БД и порционной выборки данных, данные параметры не обязательны в строке запроса, при их отсутсвии заполнение происходит при формировании запроса к БД предустановленными значениями. Для загрузки следующей порции данных необходимо использовать параметр **offset**, например **offset=5** выберет следующий набор данных начиная с 6 строки.

 
Для запуска приложения на локальном компьетере необходимо создать в PostgreSQL БД **"helloBackEnd"**, в файле **/resources/application.yml** изменить **username** и **password** для доступа к созданной локальной базе, склонировать данный [репозиторий](https://github.com/aleksandrbogomolov/HelloBackEnd.git), перейти в терминале в созданный каталог и выполнить команды **mvn package** и **java -jar target/hellobackend-0.0.1-SNAPSHOT.jar**.     

Примеры запросов  
[http://localhost:8080/hello/contacts?nameFilter=^A.\*$&offset=0&limit=5](http://localhost:8080/hello/contacts?nameFilter=^A.\*$&offset=0&limit=5) - возвращает часть контактов, которые НЕ начинаются с A.  
[http://localhost:8080/hello/contacts?nameFilter=^.*[ai].*$&offset=0&limit=5](http://localhost:8080/hello/contacts?nameFilter=^.*[ai].*$&offset=0&limit=5) - возвращает часть контактов, которые НЕ содержат букв a, i.
