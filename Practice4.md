# Практика "Работа с сетью"

Для решения этой практики нужно решить практику
[Список с деталями](https://github.com/MobileDevelopmentUrfuAutumn2024/ConsecutivePracticesReadme/blob/main/Practice3.md).
Залейте изменения с этой задачей в master или dev и начните новую ветку для этой практики. 

### Задача
Реализуйте загрузку данных из сети в соответствии с выбранной в прошлой 
практике темой и API. 

### Требования

- Загрузка данных должна быть асинхронной, на соответствующем потоке/диспатчере 
- Используйте актуальные библиотеки (Retrofit, Ktor и т.д.)
- Обеспечьте обработку ошибок и состояния загрузки
- Обращение к сети управляется во ViewModel/Presenter
- Используйте чистую архитектуру. Выделите доменный слой и слои данных и представления, реализуйте нужные модели и объекты (Api, Repository, UseCase и т.д.)
- Должен использоваться как минимум один настраиваемый запрос (query, path параметры)

### Бонус

Можете попрактиковаться с flow - например, сделав кэширование запросов

### Сдача 

- pull-request согласной общей схеме 
- Запись работы приложения
- В записи или отдельно приложить скрин App Inspector/Chucker/сниффера, где видно выполнение запросов

### Полезные сcылки

Про корутины:

https://kotlinlang.org/docs/coroutines-overview.html

https://habr.com/ru/articles/838974/

https://metanit.com/kotlin/tutorial/8.1.php

Про Retrofit:

https://square.github.io/retrofit/

https://habr.com/ru/articles/568792/

Про Clean Architecture:

https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html 

https://habr.com/ru/companies/bsl/articles/788940/
