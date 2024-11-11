# Stock Data Processing Application

This is a Spring Boot application for processing stock data with AI capabilities.

## Branch Information
**Branch:** `stock-data-with-ai`

## Prerequisites

1. **Java Development Kit (JDK)**: Ensure JDK is installed (version compatible with Spring Boot).
2. **Maven**: Ensure Maven is installed for managing dependencies and building the project.
3. **Database**: Set up a database named `batch-process`.

## Setup Instructions

### 1. Database Configuration

- Make sure you have a database named `batch-process` configured in your database server (e.g., PostgreSQL, MySQL).
- Update the database settings in `src/main/resources/application.properties` or `application.yml` according to your database connection details.

### 2. Storage Location

- Set the file storage location in the application properties:
  ```properties
  storage.efsFileDire=G://efs
