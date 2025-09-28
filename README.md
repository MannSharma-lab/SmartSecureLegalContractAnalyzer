# SmartSecure Legal Contract Analyzer

SmartSecure Legal Contract Analyzer is a web-based Java Spring Boot application designed to upload, analyze, and manage legal contracts. It allows users to extract key information, manage expiry dates, and export results in a clean format.

---

## Features

- Upload legal contract files (PDF or text)
- Automatically extract key clauses and metadata
- View extracted data in user dashboard
- Track contract expiry dates
- Export analyzed data to PDF
- Admin dashboard to manage all users and contracts

---

##  Tech Stack

- **Backend**: Java 17, Spring Boot, Hibernate JPA, Maven
- **Frontend**: Thymeleaf, Bootstrap, HTML/CSS
- **Database**: MySQL
- **Other**: Spring Security, Apache PDFBox, Lombok

---

## Getting Started

###  Prerequisites

- Java 17+
- Maven
- MySQL 8+

### Project Setup

1. **Clone the Repository**
```bash
git clone https://github.com/your-username/legal-analyzer.git
cd legal-analyzer
```

2. Configure MySQL Database


Create a database in MySQL:
```sql
CREATE DATABASE smart_contract_analyzer;
```
Update src/main/resources/application.properties:

```Propreties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_contract_analyzer
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
3. Build and Run the App

```bash
mvn clean install
mvn spring-boot:run
```
4. Visit:
http://localhost:8080/




---

## Screenshots

> https://drive.google.com/drive/folders/1wsogmTSFgDIByOXHBeHWtzhZ2U9N7aGb

---

 ## Deployment

```
This app can be deployed on:

Render

Railway

Heroku

AWS (Elastic Beanstalk)


Make sure to configure environment variables and database credentials in production.


```
Database Schema

```sql

-- User Table
CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);

-- Contract Table
CREATE TABLE contract (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  file_name VARCHAR(255),
  content LONGTEXT,
  created_at DATETIME,
  expiry_date DATE,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

```
---

 ## License

This project is licensed under the MIT License.
Feel free to use, modify, and share.


---

## Author
Mann Sharma
---
Email-Id:- 2023bcaaidsmann14707@poornima.edu.in
---

LinkedIn-Id:- https://www.linkedin.com/in/mann-sharma-5425b9290?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app


---

##  Tips

Forgot admin password? Manually update it in the DB with BCrypt hash.

Whitelabel error page? Check logs — likely due to DB or missing templates.

Use Postman to test endpoints during debugging.



---
