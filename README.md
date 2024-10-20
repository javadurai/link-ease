# Linkease

Linkease is an open-source application built with Spring Boot and React JS, designed to manage and share links easily. The application supports user authentication, role management, and allows users to categorize links with tags.

## Features

- User registration and authentication
- Role-based access control
- Link management with titles, URLs, descriptions, and tags
- In-memory database using H2 (with an option to persist data)
- H2 console for database management

## Technologies Used

- **Backend**: Spring Boot
- **Frontend**: React JS
- **Database**: H2 (in-memory or persistent)
- **Security**: Spring Security
- **Dependency Management**: Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Node.js and npm (for the React frontend)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/linkease.git
   cd linkease
   ```

2. Build the backend:

    ```bash
    cd backend
    mvn clean install
    ``` 

3. Start the backend application:

    ```bash
    mvn spring-boot:run
    ```
    The backend will be running at http://localhost:8080.

4. Build the frontend:

    ```bash
    cd frontend
    npm install
    npm start
    ```
    The frontend will be running at http://localhost:3000.

5. Database Configuration

    By default, the application uses an in-memory H2 database. If you want to persist the database, modify the application.properties or application.yml file in the backend project to specify a file location:

    properties

6. Example for application.properties
    spring.datasource.url=jdbc:h2:file:/path/to/your/database/dbfile;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE

7. Accessing the H2 Console

    You can access the H2 console for database management at:

    ```bash
    http://localhost:8080/h2-console

    JDBC URL: jdbc:h2:file:/path/to/your/database/dbfile
    Username: sa
    Password: password
   ```

8. Default Admin User

    Upon the first run, a default admin user will be created with the following credentials:

    ```
   Username: admin
   Password: admin123
   ```

9. Contributing

    Contributions are welcome! Please fork the repository and submit a pull request for any improvements or new features.
    
10. License
    
    This project is licensed under the MIT License - see the LICENSE file for details.
    
11. Acknowledgements

    - Spring Boot
    - React
    - H2 Database

### Key Sections:
- **Features**: Lists the core functionalities of the application.
- **Technologies Used**: Details the tech stack.
- **Getting Started**: Provides instructions for setting up the project locally.
- **Database Configuration**: Explains how to set up a persistent database.
- **Accessing the H2 Console**: Instructions for accessing the database console.
- **Default Admin User**: Information about the default admin account created.
- **Contributing**: Guidelines for contributing to the project.
- **License**: Specifies the project's licensing.

Feel free to adjust any part to better suit your project specifics!