# TouchsoftChat

### Задание №1. Консольный чат.


#### Необходимый функционал:

- [X] Два типа пользователей: “клиент” и “агент”, которые могут общаться друг с другом.
- [X] Поддерживаются только личные сообщения клиента с агентом (клиент не может писать другому клиенту,
 а агент - другому агенту).
- [X] Когда клиент начинает писать сообщение, он ещё не знает, с каким агентом он будет общаться.
- [X] При получении первого сообщения клиента сервер должен выбрать свободного агента в качестве собеседника.
- [X] Агент может вести беседу одновременно только с одним клиентом.
- [X] Должны поддерживаться консольные команды для регистрации в системе, завершения чата и выходы из системы.
- [X] Сообщения, отправленные клиентом в отсутствии соединения с агентом не должны теряться.

#### Технические требования:
- [X] Автоматическая сборка проекта посредством Apache Maven
- [ ] Покрытие кода unit-тестами
- [ ] Логирование основных событий с указанием времени:
    - [ ] Появления агента и клиента в системе
    - [ ] Начало и завершение чата
   
#### Пример варианта использования: 
- [X] Запускаем сервер
- [X] Запускаем клиентское приложение
- [X] Вводим “/register agent Cooper”
- [X] Сервер регистрирует нас как агента
- [X] Запускаем ещё один экземпляр клиентского приложения
- [X] Вводим “/register client Alice”
- [X] Сервер регистрирует нового клиента
- [x] Клиент вводит “Hello, I need help”
- [x] Агент получает сообщение и может ему ответить “How can I help you?”
- [x] Клиент может продолжить переписку или завершить чат: “/leave”
- [x] После завершения чата клиент может снова написать сообщение и, возможно, попасть на другого агента
- [x] Клиент или агент может выйти из системы, введя “/exit”, в этом случае приложение закрывается.

### Задание №2. Веб-интерфейс.

- [x] Необходимо добавить веб-интерфейс для вашего агентского/клиентского приложения.
 Он должен быть представлен в виде веб-страницы со следующими элементами:
    - [x] поле для ввода сообщений
    - [x] поле для вывода сообщений
    - [x] кнопка для отправки сообщений.
- [X] При этом, должна сохраняться обратная совместимость с вашим консольным приложением таким образом,
 чтобы консольный агент мог общаться с веб-клиентами и консольные клиенты могли общаться с веб-агентами.

### Задание №3. Code Review.

### Задание №4. REST API.

#### В рамках текущего задания надо добавить REST-сервисы, реализующие следующий функционал:
- [x] Получить всех зарегистрированных агентов
- [x] Получить всех свободных агентов
- [x] Получить детальную информацию об одном указанном агенте
- [x] Получить количество свободных агентов
- [x] Получить текущие открытые чаты
- [x] Получить детальную информацию об одном указанном чате
- [x] Получить всех клиентов, находящихся в очереди
- [x] Получить детальную информацию об одном указанном клиенте
- [x] Зарегистрировать агента
- [x] Зарегистрировать клиента
- [x] Послать сообщение агенту
- [x] Послать сообщение клиенту
- [x] Получить новые сообщения
- [ ] Покинуть чат
 + Транспортный протокол: HTTP
 + Формат запросов: JSON
- [x] Сервисы, позволяющие получить списки, должны поддерживать пагинацию - передаём входные параметры “номер страницы” 
и “размер страницы”. Например: GET http://localhost:8080/app/agents?pageNumber=0&pageSize=5 - вернёт
 первые 5 агентов в списке.
- [x] Также необходимо сгенерировать документацию сервисов.

+ Рекомендации:
  + Использовать один из REST Frameworks: RESTEasy, Jersey, средства Spring (RestController)
  + Тестировать сервисы с помощью сторонних REST-клиентов (e.g. YARC, Postman)
  + Генерировать документацию с помощью таких инструментов, как Swagger или SpringRestDoc
    
- [x] Действия, произведённые через REST клиент, должны быть интегрированы с действиями из консольного и веб-клиента
 (т.е. можно отправить сообщение из REST-клиента агенту, использующему веб-интерфейс или клиенту, использующему
  консольный интерфейс, и т. д.)
