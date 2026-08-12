# Mini Ticket Tracker Backend

REST API's for managing support tickets.


## Setup and Run Instructions
### Prerequisites :

Make sure the following are installed:

* Java 17+
* Maven 3.9+
* PostgreSQL

### Database Setup :

Create a PostgreSQL database:

```sql
CREATE DATABASE lodwarticket;
```

Create the database user:

```sql
CREATE USER lodwarticketadmin WITH PASSWORD 'lodwarticketpassword';
```

Grant access:

```sql
GRANT ALL PRIVILEGES ON DATABASE lodwarticket TO lodwarticketadmin;
```

The application is currently configured to use:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lodwarticket
spring.datasource.username=lodwarticketadmin
spring.datasource.password=lodwarticketpassword
```

### Running the Application :

Clone the repository and navigate into the project:

```bash
cd LodwarTicketBackend
```

Run the application with Maven:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Decisions & Tradeoffs

1. The implementation uses Spring Data JPA repositories to keep persistence logic simple and reduce boilerplate. 
2. Pagination and status filtering are handled at the database level rather than loading all tickets into memory. 
3. Enum values are stored as strings to make the database representation easier to understand and maintain.  
4. CORS is currently configured with `allowedOrigins("*")` for development convenience, but production should restrict this to known frontend origins. 

With more time: 
I would add database migrations, stronger validation for enum values, proper custom error handling and structured error responses.
