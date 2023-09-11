<h1>#myBookShop - интернет магазин книг</h1>
<hr/>
<h3>использованы технологии</h3>
Spring boot, thymeleaf,  postgresql, redis, oauth2-client google, oauth2-client github, liquibase, swagger, selenium.
<hr>

Для запуска создайте application.properties со следующим содержимым: <br>
<code>
server.port=8085
spring.messages.basename=lang.messages
spring.web.resources.static-locations=classpath:/spring-frontend/
spring.thymeleaf.prefix=classpath:/spring-frontend/
spring.datasource.url=jdbc:postgresql://192.168.1.10:5432/postgres
spring.datasource.username= YouUser
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.password= YouPass
spring.jpa.hibernate.ddl-auto=none
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false
spring.jpa.defer-datasource-initialization=false
spring.sql.init.data-locations=classpath:data.sql
spring.datasource.initialization-mode=always
#spring.sql.init.mode=always
spring.liquibase.enabled=true
spring.liquibase.changelog=classpath:/db/changelog/db.changelog-master.xml
upload.path= #YouPath
download.path= #YouPath
spring.security.user.name=
spring.security.user.password=
auth.secret=skillbox
server.servlet.session.tracking-modes=cookie
spring.security.oauth2.client.registration.github.clientId=
spring.security.oauth2.client.registration.github.clientSecret=
spring.security.oauth2.client.registration.google.clientId=
spring.security.oauth2.client.registration.google.clientSecret=
spring.main.allow-bean-definition-overriding=true
appEmail.email=Mail site
appEmail.password=Mail password
google.books.api.key=
robokassa.merchant.login=mybookstore2023000
robokassa.pass.first.test= 
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=*
twilio.ACCOUNT_SID=
sms-ru.Account.id=
bookshop.is.testing.mode=true
</code>
