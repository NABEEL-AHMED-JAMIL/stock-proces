# Stock Data Processing Backend (Stock Data)

This is a Spring Boot application for processing stock data with AI capabilities.

- Uploading stock data files (CSV, Parquet) from the frontend
- Saving uploaded files on the server's local filesystem
- Parsing and processing files to extract financial data
- Generating vector embeddings using Ollama Embedding API
- Storing embeddings in PostgreSQL with the `pgvector` extension
- Querying vector database to find relevant stock data for user queries
- Calling Ollama GPT API to generate contextual natural language answers based on retrieved data

### Features

- **File Upload API:** Accepts multipart file uploads and saves files locally
- **File Processing:** Parses CSV and Parquet files to extract stock data
- **Embedding Generation:** Converts extracted data into vector embeddings via Ollama3
- **Vector Storage:** Persists embeddings into PostgreSQL with pgvector for similarity search
- **Query API:** Receives user queries, retrieves similar vectors, and returns generated answers

### Prerequisites

- Java 8
- Maven or Gradle
- PostgreSQL with `pgvector` extension installed and enabled
- Ollama3 API key
- Backend server machine with sufficient disk space for storing uploaded files

## Branch Information

**Branch:** `stock-data-with-ai`

## Prerequisites

1. **Java Development Kit (JDK)**: Ensure JDK is installed (version compatible with Spring Boot).
2. **Maven**: Ensure Maven is installed for managing dependencies and building the project.
3. **Database**: Set up a database named `batch-process`.

## Setup Instructions

### 1. Database Configuration

- Make sure you have a database named `batch-process` configured in your database server (e.g., PostgreSQL).
- Update the database settings in `src/main/resources/application.properties` or `application.yml` according to your database connection details.

### 2. Storage Location

- Set the file storage location in the application properties:
  ```properties
  storage.efsFileDire=G://efs

### 3. Environment Variables

- Set the following environment variables or provide in `application.properties`:

## 4. Dataset

We use the **[Price and Volume Data for All US Stocks & ETFs](https://www.kaggle.com/datasets/borismarjanovic/price-volume-data-for-all-us-stocks-etfs)** dataset by Boris Marjanovic, hosted on Kaggle.

## Project Structure

### src/main/java/com/stock/process/
- api # REST API controllers and related classes
- config # Configuration classes (e.g., thread pools, properties)
- domain # Domain models and dto, enums, repository, service etc.
- etl # ETL (Extract, Transform, Load) related logic
- util # Utility classes and helpers
- StockProcessApplication.java # Main Spring Boot application entry point

### src/main/resources/
- db.changelog # Database migration scripts (Liquibase or Flyway)
- application.properties # Base configuration properties
- application-dev.properties # Development environment properties
- application-prod.properties# Production environment properties
- application-staging.properties # Staging environment properties


### 📝 Description

The dataset contains daily stock market data for a wide range of US stocks and ETFs, with individual CSV files for each ticker symbol (e.g., `AAPL.csv`, `MSFT.csv`).

Each file includes:

- `Date`: Trading date
- `Open`: Opening price
- `High`: Highest price of the day
- `Low`: Lowest price of the day
- `Close`: Closing price
- `Volume`: Number of shares traded
- `OpenInt`: Open interest (usually 0 for stocks)

### 🗂️ Sample Format

| Date       | Open   | High   | Low    | Close  | Volume   | OpenInt |
|------------|--------|--------|--------|--------|----------|---------|
| 2010-01-04 | 213.43 | 214.50 | 212.38 | 214.01 | 1234324  | 0       |
