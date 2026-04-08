# Student Onboarding System

A distributed system with Python and Java microservices for processing student data through CSV files and storing them in PostgreSQL.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Student Onboarding System                 │
└─────────────────────────────────────────────────────────────┘
                              │
                ┌─────────────┴─────────────┐
                │                           │
        ┌───────▼────────┐        ┌────────▼────────┐
        │  Python Service│        │  Java Service   │
        │  (Port 8001)   │        │  (Port 8080)    │
        └────────┬───────┘        └────────┬────────┘
                 │                         │
         ┌───────▼──────────┐     ┌────────▼──────────┐
         │ - CSV Generator  │     │ - Batch API       │
         │ - File Watcher   │     │ - JPA Repository  │
         │ - CSV Parser     │     │ - Spring Boot     │
         │ - Data Validator │     │                   │
         │ - Batch Sender   │     │                   │
         └───────┬──────────┘     └────────┬──────────┘
                 │                         │
                 └──────────────┬──────────┘
                                │
                        ┌───────▼─────────┐
                        │   PostgreSQL    │
                        │   (Port 5432)   │
                        │   Database      │
                        └─────────────────┘
```

---

## Prerequisites

### Required Software
- **Python 3.10+**
- **Java 17+**
- **PostgreSQL 12+**
- **Maven 3.8+**

### Required Packages

**Python:**
- fastapi
- uvicorn
- pandas
- watchdog
- requests
- faker

**Java:**
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL JDBC Driver

---

## Project Structure

```
bct_project1/
├── README.md
├── data/                          # CSV files (monitored by watcher)
├── service_a_python/              # Python FastAPI Service
│   ├── app/
│   │   ├── __init__.py
│   │   └── main.py               # FastAPI app entry point
│   ├── client/
│   │   └── api_client.py          # Sends batches to Java service
│   ├── config/
│   │   └── settings.py            # Configuration (URLs, batch size)
│   ├── generator/
│   │   └── csv_generator.py       # Generates test CSV data
│   ├── models/
│   │   └── student.py             # Student data model
│   ├── parser/
│   │   └── csv_parser.py          # Parses CSV files
│   ├── utils/
│   │   └── helper.py              # Utility functions
│   ├── validator/
│   │   └── validator.py           # Validates student records
│   ├── watcher/
│   │   └── file_watcher.py        # Monitors data/ directory
│   ├── requirements.txt           # Python dependencies
│   └── __init__.py
│
└── service_b_java/                # Java Spring Boot Service
    ├── pom.xml
    ├── src/main/
    │   ├── java/com/example/onboarding/
    │   │   ├── OnboardingApplication.java  # Spring Boot main
    │   │   ├── controller/
    │   │   │   └── StudentController.java  # REST API endpoint
    │   │   ├── dto/
    │   │   │   └── StudentDTO.java
    │   │   ├── entity/
    │   │   │   └── Student.java            # JPA entity
    │   │   ├── repository/
    │   │   │   └── StudentRepository.java  # Database operations
    │   │   └── service/
    │   │       └── StudentService.java     # Business logic
    │   └── resources/
    │       └── application.properties      # Database config
    └── target/                   # Compiled Java artifacts
```

---

## Setup & Installation

### 1. Install Python Dependencies

```powershell
cd c:\Users\starl\Desktop\Bct\bct_project1

# Activate virtual environment (if needed)
.\.venv\Scripts\Activate.ps1

# Install packages
pip install -r service_a_python/requirements.txt
```

### 2. Install Java Dependencies

```powershell
cd c:\Users\starl\Desktop\Bct\bct_project1\service_b_java

# Compile and package Java service
mvn clean compile
```

### 3. Setup PostgreSQL Database

```powershell
# Connect to PostgreSQL
psql -U postgres

# Run in PostgreSQL console:
CREATE DATABASE studentdb;
\c studentdb
```

Or use this command in one go:
```powershell
psql -U postgres -h localhost -c "CREATE DATABASE studentdb;"
```

---

## Running the Project

### Option 1: Run Both Services (Recommended)

**Terminal 1 - Start Java Service:**
```powershell
cd c:\Users\starl\Desktop\Bct\bct_project1\service_b_java
mvn spring-boot:run
```

Wait for: `Started OnboardingApplication in X seconds`

**Terminal 2 - Start Python Service:**
```powershell
cd c:\Users\starl\Desktop\Bct\bct_project1
python -m uvicorn service_a_python.app.main:app --host 0.0.0.0 --port 8001
```

Wait for: 
```
INFO:     Application startup complete.
INFO:     Uvicorn running on http://0.0.0.0:8001
Watching directory: ...
```

---

## Testing Workflow

### Step 1: Generate Test Data

Run the CSV generator to create 120 student records:

```powershell
curl -UseBasicParsing http://localhost:8001/generate
```

**Expected Response:**
```json
{"message":"CSV generated"}
```

**What happens:**
- Files a CSV in `data/students.csv` with 120 randomly generated student records
- Each student has: name, email, age, phone

### Step 2: Monitor Processing

The Python file watcher automatically detects the new CSV and processes it:

```
Watching directory: C:\Users\starl\Desktop\Bct\bct_project1\data
New file detected: C:\Users\starl\Desktop\Bct\bct_project1\data\students.csv
Valid: 110, Invalid: 10
Sent batch 1, status: 200
Sent batch 2, status: 200
Sent batch 3, status: 200
```

**What's happening:**
- ✅ CSV parser reads the file
- ✅ Validator checks each record (valid email + valid phone)
- ✅ Valid records are sent in batches of 50 to Java service
- ✅ Java service saves to PostgreSQL

### Step 3: Wait for Batch Processing

Wait 2-3 seconds for all batches to be sent:

```powershell
Start-Sleep -Seconds 3
```

### Step 4: Verify Data in Database

**Check total count:**
```powershell
psql -U postgres -h localhost -d studentdb -c "SELECT COUNT(*) FROM student;"
```

**Expected output:** ~110 records (depending on validation)

```
 count
-------
   110
(1 row)
```

**View sample records:**
```powershell
psql -U postgres -h localhost -d studentdb -c "SELECT id, name, email, age, phone FROM student LIMIT 5;"
```

**Expected output:**
```
 id |      name       |           email            | age |     phone
----+-----------------+----------------------------+-----+------------------
  1 | John Doe        | john.doe@example.com       |  25 | (555) 123-4567
  2 | Jane Smith      | jane.smith@example.com     |  22 | 555-234-5678
  3 | Michael Johnson | michael@example.com        |  28 | +1 (555) 234-5678
  4 | Sarah Williams  | sarah@example.com          |  24 | 555-345-6789
  5 | Robert Brown    | robert.brown@example.com   |  26 | (555) 456-7890
(5 rows)
```

**Get detailed statistics:**
```powershell
psql -U postgres -h localhost -d studentdb -c "
SELECT 
    COUNT(*) as total_students,
    COUNT(DISTINCT name) as unique_names,
    AVG(age) as avg_age,
    MIN(age) as min_age,
    MAX(age) as max_age
FROM student;
"
```

**Expected output:**
```
 total_students | unique_names | avg_age | min_age | max_age
----------------+--------------+---------+---------+---------
            110 |          110 |    24.5 |      18 |      30
```

### Step 5: Generate More Data (Optional)

Test the system again with different data:

```powershell
curl -UseBasicParsing http://localhost:8001/generate
```

Wait 3 seconds and check the database again. The count should increase.

---

## Testing Endpoints Directly (Without CSV)

### Send Data Directly to Java Service

Test the Java batch endpoint directly:

```powershell
$testData = @(
    @{name="Alice Johnson"; email="alice@example.com"; age=25; phone="555-1234567"},
    @{name="Bob Smith"; email="bob@example.com"; age=28; phone="555-9876543"}
) | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/students/batch" `
    -Method Post `
    -Headers @{"Content-Type"="application/json"} `
    -Body $testData
```

**Expected Response:**
```
Saved 2 students
```

Then verify:
```powershell
psql -U postgres -h localhost -d studentdb -c "SELECT COUNT(*) FROM student;"
```

---

## Validation Rules

The system validates student records based on these rules:

### Required Fields
- ✅ **name**: Must not be empty
- ✅ **email**: Must be valid email format (contains `@` and `.`)
- ✅ **age**: Must be a valid integer (18-30 in generated data)
- ✅ **phone**: Must be 7+ characters of digits, spaces, hyphens, parentheses, or `+`

### Examples of Valid Records
```
name: "John Doe"
email: "john@example.com"
age: 25
phone: "(555) 123-4567"  ✅ Valid
phone: "555-123-4567"    ✅ Valid
phone: "+1 (555) 123"    ✅ Valid
phone: "123456789"       ✅ Valid (9 digits)
```

### Examples of Invalid Records
```
name: ""                 ❌ Empty name
email: "invalid.email"   ❌ No @ or .
phone: "123"             ❌ Too short (< 7 chars)
phone: "ABC-DEF-GHIJ"    ❌ No digits
```

---

## Troubleshooting

### Issue: Port Already in Use

```powershell
# Kill existing Python processes
Get-Process python | Stop-Process -Force

# Restart the service
python -m uvicorn service_a_python.app.main:app --host 0.0.0.0 --port 8001
```

### Issue: PostgreSQL Connection Failed

```powershell
# Check if PostgreSQL is running
Get-Process postgres

# If not running, start PostgreSQL service
net start PostgreSQL-x64-13

# Test connection
psql -U postgres -h localhost -c "SELECT version();"
```

### Issue: Data Not Appearing in Database

1. Check Python service logs for errors
2. Verify Java service is running on port 8080
3. Check for validation errors:
   ```powershell
   psql -U postgres -h localhost -d studentdb -c "
   SELECT name, email, phone FROM student LIMIT 1;
   "
   ```
4. Manually test Java endpoint:
   ```powershell
   curl -UseBasicParsing http://localhost:8080/students/batch
   ```

### Issue: CSV File Not Being Detected

1. Verify `data/` directory exists:
   ```powershell
   ls C:\Users\starl\Desktop\Bct\bct_project1\data\
   ```

2. Check Python service is watching:
   ```
   Watching directory: C:\Users\starl\Desktop\Bct\bct_project1\service_a_python\..\data
   ```

3. Manually place a CSV in data/ folder and check if it's detected

---

## Configuration

### Python Service (`service_a_python/config/settings.py`)

```python
SERVICE_B_URL = "http://localhost:8080/students/batch"  # Java service endpoint
BATCH_SIZE = 50                                          # Records per batch
DATA_DIR = "data"                                        # CSV directory to monitor
```

### Java Service (`service_b_java/src/main/resources/application.properties`)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/studentdb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update              # Auto-create tables
spring.jpa.show-sql=true                          # Log SQL queries
```

---

## Database Schema

### Student Table

```sql
CREATE TABLE student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    age INT,
    phone VARCHAR(20)
);
```

**Query to view schema:**
```powershell
psql -U postgres -h localhost -d studentdb -c "\d student"
```

**Expected output:**
```
                          Table "public.student"
 Column |       Type        | Collation | Nullable |      Default
--------+-------------------+-----------+----------+-------------------
 id     | bigint            |           | not null | nextval('s...')
 name   | character varying |           |          |
 email  | character varying |           |          |
 age    | integer           |           |          |
 phone  | character varying |           |          |
```

---

## Complete Testing Workflow Script

Save this as `test.ps1` and run it:

```powershell
Write-Host "================================" -ForegroundColor Green
Write-Host "  Student Onboarding System Test" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host ""

Write-Host "Step 1: Generating test data..." -ForegroundColor Yellow
curl -UseBasicParsing http://localhost:8001/generate
Write-Host ""

Write-Host "Step 2: Waiting for file processing..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

Write-Host "Step 3: Checking database..." -ForegroundColor Yellow
Write-Host ""
psql -U postgres -h localhost -d studentdb -c "SELECT COUNT(*) as total_students FROM student;"
Write-Host ""

Write-Host "Step 4: Viewing sample records..." -ForegroundColor Yellow
Write-Host ""
psql -U postgres -h localhost -d studentdb -c "SELECT id, name, email, age, phone FROM student LIMIT 5;"
Write-Host ""

Write-Host "Step 5: Getting statistics..." -ForegroundColor Yellow
Write-Host ""
psql -U postgres -h localhost -d studentdb -c "
SELECT 
    COUNT(*) as total,
    AVG(age) as avg_age,
    MIN(age) as min_age,
    MAX(age) as max_age
FROM student;
"
Write-Host ""

Write-Host "================================" -ForegroundColor Green
Write-Host "  Test Complete!" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
```

Run it:
```powershell
.\test.ps1
```

---

## Service Endpoints

### Python Service (Port 8001)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/generate` | GET | Generate CSV with 120 test students |
| `/docs` | GET | Interactive API documentation (Swagger UI) |

### Java Service (Port 8080)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/students/batch` | POST | Save multiple students (expects JSON array) |
| `/actuator/health` | GET | Health check endpoint |

**Java Health Check:**
```powershell
curl -UseBasicParsing http://localhost:8080/actuator/health
```

---

## Performance Notes

- **Batch Size**: 50 records per HTTP request (configured in settings.py)
- **Processing Speed**: ~120 records processed in ~1-2 seconds
- **Validation Rate**: Typically 90% valid records (10% random failures in generated data)
- **Database**: PostgreSQL handles concurrent writes efficiently

---

## Next Steps / Future Improvements

- [ ] Add authentication/authorization (JWT)
- [ ] Add request/response logging
- [ ] Add monitoring and metrics (Prometheus)
- [ ] Add Docker & Docker Compose for easier deployment
- [ ] Add unit tests (Python pytest, Java JUnit)
- [ ] Add CI/CD pipeline (GitHub Actions)
- [ ] Add API rate limiting
- [ ] Add data encryption for sensitive fields

---

## Support & Debugging

1. **View Python logs**: Check the terminal where Python service is running
2. **View Java logs**: Check the terminal where Java service is running
3. **Database queries**: Use psql commands to inspect the database
4. **File watcher logs**: Check the Python terminal for "New file detected" messages

For issues, verify:
1. Both services are running
2. PostgreSQL is accessible
3. Ports 8001 (Python) and 8080 (Java) are available
4. CSV file exists in `data/` directory

---

**Last Updated**: April 2026
