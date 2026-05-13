# Coursework Tracker API

A robust RESTful backend service engineered to manage academic deadlines and progress tracking. This application demonstrates a production-ready approach to Java development, focusing on persistent storage, containerization, and scalable architecture.

## Project Overview
Developed as a proactive technical deep-dive prior to a FinTech Software Engineering internship, this project serves as a comprehensive implementation of modern backend standards. It moves beyond academic theory to demonstrate proficiency in:
*   **System Modeling:** Designing a domain-driven API structure.
*   **Infrastructure as Code:** Utilizing Docker for consistent environment orchestration.
*   **Enterprise Workflow:** Implementing strict Git-flow methodologies, including feature branching and pull request (PR) reviews to ensure code integrity.

## Tech Stack
*   **Language:** Java 17+
*   **Framework:** Spring Boot (Spring Data JPA, Spring Web)
*   **Database:** PostgreSQL
*   **Containerization:** Docker & Docker Compose
*   **Build Tool:** Maven
*   **Version Control:** Git / GitHub

## Key Features
*   **Full CRUD Functionality:** Create, Read, Update, and Delete coursework entries via REST endpoints.
*   **Relational Persistence:** Integration with PostgreSQL for reliable data management and schema consistency.
*   **Automated Configuration:** Centralized environment management via `application.properties` and Maven.
*   **Environment Agnostic:** Fully containerized setup ensuring the application runs identically across development, testing, and production environments.

## Getting Started / Installation
### Prerequisites
*   Docker Desktop installed.

### Execution
Clone the repository and run the following command in the root directory:
```bash
docker-compose up --build
```

The API will be accessible at http://localhost:8080.

## API Documentation

Method: `GET`	
Endpoint: `1/api/v1/coursework1`
Returns a list of all coursework entries.

Method: `GET`	
Endpoint: `/api/v1/coursework/{id}`	
Returns a specific entry by its unique ID.

Method: `POST`	
Endpoint: `/api/v1/coursework`	
Creates a new coursework record. Expects a JSON body.

Method: `PUT`	
Endpoint: `/api/v1/coursework/{id}`	
Updates details of an existing entry.

Method: `DELETE`	
Endpoint: `/api/v1/coursework/{id}`	
Permanently removes an entry from the database.

Example POST Body
```{
  "subject": "Data Structures",
  "taskName": "Binary Tree Implementation",
  "dueDate": "2026-06-01",
  "status": "In Progress"
}
```